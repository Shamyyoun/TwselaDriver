package com.twsela.driver;

/**
 * Created by Shamyyoun on 18/12/15.
 * A class contains constants for the application and for some of the utility classes.
 */
public class Const {
    // App level constants:--------------
    public static final String LOG_TAG = "TwselaDriver";
    public static final String SHARED_PREFERENCES_FILE_NAME = "TwselaDriver";
    public static final String APP_FILES_DIR = "/.twsela_driver";
    public static final String END_POINT = "http://35.167.31.161:3300";
    public static final int DEFAULT_ITEM_ID = -1; // this is used to add a default item in lists used in adapter
    public static final int INITIAL_ZOOM_LEVEL = 15;
    public static final int LOCATION_REFRESH_RATE = 6 * 1000; // in milliseconds
    public static final int MAP_REFRESH_RATE = 6 * 1000; // in milliseconds
    public static final int GOOGLE_MAX_ORIGINS = 10; // for google distanceMatrix api
    public static final int DISTANCE_LISTENER_MIN_TIME = 1 * 1000;
    public static final int DISTANCE_LISTENER_MIN_DISTANCE = 0;

    // Server Constants:--------------------
    public static final int SER_CODE_200 = 200;
    public static final String SER_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String SER_DATE_FORMAT = "yyyy/MM/dd";
    public static final String SER_TIME_FORMAT = "HH:mm:ss";

    // Permission Requests:------------------
    public static final int PERM_REQ_LOCATION = 1;

    // API Routes:---------------------------
    public static final String ROUTE_DRIVER = "driver";
    public static final String ROUTE_TRIP = "trip";
    public static final String ROUTE_LOGIN = "login";
    public static final String ROUTE_UPDATE_STATUS = "updateStatus";
    public static final String ROUTE_UPDATE_LOCATION = "updateLocation";
    public static final String ROUTE_GET_DETAILS_BY_ID = "getDetailsById";
    public static final String ROUTE_ACCEPT_TRIP = "acceptTrip";
    public static final String ROUTE_CANCEL_TRIP = "cancelTrip";
    public static final String ROUTE_ARRIVED = "arrived";
    public static final String ROUTE_START_TRIP = "startTrip";
    public static final String ROUTE_END_TRIP = "endTrip";

    // API Params:---------------------------
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_GCM = "gcm";
    public static final String PARAM_ID = "id";
    public static final String PARAM_DRIVER_ID = "driver_id";
    public static final String PARAM_CAR_ID = "car_id";
    public static final String PARAM_TRIP_ID = "trip_id";

    // SharePrefs Keys:---------------------
    public static final String SP_USER = "user";

    // Activity Requests:-------------------
    public static final int REQ_ENABLE_GPS = 1;

    // Keys:--------------------------------
    public static final String KEY_ID = "id";
    public static final String KEY_STATUS = "status";

    // Notification IDs:-------------------
    public static final int NOTI_UPDATE_LOCATION_SERVICES = 1;
    public static final int NOTI_TRIP_CHANGED = 2;

    // Others:-----------------------------
    public static final String TAG_DISTANCE_MATRIX = "distancematrix";
    public static final String TAG_DIRECTIONS = "directions";
}
