package com.example.msnma.movienotifier.provider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.msnma.movienotifier.database.MovieDatabase;
import com.tjeannin.provigen.ProviGenOpenHelper;
import com.tjeannin.provigen.ProviGenProvider;

import static java.security.AccessController.getContext;

public class MovieContentProvider extends ProviGenProvider {

    private static final String DB_NAME = "movies";

    private static final String AUTHORITY = "com.example.msnma.movienotifier.provider.MovieContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + MovieDatabase.TITLE);

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
