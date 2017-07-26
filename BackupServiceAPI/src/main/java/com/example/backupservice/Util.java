package com.example.backupservice;

import android.app.NotificationManager;
import android.content.Context;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Owais on 7/25/2017.
 */

public class Util {

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
        LocalDate endOfMonth = date.dayOfMonth().withMaximumValue();
        int daysBetween = Days.daysBetween(date, endOfMonth).getDays();
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

    public static void parseException(Context context, Exception e) {
        try {
            String exception = e.getMessage().toString();
            String exceptionMessage = exception.substring(exception.indexOf(":", exception.indexOf(":") + 1) + 1);
            Toast.makeText(context, exceptionMessage, Toast.LENGTH_LONG).show();
        }
        catch (Exception e1){
            Toast.makeText(context, e1.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
