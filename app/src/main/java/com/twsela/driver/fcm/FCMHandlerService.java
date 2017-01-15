package com.twsela.driver.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.twsela.driver.Const;
import com.twsela.driver.R;
import com.twsela.driver.activities.TripRequestActivity;
import com.twsela.driver.controllers.ActiveUserController;
import com.twsela.driver.models.enums.NotificationKey;
import com.twsela.driver.models.enums.TripStatus;
import com.twsela.driver.models.payloads.TripRequestPayload;
import com.twsela.driver.utils.Utils;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Shamyyoun on 11/29/16.
 */
public class FCMHandlerService extends FirebaseMessagingService {
    private static final String TAG = "FCMHandler";
    private ActiveUserController activeUserController;

    @Override
    public void onCreate() {
        super.onCreate();
        activeUserController = new ActiveUserController(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // log the details
        logDetails(remoteMessage);

        // check active user
        ActiveUserController activeUserController = new ActiveUserController(this);
        if (!activeUserController.hasLoggedInUser()) {
            return;
        }

        // check data map
        Map data = remoteMessage.getData();
        if (data == null) {
            return;
        }

        // prepare key and content
        String key = null;
        String contentStr = null;
        try {
            String key1Str = remoteMessage.getData().get("key1");
            JSONObject key1Object = new JSONObject(key1Str);
            JSONObject dataObject = key1Object.getJSONObject("data");
            key = dataObject.getString("key");
            contentStr = dataObject.getJSONObject("content").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (NotificationKey.TRIP_REQUEST.getValue().equals(key)) {
            handleTripRequestNotification(contentStr);
        }
    }

    private void handleTripRequestNotification(String contentStr) {
        // check active trip
        if (activeUserController.getUser().getActiveTrip() != null) {
            // driver is already in an active trip
            // ignore this request
            return;
        }

        // parse the trip payload object
        Gson gson = new Gson();
        TripRequestPayload payload = gson.fromJson(contentStr, TripRequestPayload.class);

        // validate the payload
        if (payload == null) {
            return;
        }

        // play horn sound
        Utils.playSound(this, R.raw.horn);

        // open trip request activity
        Intent intent = new Intent(this, TripRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Const.KEY_ID, payload.getId());
        intent.putExtra(Const.KEY_STATUS, TripStatus.ACCEPTED.getValue());
        startActivity(intent);
    }

    private void showNotification(int notificationId, String message) {
        // create the notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.twsela))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri);
        builder.getNotification().flags = Notification.FLAG_AUTO_CANCEL;

        // show the notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }

    private void logDetails(RemoteMessage remoteMessage) {
        if (!Utils.DEBUGGABLE) {
            return;
        }

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