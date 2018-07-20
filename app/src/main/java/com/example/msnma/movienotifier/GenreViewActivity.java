package com.example.msnma.movienotifier;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GenreViewActivity extends AppCompatActivity {

    //AccountActivity aa = new AccountActivity();

    private ListView lv;
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_genre_view);
        lv = findViewById(R.id.listview);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        ArrayList<String> your_array_list = new ArrayList<String>();
        /*your_array_list.add("foo");
        your_array_list.add("bar");*/

        //your_array_list = AccountActivity.getSelectedString();


        if(getIntent()!=null){
            your_array_list = getIntent().getStringArrayListExtra("your_list");
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

}
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_view);
    }*/

