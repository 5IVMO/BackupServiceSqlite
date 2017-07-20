package com.example.backupservice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Owais on 7/14/2017.
 */

public class Params implements Parcelable {
    public enum Schedule {
        DAILY, WEEKLY, MONTHLY
    }

    private String appName;
    private String dbName;
    private String storagePath;
    private Schedule schedule;
    private String packageName;
    private String Password;
    private int noOfExpiryDays;
    private boolean keepMonthlyBackup,encryptDB;

    public Params() {
    }

    public Params(String appName, String dbName, String storagePath, Schedule schedule, String packageName, String password, int noOfExpiryDays, boolean keepMonthlyBackup, boolean encryptDB) {
        this.appName = appName;
        this.dbName = dbName;
        this.storagePath = storagePath;
        this.schedule = schedule;
        this.packageName = packageName;
        Password = password;
        this.noOfExpiryDays = noOfExpiryDays;
        this.keepMonthlyBackup = keepMonthlyBackup;
        this.encryptDB = encryptDB;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public int getNoOfExpiryDays() {
        return noOfExpiryDays;
    }

    public void setNoOfExpiryDays(int noOfExpiryDays) {
        this.noOfExpiryDays = noOfExpiryDays;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isKeepMonthlyBackup() {
        return keepMonthlyBackup;
    }

    public void setKeepMonthlyBackup(boolean keepMonthlyBackup) {
        this.keepMonthlyBackup = keepMonthlyBackup;
    }

    public boolean isEncryptDB() {
        return encryptDB;
    }

    public void setEncryptDB(boolean encryptDB) {
        this.encryptDB = encryptDB;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appName);
        dest.writeString(this.dbName);
        dest.writeString(this.storagePath);
        dest.writeString(this.schedule.name());
        dest.writeString(this.packageName);
        dest.writeString(this.Password);
        dest.writeInt(this.noOfExpiryDays);
        dest.writeInt(this.keepMonthlyBackup ? 1 : 0);
        dest.writeInt(this.encryptDB ? 1 : 0);
    }

    protected Params(Parcel in) {
        this.appName=in.readString();
        this.dbName = in.readString();
        this.storagePath = in.readString();
        this.schedule = Schedule.valueOf(in.readString());
        this.packageName = in.readString();
        this.Password = in.readString();
        this.noOfExpiryDays = in.readInt();
        this.keepMonthlyBackup= (in.readInt() == 0) ? false : true;
        this.encryptDB= (in.readInt() == 0) ? false : true;
    }

    public static final Parcelable.Creator<Params> CREATOR = new Parcelable.Creator<Params>() {
        @Override
        public Params createFromParcel(Parcel source) {
            return new Params(source);
        }

        @Override
        public Params[] newArray(int size) {
            return new Params[size];
        }
    };
}
