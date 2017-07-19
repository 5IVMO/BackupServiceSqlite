package com.example.backupservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by Owais on 7/19/2017.
 */

public class BackupService {

    private Context context;

    public BackupService(Context context) {
        this.context=context;
    }

    public void setupAlarm(Params params) {
        Intent intentService = new Intent(context, TimerReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("params", params);
        intentService.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentService, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 10); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        final AlarmManager alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        // alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+ 10 * 1000, 55 * 1000, pendingIntent);

    }
}
