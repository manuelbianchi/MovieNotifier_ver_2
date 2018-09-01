package com.example.msnma.movienotifier;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
