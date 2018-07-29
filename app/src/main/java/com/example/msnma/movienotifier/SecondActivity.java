package com.example.msnma.movienotifier;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
//import android.widget.SearchView;
import android.widget.Toast;

//import com.miguelcatalan.materialsearchview.MaterialSearchView;
//import com.miguelcatalan.materialsearchview.MaterialSearchView.OnQueryTextListener;
//import com.miguelcatalan.materialsearchview.SearchAdapter;

public class SecondActivity extends AppCompatActivity {
    //MaterialSearchView searchView;
    //SearchView searchView;
    private ListView listView;
    private SearchView searchView;

    //private DbBackend databaseObject;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        /*searchView = (MaterialSearchView) findViewById(R.id.search);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });
        /*int id = searchView.getContext().getResources().getIdentifier("android:id/txtsearch", null, null);
        EditText editText = (EditText) searchView.findViewById(id);
        editText.setOnClickListener(listener);*/
        /*searchViewCode();
        // Enable the Up button*/
        //searchView = (MaterialSearchView) findViewById(R.id.searchView);
        //searchView.setQueryHint("Enter search");
        //databaseObject = new DbBackend(SecondActivity.this);
        //listView = (ListView)findViewById(R.id.listView);
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                List<ItemObject> dictionaryObject = databaseObject.searchDictionaryWords(query);
                SearchAdapter mSearchAdapter = new SearchAdapter(SecondActivity.this, dictionaryObject);
                listView.setAdapter(mSearchAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                    }
                });
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView=(SearchView) findViewById(R.id.searchbox);
        searchView.setQueryHint("Search movie...");
        EditText searchEditText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //searchEditText.setTextColor(getResources().getColor(R.color.black));
        //searchEditText.setHintTextColor(getResources().getColor(R.color.ltblack));
        searchView.setBackgroundColor(Color.DKGRAY);
        searchView.setFocusable(true);// searchView is null
        searchView.setFocusableInTouchMode(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        ab.setDisplayHomeAsUpEnabled(true);
    }
    private void searchViewCode() {

    }   /*click alt+insert key */

    //da cambiare per il menu secondactivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        //MenuItem item = menu.findItem(R.id.search);
        //searchView.setMenuItem(item);
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
        /*if (id == R.id.action_search)
        {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }*/

    /** Called when the user taps the Send button */
    public void pressAccountButton(MenuItem item)
    {
        Intent intent = new Intent(this, AccountActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
