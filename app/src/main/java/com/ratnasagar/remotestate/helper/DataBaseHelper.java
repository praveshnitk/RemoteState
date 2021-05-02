package com.ratnasagar.remotestate.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ratnasagar.remotestate.model.TrackingData;
import com.ratnasagar.remotestate.viewmodel.MainViewModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "remotestate.db";

    /*Login Table Structure*/
    private static final String ROW_ID = "rowid";
    private static final String TABLE_TRACKING = "UserTracking";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String TRACKING_TIME = "TrackingTime";
    private static final String ADDRESS = "Address";

    public static final String CREATE_TABLE_TRACKING = "CREATE TABLE "
            + TABLE_TRACKING + "(" + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LATITUDE + " TEXT, " + LONGITUDE + " TEXT, "
            + TRACKING_TIME + " TEXT, " + ADDRESS + " TEXT)";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRACKING);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.i("tag", oldVersion +" newVersion "+newVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DeleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_TRACKING);
    }

    /*Insert Tracking Table*/
    public void insertTrackingData(String latitude, String longitude, String TrackingTime, String Address) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LATITUDE, latitude);
        contentValues.put(LONGITUDE, longitude);
        contentValues.put(TRACKING_TIME, TrackingTime);
        contentValues.put(ADDRESS, Address);
        db.insert(TABLE_TRACKING, null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<TrackingData> getTrackAll() {
        ArrayList<TrackingData> trackingData = new ArrayList<>();
        trackingData.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("select * from " + TABLE_TRACKING + " order by " + ROW_ID + " desc", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String latitude = cursor.getString(cursor.getColumnIndex(LATITUDE));
                    String longitude = cursor.getString(cursor.getColumnIndex(LONGITUDE));
                    String TrackingTime = cursor.getString(cursor.getColumnIndex(TRACKING_TIME));
                    String Address = cursor.getString(cursor.getColumnIndex(ADDRESS));
                    TrackingData trackingData1 = new TrackingData(latitude, longitude, TrackingTime, Address);
                    trackingData.add(trackingData1);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }finally {
            cursor.close();
        }

        return trackingData;
    }

}



