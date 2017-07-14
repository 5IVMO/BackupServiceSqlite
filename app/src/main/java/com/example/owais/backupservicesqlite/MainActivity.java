package com.example.owais.backupservicesqlite;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AppPreferences appPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appPrefs = new AppPreferences(this);
        appPrefs.saveUserID("001");
        setupAlarm();
    }
    public void setupAlarm(){
        Intent intentService = new Intent(this,TimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentService, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 16); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        final AlarmManager alarmManager = (AlarmManager)(getSystemService( Context.ALARM_SERVICE ));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
