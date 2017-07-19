package com.example.backupservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by owais on 7/14/2017.
 */
public class TimerReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION = 3456;
    private AppPreferences appPrefs;
    private String userID;
    int i = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "Started");
        appPrefs = new AppPreferences(context);
        userID = appPrefs.getUserID();

        if (!(userID.equals(""))) {
            Intent intentService = new Intent(context, ReminderService.class);
            intentService.putExtras(intent.getExtras());
            context.startService(intentService);
        }
    }
}
