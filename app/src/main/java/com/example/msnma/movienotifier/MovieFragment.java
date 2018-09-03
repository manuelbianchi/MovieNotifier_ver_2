package com.example.msnma.movienotifier;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.msnma.movienotifier.event.ShowMovieEvent;
import com.example.msnma.movienotifier.model.Movie;
//import com.example.msnma.movienotifier.notify.NotificationReceiver;
import com.example.msnma.movienotifier.util.MoviesUtil;
import com.example.msnma.movienotifier.util.Util;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.State;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

public class MovieFragment extends BaseFragment {

    Context context;
    //Button showNotif;
    //Button cancelNotif;
    @State
    Movie movie;

    @BindView(R.id.backdrop)
    ImageView backdropView;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.release_date)
    TextView releaseDateView;
    @BindView(R.id.rating)
    TextView ratingView;
    @BindView(R.id.overview)
    TextView overviewView;

//    @BindView(R.id.favorite)
//    FloatingActionButton favoriteView;        todo change this in the way that will handle the add to watched list

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, rootView);

        //parte push
        //showNotif = (Button) findViewById(R.id.show);
        //cancelNotif = (Button) findViewById(R.id.cancel);
        /*showNotif.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Intent intent = new Intent(getActivity(),NotificationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
                Notification notification = new Notification.Builder(getActivity())
                        .setSound(soundUri)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Notifica")
                        .setContentText("Abbiamo le notifiche").
                                addAction(R.drawable.ic_launcher_foreground, "Open", pendingIntent)
                        .addAction(0, "Remind", pendingIntent).build();

                NotificationManager notifManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                notifManager.notify(0, notification);
            }
        });*/

        /*cancelNotif.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(NOTIFICATION_SERVICE != null) {
                    String ns = NOTIFICATION_SERVICE;
                    NotificationManager notifManager = (NotificationManager) context.getSystemService(ns);
                    notifManager.cancel(0); // The notification id is being passed as argument
                }

            }
        });*/


        if (movie != null) {
            init();
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
         EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true)
    public void onEvent(ShowMovieEvent event) {
       movie = event.movie;
       init();
    }

    @OnClick(R.id.trailer)
    public void playTrailer() {
        Util.openLinkInExternalApp(getContext(), movie.getTrailerUrl());
    }

//    @OnClick(R.id.watched)
//    public void toggleFavorite() {
//        boolean isFavorite = MoviesUtil.toggleFavorite(getContext(), movie);
//        updateFavoriteFab(isFavorite);
//         EventBus.getDefault().postSticky(new UpdateFavoritesEvent());
//    }

    @Override
    protected void init() {
        Glide.with(getContext())
                .load(movie.getBackdropUrl())
                .into(backdropView);
        titleView.setText(movie.getTitle());
        releaseDateView.setText(Util.toPrettyDate(movie.getReleaseDate()));
        ratingView.setText(movie.getRating() + "");
        overviewView.setText(movie.getOverview());
//        updateFavoriteFab(MoviesUtil.isWatched(getContext(), movie));
    }

//    private void updateFavoriteFab(boolean isFavorite) {
//        GoogleMaterial.Icon favoriteIcon = isFavorite ?
//                GoogleMaterial.Icon.gmd_favorite : GoogleMaterial.Icon.gmd_favorite_border;
//        favoriteView.setImageDrawable(new IconicsDrawable(getContext())
//                .icon(favoriteIcon)
//                .color(Color.WHITE)
//                .sizeDp(48));
//    }

    public void shareMovie() {
        String text = String.format("%s\n%s", movie.getTitle(), movie.getTrailerUrl());
        Util.shareText(getActivity(), text);
    }


}
