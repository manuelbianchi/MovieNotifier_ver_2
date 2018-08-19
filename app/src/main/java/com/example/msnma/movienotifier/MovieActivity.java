package com.example.msnma.movienotifier;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends BaseActivity {

    private MovieFragment movieFrag;



    //parte per il popup data


    //private DatePickerDialog fromDatePickerDialog;
    //private DatePickerDialog toDatePickerDialog;

    //private SimpleDateFormat dateFormatter;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie, menu);
        menu.findItem(R.id.share)
                .setIcon(new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_share)
                        .color(Color.WHITE)
                        .sizeDp(24));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.share:
                if (movieFrag != null) {
                    movieFrag.shareMovie();
                }
                break;
        }
        return true;
    }

    @Override
    protected void init() {
        movieFrag = new MovieFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, movieFrag)
                .commit();
    }

    //nuovo codice per alert dialog














    /*@Override
    public void onClick(View view) {
        if(view == fromDateEtxt)
            fromDatePickerDialog.show();
    }*/
    //Intent myIntent = new Intent(view.getContext(), agones.class);
            //startActivityForResult(myIntent, 0);





}
