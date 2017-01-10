package com.twsela.driver.utils;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Shamyyoun on 27/1/16.
 * A utils class, contains useful methods for working with Firebase Services such as fcm.
 * Some of these methods need the Firebase libraries (like fcm library) to be added to the project.
 */
public class FirebaseUtils {
    public static final String KEY_FIREBASE_TOKEN = "firebase_token_key";

    public static void cacheFirebaseToken(Context context, String token) {
        Utils.cacheString(context, KEY_FIREBASE_TOKEN, token);
    }

    /**
     * Gets the cached firebase token (if found and the app version code didn't change) or return null.
     *
     * @param context
     * @return the cached firebase token if no new one is required otherwise null.
     */
    public static String getCachedFirebaseToken(Context context) {
        String token = Utils.getCachedString(context, KEY_FIREBASE_TOKEN, null);
        if (token == null) {
            Utils.logE("Firebase token not found.");
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

    /**
     * method used to generate a firebase token and return it
     *
     * @return
     */
    public static String getFirebaseToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        return token;
    }

    public static void clearGCMToken(Context context) {
        Utils.clearCachedKey(context, KEY_FIREBASE_TOKEN);
    }
}