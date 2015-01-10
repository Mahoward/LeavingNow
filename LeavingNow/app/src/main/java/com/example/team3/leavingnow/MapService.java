package com.example.team3.leavingnow;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MapService extends Service {

    IBinder mBinder;
    LocationManager lm;
    LocationListener locationListener;
    //the amount of time after which you want to stop the service
    private final long INTERVAL = 5000; // I choose 5 seconds

    TimerTask myTask = new TimerTask()
    {
        public void run()
        {
            Log.i("timer", "timer done");
            stopSelf();
        }
    };
    //Timer that will make the runnable run.
    Timer myTimer = new Timer();

    CountDownTimer aCounter = new CountDownTimer(15000 , 1000) {
        public void onTick(long millisUntilFinished) {
            Log.i("timer", "timer going");
            fireBaseUpdate();
        }
        public void onFinish() {
            stopSelf();
        }
    };


    @Override
    public void onCreate(){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //myTimer.schedule(myTask, INTERVAL);
        aCounter.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(locationListener);

        Toast.makeText(this, "Leaving Now no longer running", Toast.LENGTH_LONG).show();

    }

    private void fireBaseUpdate(){



        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Firebase ref = new Firebase("https://leaving-now.firebaseio.com/");
            SharedPreferences prefs = getSharedPreferences("com.example.homebase", Context.MODE_PRIVATE);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Log.i("location",String.valueOf(latitude));

            Firebase usersRef = ref.child("Users");
            usersRef.child(prefs.getString("id", "null")).child("Lat").setValue(latitude);
            usersRef.child(prefs.getString("id", "null")).child("Long").setValue(longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

    }


}
