package com.twsela.driver.controllers;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.twsela.driver.models.entities.MongoLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 1/11/17.
 */

public class LocationController {
    public boolean isLocationValid(double lat, double lng) {
        return !((lat == 0 && lng == 0));
    }

    public boolean isLocationValid(Location location) {
        if (location == null) {
            return false;
        } else {
            return isLocationValid(location.getLatitude(), location.getLongitude());
        }
    }

    public double getLatitude(MongoLocation mongoLocation) {
        try {
            return mongoLocation.getCoordinates().get(0);
        } catch (Exception e) {
            return 0;
        }
    }

    public double getLongitude(MongoLocation mongoLocation) {
        try {
            return mongoLocation.getCoordinates().get(1);
        } catch (Exception e) {
            return 0;
        }
    }

    public MongoLocation createLocation(double lat, double lng) {
        MongoLocation location = new MongoLocation();
        List<Double> coordinates = new ArrayList<>(2);
        coordinates.add(lat);
        coordinates.add(lng);
        location.setCoordinates(coordinates);
        return location;
    }

    public LatLng createLatLng(MongoLocation location) {
        LatLng latLng = new LatLng(getLatitude(location), getLongitude(location));
        return latLng;
    }
}
