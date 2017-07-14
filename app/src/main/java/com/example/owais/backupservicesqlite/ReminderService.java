package com.example.owais.backupservicesqlite;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by owais on 7/14/2017.
 */
public class ReminderService extends Service {

    Context mContext;
    String TAG = "service";
    String dbName, storagePath, Schedule;
    long noOfDays;
    inputParams params;
    String CurrentTime, CurrentDate;
    int Day, Month, Year, Hour, Minute;
    private AppPreferences appPrefs;
    private static final int START_AFTER_SECONDS = 10;

    public ReminderService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "Started");
        mContext = this.getApplicationContext();

        Bundle bundle = intent.getExtras();
        params = bundle.getParcelable("params");
        dbName = params.getDbName();
        Schedule = params.getSchedule();
        storagePath = params.getStoragePath();
        noOfDays = params.getNoOfDays();

        Log.d("params in service", dbName);
        Log.d("params in service", Schedule);
        Log.d("params in service", storagePath);
        Log.d("params in service", "" + noOfDays);
        GetCurrentDateTime();
        takeBackup();
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    public void takeBackup() {

        notifyUser();
        stopSelf();
    }

    public void notifyUser() {

        int count = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setTicker("Notification")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("Successfully take Backup!")
                .setContentText(new StringBuilder().append("Database Name: ").append(dbName).append("\n").append("Storage Path: ").append(storagePath));
        mBuilder.setContentIntent(PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0))
                .setAutoCancel(true);
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(count, mBuilder.build());
    }

    public void GetCurrentDateTime() {
        //Get Current Date
        Calendar calendar = Calendar.getInstance();
        Day = calendar.get(calendar.DAY_OF_MONTH); //26
        Month = calendar.get(calendar.MONTH); //0
        Year = calendar.get(calendar.YEAR);//2016

        CurrentDate = Month + 1 + "/" + Day + "/" + Year;

        //Get Current Time
        Hour = calendar.get(calendar.HOUR_OF_DAY);//14
        Minute = calendar.get(calendar.MINUTE);//39

        String state = " ";
        String min = " ";
        if (Hour > 12) {
            Hour -= 12;
            state = "PM";
        } else if (Hour == 0) {
            Hour += 12;
            state = "AM";
        } else if (Hour == 12)
            state = "PM";
        else
            state = "AM";

        if (Minute < 10)
            min = "0" + Minute;
        else
            min = String.valueOf(Minute);

        CurrentTime = Hour + ":" + min + " " + state;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
