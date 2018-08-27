package com.example.msnma.movienotifier;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.msnma.movienotifier.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends FragmentStatePagerAdapter
{
    String query;

    public SearchAdapter(FragmentManager fm, String query) {
        super(fm);
        this.query = query;
    }

    @Override
    public Fragment getItem(int position) {
        return MoviesFragment.newInstance(MoviesFragment.Type.SEARCH, false, query);
    }

    @Override
    public int getCount() {
        return 1;
    }

}
