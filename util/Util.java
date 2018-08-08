package com.example.msnma.movienotifier.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msnma.movienotifier.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private static ConnectivityManager connectivityManager;

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static Date toDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateString);
        return date;
//        return DateTimeFormat.forPattern("yyyy-MM-dd")
//               .parseDateTime(date);
    }

    public static String toDbDate(Date date) {
//        return DateTimeFormat.forPattern("yyyy-MM-dd").print(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

   public static String toPrettyDate(Date date) {
//        return DateTimeFormat.mediumDate().print(date.getMillis());
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      return format.format(date);
    }

    public static void setupToolbar(Context context, Toolbar toolbar) {
        TextView titleView = null;
        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleView = (TextView) f.get(toolbar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (titleView != null) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/MarckScript-Regular.ttf");
            titleView.setTypeface(font);
            titleView.setTextSize(26);
            titleView.setPadding(20, 10, 0, 0);
        }
        toolbar.setLogo(new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_movie)
                .sizeDp(24));
    }

    public static boolean isConnected(final Activity activity, boolean showToast) {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = (info != null && info.isConnected());
//        if (!isConnected && showToast) {
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(activity, R.string.conn_internet, Toast.LENGTH_SHORT).show();
//                }
//            });
        //       }
        return isConnected;
    }

    public static void shareText(Activity activity, String text) {
        ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setText(text)
                .startChooser();
    }

    public static void openLinkInExternalApp(Context context, String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        context.startActivity(intent);
    }

}
