package com.example.msnma.movienotifier;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentPagerAdapter
{

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
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
                return MoviesFragment.newInstance(MoviesFragment.Type.NOTIFY);
            case 1:
                return MoviesFragment.newInstance(MoviesFragment.Type.SUGGESTED);
            case 2:
                return MoviesFragment.newInstance(MoviesFragment.Type.WATCHED);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
