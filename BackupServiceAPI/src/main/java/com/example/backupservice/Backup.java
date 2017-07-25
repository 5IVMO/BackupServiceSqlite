package com.example.backupservice;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Owais on 7/19/2017.
 */

public class Backup {

    private Context context;
    Dialog dialogPassword;
    Button cancelPassword, enterPassword;
    EditText editTextPassword;

    public Backup(Context context) {
        this.context = context;
    }

    public void setupDialog() {
        dialogPassword = new Dialog(context);
        dialogPassword.setContentView(R.layout.dialog_password);
        enterPassword = (Button) dialogPassword.findViewById(R.id.button_password);
        cancelPassword = (Button) dialogPassword.findViewById(R.id.button_cancel);
        editTextPassword = (EditText) dialogPassword.findViewById(R.id.editText_password);
        dialogPassword.show();
    }

    public void setupService(Params params) {
        Intent intentService = new Intent(context, TimerReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable("params", params);
        intentService.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentService, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 4); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, 36);
        calendar.set(Calendar.SECOND, 0);
        final AlarmManager alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 10 * 1000, 55 * 1000, pendingIntent);
    }

    public void importDB(final String FilePath, final String dbName) {
        String extension = "";
        if (FilePath.contains(".")) {
            extension = FilePath.substring(FilePath.lastIndexOf("."));
        }
        if (!TextUtils.isEmpty(extension)) {

            if (extension.equals(".db") || extension.equals(".zip")) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setTitle("Override Old Database");
                builder1.setMessage("Are you Sure You want to override old Database ?");
                builder1.setCancelable(true);

                final String finalExtension = extension;
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                boolean response = false;
                                try {
                                    final String DBPath = context.getDatabasePath(dbName).getPath();
                                    if (finalExtension.equals(".db")) {
                                        response = new BackupAndRestore().restore(FilePath, DBPath);
                                        if (response) {
                                            Toast.makeText(context, "Successfully Restore Database", Toast.LENGTH_LONG).show();
                                        }
                                    } else if (finalExtension.equals(".zip")) {
                                        setupDialog();
                                        enterPassword.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String password = editTextPassword.getText().toString();
                                                if (!password.equals("")) {
                                                    boolean responseDecrypt = new BackupAndRestore().decryptBackup(FilePath, DBPath, password);
                                                    if (responseDecrypt) {
                                                        Toast.makeText(context, "Successfully Restore Database", Toast.LENGTH_LONG).show();
                                                    }
                                                    dialogPassword.dismiss();
                                                } else {
                                                    Toast.makeText(context, "Please enter Password", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        cancelPassword.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialogPassword.dismiss();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
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
