package com.example.backupservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by owais on 7/14/2017.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, BackupService.class);
        intentService.putExtras(intent.getExtras());
        context.startService(intentService);
    }
}
