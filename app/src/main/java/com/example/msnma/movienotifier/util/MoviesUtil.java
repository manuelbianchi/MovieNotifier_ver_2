package com.example.msnma.movienotifier.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.msnma.movienotifier.CoreActivity;
import com.example.msnma.movienotifier.MainActivity;
import com.example.msnma.movienotifier.MovieFragment;
import com.example.msnma.movienotifier.MoviesFragment;
import com.example.msnma.movienotifier.R;
import com.example.msnma.movienotifier.callback.MoviesCallback;
import com.example.msnma.movienotifier.database.MovieDatabase;
import com.example.msnma.movienotifier.databaseModel.MovieDBModel;
import com.example.msnma.movienotifier.mapper.MovieMapper;
import com.example.msnma.movienotifier.model.Movie;
import com.example.msnma.movienotifier.provider.MovieContract;
import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MoviesUtil {
    private static final Webb WEBB = Webb.create();

    private static final String TMDB_API_MOVIES_URL = "http://api.themoviedb.org/3/movie/%s?api_key=%s&page=%s";
    private static final String TMDB_API_VIDEOS_URL = "http://api.themoviedb.org/3/movie/%s/videos?api_key=%s";
    private static final String TMDB_POSTER_URL = "https://image.tmdb.org/t/p/w185%s";
    private static final String TMDB_BACKDROP_URL = "https://image.tmdb.org/t/p/w300%s";

    private static final String TMDB_UPCOMING_MOVIES ="http://api.themoviedb.org/3/movie/upcoming?api_key=%s";
    private static final String TMDB_IN_THEATRES = "http://api.themoviedb.org/3/movie/now_playing?api_key=%s";
    private static final String TMDB_SEARCH ="https://api.themoviedb.org/3/search/movie?api_key=%s&language=en-US&query=%s&page=%s&s&include_adult=false";

    private static final String TYPE_NOTIFY = "NOTIFY";
    private static final String TYPE_WATCHED = "WATCHED";
    private static final String TYPE_SUGGESTED = "popular";

    private static final MovieMapper mapper = new MovieMapper();
    static MoviesFragment fragment;

    public static boolean isWatched(Context context, Movie movie) {
        Cursor cursor = context.getContentResolver()
                .query(MovieContract.CONTENT_URI,
                        null,
                        String.format("%s = ? and %s = ?", MovieContract.MOVIE_ID, MovieContract.TYPE),
                        new String[]{movie.getId() + "", TYPE_WATCHED},
                        null
                );
        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        return isFavorite;
    }

    public static boolean toggleFavorite(Context context, Movie movie) {
        if (isWatched(context, movie)) {
            deleteMovie(context, TYPE_WATCHED, movie);
            return false;
        } else {
            saveMovie(context, TYPE_WATCHED, movie);
            return true;
        }
    }

    public static void getNotifyMeMovies(Activity activity, MoviesCallback callback, MoviesFragment frag) {
        getMovies(activity, TYPE_NOTIFY, callback);
        fragment = frag;
    }

    public static void getSuggestedMovies(Activity activity, MoviesCallback callback) {
        getMovies(activity, TYPE_SUGGESTED, callback);
    }

    public static void getWatchedMovies(Activity activity, MoviesCallback callback, MoviesFragment frag) {
        getMovies(activity, TYPE_WATCHED, callback);
        fragment = frag;
    }

    public static void getSeachMovies(Activity activity, String query, MoviesCallback callback) {
        getMovies(activity, query, callback);
    }

    private static void getMovies(final Activity activity, final String type, final MoviesCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                if (type.equals(TYPE_NOTIFY) || type.equals(TYPE_WATCHED)) {
                    getMoviesFromDb(activity, type, callback);
                    try {
                        final List<Movie> movies = mapper.toMovieList(MainActivity.getMovieDatabase().getAllMovieByType(type));
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.success(movies);
                                isMoviesListEmpty(movies.size());
                            }
                        });
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else  if (type.equals(TYPE_SUGGESTED))  {
                    if (Util.isConnected(activity, false)) {
                        getMoviesFromApi(activity, TYPE_SUGGESTED, TMDB_UPCOMING_MOVIES);
                    }
                    getMoviesFromDb(activity, type, callback);
                } else{
                    if (Util.isConnected(activity, false)) {
                        try {
                            getMoviesFromApiForSearch(activity, type);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    getMoviesFromDb(activity, "search", callback);
                }
            }
        });
    }

    private static void getMoviesFromApi(Activity activity, String type, String apiUrl) {
        String finalApiUrl = String.format(apiUrl, activity.getString(R.string.tmdb_api_key), 1);
        try {
            JSONArray moviesJson = WEBB.get(finalApiUrl)
                    .asJsonObject()
                    .getBody()
                    .getJSONArray("results");
            List<Movie> movies = toMovies(activity, moviesJson);
            movies = filterSuggestedMovies(movies);
            deleteMovies(activity, type);
            saveMovies(activity, type, movies);
            CoreActivity.getPd().dismiss();
//            MovieDatabase.saveMoviesOnDB(movies, "notify");
//            MovieDatabase.saveMoviesOnDB(movies, "watched");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void getMoviesFromDb(Activity activity, String type, final MoviesCallback callback) {
        try {
            Cursor cursor = activity.getContentResolver()
                    .query(MovieContract.CONTENT_URI,
                            null,
                            MovieContract.TYPE + " = ?",
                            new String[]{type},
                            null
                    );
            final List<Movie> movies = toMovies(cursor);
            cursor.close();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.success(movies);
                }
            });
        } catch (final Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.error(e);
                }
            });
        }
    }

    public static void getMoviesFromApiForSearch(Activity activity, String query) throws ParseException {
        String apiUrl;
        if(query.equals("")){
            apiUrl = String.format(TMDB_IN_THEATRES, activity.getString(R.string.tmdb_api_key), query, 1);
        }else{
            apiUrl = String.format(TMDB_SEARCH, activity.getString(R.string.tmdb_api_key), query, 1);
        }
        try {
            JSONArray moviesJson = WEBB.get(apiUrl)
                    .asJsonObject()
                    .getBody()
                    .getJSONArray("results");
            List<Movie> movies = toMovies(activity, moviesJson);
            movies = filterSuggestedMovies(movies);
            deleteMovies(activity, "search");
            saveMovies(activity, "search", movies);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void saveMovie(final Context context, final String type, final Movie movie) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<Movie> movies = new ArrayList<>();
                movies.add(movie);
                saveMovies(context, type, movies);
            }
        });
    }

    private static void saveMovies(Context context, String type, List<Movie> movies) {
        if (movies != null) {
            ContentValues[] moviesValues = new ContentValues[movies.size()];
            for (int i = 0; i < movies.size(); i++) {
                try {
                    Movie movie = movies.get(i);
                    ContentValues movieValues = new ContentValues();
                    movieValues.put(MovieContract.MOVIE_ID, movie.getId());
                    movieValues.put(MovieContract.TYPE, type);
                    movieValues.put(MovieContract.TITLE, movie.getTitle());
                    movieValues.put(MovieContract.OVERVIEW, movie.getOverview());
                    movieValues.put(MovieContract.POSTER_URL, movie.getPosterUrl());
                    movieValues.put(MovieContract.BACKDROP_URL, movie.getBackdropUrl());
                    movieValues.put(MovieContract.TRAILER_URL, movie.getTrailerUrl());
                    movieValues.put(MovieContract.RELEASE_DATE, Util.toDbDate(movie.getReleaseDate()));
                    movieValues.put(MovieContract.RATING, movie.getRating());
                    movieValues.put(MovieContract.ADULT, movie.isAdult() ? 1 : 0);
                    moviesValues[i] = movieValues;
                } catch (Exception ignore) {
                }
            }
            context.getContentResolver()
                    .bulkInsert(MovieContract.CONTENT_URI, moviesValues);
        }
    }

    private static void deleteMovie(final Context context, final String type, final Movie movie) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                context.getContentResolver()
                        .delete(MovieContract.CONTENT_URI,
                                MovieContract.MOVIE_ID + " = ? and " + MovieContract.TYPE + " = ?",
                                new String[]{movie.getId() + "", type});
            }
        });
    }

    private static void deleteMovies(final Context context, final String type) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                context.getContentResolver()
                        .delete(MovieContract.CONTENT_URI,
                                MovieContract.TYPE + " = ?",
                                new String[]{type});
            }
        });
    }

    private static List<Movie> toMovies(Cursor cursor) throws ParseException {
        List<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(
                    cursor.getColumnIndex(MovieContract.MOVIE_ID)));
            movie.setTitle(cursor.getString(
                    cursor.getColumnIndex(MovieContract.TITLE)));
            movie.setOverview(cursor.getString(
                    cursor.getColumnIndex(MovieContract.OVERVIEW)));
            movie.setPosterUrl(cursor.getString(
                    cursor.getColumnIndex(MovieContract.POSTER_URL)));
            movie.setBackdropUrl(cursor.getString(
                    cursor.getColumnIndex(MovieContract.BACKDROP_URL)));
            movie.setTrailerUrl(cursor.getString(
                    cursor.getColumnIndex(MovieContract.TRAILER_URL)));
            movie.setReleaseDate(Util.toDate(cursor.getString(
                    cursor.getColumnIndex(MovieContract.RELEASE_DATE))));
            movie.setRating(cursor.getFloat(
                    cursor.getColumnIndex(MovieContract.RATING)));
            movie.setAdult(cursor.getInt(
                    cursor.getColumnIndex(MovieContract.ADULT)) == 1);
            movies.add(movie);
        }
        return movies;
    }

    private static List<Movie> toMovies(Context context, JSONArray jsonMovies) {
        List<Movie> movies = new ArrayList<>();
        if (jsonMovies != null) {
            for (int i = 0; i < jsonMovies.length(); i++) {
                try {
                    JSONObject jsonMovie = jsonMovies.getJSONObject(i);
                    int movieId = jsonMovie.getInt("id");
                    Movie movie = new Movie();
                    movie.setId(movieId);
                    movie.setTitle(jsonMovie.getString("title"));
                    movie.setOverview(jsonMovie.getString("overview"));
                    movie.setPosterUrl(String.format(TMDB_POSTER_URL, jsonMovie.getString("poster_path")));
                    movie.setBackdropUrl(String.format(TMDB_BACKDROP_URL, jsonMovie.getString("backdrop_path")));
                    movie.setTrailerUrl(getTrailerUrl(context, movieId));
                    movie.setReleaseDate(Util.toDate(jsonMovie.getString("release_date")));
                    movie.setRating((float) jsonMovie.getDouble("vote_average"));
                    movie.setAdult(jsonMovie.getBoolean("adult"));
                    movies.add(movie);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return movies;
    }

    private static String getTrailerUrl(Context context, int movieId) {
        String apiUrl = String.format(TMDB_API_VIDEOS_URL, movieId, context.getString(R.string.tmdb_api_key));
        try {
            JSONArray trailersJson = WEBB.get(apiUrl)
                    .asJsonObject()
                    .getBody()
                    .getJSONArray("results");
            for (int i = 0; i < trailersJson.length(); i++) {
                JSONObject trailerJson = trailersJson.getJSONObject(i);
                if (trailerJson.getString("site").toLowerCase().equals("youtube")) {
                    return "https://youtube.com/watch?v=" + trailerJson.getString("key");
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static List<Movie> filterSuggestedMovies(List<Movie> movies) throws ParseException {
        List<MovieDBModel> allMovies = MovieDatabase.getAllMovies();
        List<Movie> freeMovies = movies;

        for(int a = 0; a<freeMovies.size(); a++) {
            for (MovieDBModel mv : allMovies) {
                if(mv.getTitle().equals(freeMovies.get(a).getTitle())){
                    freeMovies.remove(a);
                    break;
                }
            }
        }

        return freeMovies;
    }

    public static void isMoviesListEmpty(int moviesSize){
        if(moviesSize==0){
            fragment.setEmptyRootView();
        }
    }
}
