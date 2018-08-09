package com.example.msnma.movienotifier.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.msnma.movienotifier.databaseModel.MovieDBModel;
import com.example.msnma.movienotifier.databaseModel.TypeDBModel;
import com.example.msnma.movienotifier.model.Movie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieDatabase extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";
    private static final String DATABASE_NAME = "movieDatabase";
    private static final int DATABASE_VERSION = 1;

    //Table Names
    private static final String TABLE_MOVIE = "MovieTable";
    private static final String TABLE_TYPE = "TypeTable";
    private static final String TABLE_MOVIE_TYPE = "MovieTypeTable";
    //Table Fields
    private static final String MOVIE_ID ="movie_id";
    private static final String TITLE ="title";
    private static final String OVERVIEW = "overview";
    private static final String POSTER_URL = "posterUrl";
    private static final String BACKDROP_URL = "backdropUrl";
    private static final String TRAILER_URL = "trailerUrl";
    private static final String RELEASE_DATE = "releaseDate";
    private static final String RATING = "rating";
    private static final String ADULT = "adult";

    private static final String TYPE_ID = "type_id";
    private static final String TYPE_DESCR = "type_descr";

    private static final String MOVIE_TYPE_ID = "movie_type_id";
    private static final String M_ID = "movie_id";
    private static final String T_ID = "type_id";

    // Table Create Statements
    private static final String CREATE_TABLE_MOVIE = "CREATE TABLE "
            + TABLE_MOVIE + "(" + MOVIE_ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT,"
            + OVERVIEW + " TEXT," + POSTER_URL + " TEXT," + BACKDROP_URL + " TEXT," + TRAILER_URL + " TEXT,"
            + RATING + " REAL," + ADULT + " INTEGER," + RELEASE_DATE + " DATETIME" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_TYPE = "CREATE TABLE " + TABLE_TYPE
            + "(" + TYPE_ID + " INTEGER PRIMARY KEY," + TYPE_DESCR + " TEXT" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_MOVIE_TYPE = "CREATE TABLE "
            + TABLE_MOVIE_TYPE + "(" + MOVIE_TYPE_ID + " INTEGER PRIMARY KEY,"
            + M_ID + " INTEGER," + T_ID + " INTEGER" + ")";

    SQLiteDatabase database;

    public MovieDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_MOVIE);
        db.execSQL(CREATE_TABLE_TYPE);
        db.execSQL(CREATE_TABLE_MOVIE_TYPE);
        insertType(); //NOTA: questo va chiamato solo la prima volta che instanziamo il DB, AGGIUNGERE CONTROLLO!
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE_TYPE);

        // create new tables
        onCreate(db);
    }

    public static void saveMoviesOnDB(List<Movie> movies){

    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    //CRUDs
    public void insertMovie(MovieDBModel movie, Integer typeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, movie.getTitle());
        values.put(OVERVIEW, movie.getOverview());
        values.put(POSTER_URL, movie.getPosterUrl());
        values.put(BACKDROP_URL, movie.getBackdropUrl());
        values.put(TRAILER_URL, movie.getTrailerUrl());
        values.put(RELEASE_DATE, movie.getReleaseDate().toString());
        values.put(RATING, movie.getRating());
        values.put(ADULT, movie.isAdult());

        // insert row
        long movieId = db.insert(TABLE_MOVIE, null, values);
        insertMovieType(movieId, typeId);

    }

    public void insertMovieType(Long movieId, Integer typeId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(M_ID, movieId.intValue());
        values.put(T_ID, typeId);

        db.insert(TABLE_MOVIE_TYPE, null, values);
    }

    private void insertType(){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues descr1 = new ContentValues();
        descr1.put(TYPE_DESCR, "NOTIFY");

        ContentValues descr2 = new ContentValues();
        descr2.put(TYPE_DESCR, "WATCHED");

        // insert row
        db.insert(TABLE_TYPE, null, descr1);
        db.insert(TABLE_TYPE, null, descr2);
    }

    public TypeDBModel getTypeByTypeDescr(String typeDescr) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TYPE + " WHERE "
                + TYPE_DESCR + " = " + typeDescr;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TypeDBModel td = new TypeDBModel();
        td.setId(c.getInt(c.getColumnIndex(TYPE_ID)));
        td.setDescription((c.getString(c.getColumnIndex(TYPE_DESCR))));

        return td;
    }

    public List<MovieDBModel> getAllMovieByType(String typeDescr) throws ParseException {
        List<MovieDBModel> movies = new ArrayList<MovieDBModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_MOVIE + " mv, "
                + TABLE_TYPE + " type, " + TABLE_MOVIE_TYPE + " tmt WHERE type."
                + TYPE_DESCR + " = '" + typeDescr + "'" + " AND type." + TYPE_ID
                + " = " + "tmt." + T_ID + " AND mv." + MOVIE_ID + " = "
                + "tmt." + M_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MovieDBModel td = new MovieDBModel();
                td.setId(c.getInt((c.getColumnIndex(MOVIE_ID))));
                td.setTitle((c.getString(c.getColumnIndex(TITLE))));
                td.setOverview(c.getString(c.getColumnIndex(OVERVIEW)));
                td.setPosterUrl(c.getString((c.getColumnIndex(POSTER_URL))));
                td.setBackdropUrl((c.getString(c.getColumnIndex(BACKDROP_URL))));
                td.setTrailerUrl(c.getString(c.getColumnIndex(TRAILER_URL)));
                String dateString =c.getString((c.getColumnIndex(RELEASE_DATE)));
                Date date = format.parse(dateString);
                td.setReleaseDate(date);
                td.setRating((c.getFloat(c.getColumnIndex(RATING))));
                int adult = c.getInt(c.getColumnIndex(ADULT));
                if(adult == 0){
                    td.setAdult(true);          //NOT sure if is the contrary
                }else{
                    td.setAdult(false);
                }

                movies.add(td);
            } while (c.moveToNext());
        }

        return movies;
    }

    public void deleteMovie(long tado_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIE, MOVIE_ID + " = ?",
                new String[] { String.valueOf(tado_id) });
    }

    public void deleteMovieType(long tado_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIE_TYPE, M_ID + " = ?",
                new String[] { String.valueOf(tado_id) });
    }

    public int updateMovieType(long id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(T_ID, tag_id);

        // updating row
        return db.update(TABLE_MOVIE_TYPE, values, M_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
}
