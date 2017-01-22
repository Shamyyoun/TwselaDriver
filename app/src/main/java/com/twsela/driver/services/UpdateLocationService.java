package com.twsela.driver.services;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.twsela.driver.ApiRequests;
import com.twsela.driver.Const;
import com.twsela.driver.R;
import com.twsela.driver.activities.MainActivity;
import com.twsela.driver.controllers.ActiveUserController;
import com.twsela.driver.models.entities.Driver;
import com.twsela.driver.utils.Utils;

/**
 * Created by Shamyyoun on 1/15/17.
 */
public class UpdateLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, LocationListener {
    private GoogleApiClient googleApiClient;
    private NotificationManager notificationManager;
    private ActiveUserController activeUserController;

    @Override
    public void onCreate() {
        super.onCreate();

        // create objects
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        activeUserController = new ActiveUserController(this);

        // create google api client if required
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // connect the google client api to request updates
        googleApiClient.connect();
        return START_STICKY;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // start listening and show ongoing notification
        startLocationListener();
        showTheNotification();
    }

    private void startLocationListener() {
        // check location perm
        if (!isLocationPermGranted()) {
            return;
        }

        // create the location request
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(Const.LOCATION_REFRESH_RATE);
        locationRequest.setFastestInterval(Const.LOCATION_REFRESH_RATE);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // start listener for location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void showTheNotification() {
        // create the content intent
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.twsela))
                .setContentText(getString(R.string.you_are_online))
                .setOngoing(true)
                .setContentIntent(contentIntent);

        // show the notification
        notificationManager.notify(Const.NOTI_UPDATE_LOCATION_SERVICES, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        endTheService();
    }

    private void endTheService() {
        stopLocationListener();
        cancelTheNotification();
    }

    private void stopLocationListener() {
        // check location perm
        if (!isLocationPermGranted()) {
            return;
        }

        // stop listening
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void cancelTheNotification() {
        notificationManager.cancel(Const.NOTI_UPDATE_LOCATION_SERVICES);
    }

    private boolean isLocationPermGranted() {
        return checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location);
    }

    private void updateLocation(Location location) {
        // prepare values
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        float bearing = location.getBearing();
        Driver user = activeUserController.getUser();
        String id = user.getId();
        String tripId = null;
        if (user.getActiveTrip() != null) {
            tripId = user.getActiveTrip().getId();
        }

        // create and send the request if possible
        if (Utils.hasInternetConnection(this)) {
            ApiRequests.updateLocation(this, null, id, lat, lng, bearing, tripId);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
}
