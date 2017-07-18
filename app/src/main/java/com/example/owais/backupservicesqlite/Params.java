package com.example.owais.backupservicesqlite;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Owais on 7/14/2017.
 */

public class Params implements Parcelable {
    private String dbName;
    private String storagePath;
    private String schedule;
    private String packageName;
    private String Password;
    private int noOfExpiryDays;

    public Params(){}

    public Params(String dbName, String storagePath, String schedule, String packageName, String password, int noOfExpiryDays) {
        this.dbName = dbName;
        this.storagePath = storagePath;
        this.schedule = schedule;
        this.packageName = packageName;
        this.Password = password;

        this.noOfExpiryDays = noOfExpiryDays;
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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dbName);
        dest.writeString(this.storagePath);
        dest.writeString(this.schedule);
        dest.writeString(this.packageName);
        dest.writeString(this.Password);
        dest.writeInt(this.noOfExpiryDays);
    }

    protected Params(Parcel in) {
        this.dbName = in.readString();
        this.storagePath = in.readString();
        this.schedule = in.readString();
        this.packageName = in.readString();
        this.Password = in.readString();
        this.noOfExpiryDays = in.readInt();
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
