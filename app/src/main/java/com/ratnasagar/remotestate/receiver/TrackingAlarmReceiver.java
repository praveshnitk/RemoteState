package com.ratnasagar.remotestate.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.MediaPlayer;
import com.ratnasagar.remotestate.helper.PreferenceHelper;
import com.ratnasagar.remotestate.services.CurrentLocation;


public class TrackingAlarmReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        PreferenceHelper pHelper = new PreferenceHelper(context);
        int trackTime = 30;
        try {
            if (pHelper.getString("StartStop","Start").equals("Start")){
                CurrentLocation currentLocation = new CurrentLocation(context);
                currentLocation.buildGoogleApiClient();
                Intent intent2 = new Intent(context, TrackingAlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 123, intent2, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + (1000 * trackTime), pendingIntent);
            }
        }
        catch (Exception ex){
            Intent intent2 = new Intent(context, TrackingAlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 123, intent2, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + (1000 * trackTime), pendingIntent);
        }
    }
}
