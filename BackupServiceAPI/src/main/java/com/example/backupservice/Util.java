package com.example.backupservice;

import android.app.NotificationManager;
import android.content.Context;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Owais on 7/25/2017.
 */

public class Util {
    String CurrentTime, CurrentDate;
    int Day, Month, Year, Hour, Minute;

    public static boolean endOfMonth() {
        LocalDate today = LocalDate.now();
        LocalDate lastDayOfMonth = LocalDate.fromDateFields(new
                Date()).dayOfMonth().withMaximumValue();
        int daysBetween = Days.daysBetween(today, lastDayOfMonth).getDays();
        if (daysBetween == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean endOfMonth(LocalDate date) {
        LocalDate lastDayOfMonth = LocalDate.fromDateFields(new
                Date()).dayOfMonth().withMaximumValue();
        int daysBetween = Days.daysBetween(date, lastDayOfMonth).getDays();
        if (daysBetween == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void notifyUser(Context context) {
        int count = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setTicker("Notification")
                .setSmallIcon(R.drawable.ic_check_circle_black_24dp)
                .setContentTitle("Successfully take Backup!");
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
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

    public static void ToastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
