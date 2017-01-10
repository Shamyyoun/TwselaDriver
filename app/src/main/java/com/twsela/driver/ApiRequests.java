package com.twsela.driver;

import android.content.Context;

import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.connection.ConnectionListener;
import com.twsela.driver.models.responses.LoginResponse;
import com.twsela.driver.utils.AppUtils;

import java.util.HashMap;
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

}
