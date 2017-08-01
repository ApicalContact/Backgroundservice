package com.apical.backgroundservice;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by tejas on 12/5/17.
 */

public class FieldViewService extends Service implements LocationListener {
    private Handler handler;
    private Runnable runnable;
    private Context context = this;
    private String coid = "";
    private String userId = "";
    private final long SCHEDULE_INTERVAL = 30 * 1000;

    LocationManager locationManager;
    Location location;
    String mprovider;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        showToast("Service started");

        initializeLocationManager();
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                //  showToast("Api call");
                if (location == null)
                    initializeLocationManager();
                getUserDetails();
                requestAPI();
                handler.postDelayed(runnable, SCHEDULE_INTERVAL);
            }
        };
        handler.postDelayed(runnable, SCHEDULE_INTERVAL);
        return START_STICKY;
    }



    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        showToast("app removed");
        Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);

  }


  public void initializeLocationManager(){
      locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      Criteria criteria = new Criteria();
      mprovider = locationManager.getBestProvider(criteria, false);

      if (mprovider != null && !mprovider.equals("")) {
//          if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//              return;
//          }
          location = locationManager.getLastKnownLocation(mprovider);
          locationManager.requestLocationUpdates(mprovider, SCHEDULE_INTERVAL/2, 2, this);

          if (location != null)
              onLocationChanged(location);
          else
              Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
      }
  }
    private void getUserDetails() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            //   showToast("Exception " + e.getMessage());
        }
    }

    private void showToast(final String msg) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FieldViewService.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestAPI() {

        try {
            showToast("Service running.");
            if (location != null){
                showToast("API call location : "+location.getLatitude() + " "+location.getLongitude());
            }
            Notification note=new Notification(R.mipmap.ic_launcher,
                    "Can you hear the music?",
                    System.currentTimeMillis());
            Intent i=new Intent(this, FieldViewService.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pi= PendingIntent.getActivity(this, 0,
                    i, 0);

//            note.setLatestEventInfo(this, "Fake Player",
//                    "Now Playing: \"Ummmm, Nothing\"",
//                    pi);
            note.flags|=Notification.FLAG_NO_CLEAR;

            startForeground(1337, note);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        showToast("Current location : "+location.getLatitude() + " "+location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
