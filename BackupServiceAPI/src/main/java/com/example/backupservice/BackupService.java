package com.example.backupservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Owais on 7/19/2017.
 */

public class BackupService {

    private Context context;

    public BackupService(Context context) {
        this.context = context;
    }

    public void setupAlarm(Params params) {
        Intent intentService = new Intent(context, TimerReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("params", params);
        intentService.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentService, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 3); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, 39);
        calendar.set(Calendar.SECOND, 0);
        final AlarmManager alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 10 * 1000, 55 * 1000, pendingIntent);

    }

    public void importDB(final String FilePath, final String DBPath) {
        String extension = "";
        if (FilePath.contains(".")) {
            extension = FilePath.substring(FilePath.lastIndexOf("."));
        }

        if (!TextUtils.isEmpty(extension)) {

            if (extension.equals(".db") || extension.equals(".zip")) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setTitle("Remove Old Database");
                builder1.setMessage("Are you Sure You want to remove old Database ?");
                builder1.setCancelable(true);

                final String finalExtension = extension;
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean response=false;
                                try {
                                    if (finalExtension.equals(".db")) {
                                        Toast.makeText(context, ".db file", Toast.LENGTH_SHORT).show();
                                        response=new BackupAndRestore().restore(FilePath, DBPath);
                                        if(response){
                                            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                                        }
                                    } else if (finalExtension.equals(".zip")) {
                                        Toast.makeText(context, ".zip file", Toast.LENGTH_SHORT).show();
                                       response=new BackupAndRestore().decryptBackup(context, FilePath, DBPath,"123");
                                        if(response){
                                            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                } catch (Exception e) {
                                    Log.d("", e.toString());
                                }
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else {
                Toast.makeText(context, "Please select .db or .Zip file", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(context, "Please select .db or .Zip file", Toast.LENGTH_LONG).show();
        }
    }
}
