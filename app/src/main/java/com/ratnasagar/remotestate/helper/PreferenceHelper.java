package com.ratnasagar.remotestate.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "remote_state";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    @SuppressLint("CommitPrefEdits")
    public PreferenceHelper(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
    public String getString(String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public void  setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    @SuppressLint("CommitPrefEdits")
    public void clear() {
        pref.edit();
        editor.clear();
        editor.commit();
    }

    public int getInt(String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();

    }

    public long getLong(String key, long defValue) {

        return pref.getLong(key, defValue);
    }

    public double getDouble(String key, float defValue) {

        return pref.getFloat(key, defValue);
    }

    public void setDouble(String key, float defValue) {

        editor.putFloat(key, defValue);
        editor.commit();
    }
}
