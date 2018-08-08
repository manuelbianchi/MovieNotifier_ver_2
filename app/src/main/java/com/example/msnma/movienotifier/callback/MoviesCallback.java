package com.example.msnma.movienotifier.callback;

import com.example.msnma.movienotifier.model.Movie;

import java.util.List;

public interface MoviesCallback {

    void success(List<Movie> movies);

    void error(Exception error);

}
