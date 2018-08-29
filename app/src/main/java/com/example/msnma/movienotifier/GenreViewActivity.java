package com.example.msnma.movienotifier;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GenreViewActivity extends AppCompatActivity {

    private ListView lv;
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_genre_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CoreActivity.class));
            }
        });
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        lv = findViewById(R.id.listview);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        ArrayList<String> your_array_list = new ArrayList<String>();

        /*your_array_list.add("foo");
        your_array_list.add("bar");*/

        //your_array_list = AccountActivity.getSelectedString();


        if(getIntent()!=null){
            //your_array_list = getIntent().getStringArrayListExtra("your_list");
            your_array_list= (ArrayList<String>) readList(this, "generi");
        }




        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);

        lv.setAdapter(arrayAdapter1);
    }

    public void pressEditButton(View view)
    {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public static List<String> readList (Context context, String prefix)
    {
        SharedPreferences prefs = context.getSharedPreferences("YourApp", Context.MODE_PRIVATE);

        int size = prefs.getInt(prefix+"_size", 0);

        List<String> data = new ArrayList<String>(size);
        for(int i=0; i<size; i++)
            data.add(prefs.getString(prefix+"_"+i, null));

        return data;
    }

}