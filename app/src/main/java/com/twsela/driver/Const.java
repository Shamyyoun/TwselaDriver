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

    // Server Constants:--------------------
    public static final int SER_CODE_200 = 200;
    public static final String SER_DATE_FORMAT = "yyyy/MM/dd";
    public static final String SER_TIME_FORMAT = "HH:mm:ss";

    // Permission Requests:------------------
    public static final int PERM_REQ_LOCATION = 1;

    // API Routes:---------------------------
    public static final String ROUTE_DRIVER = "driver";
    public static final String ROUTE_LOGIN = "login";

    // API Params:---------------------------
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_GCM = "gcm";

    // SharePrefs Keys:---------------------
    public static final String SP_USER = "user";
}
