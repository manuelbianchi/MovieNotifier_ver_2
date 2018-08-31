package com.example.msnma.movienotifier.notify;

import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;

public class WorkRequest
{
    //we set a tag to be able to cancel all work of this type if needed
    public static final String workTag = "notificationWork";

   /* //store DBEventID to pass it to the PendingIntent and open the appropriate event page on notification click
    Data inputData = new Data.Builder().putInt(DBEventIDTag, DBEventID).build();
// we then retrieve it inside the NotifyWorker with:
// final int DBEventID = getInputData().getInt(DBEventIDTag, ERROR_VALUE);

    OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
            .setInitialDelay(calculateDelay(event.getDate()), TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(workTag)
            .build();*/
}
