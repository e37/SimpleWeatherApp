package learn.com.weatherapp;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

class MyLocationListener implements LocationListener {
    static Context mainContext;

    static LocationManager locationManager;
    static LocationListener locationListener;

    static Location imHere; // здесь будет всегда доступна самая последняя информация о местоположении пользователя.

    public static void SetUpLocationListener(Context context) {
        mainContext = context;
        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    100,
                    1000,
                    locationListener, Looper.getMainLooper());;//, Looper.getMainLooper());
            Log.e("myTags", "SetUpLocationListener: network enabled");
            imHere = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    100,
                    1000,
                    locationListener, Looper.getMainLooper());
            imHere = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.e("myTags", "SetUpLocationListener: GPS enabled");
            Log.e("myLogs", "SetUpLocationListener:  im HERE:  " + String.valueOf(imHere));
        }

//        AsyncTask<Void, Void, Void> mAsyncTask = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                MyLocationListener.SetUpLocationListener(mainContext);
//                while (MyLocationListener.imHere == null) {
//                    try {
//                        Log.e("myTags", "waiting a location");
//                        TimeUnit.MILLISECONDS.sleep(300);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Log.e("myTags", " location has been found. wait 5 sec");
//                try {
//                    TimeUnit.SECONDS.sleep(5);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//
//        };
//        mAsyncTask.execute();
    }

    public static void turnItOff() {
        if (ActivityCompat.checkSelfPermission(mainContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onLocationChanged(Location loc) {
        imHere = loc;

        WeatherFragment wf = new WeatherFragment();
        wf.updateWeatherData((int)loc.getLatitude(),(int)loc.getLongitude());
        Log.e("myLogs", "OnLocationChange:  im HERE:  " + String.valueOf(imHere));
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
