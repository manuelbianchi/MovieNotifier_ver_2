package com.example.msnma.movienotifier.provider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.tjeannin.provigen.ProviGenOpenHelper;
import com.tjeannin.provigen.ProviGenProvider;

import static java.security.AccessController.getContext;

public class MovieContentProvider extends ProviGenProvider {

    private static final String DB_NAME = "movies";
    private static final Class[] CONTRACTS = new Class[]{
            MovieContract.class
    };

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new ProviGenOpenHelper(getContext(), DB_NAME, null, 1, CONTRACTS);
    }

    @Override
    public Class[] contractClasses() {

        return CONTRACTS;
    }
}
