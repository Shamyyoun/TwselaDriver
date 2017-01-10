package com.twsela.driver.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Shamyyoun on 11/29/16.
 */
public class FCMRegistrationService extends FirebaseInstanceIdService {
    private static final String TAG = "FCMRegistration";

    @Override
    public void onTokenRefresh() {
        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Firebase token refreshed: " + firebaseToken);
        Log.e(TAG, "Should send the new token to the server.");
        // TODO should send the new token when refreshed to the server
    }
}