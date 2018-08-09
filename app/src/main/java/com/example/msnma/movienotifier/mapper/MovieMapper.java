package com.example.msnma.movienotifier.mapper;

import com.example.msnma.movienotifier.databaseModel.MovieDBModel;
import com.example.msnma.movienotifier.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieMapper {

    public Movie toMovie(MovieDBModel movieDb){
        Movie movie = new Movie();
        movie.setId(movieDb.getId());
        movie.setTitle(movieDb.getTitle());
        movie.setOverview(movieDb.getOverview());
        movie.setBackdropUrl(movieDb.getBackdropUrl());
        movie.setPosterUrl(movieDb.getPosterUrl());
        movie.setTrailerUrl(movieDb.getTrailerUrl());
        movie.setReleaseDate(movieDb.getReleaseDate());
        movie.setRating(movieDb.getRating());
        movie.setAdult(movieDb.isAdult());
        return movie;
    }

    public MovieDBModel toMovieDBModel(Movie movieDb){
        MovieDBModel movie = new MovieDBModel();
        movie.setId(movieDb.getId());
        movie.setTitle(movieDb.getTitle());
        movie.setOverview(movieDb.getOverview());
        movie.setBackdropUrl(movieDb.getBackdropUrl());
        movie.setPosterUrl(movieDb.getPosterUrl());
        movie.setTrailerUrl(movieDb.getTrailerUrl());
        movie.setReleaseDate(movieDb.getReleaseDate());
        movie.setRating(movieDb.getRating());
        movie.setAdult(movieDb.isAdult());
        return movie;
    }

    public List<Movie> toMovieList(List<MovieDBModel> movieDBs){
        List<Movie> movies = new ArrayList<Movie>();
        for(MovieDBModel mDb : movieDBs){
            movies.add(toMovie(mDb));
        }
        return movies;
    }

    public List<MovieDBModel> toMovieDBModelList(List<Movie> movie){
        List<MovieDBModel> movies = new ArrayList<MovieDBModel>();
        for(Movie m : movie){
            movies.add(toMovieDBModel(m));
        }
        return movies;
    }
}
