package com.example.msnma.movienotifier;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.msnma.movienotifier.database.MovieDatabase;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity
{
    public static MainActivity mainActivity;
    public static MovieDatabase db;
    //private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new MovieDatabase(this);
        mainActivity = this;
        //se la lista è vuota parte la main activity
        try {
            if(GenreViewActivity.readList(this, "generi").isEmpty() && db.isEmpty()) {
                setContentView(R.layout.activity_main);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
            } else {
                Intent intent = new Intent(this, CoreActivity.class);
                startActivity(intent);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    /** Called when the user taps the Send button */
    public void pressAddButton(View view)
    {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }


    /** Called when the user taps the Send button */
    /*public void pressAccountButton(View view)
    {
        Intent intent = new Intent(this, AccountActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void pressAccountButton(MenuItem item) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public static MainActivity getInstance(){
        return mainActivity;
    }

    public static MovieDatabase getMovieDatabase(){
        return db;
    }
}
