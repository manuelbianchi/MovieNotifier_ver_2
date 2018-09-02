package com.example.msnma.movienotifier.adapter;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.msnma.movienotifier.MainActivity;
import com.example.msnma.movienotifier.MoviesFragment;
import com.example.msnma.movienotifier.R;
import com.example.msnma.movienotifier.SectionsPageAdapter;
import com.example.msnma.movienotifier.database.MovieDatabase;
import com.example.msnma.movienotifier.databaseModel.MovieDBModel;
import com.example.msnma.movienotifier.model.Movie;
import com.example.msnma.movienotifier.notify.NotificationPublisher;
import com.example.msnma.movienotifier.notify.NotifyWorker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;
import static com.example.msnma.movienotifier.notify.Constants.KEY_MOVIE;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>{

    private UUID notifyRequestID = null;


    private Context context;
    private List<Movie> movies;
    private View itemView;
    private RecyclerView rv;

    //per la data
    private EditText fromDateEtxt;
    private EditText eReminderTime;
    private boolean active = false;

    private int mese = (Calendar.getInstance().getTime().getMonth())+1;
    private int anno = (Calendar.getInstance().getTime().getYear())+1900 ;
    private int giorno = Calendar.getInstance().getTime().getDate();
    private int ora;
    private int minuti;
    private int secondi;
    private boolean modify = false;
    private Date datatime;

    private static String tipo = "NOTIFY";

    //non sono ancora sicuro se metterlo qui

    private WorkManager mWorkManager;
    static MoviesFragment fragmentInstance;
    static SectionsPageAdapter spa;

    public MoviesAdapter(Context context, List<Movie> movies, MoviesFragment fragment) {
        this.context = context;
        this.movies = movies;
        this.fragmentInstance = fragment;
        this.spa = null;
    }

    public MoviesAdapter(Context context, List<Movie> movies, SectionsPageAdapter spa) {
        this.context = context;
        this.movies = movies;
        this.spa = spa;
        this.fragmentInstance = null;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        mWorkManager = WorkManager.getInstance();

        final Movie movie = movies.get(position);
        Glide.with(context)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.poster_placeholder)
                .into(holder.posterView);
        holder.title_movie.setText(movie.getTitle());
        // prende solo la data + anno
        String yourString = String.valueOf(movie.getReleaseDate());
        String date = yourString.substring(0, 10);
        String year = yourString.substring(yourString.length()-5,yourString.length());

        //per fare il testo bold
        final SpannableStringBuilder sb = new SpannableStringBuilder("Release: "+date+year);

        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold
        final StyleSpan nss = new StyleSpan(Typeface.NORMAL); //Span to make text italic
        sb.setSpan(bss, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold
        sb.setSpan(nss, 7, sb.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make last 2 characters Italic


        holder.release_date.setText(sb);
        if(getTipo().equals("NOTIFY")) {
            Toast.makeText(context, "Notify", Toast.LENGTH_LONG).show();
            holder.movie_notify.setVisibility(View.VISIBLE);
            holder.notifyButton.setVisibility(View.GONE);
            holder.changeDateTimeButton.setVisibility(View.VISIBLE);
            holder.watchedButton.setVisibility(View.VISIBLE);
            holder.removeButton.setVisibility(View.VISIBLE);

            String yourString1 = String.valueOf(movie.getNotifyDate());
            Log.i("STRINGA",yourString1);
            if(!(yourString1.equals("null"))) {
                date = yourString1.substring(0, 16);
                year = yourString1.substring(yourString1.length() - 5, yourString1.length());
                //per fare il testo bold
                final SpannableStringBuilder sb1 = new SpannableStringBuilder("Notify: " + date + year);
                final StyleSpan bss1 = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold
                final StyleSpan nss1 = new StyleSpan(Typeface.NORMAL); //Span to make text normal
                sb1.setSpan(bss1, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold
                sb1.setSpan(nss1, 6, sb.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make last 2 characters Italic
                holder.movie_notify.setText(sb1);
            }

            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MovieDatabase md = new MovieDatabase(context);
                    md.deleteMovie(movies.get(position).getId());
                    refreshLists();
                }
            });

            holder.changeDateTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertFormElements(position, true);
                }
            });

            holder.watchedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MovieDBModel mdm = new MovieDBModel(movies.get(position).getId(), movies.get(position).getTitle(), movies.get(position).getOverview(),
                            movies.get(position).getPosterUrl(), movies.get(position).getBackdropUrl(), movies.get(position).getTrailerUrl(),
                            movies.get(position).getReleaseDate(), movies.get(position).getRating(), movies.get(position).isAdult(),null);
                    MovieDatabase.updateMovieType(movies.get(position).getId(), 2,MainActivity.getMovieDatabase());
                    String testo = "Added " + movies.get(position).getTitle() + "\n" + "in tab watched";
                    Toast tostato = Toast.makeText(context, testo, Toast.LENGTH_SHORT);
                    tostato.show();
                    refreshLists();
                }
            });
        }

        //solo se è di tipo suggested
        if(getTipo().equals("SUGGESTED")) {
            //disabilitare bottone remove

            holder.movie_notify.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.GONE);
            holder.notifyButton.setVisibility(View.VISIBLE);
            holder.watchedButton.setVisibility(View.VISIBLE);
            holder.changeDateTimeButton.setVisibility(View.GONE);
            Toast.makeText(context,"Suggested", Toast.LENGTH_LONG).show();

            holder.notifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    alertFormElements(position, false);
                }
            });
            holder.watchedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MovieDBModel mdm = new MovieDBModel(movies.get(position).getId(), movies.get(position).getTitle(), movies.get(position).getOverview(),
                            movies.get(position).getPosterUrl(), movies.get(position).getBackdropUrl(), movies.get(position).getTrailerUrl(),
                            movies.get(position).getReleaseDate(), movies.get(position).getRating(), movies.get(position).isAdult(),null);
                    MovieDatabase.insertMovie(mdm, 2, MainActivity.getMovieDatabase());
                    String testo = "Added " + movies.get(position).getTitle() + "\n" + "in tab watched";
                    Toast tostato = Toast.makeText(context, testo, Toast.LENGTH_SHORT);
                    tostato.show();
                    refreshLists();
                }
            });


        }
        if (getTipo().equals("WATCHED")) {

            holder.movie_notify.setVisibility(View.GONE);
            holder.notifyButton.setVisibility(View.GONE);
            holder.watchedButton.setVisibility(View.GONE);
            holder.removeButton.setVisibility(View.VISIBLE);
            holder.changeDateTimeButton.setVisibility(View.GONE);

            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MovieDatabase md = new MovieDatabase(context);
                    md.deleteMovie(movies.get(position).getId());
                    refreshLists();
                }
            });
            Toast.makeText(context,"WATCHED", Toast.LENGTH_LONG).show();
        }
    }


    //nuovo codice riguardo l'alerDialog

    public final void alertFormElements(final int position, final boolean modify) {
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

        //Calendario ci servirà dopo per inserire i dati nel DB

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
                                anno = year;
                                giorno = dayOfMonth;
                                mese = monthOfYear + 1;

                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        //parte orario
        ora = 9;
        minuti = 30;

        eReminderTime = (EditText) formElementsView.findViewById(R.id.timeEditText);
        eReminderTime.setText( ora + ":" + minuti);
        eReminderTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {


                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                    {
                        eReminderTime.setText( selectedHour + ":" + selectedMinute);
                        ora = selectedHour;
                        minuti = selectedMinute;
                    }
                    //}
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

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
                        Date datatime = null;
                        if(selectedRadioButton.getId() == R.id.ReleaseDayRadioButton) {
                            toastString += "Selected radio button is: " + selectedRadioButton.getText() +"!\n";
                            Date release = movies.get(position).getReleaseDate();
                            release.setHours(ora);
                            release.setMinutes(minuti);
                            release.setSeconds(secondi);
                            datatime = new Date (anno-1900,mese-1,giorno,ora, minuti, secondi);

                        }
                        else if(selectedRadioButton.getId() == R.id.OneDayRadioButton) {
                            toastString += "Selected radio button is: "
                                    + selectedRadioButton.getText() + "!\n";
                            Date release = movies.get(position).getReleaseDate();
                            release.setHours(ora);
                            release.setMinutes(minuti);
                            release.setSeconds(secondi);
                            datatime = new Date (anno-1900,mese-1,giorno-1,release.getHours(),release.getMinutes(), release.getSeconds());
                        }
                        else if(selectedRadioButton.getId() == R.id.OnRadioButton) {
                            toastString += "Selected radio button is: " + fromDateEtxt.getText() +"!\n";
                            datatime = new Date (anno-1900,mese-1,giorno,ora, minuti, secondi);
                        }

                        setDataTime(datatime);
                        toastString += eReminderTime.getText();
                        //Date(int year, int month, int date, int hrs, int min, int sec)
                        //Date datatime = new Date (anno-1900,mese-1,giorno,ora, minuti, secondi);


                        //ora scriviamo tutta questa roba sulla base di dati


                /*String testo = movies.get(position).getTitle()+ "\n" + "I WATCH IT";
                Toast tostato = Toast.makeText(context,testo,Toast.LENGTH_SHORT);
                tostato.show();*/
                        if(modify == false) {
                            // public MovieDBModel(int id, String title,  String overview, String posterUrl, String backdropUrl, String trailerUrl,
                            //                        Date releaseDate, float rating, boolean adult, date datatime){
                            //Log.i("DATATIME", datatime.toString());
                            MovieDBModel mdm2 = new MovieDBModel(movies.get(position).getId(), movies.get(position).getTitle(), movies.get(position).getOverview(),
                                    movies.get(position).getPosterUrl(), movies.get(position).getBackdropUrl(), movies.get(position).getTrailerUrl(),
                                    movies.get(position).getReleaseDate(), movies.get(position).getRating(), movies.get(position).isAdult(), datatime);
                            MovieDatabase.insertMovie(mdm2, 1, MainActivity.getMovieDatabase());
                            //notifyRequestID= scheduleNotify(datatime,position);
                            scheduleNotification(getNotification(movies.get(position).getTitle()),datatime);
                            refreshLists();
                        }
                        else {
                            // provare la base di dati
                            MovieDatabase md = new MovieDatabase(context);
                            Log.i("DATATIME","ID"+ movies.get(position).getId() +"DATATIME: "+ datatime);
                            md.updateNotifyDate(movies.get(position).getId(),datatime);
                            //deleteNotify(notifyRequestID);
                            //inserire funzione deleteNotify
                            scheduleNotification(getNotification(movies.get(position).getTitle()),datatime);
                            refreshLists();
                        }
                        String testo = "Added " + movies.get(position).getTitle() + "\n" + "in tab watched";
                        Toast tostato = Toast.makeText(context, testo, Toast.LENGTH_SHORT);
                        tostato.show();
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
        @BindView(R.id.movie_time_notify)
        public TextView movie_notify;
        @BindView(R.id.editNotify)
        public Button notifyButton;
        @BindView(R.id.iWatchItMovie)
        public Button watchedButton;
        @BindView(R.id.remove)
        public Button removeButton;
        @BindView(R.id.change)
        public Button changeDateTimeButton;

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
            fromDateEtxt.setText(giorno+"-"+mese+"-"+anno);
        }
    }

    public static void setTipo(String tipo) {
        MoviesAdapter.tipo = tipo;
    }

    public static void setFragment(MoviesFragment fragment) {
        MoviesAdapter.fragmentInstance = fragment;
    }

    public static String getTipo() {
        return tipo;
    }

    public Date getDatatime() {
        return datatime;
    }

    public void setDataTime(Date datatime) {
        this.datatime = datatime;
    }


    private Data createInputDataForUri(Movie movie) {
        Data.Builder builder = new Data.Builder();
        if (movie != null) {
            builder.putString(KEY_MOVIE,movie.getTitle());
        }
        return builder.build();
    }

    /*private UUID scheduleNotify(Date d, int position) {
        Constraints myConstraints = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            myConstraints = new Constraints.Builder()
                    .setRequiresDeviceIdle(true)
                    .build();
        }
        long currentTime= System.currentTimeMillis();
        //Calendar c = new Date;
        long specificTimeToTrigger = d.getTime();
        //d.getTimeToMillis();
        long delayToPass = specificTimeToTrigger - currentTime;



        //inizialmente è molto semplice la notifica
        OneTimeWorkRequest notifyRequest =
                new OneTimeWorkRequest.Builder(NotifyWorker.class)
                        .setInputData(createInputDataForUri(movies.get(position)))
                        .setConstraints(myConstraints)
                        .setInitialDelay(delayToPass,TimeUnit.MILLISECONDS)
                        .build();
        mWorkManager.enqueue(notifyRequest);
        UUID notify_ID = notifyRequest.getId();

        //WorkManager.getInstance().enqueue(compressionWork);

        return notify_ID;

    }*/


    /*public void deleteNotify(UUID notify_ID) {
        WorkManager.getInstance().cancelWorkById(notify_ID);
    }*/


    private void scheduleNotification(Notification notification, /*int delay*/Date d) {

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP,d.getTime(), pendingIntent);

    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("WARNING!!! REMEMBER MOVIE: ");
        builder.setContentText(content);
        builder.setDefaults(DEFAULT_ALL);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        return builder.build();
    }


    public void refreshLists(){
        if(fragmentInstance!= null){
            fragmentInstance.onRefresh();
        }else{
            MoviesFragment mf1 = (MoviesFragment)spa.getItem(0);
            mf1.setArgFragType(MoviesFragment.Type.NOTIFY);
            mf1.onRefresh();
            MoviesFragment mf2 = (MoviesFragment)spa.getItem(1);
            mf2.setArgFragType(MoviesFragment.Type.SUGGESTED);
            mf2.onRefresh();
            MoviesFragment mf3 = (MoviesFragment)spa.getItem(2);
            mf3.setArgFragType(MoviesFragment.Type.WATCHED);
            mf3.onRefresh();
        }
    }
}