package com.twsela.driver.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twsela.driver.ApiRequests;
import com.twsela.driver.Const;
import com.twsela.driver.R;
import com.twsela.driver.TwselaApp;
import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.controllers.DistanceMatrixController;
import com.twsela.driver.controllers.LocationController;
import com.twsela.driver.controllers.TripController;
import com.twsela.driver.models.entities.DistanceMatrixResult;
import com.twsela.driver.models.entities.Trip;
import com.twsela.driver.models.responses.DistanceMatrixResponse;
import com.twsela.driver.models.responses.TripResponse;
import com.twsela.driver.utils.AppUtils;
import com.twsela.driver.utils.Utils;

public class TripDetailsActivity extends ParentActivity {
    private String tripId;
    private TripController tripController;
    private LocationController locationController;
    private DistanceMatrixController distanceMatrixController;

    private TextView tvPassengerName;
    private TextView tvPickupAddress;
    private TextView tvDestinationAddress;
    private TextView tvDistance;
    private TextView tvDuration;
    private Button btnClose;

    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        // obtain main objects
        tripId = getIntent().getStringExtra(Const.KEY_ID);
        tripController = new TripController();
        locationController = new LocationController();
        distanceMatrixController = new DistanceMatrixController();

        // init views
        tvPassengerName = (TextView) findViewById(R.id.tv_passenger_name);
        tvPickupAddress = (TextView) findViewById(R.id.tv_pickup_address);
        tvDestinationAddress = (TextView) findViewById(R.id.tv_destination_address);
        tvDistance = (TextView) findViewById(R.id.tv_distance);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnClose = (Button) findViewById(R.id.btn_close);

        // add listeners
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_close) {
            onBackPressed();
        } else {
            super.onClick(v);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // load trip details
        preLoadTripDetails();
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
        if (trip.getActualDestinationLocation() != null) {
            if (!Utils.isNullOrEmpty(trip.getActualDestinationAddress())) {
                tvDestinationAddress.setText(trip.getActualDestinationAddress());
            } else {
                tvDestinationAddress.setText(R.string.point_on_map);
            }
        } else {
            tvDestinationAddress.setText(R.string.not_available);
        }

        // set duration
        String duration = tripController.getDuration(this, trip);
        if (duration != null) {
            tvDuration.setText(duration);
        } else {
            tvDuration.setText("---------------");
        }
    }

    private void updateDistanceUI(String distance) {
        if (distance != null) {
            tvDistance.setText(distance);
        } else {
            tvDistance.setText("---------------");
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

        // check tag
        if (Const.ROUTE_GET_DETAILS_BY_ID.equals(tag)) {
            // trip details request
            // check response
            TripResponse tripResponse = (TripResponse) response;
            if (tripResponse.isSuccess() && tripResponse.getContent() != null) {
                // update the ui
                this.trip = tripResponse.getContent();
                updateUI();

                // load distance matrix
                loadDistanceMatrix();
            } else {
                // show msg
                String msg = AppUtils.getResponseMsg(this, tripResponse, R.string.failed_loading_details);
                Utils.showShortToast(this, msg);
            }
        } else if (Const.TAG_DISTANCE_MATRIX.equals(tag)) {
            hideProgressDialog();

            // get distance matrix result
            DistanceMatrixResponse distanceMatrixResponse = (DistanceMatrixResponse) response;
            DistanceMatrixResult distanceResult = distanceMatrixController.getDistanceResult(distanceMatrixResponse);

            // check it
            if (distanceResult != null) {
                updateDistanceUI(distanceResult.getText());
            } else {
                updateDistanceUI(null);
                // show msg
                Utils.showShortToast(this, R.string.failed_calculating_distance);
            }
        }
    }

    private void loadDistanceMatrix() {
        // prepare params
        double originLat = locationController.getLatitude(trip.getPickupLocation());
        double originLng = locationController.getLongitude(trip.getPickupLocation());
        double destLat = locationController.getLatitude(trip.getActualDestinationLocation());
        double destLng = locationController.getLongitude(trip.getActualDestinationLocation());
        String distanceApiKey = getString(R.string.distance_matrix_api_key);
        String language = TwselaApp.getLanguage(this);

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.getDistanceMatrix(this, this, originLat,
                originLng, destLat, destLng, distanceApiKey, language);
        cancelWhenDestroyed(connectionHandler);
    }
}