package com.ratnasagar.remotestate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ratnasagar.remotestate.utils.GpsOnRequest;

public class UserLocation extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, LocationListener {

    private LocationManager locationManager;
    private GoogleMap mMap;
    String trackingTime="",address="";
    Double latitude=0.0,longitude=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
        getSupportActionBar().setTitle("User Tracking");
        Intent intent = getIntent();
        if (intent!=null){
            latitude = Double.valueOf(intent.getStringExtra("latitude"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));
            trackingTime = intent.getStringExtra("trackingTime");
            address = intent.getStringExtra("address");
        }

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, supportMapFragment).commit();
        }

        supportMapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(getActivity(), "Gps is Enabled", Toast.LENGTH_SHORT).show();
        } else {
            GpsOnRequest gpsOnRequest = new GpsOnRequest(UserLocation.this);
            gpsOnRequest.mEnableGps();
        }
        //createMarker(28.6981,77.2073,"Ratna Sagar Pvt Ltd","2021-04-09 03:30 PM");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            //mMap.setMyLocationEnabled(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 55);
            } else {
                //createMarker(28.6981,77.2073,"Ratna Sagar Pvt Ltd","2021-04-09 03:30 PM");
                createMarker(latitude,longitude,address,trackingTime);
            }
        }catch (Exception ex){
        }
    }

    private void createMarker( Double Lat, Double Lng, String address,String time) {
        try {
            CameraUpdateFactory.zoomTo(5);
            LatLngBounds India = new LatLngBounds(
                    new LatLng(Lat, Lng), new LatLng(Lat, Lng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(India, 0));

                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.location_start).copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(bm);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(25);
                paint.setTextAlign(Paint.Align.CENTER);
                int xPos = (canvas.getWidth() / 2);
                canvas.drawText("Location", xPos, bm.getHeight(), paint); // paint defines the text color, stroke width, size
                BitmapDrawable draw = new BitmapDrawable(getResources(), bm);
                Bitmap drawBmp = draw.getBitmap();
                mMap.addMarker(new MarkerOptions()
                        //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_icon))
                        .icon(BitmapDescriptorFactory.fromBitmap(drawBmp))
                        .
                                position(
                                        new LatLng(Lat, Lng)).title("( " + time + " )").snippet(address)).showInfoWindow();
        }catch (Exception ex){
        }
    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}