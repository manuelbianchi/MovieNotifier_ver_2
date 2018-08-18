package com.example.msnma.movienotifier.event;


import com.example.msnma.movienotifier.model.Movie;

public class ShowMovieEvent {
    public final Movie movie;

    public ShowMovieEvent(Movie movie) {

        this.movie = movie;
    }
}
