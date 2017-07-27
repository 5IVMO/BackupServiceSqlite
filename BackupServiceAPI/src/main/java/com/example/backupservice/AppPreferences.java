package com.example.backupservice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by hii on 1/27/2016.
 */
public class AppPreferences {
    public static final String KEY_PREFS_USER_ID = "userID";
    public static final String KEY_PREFS_PARAMS = "Params";
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); //  Name of the file -.xml
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public AppPreferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    }

    public String getUserID() {
        return sharedPrefs.getString(KEY_PREFS_USER_ID, "");
    }

    public void saveUserID(String text) {
        prefsEditor.putString(KEY_PREFS_USER_ID, text);
        prefsEditor.commit();
    }

    public void signOutUser() {
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public void saveParams(Params params) {
        Gson gson = new Gson();
        String json = gson.toJson(params);
        prefsEditor.putString(KEY_PREFS_PARAMS, json);
        prefsEditor.commit();
    }
    public Params getParams(){
        Gson gson = new Gson();
        String json = sharedPrefs.getString(KEY_PREFS_PARAMS, "");
        Params params = gson.fromJson(json, Params.class);
        return params;
    }
}
