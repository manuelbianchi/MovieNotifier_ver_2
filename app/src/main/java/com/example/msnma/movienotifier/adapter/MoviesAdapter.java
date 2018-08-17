package com.example.msnma.movienotifier.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.msnma.movienotifier.R;
import com.example.msnma.movienotifier.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movies;
    private View itemView;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Glide.with(context)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.poster_placeholder)
                .into(holder.posterView);
        holder.title_movie.setText(movie.getTitle());
        // prende solo la data + anno
        String yourString = String.valueOf(movie.getReleaseDate());
        String date = yourString.substring(0, 10);
        String year = yourString.substring(yourString.length()-5,yourString.length());
        holder.release_date.setText(date+year);

    }

    //nuovo codice
    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_movie, null);
            holder = new ViewHolder();


            holder.title_movie = (TextView) convertView.findViewById(R.id.movie_title);
            holder.release_date = (TextView) convertView
                    .findViewById(R.id.movie_release_date);

            Movie row_pos = movies.get(position);

            //holder.profile_pic.setImageResource(row_pos.getProfile_pic_id());
            holder.title_movie.setText(row_pos.getTitle());
            holder.release_date.setText((CharSequence) row_pos.getReleaseDate());
            //holder.contactType.setText(row_pos.getContactType());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }*/

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.posterView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster)
        public ImageView posterView;
        //nuovo codice
        @BindView(R.id.movie_title)
        public TextView title_movie;
        @BindView(R.id.movie_release_date)
        public TextView release_date;
        //fine nuovo codice

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
