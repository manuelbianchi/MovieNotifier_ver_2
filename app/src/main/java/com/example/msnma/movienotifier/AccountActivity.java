package com.example.msnma.movienotifier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.msnma.movienotifier.GenreViewActivity.readList;

public class AccountActivity extends AppCompatActivity {

    //private ViewHolder finalViewHolder;
    private List<Item> list;
    private ViewHolder viewHolder;


    public class Item {
        boolean checked;
        Drawable ItemDrawable;
        String ItemString;
        Item(/*Drawable drawable,*/ String t, boolean b){
            //ItemDrawable = drawable;
            ItemString = t;
            checked = b;
        }

        public boolean isChecked(){
            return checked;
        }
    }

    static class ViewHolder {
        CheckBox checkBox;
        ImageView icon;
        TextView text;
    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        /*messo nella classe
        private List<Item> list;*/

        ItemsListAdapter(Context c, List<Item> l) {
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            ViewHolder viewHolder = new ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.row, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
                //viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            //viewHolder.icon.setImageDrawable(list.get(position).ItemDrawable);
            viewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr = list.get(position).ItemString;
            viewHolder.text.setText(itemStr);

            viewHolder.checkBox.setTag(position);

            ArrayList<String> your_array_list = new ArrayList<String>();
            your_array_list= (ArrayList<String>) GenreViewActivity.readList( context, "generi");
            final ViewHolder finalViewHolder = viewHolder;
            if(!your_array_list.isEmpty())
            {
                for (int i = 0; i < items.size(); i++) {
                    if(your_array_list.contains(items.get(i).ItemString))
                    {
                        list.get(i).checked = true;
                        finalViewHolder.checkBox.setChecked(list.get(i).isChecked());
                    }
                }
            }

            /*
            viewHolder.checkBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    list.get(position).checked = b;

                    Toast.makeText(getApplicationContext(),
                            itemStr + "onCheckedChanged\nchecked: " + b,
                            Toast.LENGTH_LONG).show();
                }
            });
            */

            //final ViewHolder finalViewHolder = viewHolder;
            //andiamo a prenderci l'array salvato e impostiamolo.
            viewHolder.text.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;
                        finalViewHolder.checkBox.setChecked(list.get(position).isChecked());
                }

            });



            viewHolder.checkBox.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;
                }

            });




                viewHolder.checkBox.setChecked(isChecked(position));


            return rowView;
        }


    }


    Button btnLookup;
    Button btnDone;
    List<Item> items;
    ListView listView;
    ItemsListAdapter myItemsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
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

        listView = (ListView)findViewById(R.id.listview);
        btnLookup = (Button)findViewById(R.id.lookup);
        btnDone = (Button) findViewById(R.id.done);

        initItems();
        myItemsListAdapter = new ItemsListAdapter(this, items);
        listView.setAdapter(myItemsListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(AccountActivity.this,
                        ((Item)(parent.getItemAtPosition(position))).ItemString,
                        Toast.LENGTH_LONG).show();
            }});

        btnLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "Check items:\n";

                for (int i=0; i<items.size(); i++){
                    if (items.get(i).isChecked()){
                        //str += i + "\n";
                        str += items.get(i).ItemString;
                    }
                }

                /*
                int cnt = myItemsListAdapter.getCount();
                for (int i=0; i<cnt; i++){
                    if(myItemsListAdapter.isChecked(i)){
                        str += i + "\n";
                    }
                }
                */

                Toast.makeText(AccountActivity.this,
                        str,
                        Toast.LENGTH_LONG).show();

            }
        });
    }

    private void initItems(){
        items = new ArrayList<Item>();

        TypedArray arrayDrawable = getResources().obtainTypedArray(R.array.resicon);
        TypedArray arrayText = getResources().obtainTypedArray(R.array.restext);

        for(int i=0; i<arrayDrawable.length(); i++){
            //Drawable d = arrayDrawable.getDrawable(i);
            String s = arrayText.getString(i);
            boolean b = false;
            Item item = new Item(/*d,*/ s, b);
            items.add(item);
        }

        arrayDrawable.recycle();
        arrayText.recycle();
    }

    public void pressDoneButton(View view)
    {
        //Arraylist di tipo stringa con elementi checked
        ArrayList <String> itemsChkStr = new ArrayList<String>();

        for (int i=0; i<items.size(); i++){
            if (items.get(i).isChecked()){
                //str += i + "\n";
                itemsChkStr.add(items.get(i).ItemString);
            }
        }
        //itemsChecks();
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        //itemsChecks();
        Intent intent = new Intent(this, GenreViewActivity.class);

        //funzione chiamata per salvare i dati
        writeList(this, itemsChkStr, "generi");


        intent.putStringArrayListExtra("your_list",itemsChkStr);
        startActivity(intent);
    }

    /*public ArrayList <String> itemsChecks()
    {
        ArrayList <String> arrGenre = new ArrayList<String>();
        for (int i=0; i<items.size(); i++){
            if (items.get(i).isChecked()){
                arrGenre.add(items.get(i).toString());
            }
        }
        viewArray(arrGenre);

    }*/


    //nuovo codice per salvare i dati
    public static void writeList(Context context, List<String> list, String prefix)
    {
        SharedPreferences prefs = context.getSharedPreferences("YourApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int size = prefs.getInt(prefix+"_size", 0);

        // clear the previous data if exists
        for(int i=0; i<size; i++)
            editor.remove(prefix+"_"+i);

        // write the current list
        for(int i=0; i<list.size(); i++)
            editor.putString(prefix+"_"+i, list.get(i));

        editor.putInt(prefix+"_size", list.size());
        editor.commit();
    }



    /* esempio */
/*
    List<String> animals = new ArrayList<String>();
animals.add("cat");
animals.add("bear");
animals.add("dog");

    writeList(someContext, animals, "animal");*/


/* come riprendere i dati */

    //List<String> animals = readList (someContext, "animal");




}
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
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
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/

