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

    public float calculateDistance(List<MongoLocation> points) {
        float distance = 0; // final distance

        float[] distanceResult = new float[1]; // used to hold every calculation result
        for (int i = 0; i < points.size() - 1; i++) {
            // get objects
            MongoLocation location1 = points.get(i);
            MongoLocation location2 = points.get(i + 1);

            // calc distance
            Location.distanceBetween(getLatitude(location1), getLongitude(location1),
                    getLatitude(location2), getLongitude(location2), distanceResult);

            // increment total distance
            distance += distanceResult[0];
        }

        return distance;
    }

    public float calculateDistance(Location location1, Location location2) {
        try {
            float[] results = new float[1];
            Location.distanceBetween(
                    location1.getLatitude(), location1.getLongitude(),
                    location2.getLatitude(), location2.getLongitude(), results);

            return results[0];
        } catch (Exception e) {
            return 0;
        }
    }
}
