package com.twsela.driver;

import android.content.Context;

import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.connection.ConnectionListener;
import com.twsela.driver.models.entities.MongoLocation;
import com.twsela.driver.models.entities.User;
import com.twsela.driver.models.responses.LoginResponse;
import com.twsela.driver.models.responses.ServerResponse;
import com.twsela.driver.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shamyyoun on 5/31/16.
 */
public class ApiRequests {

    public static ConnectionHandler<LoginResponse> login(Context context, ConnectionListener<LoginResponse> listener,
                                                         String username, String password, String gcm) {

        // prepare url & tag
        String url = AppUtils.getDriverApiUrl(Const.ROUTE_LOGIN);
        String tag = AppUtils.getDriverTag(Const.ROUTE_LOGIN);

        // create connection handler
        ConnectionHandler<LoginResponse> connectionHandler = new ConnectionHandler(context, url,
                LoginResponse.class, listener, tag);

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
        User body = new User();
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
        User body = new User();
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

}
