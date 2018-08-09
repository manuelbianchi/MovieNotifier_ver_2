package com.example.msnma.movienotifier.databaseModel;

public class TypeDBModel {

    private int id;
    private String descr;

    public TypeDBModel(){
    }

    public TypeDBModel(int id, String descr){
        this.id = id;
        this.descr = descr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return descr;
    }

    public void setDescription(String descr) {
        this.descr = descr;
    }
}
