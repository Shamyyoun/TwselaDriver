package com.twsela.driver.activities;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twsela.driver.ApiRequests;
import com.twsela.driver.Const;
import com.twsela.driver.R;
import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.controllers.ActiveUserController;
import com.twsela.driver.controllers.TripController;
import com.twsela.driver.models.entities.Trip;
import com.twsela.driver.models.enums.TripStatus;
import com.twsela.driver.models.responses.ServerResponse;
import com.twsela.driver.models.responses.TripResponse;
import com.twsela.driver.utils.AppUtils;
import com.twsela.driver.utils.Utils;

public class TripRequestActivity extends ParentActivity {
    private String tripId;
    private ActiveUserController activeUserController;
    private TripController tripController;

    private TextView tvPassengerName;
    private TextView tvPickupAddress;
    private TextView tvDestinationAddress;
    private Button btnAccept;
    private Button btnIgnore;

    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_request);

        // obtain main objects
        tripId = getIntent().getStringExtra(Const.KEY_ID);
        activeUserController = new ActiveUserController(this);
        tripController = new TripController();

        // init views
        tvPassengerName = (TextView) findViewById(R.id.tv_passenger_name);
        tvPickupAddress = (TextView) findViewById(R.id.tv_pickup_address);
        tvDestinationAddress = (TextView) findViewById(R.id.tv_destination_address);
        btnAccept = (Button) findViewById(R.id.btn_accept);
        btnIgnore = (Button) findViewById(R.id.btn_ignore);

        // add listeners
        btnAccept.setOnClickListener(this);
        btnIgnore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_accept) {
            preAcceptRequest();
        } else if (v.getId() == R.id.btn_ignore) {
            onBackPressed();
        } else {
            super.onClick(v);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // load trip details and wake up the device
        preLoadTripDetails();
        wakeupDevice();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // load trip details and wake up the device
        tripId = intent.getStringExtra(Const.KEY_ID);
        preLoadTripDetails();
        wakeupDevice();
    }

    private void wakeupDevice() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
    }

    private void updateUI() {
        // set passenger name
        String passengerUsername = tripController.getPassengerUsername(trip);
        if (passengerUsername != null) {
            tvPassengerName.setText(passengerUsername);
        } else {
            tvPassengerName.setText("---------------");
        }

        // set pickup address
        if (!Utils.isNullOrEmpty(trip.getPickupAddress())) {
            tvPickupAddress.setText(trip.getPickupAddress());
        } else {
            tvPickupAddress.setText(R.string.point_on_map);
        }

        // set destination
        if (trip.getDestinationLocation() != null) {
            if (!Utils.isNullOrEmpty(trip.getDestinationAddress())) {
                tvDestinationAddress.setText(trip.getDestinationAddress());
            } else {
                tvDestinationAddress.setText(R.string.point_on_map);
            }
        } else {
            tvDestinationAddress.setText(R.string.not_available);
        }
    }

    private void preLoadTripDetails() {
        // check internet connection
        if (hasInternetConnection()) {
            showProgressDialog();
            loadTripDetails();
        } else {
            Utils.showShortToast(this, R.string.no_internet_connection);
        }
    }

    private void loadTripDetails() {
        // send the request
        ConnectionHandler connectionHandler = ApiRequests.getTripDetails(this, this, tripId);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        // check tag
        if (Const.ROUTE_GET_DETAILS_BY_ID.equals(tag)) {
            // trip details request
            // check response
            TripResponse tripResponse = (TripResponse) response;
            if (tripResponse.isSuccess() && tripResponse.getContent() != null) {
                // update the ui
                this.trip = tripResponse.getContent();
                updateUI();
            } else {
                // show msg
                String msg = AppUtils.getResponseMsg(this, tripResponse, R.string.failed_loading_details);
                Utils.showShortToast(this, msg);
            }
        } else if (Const.ROUTE_ACCEPT_TRIP.equals(tag)) {
            // accept trip request
            // check response
            ServerResponse serverResponse = (ServerResponse) response;
            if (serverResponse.isSuccess()) {
                // update active trip and open details activity
                activeUserController.updateActiveTripStatus(tripId, TripStatus.ACCEPTED);
                openTripDetailsActivity();
            } else {
                // show msg
                String msg = AppUtils.getResponseMsg(this, serverResponse, R.string.failed_accepting_request);
                Utils.showShortToast(this, msg);
            }
        }
    }

    private void openTripDetailsActivity() {
        Intent intent = new Intent(this, TripActivity.class);
        intent.putExtra(Const.KEY_ID, tripId);
        intent.putExtra(Const.KEY_STATUS, TripStatus.ACCEPTED.getValue());
        startActivity(intent);
        finish();
    }

    private void preAcceptRequest() {
        // check internet connection
        if (hasInternetConnection()) {
            showProgressDialog();
            acceptRequest();
        } else {
            Utils.showShortToast(this, R.string.no_internet_connection);
        }
    }

    private void acceptRequest() {
        // prepare params
        String driverId = activeUserController.getUser().getId();
        String carId = activeUserController.getCarId();

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.acceptTrip(this, this, driverId, carId, tripId);
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // release screen lock
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
    }
}