package com.twsela.driver;

import android.content.Context;

import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.connection.ConnectionListener;
import com.twsela.driver.models.bodies.TripActionBody;
import com.twsela.driver.models.entities.Driver;
import com.twsela.driver.models.entities.MongoLocation;
import com.twsela.driver.models.responses.DirectionsResponse;
import com.twsela.driver.models.responses.DistanceMatrixResponse;
import com.twsela.driver.models.responses.LoginResponse;
import com.twsela.driver.models.responses.ServerResponse;
import com.twsela.driver.models.responses.TripResponse;
import com.twsela.driver.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Shamyyoun on 5/31/16.
 */
public class ApiRequests {

    public static ConnectionHandler<LoginResponse> login(Context context, ConnectionListener<LoginResponse> listener,
                                                         String username, String password, String gcm) {

        // prepare url
        String url = AppUtils.getDriverApiUrl(Const.ROUTE_LOGIN);

        // create connection handler
        ConnectionHandler<LoginResponse> connectionHandler = new ConnectionHandler(context, url,
                LoginResponse.class, listener, Const.ROUTE_LOGIN);

        // add parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.PARAM_USERNAME, username);
        params.put(Const.PARAM_PASSWORD, password);
        params.put(Const.PARAM_GCM, gcm);
        connectionHandler.setParams(params);

        // execute and return
        connectionHandler.executePost();
        return connectionHandler;
    }

