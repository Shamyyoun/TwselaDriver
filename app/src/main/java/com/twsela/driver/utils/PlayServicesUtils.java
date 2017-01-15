package com.twsela.driver.utils;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by basim on 27/1/16.
 * A utils class, contains useful methods for working with Google Play Services such as gcm.
 * Some of these methods need the Play Services libraries (like GCM library) to be added to the project.
 */
public class PlayServicesUtils {

    public static final String KEY_GCM_TOKEN = "gcm_token_key";

    /**
     * Checks if the google play services is available on the device, because
     * other services depend on it such as the GCM service.
     *
     * @param context an context that can receive the result of the error dialog.
     * @return true if available or false.
     */
    public static boolean isPlayServicesAvailable(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }


    public static void cacheGCMToken(Context context, String token) {
        Utils.cacheString(context, KEY_GCM_TOKEN, token);
    }

    /**
     * Gets the cached GCM token (if found and the app version code didn't change) or return null.
     *
     * @param context
     * @return the cached GCM token if no new one is required otherwise null.
     */
    public static String getCachedGCMToken(Context context) {
        String token = Utils.getCachedString(context, KEY_GCM_TOKEN, null);
        if (token == null) {
            Utils.logE("GCM Token not found.");
            return null;
        }

        // Check if app was updated; if so, it must clear the token
        // since the existing token is not guaranteed to work with the new app version.
        int cachedVersionCode = Utils.getCachedAppVersionCode(context);
        int currentVersion = Utils.getAppVersionCode(context);
        if (cachedVersionCode != currentVersion) {
            Utils.logE("App version changed.");
            return null;
        }
        return token;
    }

    public static void clearGCMToken(Context context) {
        Utils.clearCachedKey(context, KEY_GCM_TOKEN);
    }


}
