package com.example.msnma.movienotifier.notify;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.example.msnma.movienotifier.R;

public class NotificationReceiver extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_core);

            TextView tv = new TextView(this);
            tv.setText("Notification Text");

            setContentView(tv);
        }

    }


