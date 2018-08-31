package com.example.msnma.movienotifier.notify;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.msnma.movienotifier.CoreActivity;

import androidx.work.Data;
import androidx.work.Worker;

import static com.example.msnma.movienotifier.CoreActivity.notifyPush;
import static com.example.msnma.movienotifier.notify.Constants.KEY_MOVIE;

public class NotifyWorker extends Worker {

    //private static final String TAG = BlurWorker.class.getSimpleName();

    @NonNull
    @Override
    public Worker.Result doWork() {

        Context applicationContext = getApplicationContext();

        String message = getInputData().getString(Constants.KEY_MOVIE);

        //setOutputData(new Data.Builder().putString(
                //KEY_MOVIE, message.toString()).build());

        try {

            /*Bitmap picture = BitmapFactory.decodeResource(
                    applicationContext.getResources(),
                    R.drawable.test);

            // Blur the bitmap
            Bitmap output = WorkerUtils.blurBitmap(picture, applicationContext);

            // Write bitmap to a temp file
            Uri outputUri = WorkerUtils.writeBitmapToFile(applicationContext, output);

            WorkerUtils.makeStatusNotification("Output is "
                    + outputUri.toString(), applicationContext);

            // If there were no errors, return SUCCESS*/

            notifyPush(message , applicationContext);
            return Worker.Result.SUCCESS;
        } catch (Throwable throwable) {

            // Technically WorkManager will return WorkerResult.FAILURE
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e("NotifyWorker", "Error notification", throwable);
            return Worker.Result.FAILURE;
        }
    }
}