    public static ConnectionHandler<ServerResponse> updateStatus(Context context, ConnectionListener<ServerResponse> listener,
                                                                 String id, boolean isOnline) {

        // prepare url
        String url = AppUtils.getDriverApiUrl(Const.ROUTE_UPDATE_STATUS);

        // create connection handler
        ConnectionHandler<ServerResponse> connectionHandler = new ConnectionHandler(context, url,
                ServerResponse.class, listener, Const.ROUTE_UPDATE_STATUS);

        // create and set the body
        Driver body = new Driver();
        body.setId(id);
        body.setOnline(isOnline);
        connectionHandler.setBody(body);

        // execute and return
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<ServerResponse> updateLocation(Context context, ConnectionListener<ServerResponse> listener,
                                                                   String id, double lat, double lng, float bearing) {

        // prepare url
        String url = AppUtils.getDriverApiUrl(Const.ROUTE_UPDATE_LOCATION);

        // create connection handler
        ConnectionHandler<ServerResponse> connectionHandler = new ConnectionHandler(context, url,
                ServerResponse.class, listener, Const.ROUTE_UPDATE_LOCATION);

        // create and set the body
        Driver body = new Driver();
        body.setId(id);
        MongoLocation location = new MongoLocation();
        List<Double> coordinates = new ArrayList<>(2);
        coordinates.add(lat);
        coordinates.add(lng);
        location.setCoordinates(coordinates);
        body.setLocation(location);
        body.setBearing("" + bearing);
        connectionHandler.setBody(body);

        // execute and return
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<TripResponse> getTripDetails(Context context,
                                                                 ConnectionListener<TripResponse> listener, String id) {

        // prepare url
        String url = AppUtils.getTripApiUrl(Const.ROUTE_GET_DETAILS_BY_ID);
        url += "?" + Const.PARAM_ID + "=" + id;

        // create connection handler
        ConnectionHandler<TripResponse> connectionHandler = new ConnectionHandler(context, url,
                TripResponse.class, listener, Const.ROUTE_GET_DETAILS_BY_ID);

        // execute and return
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<ServerResponse> acceptTrip(Context context, ConnectionListener<ServerResponse> listener,
                                                               String driverId, String carId, String tripId) {

        return tripAction(context, listener, Const.ROUTE_ACCEPT_TRIP, driverId, carId, tripId);
    }

    public static ConnectionHandler<ServerResponse> cancelTrip(Context context, ConnectionListener<ServerResponse> listener,
                                                               String driverId, String carId, String tripId) {

        return tripAction(context, listener, Const.ROUTE_CANCEL_TRIP, driverId, carId, tripId);
    }

    public static ConnectionHandler<ServerResponse> arriveTrip(Context context, ConnectionListener<ServerResponse> listener,
                                                               String driverId, String carId, String tripId) {

        return tripAction(context, listener, Const.ROUTE_ARRIVED, driverId, carId, tripId);
    }

    public static ConnectionHandler<ServerResponse> startTrip(Context context, ConnectionListener<ServerResponse> listener,
                                                              String driverId, String carId, String tripId) {

        return tripAction(context, listener, Const.ROUTE_START_TRIP, driverId, carId, tripId);
    }

    public static ConnectionHandler<ServerResponse> tripAction(Context context, ConnectionListener<ServerResponse> listener, String route,
                                                               String driverId, String carId, String tripId) {

        // prepare url
        String url = AppUtils.getDriverApiUrl(route);

        // create connection handler
        ConnectionHandler<ServerResponse> connectionHandler = new ConnectionHandler(context, url,
                ServerResponse.class, listener, route);

        // add parameters
        Map<String, String> params = new HashMap<>();
        params.put(Const.PARAM_DRIVER_ID, driverId);
        params.put(Const.PARAM_CAR_ID, carId);
        params.put(Const.PARAM_TRIP_ID, tripId);
        connectionHandler.setParams(params);

        // execute and return
        connectionHandler.executePost();
        return connectionHandler;
    }

    public static ConnectionHandler<ServerResponse> endTrip(Context context, ConnectionListener<ServerResponse> listener,
                                                            String driverId, String carId, String tripId,
                                                            double lat, double lng, String address) {

        // prepare url
        String url = AppUtils.getDriverApiUrl(Const.ROUTE_END_TRIP);

        // create connection handler
        ConnectionHandler<ServerResponse> connectionHandler = new ConnectionHandler(context, url,
                ServerResponse.class, listener, Const.ROUTE_END_TRIP);

        // create and set the body
        TripActionBody body = new TripActionBody();
        body.setDriverId(driverId);
        body.setCarId(carId);
        body.setTripId(tripId);
        MongoLocation location = new MongoLocation();
        List<Double> coordinates = new ArrayList<>(2);
        coordinates.add(lat);
        coordinates.add(lng);
        location.setCoordinates(coordinates);
        body.setActualDestinationLocation(location);
        body.setActualDestinationAddress(address);
        connectionHandler.setBody(body);

        // execute and return
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<DistanceMatrixResponse> getDistanceMatrix(Context context,
                                                                              ConnectionListener<DistanceMatrixResponse> listener,
                                                                              double originLat, double originLng,
                                                                              double destLat, double destLng,
                                                                              String apiKey, String language) {
        // prepare url
        String url = String.format(Locale.ENGLISH,
                "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%f,%f&destinations=%f,%f&language=%s&key=%s",
                originLat, originLng, destLat, destLng, language, apiKey);

        // create connection handler
        ConnectionHandler<DistanceMatrixResponse> connectionHandler = new ConnectionHandler(context, url,
                DistanceMatrixResponse.class, listener, Const.TAG_DISTANCE_MATRIX);

        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<DirectionsResponse> getDirections(Context context,
                                                                          ConnectionListener<DirectionsResponse> listener,
                                                                          double originLat, double originLng,
                                                                          double destLat, double destLng,
                                                                          String apiKey, String language) {
        // prepare url
        String url = String.format(Locale.ENGLISH,
                "https://maps.googleapis.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f&language=%s&key=%s",
                originLat, originLng, destLat, destLng, language, apiKey);

        // create connection handler
        ConnectionHandler<DirectionsResponse> connectionHandler = new ConnectionHandler(context, url,
                DirectionsResponse.class, listener, Const.TAG_DIRECTIONS);

        connectionHandler.executeGet();
        return connectionHandler;
    }

}
