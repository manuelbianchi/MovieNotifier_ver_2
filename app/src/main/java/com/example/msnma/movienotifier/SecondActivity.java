package com.example.msnma.movienotifier;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.msnma.movienotifier.adapter.MoviesAdapter;
import com.example.msnma.movienotifier.event.TwoPaneEvent;
import com.example.msnma.movienotifier.model.Movie;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;

public class SecondActivity extends AppCompatActivity {

    SearchView searchView;
    SearchAdapter searchAdapter;
    @BindView(R.id.movies)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView=(SearchView) findViewById(R.id.searchbox);
        searchView.setQueryHint("Search movie...");

//        EditText searchEditText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setBackgroundColor(Color.DKGRAY);
        searchView.setFocusable(true);// searchView is null
        searchView.setIconified(false);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        ab.setDisplayHomeAsUpEnabled(true);

        searchAdapter = new SearchAdapter(getSupportFragmentManager(), "");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(searchAdapter);

        EventBus.getDefault().postSticky(new TwoPaneEvent(false));

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchMovies(query);
        }
    }

    private void searchMovies(String query) {
//        SQLiteHelper sqLiteHelper = ((MyApplication)getApplication()).getDbHelper();
//              Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery("SELECT " + DatabaseConstants.COL_LANG_ID + ", " +
//                DatabaseConstants.COL_LANG_NAME + " FROM " + DatabaseConstants.TABLE_LANG +
//                " WHERE upper(" + DatabaseConstants.COL_LANG_NAME + ") like '%" + query.toUpperCase() + "%'", null);
//        setListAdapter(new SimpleCursorAdapter(this, R.layout.container_list_item_view, cursor,
//                new String[] {DatabaseConstants.COL_LANG_NAME }, new int[]{R.id.list_item}));

        searchAdapter = new SearchAdapter(getSupportFragmentManager(), query);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(searchAdapter);
        searchView.setFocusable(false);
        searchView.setIconified(true);
        EventBus.getDefault().postSticky(new TwoPaneEvent(false));
    }

    //da cambiare per il menu secondactivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu)       //this is a callback
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        // get a reference to the SearchableInfo, which represent the search configuration
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);

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
