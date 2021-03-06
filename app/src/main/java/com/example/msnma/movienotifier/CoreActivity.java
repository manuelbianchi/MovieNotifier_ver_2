package com.example.msnma.movienotifier;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.msnma.movienotifier.adapter.MoviesAdapter;

import com.example.msnma.movienotifier.event.TwoPaneEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class CoreActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static Context context;
    static ProgressDialog pd;

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
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        pd = new ProgressDialog(this);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                SectionsPageAdapter spa = (SectionsPageAdapter) mViewPager.getAdapter();
                //mViewPager.setCurrentItem(position);
                Fragment getFragment = getSupportFragmentManager().getFragments().get(position);
                if (getFragment instanceof MoviesFragment) {
                    MoviesFragment thisFragment = (MoviesFragment) getFragment;
                    MoviesAdapter adapter = new MoviesAdapter(thisFragment.getContext(), thisFragment.movies, spa);
//                    adapter.setFragment(thisFragment);
                    switch (position) {
                        case 0: {
                            adapter.setTipo(String.valueOf(MoviesFragment.Type.NOTIFY));
                            break;
                        }
                        case 1: {
                            adapter.setTipo(String.valueOf(MoviesFragment.Type.SUGGESTED));
                            break;
                        }
                        case 2: {
                            adapter.setTipo(String.valueOf(MoviesFragment.Type.WATCHED));
                            break;
                        }
                    }
                    thisFragment.moviesView.setAdapter(adapter);
                }
                //Toast.makeText(CoreActivity.this,"Tab "+position,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels ) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }


        });
        mViewPager.setCurrentItem(0);

        EventBus.getDefault().postSticky(new TwoPaneEvent(twoPane));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        //MenuItem item = menu.findItem(R.id.search);
        //searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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



    public void pressAccountButton(MenuItem item) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    /** Called when the user taps the Send button */
    public void pressAddButton(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    //nuovo codice
    @Override
    protected void onResume() {
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

    public static ProgressDialog getPd() {
        return pd;
    }

    /*public static void notifyPush(String message, Context context)
    {
        /*Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Intent intent = new Intent(context,NotificationReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //nuovo codice
        Intent intent = new Intent(CoreActivity.this , NotificationReceiver.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(CoreActivity.this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 hours



        //fine nuovo codice
        Notification notification = new Notification.Builder(context)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Notifica")
                .setContentText("Abbiamo le notifiche").
                        addAction(R.drawable.ic_launcher_foreground, "Open", pendingIntent)
                .addAction(0, "Remind", pendingIntent).build();

        NotificationManager notifManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notifManager.notify(0, notification);*/
        //codice di un altro programma

        // Make a channel if necessary
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = Constants.VERBOSE_NOTIFICATION_CHANNEL_NAME;
            String description = Constants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Add the channel
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(Constants.NOTIFICATION_TITLE)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(DEFAULT_ALL);
                //.setVibrate(new long[0]);

        // Show the notification
        NotificationManagerCompat.from(context).notify(Constants.NOTIFICATION_ID, builder.build());
    }*/

}




