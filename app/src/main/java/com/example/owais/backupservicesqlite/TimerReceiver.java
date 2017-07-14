package com.example.owais.backupservicesqlite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

            inputParams params = new inputParams();
            params.setDbName("DB Name");
            params.setStoragePath("Path");
            params.setSchedule("Schedule");
            params.setNoOfDays(1);

            Bundle bundle = new Bundle();
            bundle.putParcelable("params", params);
            intentService.putExtras(bundle);

            context.startService(intentService);
        }
    }
}
