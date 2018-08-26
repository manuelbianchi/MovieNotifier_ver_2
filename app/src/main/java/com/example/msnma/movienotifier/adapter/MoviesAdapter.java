package com.example.msnma.movienotifier.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.msnma.movienotifier.MainActivity;
import com.example.msnma.movienotifier.MoviesFragment;
import com.example.msnma.movienotifier.R;
import com.example.msnma.movienotifier.database.MovieDatabase;
import com.example.msnma.movienotifier.databaseModel.MovieDBModel;
import com.example.msnma.movienotifier.model.Movie;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

import static android.app.PendingIntent.getActivity;
import static com.example.msnma.movienotifier.MoviesFragment.*;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>{

    private Context context;
    private List<Movie> movies;
    private View itemView;
    private RecyclerView rv;

    //per la data
    private EditText fromDateEtxt;
    private boolean active = false;
    //private Context context;
    private static String tipo = "NOTIFY";

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

            View itemView;
            /*if(getTipo().equals("Suggested"))
            {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
                return new ViewHolder(itemView);
            }
                else if(getTipo().equals("Watched"))
                {
                    itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_watched, parent, false);
                    return new ViewHolder(itemView);
                }
                else if(getTipo().equals("Notify"))
            {*/

                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);

                return new ViewHolder(itemView);
            //}


        //return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
        if(getTipo().equals("NOTIFY"))
        {
            Toast.makeText(context,"Notify", Toast.LENGTH_LONG).show();
            holder.notifyButton.setVisibility(View.GONE);
            holder.watchedButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.VISIBLE);
        }
        //solo se è di tipo suggested
        if(getTipo().equals("SUGGESTED"))
        {
            //disabilitare bottone remove


            holder.removeButton.setVisibility(View.GONE);
            holder.notifyButton.setVisibility(View.VISIBLE);
            holder.watchedButton.setVisibility(View.VISIBLE);
            Toast.makeText(context,"Suggested", Toast.LENGTH_LONG).show();

            holder.notifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                /*String testo = movies.get(position).getTitle().toString();
               Toast tostato = Toast.makeText(context,testo,Toast.LENGTH_SHORT);
                tostato.show();*/
                    alertFormElements(position);
                }
            });
            holder.watchedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                /*String testo = movies.get(position).getTitle()+ "\n" + "I WATCH IT";
                Toast tostato = Toast.makeText(context,testo,Toast.LENGTH_SHORT);
                tostato.show();*/

                    // public MovieDBModel(int id, String title,  String overview, String posterUrl, String backdropUrl, String trailerUrl,
                    //                        Date releaseDate, float rating, boolean adult){
                    MovieDBModel mdm = new MovieDBModel(movies.get(position).getId(), movies.get(position).getTitle(), movies.get(position).getOverview(),
                            movies.get(position).getPosterUrl(), movies.get(position).getBackdropUrl(), movies.get(position).getTrailerUrl(),
                            movies.get(position).getReleaseDate(), movies.get(position).getRating(), movies.get(position).isAdult());
                    MovieDatabase.insertMovie(mdm, 2, MainActivity.getMovieDatabase());
                    String testo = "Added " + movies.get(position).getTitle() + "\n" + "in tab watched";
                    Toast tostato = Toast.makeText(context, testo, Toast.LENGTH_SHORT);
                    tostato.show();
                }
            });
         }
         if (getTipo().equals("WATCHED"))
        {


            holder.notifyButton.setVisibility(View.GONE);
            holder.watchedButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.VISIBLE);


            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                 //da implementare
                }
            });
            Toast.makeText(context,"WATCHED", Toast.LENGTH_LONG).show();
        }





    }


    //nuovo codice riguardo l'alerDialog

    public final void alertFormElements(final int position)
    {

        /*
         * Inflate the XML view. activity_main is in
         * res/layout/form_elements.xml
         */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.form_elements,
                null, false);

        // You have to list down your form elements
        /*final CheckBox myCheckBox = (CheckBox) formElementsView
                .findViewById(R.id.myCheckBox);*/

        final RadioGroup genderRadioGroup = (RadioGroup) formElementsView
                .findViewById(R.id.NotifyAlertRadioGroup);
        //nuovo codice
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                                       {
                                           @Override
                                           public void onCheckedChanged(RadioGroup group, int checkedId)
                                           {
                                               switch (checkedId)
                                               {
                                                   case R.id.OneDayRadioButton:
                                                       actv(false);
                                                       break;

                                                   case R.id.ReleaseDayRadioButton:
                                                       actv(false);
                                                       break;

                                                   case R.id.OnRadioButton:
                                                       actv(true);
                                                       break;
                                               }
                                           }

                                       });

        //questo sarà sostituito con un calendario.
        /*final EditText nameEditText = (EditText) formElementsView
                .findViewById(R.id.nameEditText);*/

        //parte data
        fromDateEtxt = (EditText) formElementsView.findViewById(R.id.nameEditText);
        fromDateEtxt.setEnabled(active);
        fromDateEtxt.setClickable(active);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();


        //metodo data
        //setDateTimeField();
        //fromDatePickerDialog.show();

        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog( context ,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                fromDateEtxt.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });




        // the alert dialog
        new AlertDialog.Builder(context).setView(formElementsView)
                .setTitle(movies.get(position).getTitle()+" Notify")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                        //fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));

                        String toastString = "";
                        String titleMovie = movies.get(position).getTitle();
                        String releaseDate = String.valueOf(movies.get(position).getReleaseDate());
                        String date = releaseDate.substring(0, 10);
                        String year = releaseDate.substring(releaseDate.length()-5,releaseDate.length());
                        releaseDate = date+year;

                        toastString = toastString + titleMovie + "\n" + releaseDate +"\n";



                        /*
                         * Detecting whether the checkbox is checked or not.
                         */
                        /*if (myCheckBox.isChecked()) {
                            toastString += "Happy is checked!\n";
                        } else {
                            toastString += "Happy IS NOT checked.\n";
                        }*/

                        /*
                         * Getting the value of selected RadioButton.
                         */
                        // get selected radio button from radioGroup
                        int selectedId = genderRadioGroup.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        RadioButton selectedRadioButton = (RadioButton) formElementsView
                                .findViewById(selectedId);

                        if(selectedRadioButton.getId() == R.id.OnRadioButton)
                        {
                            toastString += "Selected radio button is: " + fromDateEtxt.getText();
                        }
                        else {
                            toastString += "Selected radio button is: "
                                    + selectedRadioButton.getText() + "!\n";
                        }




                        /*
                         * Getting the value of an EditText.
                         */
                        /*toastString += "Name is: " + nameEditText.getText()
                                + "!\n";*/

                        Toast toast =  Toast.makeText(context, toastString, Toast.LENGTH_LONG);
                        toast.show();

                        dialog.cancel();
                    }

                }).show();
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
        @BindView(R.id.movie_title)
        public TextView title_movie;
        @BindView(R.id.movie_release_date)
        public TextView release_date;
        @BindView(R.id.editNotify)
        public Button notifyButton;
        @BindView(R.id.iWatchItMovie)
        public Button watchedButton;
        @BindView(R.id.remove)
        public Button removeButton;



        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    //parte per attivare/disattivare l'editText



    private void actv(final boolean active)
    {
        fromDateEtxt.setEnabled(active);
        if (active)
        {
            fromDateEtxt.requestFocus();
        }
    }

    public static void setTipo(String tipo) {
        MoviesAdapter.tipo = tipo;
    }

    public static String getTipo() {
        return tipo;
    }
}
