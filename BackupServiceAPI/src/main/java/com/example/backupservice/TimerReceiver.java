package com.example.backupservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by owais on 7/14/2017.
 */
public class TimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "Started");
        Intent intentService = new Intent(context, ReminderService.class);
        intentService.putExtras(intent.getExtras());
        context.startService(intentService);
    }
}
