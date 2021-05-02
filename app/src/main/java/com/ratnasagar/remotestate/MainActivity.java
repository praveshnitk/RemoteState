package com.ratnasagar.remotestate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ratnasagar.remotestate.adapter.TrackingListAdapter;
import com.ratnasagar.remotestate.helper.DataBaseHelper;
import com.ratnasagar.remotestate.helper.PreferenceHelper;
import com.ratnasagar.remotestate.model.TrackingData;
import com.ratnasagar.remotestate.receiver.TrackingAlarmReceiver;
import com.ratnasagar.remotestate.services.CurrentLocation;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnStartLocation,btnStopLocation;
    private PreferenceHelper pHelper;
    private TextView tvTrack;
    private Button btnReload;
    private TextView tvStartTime,tvStopTime;
    private RecyclerView recycler_view;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pHelper = new PreferenceHelper(MainActivity.this);
        dbHelper = new DataBaseHelper(MainActivity.this);
        getSupportActionBar().setTitle("Remote State Tracking");
        btnStartLocation = (Button) findViewById(R.id.btnStartLocation);
        btnStopLocation = (Button) findViewById(R.id.btnStopLocation);
        btnReload = (Button) findViewById(R.id.btnReload);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvStopTime = (TextView) findViewById(R.id.tvStopTime);
        tvTrack = (TextView) findViewById(R.id.tvTrack);
        tvTrack = (TextView) findViewById(R.id.tvTrack);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recycler_view.setLayoutManager(mLayoutManager);
        tvTrack.setText("Tracking "+pHelper.getString("StartStop",""));
        tvStartTime.setText(pHelper.getString("StartTime",""));
        tvStopTime.setText(pHelper.getString("StopTime",""));
        btnStartLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pHelper.getString("StartStop","").equals("Stop") || pHelper.getString("StartStop","").equals("")){
                    try {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Start Tracking")
                                .setMessage("Do you want to start tracking?")
                                .setCancelable(false)
                                .setIcon(R.mipmap.ic_launcher)
                                .setNegativeButton(
                                        "No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(MainActivity.this, TrackingAlarmReceiver.class);
                                                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 123, intent, PendingIntent.FLAG_ONE_SHOT);
                                                AlarmManager mgr = (AlarmManager) MyApplication.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
                                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
                                                pHelper.setString("StartStop","Start");
                                                pHelper.setString("StartTime",getDateAndTime());
                                                pHelper.setString("StopTime","");
                                                tvTrack.setText("Tracking "+pHelper.getString("StartStop",""));
                                                tvStartTime.setText(pHelper.getString("StartTime",""));
                                                tvStopTime.setText("");
                                            }
                                        }
                                ).show();
                    }
                    catch (Throwable tx) {
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Tracking already start you can stop tracking.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStopLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pHelper.getString("StartStop","").equals("Start")){
                    try {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Start Tracking")
                                .setMessage("Do you want to stop tracking?")
                                .setCancelable(false)
                                .setIcon(R.mipmap.ic_launcher)
                                .setNegativeButton(
                                        "No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(MainActivity.this, TrackingAlarmReceiver.class);
                                                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 123, intent, PendingIntent.FLAG_ONE_SHOT);
                                                AlarmManager mgr = (AlarmManager) MyApplication.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
                                                mgr.cancel(pendingIntent);
                                                pHelper.setString("StartStop","Stop");
                                                pHelper.setString("StopTime",getDateAndTime());
                                                tvTrack.setText("Tracking "+pHelper.getString("StartStop",""));
                                                tvStopTime.setText(pHelper.getString("StopTime",""));
                                            }
                                        }
                                ).show();
                    }
                    catch (Throwable tx) {
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Tracking already stop you can start tracking.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(MainActivity.this, UserLocation.class);
                startActivity(intent);*/
                callSetUpAdapter();
            }
        });

        callSetUpAdapter();
    }

    private void callSetUpAdapter() {

        ArrayList<TrackingData> trackingData = dbHelper.getTrackAll();
        TrackingListAdapter trackingListAdapter = new TrackingListAdapter(trackingData, MainActivity.this);
        recycler_view.setAdapter(trackingListAdapter);
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
}