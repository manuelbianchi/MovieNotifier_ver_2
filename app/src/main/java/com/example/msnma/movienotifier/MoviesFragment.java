package com.example.msnma.movienotifier;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msnma.movienotifier.adapter.MoviesAdapter;
import com.example.msnma.movienotifier.callback.MoviesCallback;
import com.example.msnma.movienotifier.event.ShowMovieEvent;
import com.example.msnma.movienotifier.event.TwoPaneEvent;
import com.example.msnma.movienotifier.event.UpdateFavoritesEvent;
import com.example.msnma.movienotifier.model.Movie;
import com.example.msnma.movienotifier.util.MoviesUtil;
import com.rohit.recycleritemclicksupport.RecyclerItemClickSupport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

import static android.support.design.widget.TabLayout.*;

public class MoviesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerItemClickSupport.OnItemClickListener {
    private static final String ARG_FRAG_TYPE = "fragType";
    private static final String ARG_FRAG_TWO_PANE = "twoPane";
    private Context context;


    public enum Type {
        NOTIFY,
        SUGGESTED,
        WATCHED
    }

    @State
    ArrayList<Movie> movies;
    @State
    Type fragType;
    @State
    boolean twoPane;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshView;
    @BindView(R.id.movies)
    RecyclerView moviesView;

    View rootView;

    public static MoviesFragment newInstance(Type fragType, boolean twoPane) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FRAG_TYPE, fragType);
        args.putBoolean(ARG_FRAG_TWO_PANE, twoPane);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        if (getArguments() != null) {
            fragType = (Type) getArguments().getSerializable(ARG_FRAG_TYPE);
            twoPane = getArguments().getBoolean(ARG_FRAG_TWO_PANE);
        } else {
            fragType = Type.SUGGESTED;
            twoPane = true;
        }
        //onRefresh();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movies_list, container, false);
        ButterKnife.bind(this, rootView);



        //onRefresh();
        init();


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

    @Override
    public void onRefresh()
    {
        movies = null;
        updateMovies();
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        showMovieAtPosition(position);
    }

    //proviamo questo codice
        //@Subscribe(sticky = true)
        //public void onEvent(UpdateFavoritesEvent event) {
        //    if (fragType == Type.WATCHED) {
        //        EventBus.getDefault().removeStickyEvent(UpdateFavoritesEvent.class);
        //        onRefresh();
        //   }
        //}

    @Subscribe(sticky = true)
    public void onEvent(TwoPaneEvent event) {
       twoPane = event.twoPane;
    }

    @Override
    protected void init() {
        RecyclerItemClickSupport.addTo(moviesView)
                .setOnItemClickListener(this);
        moviesView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        moviesView.setHasFixedSize(true);
        refreshView.setOnRefreshListener(this);
        updateMovies();
    }

    private void updateMovies(){
        if (movies == null) {
            MoviesCallback callback = new MoviesCallback() {
                @Override
                public void success(List<Movie> result) {
                    movies = new ArrayList<>(result);
                    if (moviesView != null) {
                        moviesView.setAdapter(new MoviesAdapter(getContext(), movies));
                    }
                    refreshView.setRefreshing(false);
                }

                @Override
                public void error(Exception error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                    refreshView.setRefreshing(false);
                }
            };
            switch (fragType) {
                case NOTIFY:
                    MoviesUtil.getNotifyMeMovies(getActivity(), callback);
                    break;
                case SUGGESTED:
                    MoviesUtil.getSuggestedMovies(getActivity(), callback);
                    break;
                case WATCHED:
                    MoviesUtil.getWatchedMovies(getActivity(), callback);
                    break;
            }
        } else if (moviesView != null) {
            moviesView.setAdapter(new MoviesAdapter(getContext(), movies));
            refreshView.setRefreshing(false);
        }
    }

    private void showMovieAtPosition(int position) {
        if (movies != null && position <= movies.size() - 1) {
            Movie movie = movies.get(position);
            EventBus.getDefault().postSticky(new ShowMovieEvent(movie));
            if (twoPane) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail, new MovieFragment())
                        .commit();
            } else {
                startActivity(new Intent(getContext(), MovieActivity.class));
            }
        }
    }

    /*public void pressEditNotifyButton(View view)
    {
        alertFormElements();
    }*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("INFO","Refresh eseguito con successo");
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
        //Toast.makeText(context,"Benvenuto", Toast.LENGTH_LONG).show();
    }


}
