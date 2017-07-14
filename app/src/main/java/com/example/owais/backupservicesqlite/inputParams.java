package com.example.owais.backupservicesqlite;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Owais on 7/14/2017.
 */

public class inputParams implements Parcelable {
    private String dbName;
    private String storagePath;
    private String schedule;
    private long noOfDays;

    public inputParams(String dbName, String storagePath, String schedule, long noOfDays) {
        this.dbName = dbName;
        this.storagePath = storagePath;
        this.schedule = schedule;
        this.noOfDays = noOfDays;
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

    public long getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(long noOfDays) {
        this.noOfDays = noOfDays;
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
        dest.writeLong(this.noOfDays);
    }

    public inputParams() {
    }

    protected inputParams(Parcel in) {
        this.dbName = in.readString();
        this.storagePath = in.readString();
        this.schedule = in.readString();
        this.noOfDays = in.readLong();
    }

    public static final Parcelable.Creator<inputParams> CREATOR = new Parcelable.Creator<inputParams>() {
        @Override
        public inputParams createFromParcel(Parcel source) {
            return new inputParams(source);
        }

        @Override
        public inputParams[] newArray(int size) {
            return new inputParams[size];
        }
    };
}
