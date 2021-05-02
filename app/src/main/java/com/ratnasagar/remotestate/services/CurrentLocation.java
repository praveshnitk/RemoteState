package com.ratnasagar.remotestate.services;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.MacAddress;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.ratnasagar.remotestate.SplashScreen;
import com.ratnasagar.remotestate.helper.DataBaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class CurrentLocation implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private DataBaseHelper dbHelper;
    Context context;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double currentLatitude, currentLongitude;

    private String Pin,State,City;

    public CurrentLocation(Context context) {
        try {
            this.context = context;
            //trackTime = mSharedPreferences.getInt("trackTime", 10);
            dbHelper = new DataBaseHelper(context);
            //Log.i("trackTime",String.valueOf(trackTime));
        }catch (Exception ex){
        }
    }

    private GoogleMap mMap;
    String inputFormat = "HH:mm aa";
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLocationRequest = new LocationRequest();
            //mLocationRequest.setInterval(60000 * trackTime);
            //mLocationRequest.setFastestInterval(60000 * trackTime);
            //mLocationRequest.setInterval(1000);
            //mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this::onLocationChanged);
            }
        }catch (Exception ex){
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            Log.i("tag: ", "Lat : " + currentLatitude + " Log : " + currentLongitude);
            Geocoder geocoder = null;
            List<Address> addresses = null;
            try {
                geocoder = new Geocoder(context, Locale.getDefault());
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                String address1 = (addresses.get(0).getPremises());
                String address2 = (addresses.get(0).getSubLocality());
                City = addresses.get(0).getLocality();
                State = addresses.get(0).getAdminArea();
                Pin = addresses.get(0).getPostalCode();
                String country = addresses.get(0).getCountryName();
                String address = address1 + ", " + address2 + ", " + City + ", " + State + ", " + country + ", " + Pin;

                String DateAndTime = getDateAndTime();
                Log.i("tag:  ",address+" DateTime: "+DateAndTime);

                dbHelper.insertTrackingData(String.valueOf(currentLatitude),String.valueOf(currentLongitude),DateAndTime,address);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //}
        }catch (Exception ex){
        }
    }


    private String getDateAndTime() {
        String Datetime="";
        try {
            String AmOrPm;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dates = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            int am_pm = c.get(Calendar.AM_PM);
            if (am_pm == 0) {
                AmOrPm = "AM";
            } else {
                AmOrPm = "PM";
            }
            Datetime = dates.format(c.getTime()).concat(AmOrPm);
        }catch (Exception ex){
        }
        return Datetime;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //Initialize Google Play Services
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }catch (Exception ex){
        }
    }

    public synchronized void buildGoogleApiClient() {
        try {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }catch (Exception ex){
        }
    }
}
