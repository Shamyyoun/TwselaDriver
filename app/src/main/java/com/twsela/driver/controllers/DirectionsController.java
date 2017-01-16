package com.twsela.driver.controllers;

import com.google.android.gms.maps.model.LatLng;
import com.twsela.driver.models.entities.DirectionsStep;
import com.twsela.driver.models.responses.DirectionsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 1/16/17.
 */

public class DirectionsController {

    public List<LatLng> getPoints(DirectionsResponse response) {
        // get steps
        List<DirectionsStep> steps = getSteps(response);

        // check them
        if (steps == null) {
            return null;
        }

        // prepare points list
        List<LatLng> points = new ArrayList<>();
        for (DirectionsStep step : steps) {
            try {
                List<LatLng> stepPoints = decodePoly(step.getPolyline().getPoints());
                points.addAll(stepPoints);
            } catch (Exception e) {
            }
        }

        return points;
    }

    private List<DirectionsStep> getSteps(DirectionsResponse response) {
        try {
            return response.getRoutes().get(0).getLegs().get(0).getSteps();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method to decode polyline points
     * From: http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private List<LatLng> decodePoly(String encodedPoint) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encodedPoint.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encodedPoint.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encodedPoint.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
