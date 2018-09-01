package com.example.msnma.movienotifier;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
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

public class MoviesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerItemClickSupport.OnItemClickListener {
    private static final String ARG_FRAG_TYPE = "fragType";
    private static final String ARG_FRAG_TWO_PANE = "twoPane";
    private static final String ARG_FRAG_QUERY = "query";

    public enum Type {
        NOTIFY,
        SUGGESTED,
        WATCHED,
        SEARCH,
        SUGGESTED_SEARCH
    }

    @State
    ArrayList<Movie> movies;
    @State
    Type fragType;
    @State
    boolean twoPane;
    String query;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshView;
    @BindView(R.id.movies)
    RecyclerView moviesView;

    View rootView;
    static MoviesFragment fragmentInstance;

    public static MoviesFragment newInstance(Type fragType, boolean twoPane) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FRAG_TYPE, fragType);
        args.putBoolean(ARG_FRAG_TWO_PANE, twoPane);
        fragment.setArguments(args);
        fragmentInstance = fragment;
        return fragment;
    }

    public static MoviesFragment newInstance(Type fragType, boolean twoPane, String query) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FRAG_TYPE, fragType);
        args.putBoolean(ARG_FRAG_TWO_PANE, twoPane);
        args.putString(ARG_FRAG_QUERY, query);
        fragment.setArguments(args);
        fragmentInstance = fragment;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        if (getArguments() != null) {
            fragType = (Type) getArguments().getSerializable(ARG_FRAG_TYPE);
            twoPane = getArguments().getBoolean(ARG_FRAG_TWO_PANE);
            if(fragType.toString().equals("SEARCH")){
                query = getArguments().getString(ARG_FRAG_QUERY);
            }
        } else {
            fragType = Type.SUGGESTED;
            twoPane = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movies_list, container, false);
        ButterKnife.bind(this, rootView);
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
    public void onRefresh() {
        movies = null;
        updateMovies();
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        showMovieAtPosition(position);
    }

    //    @Subscribe(sticky = true)
    //    public void onEvent(UpdateFavoritesEvent event) {
    //        if (fragType == Type.FAVORITES) {
    //            EventBus.getDefault().removeStickyEvent(UpdateFavoritesEvent.class);
    //            onRefresh();
    //       }
    //    }

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
                        MoviesAdapter adapter = new MoviesAdapter(getContext(), movies, fragmentInstance);
                        if(fragType.toString().equals("SEARCH") || fragType.toString().equals("SUGGESTED")){
                            adapter.setTipo(String.valueOf(MoviesFragment.Type.SUGGESTED));
                        }else if(fragType.toString().equals("NOTIFY")){
                            adapter.setTipo(String.valueOf(Type.NOTIFY));
                        }else{
                            adapter.setTipo(String.valueOf(Type.WATCHED));
                        }
                        moviesView.setAdapter(adapter);
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
                case SEARCH:
                    MoviesUtil.getSeachMovies(getActivity(), query, callback);
                    break;
            }
        } else if (moviesView != null) {
            moviesView.setAdapter(new MoviesAdapter(getContext(), movies, fragmentInstance));
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

    public void setArgFragType(Type type){
        fragType = type;
    }

}
