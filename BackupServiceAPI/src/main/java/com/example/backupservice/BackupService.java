package com.example.backupservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import org.joda.time.LocalDate;

import static com.example.backupservice.Util.endOfMonth;
import static com.example.backupservice.Util.notifyUser;

/**
 * Created by owais on 7/14/2017.
 */
public class BackupService extends Service {

    Context mContext;
    String dbName,storagePath,Password;
    Boolean keepMonthlyBackup, encryptDB;
    Params.Schedule Schedule;
    int noOfExpiryDays;
    Params params;

    public BackupService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this.getApplicationContext();
        extractBundle(intent);
        takeBackup();
        expireBackup();
        stopSelf();
        return START_REDELIVER_INTENT;
    }

    private void extractBundle(Intent intent) {
        Bundle bundle = intent.getExtras();
        params = bundle.getParcelable("params");
        dbName = params.getDbName();
        Schedule = params.getSchedule();
        storagePath = params.getStoragePath();
        noOfExpiryDays = params.getNoOfExpiryDays();
        Password = params.getPassword();
        keepMonthlyBackup = params.isKeepMonthlyBackup();
        encryptDB = params.isEncryptDB();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    private void expireBackup() {
        boolean responseExpire = false;
        boolean responseBackup = false;
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = today.minusDays(noOfExpiryDays);
        if (keepMonthlyBackup) {
            if (!endOfMonth(expiryDate)) {
                responseExpire = new BackupAndRestore().expire(expiryDate, dbName, storagePath);
            }
        } else {
            responseExpire = new BackupAndRestore().expire(expiryDate, dbName, storagePath);
        }
        if (responseExpire) {
            if (Schedule == Params.Schedule.WEEKLY || Schedule == Params.Schedule.MONTHLY) {
                responseBackup = new BackupAndRestore().takeEncryptedBackup(mContext, dbName, storagePath, Password);
                if (responseBackup) {
                    notifyUser(mContext);
                }
            }
        }
    }

    public void takeBackup() {
        boolean response = false;
        if (keepMonthlyBackup) {
            if (endOfMonth()) {
                response = new BackupAndRestore().takeEncryptedBackup(mContext, dbName, storagePath, Password);
            }
        }
        if (Schedule == Params.Schedule.DAILY) {
            if (encryptDB && !Password.equals("")) {
                response = new BackupAndRestore().takeEncryptedBackup(mContext, dbName, storagePath, Password);
            } else {
                response = new BackupAndRestore().takeBackup(mContext, dbName, storagePath);
            }
        } else if (Schedule == Params.Schedule.WEEKLY) {
            LocalDate today = LocalDate.now();
            if (today.getDayOfWeek() == 7) { //7 represent sunday
                if (encryptDB && !Password.equals("")) {
                    response = new BackupAndRestore().takeEncryptedBackup(mContext, dbName, storagePath, Password);
                } else {
                    response = new BackupAndRestore().takeBackup(mContext, dbName, storagePath);
                }
            }
        } else if (Schedule == Params.Schedule.MONTHLY) {
            if (endOfMonth()) {
                if (encryptDB && !Password.equals("")) {
                    response = new BackupAndRestore().takeEncryptedBackup(mContext, dbName, storagePath, Password);
                } else {
                    response = new BackupAndRestore().takeBackup(mContext, dbName, storagePath);
                }
            }
        }
        if (response) {
            notifyUser(mContext);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
