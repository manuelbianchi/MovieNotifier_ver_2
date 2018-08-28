package com.example.msnma.movienotifier;


import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.example.msnma.movienotifier.MoviesFragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.msnma.movienotifier.adapter.MoviesAdapter;
import com.example.msnma.movienotifier.notify.NotificationReceiver;

import com.example.msnma.movienotifier.event.TwoPaneEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;
import static com.google.common.reflect.Reflection.initialize;
import static java.security.AccessController.getContext;

public class CoreActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {


    SectionsPageAdapter mSectionsPageAdapter;
    @BindView(R.id.movies)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail) != null) {
            twoPane = true;
        }
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager(), twoPane);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mSectionsPageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        final int numTab = tab.getPosition();

                        //Toast.makeText(getApplicationContext(),tab.getText(), Toast.LENGTH_LONG).show();
                         switch (tab.getPosition()) {
                             case 0: {

                                 MoviesAdapter.setTipo(String.valueOf(MoviesFragment.Type.NOTIFY));
                                 break;
                             }
                             case 1: {
                                 MoviesAdapter.setTipo(String.valueOf(MoviesFragment.Type.SUGGESTED));
                                 break;
                             }

                             case 2: {

                                 MoviesAdapter.setTipo(String.valueOf(MoviesFragment.Type.WATCHED));
                                 break;
                             }

                         }


                    }

                });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                               @Override
                                               public void onPageSelected(int position) {

                                                   //mViewPager.setCurrentItem(position);
                                                   // ci sono vicinissimo alla soluzione.
                                                   if(position == 0 || position == 2) {
                                                       Fragment getFragment = getSupportFragmentManager().getFragments().get(position);
                                                       if (getFragment instanceof MoviesFragment) {
                                                           MoviesFragment thisFragment = (MoviesFragment) getFragment;
                                                           thisFragment.onRefresh();
                                                           Log.i("REFRESH","Dovrebbe essere refreshato");
                                                       }
                                                       Log.i("PAGINASELEZIONATA", "Esegue onResume " + position);

                                                   }
                                                       //Toast.makeText(CoreActivity.this,"Tab "+position,Toast.LENGTH_LONG).show();





                                               }

                                               @Override
                                               public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels )
                                               {
                                                   Fragment getFragment = getSupportFragmentManager().getFragments().get(position);
                                                   if (getFragment instanceof MoviesFragment) {
                                                       MoviesFragment thisFragment = (MoviesFragment) getFragment;
                                                       if ((position == 0 || position == 2) && positionOffset == 0.0 && positionOffsetPixels == 0)
                                                           thisFragment.onRefresh();
                                                   }
                                               }

                                               @Override
                                               public void onPageScrollStateChanged(int arg0) {
                                               }


                                           });
                EventBus.getDefault().postSticky(new TwoPaneEvent(twoPane));
    }

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



    public void pressAccountButton(MenuItem item)
    {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    /** Called when the user taps the Send button */
    public void pressAddButton(View view)
    {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    //nuovo codice
    @Override
    protected void onResume()
    {
        super.onResume();
        //Toast.makeText(getBaseContext(),"PORCO DIO",Toast.LENGTH_LONG).show();
        if (!(mSectionsPageAdapter == null)) {
            mSectionsPageAdapter.notifyDataSetChanged();
            Log.i("INFOonResume", "Metodo onResume eseguito");
        }


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //Log.i("INFOTAB", "La tabella selezionata è "+tab.getTag());
        onResume();


    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }



}
