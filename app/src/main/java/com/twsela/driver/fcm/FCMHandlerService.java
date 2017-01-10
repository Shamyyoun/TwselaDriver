package com.twsela.driver.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Shamyyoun on 11/29/16.
 */
public class FCMHandlerService extends FirebaseMessagingService {
    private static final String TAG = "FCMHandler";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        logDetails(remoteMessage);
    }

    private void logDetails(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        } else {
            Log.e(TAG, "Message doesn't contain data payload.");
        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        } else {
            Log.e(TAG, "Message doesn't contain Notification Body.");
        }
    }
}