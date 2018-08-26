package com.example.msnma.movienotifier;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.msnma.movienotifier.adapter.MoviesAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentStatePagerAdapter
{
    boolean twoPane;

    public SectionsPageAdapter(FragmentManager fm, boolean twoPane) {
        super(fm);
        this.twoPane = twoPane;


    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Notify";        //todo this should referred to a string in the string.xml file
            case 1:
                return "Suggested";
            case 2:
                return "Watched";
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MoviesFragment.newInstance(MoviesFragment.Type.NOTIFY, twoPane);
            case 1:
                return MoviesFragment.newInstance(MoviesFragment.Type.SUGGESTED, twoPane);
            case 2:
                return MoviesFragment.newInstance(MoviesFragment.Type.WATCHED, twoPane);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
