package com.example.msnma.movienotifier.databaseModel;

import java.util.Date;

public class MovieDBModel {

    private int id;
    private String title;
    private String overview;
    private String posterUrl;
    private String backdropUrl;
    private String trailerUrl;
    private Date releaseDate;
    private float rating;
    private boolean adult;
    private Date notifyDate;

    public Date getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Date notifyDate) {
        this.notifyDate = notifyDate;
    }



    public MovieDBModel(){
    }

    public MovieDBModel(int id, String title,  String overview, String posterUrl, String backdropUrl, String trailerUrl,
                        Date releaseDate, float rating, boolean adult, Date notifyDate ){
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterUrl = posterUrl;
        this.backdropUrl = backdropUrl;
        this.trailerUrl = trailerUrl;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.adult = adult;
        this.notifyDate = notifyDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }
}
