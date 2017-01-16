package com.twsela.driver.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twsela.driver.ApiRequests;
import com.twsela.driver.Const;
import com.twsela.driver.R;
import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.controllers.ActiveUserController;
import com.twsela.driver.controllers.LocationController;
import com.twsela.driver.controllers.TripController;
import com.twsela.driver.models.entities.Trip;
import com.twsela.driver.models.enums.TripStatus;
import com.twsela.driver.models.responses.ServerResponse;
import com.twsela.driver.models.responses.TripResponse;
import com.twsela.driver.utils.AppUtils;
import com.twsela.driver.utils.LocationUtils;
import com.twsela.driver.utils.MarkerUtils;
import com.twsela.driver.utils.Utils;

public class TripActivity extends ParentActivity implements OnMapReadyCallback, Runnable {
    private static final int MAP_PADDING = 200;
    private static final int MARKER_SIGN_DRIVER = 0;
    private static final int MARKER_SIGN_PICKUP = 1;
    private static final int MARKER_SIGN_DESTINATION = 2;

    private String id;
    private String tripStatus;
    private TripController tripController;
    private LocationController locationController;
    private ActiveUserController activeUserController;

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private TextView tvTripStatus;
    private TextView tvPassengerName;
    private Button btnMainAction;
    private Button btnCancel;

    private Trip trip;
    private Handler tripDetailsHandler;
    private Marker[] markers;
    private boolean firstTripDetailsReq = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        // obtain main objects
        id = getIntent().getStringExtra(Const.KEY_ID);
        tripStatus = getIntent().getStringExtra(Const.KEY_STATUS);
        tripController = new TripController();
        locationController = new LocationController();
        activeUserController = new ActiveUserController(this);
        markers = new Marker[3];

        // init views
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        tvTripStatus = (TextView) findViewById(R.id.tv_trip_status);
        tvPassengerName = (TextView) findViewById(R.id.tv_passenger_name);
        btnMainAction = (Button) findViewById(R.id.btn_main_action);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // add listeners
        btnMainAction.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_main_action) {
            onMainActionButton();
        } else if (v.getId() == R.id.btn_cancel) {
            preCancelTrip();
        } else {
            super.onClick(v);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // get the map
        mapFragment.getMapAsync(this);

        // update trip status
        updateTripStatusUI();

        // load trip details
        preLoadTripDetails();

        // create the trip details handler
        tripDetailsHandler = new Handler();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // customize map
        this.map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
    }

    private void updateTripStatusUI() {
        if (TripStatus.ACCEPTED.getValue().equals(tripStatus)) {
            tvTripStatus.setText(R.string.in_your_way_to_pickup);
        } else if (TripStatus.DRIVER_ARRIVED.getValue().equals(tripStatus)) {
            tvTripStatus.setText(R.string.you_have_arrived_to_pickup);
        } else if (TripStatus.STARTED.getValue().equals(tripStatus)) {
            tvTripStatus.setText(R.string.in_your_way_to_destination);
        } else {
            tvTripStatus.setText("----------------");
        }
    }

    private void updateUI() {
        // set passenger name if possible
        String passengerUsername = tripController.getPassengerUsername(trip);
        if (passengerUsername != null) {
            tvPassengerName.setText(passengerUsername);
        } else {
            tvPassengerName.setText("----------------");
        }

        // check status to customize buttons
        if (TripStatus.DRIVER_ARRIVED.getValue().equals(tripStatus)) {
            btnCancel.setVisibility(View.GONE);
            btnMainAction.setBackgroundResource(R.drawable.green_rect_button_bg);
            btnMainAction.setText(R.string.start_trip);
        } else if (TripStatus.STARTED.getValue().equals(tripStatus)) {
            btnCancel.setVisibility(View.GONE);
            btnMainAction.setBackgroundResource(R.drawable.gray_rect_button_bg);
            btnMainAction.setText(R.string.end_trip);
        }
    }

    private void preLoadTripDetails() {
        if (hasInternetConnection()) {
            showProgressDialog();
            loadTripDetails();
        } else {
            Utils.showShortToast(activity, R.string.no_internet_connection);
        }
    }

    private void loadTripDetails() {
        ConnectionHandler request = ApiRequests.getTripDetails(activity, this, id);
        cancelWhenDestroyed(request);
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
                trip = tripResponse.getContent();
                updateUI();

                // update driver marker
                updateDriverMarker();

                // check if first request
                if (firstTripDetailsReq) {
                    // check status to show destination marker
                    if (TripStatus.STARTED.getValue().equals(tripStatus)) {
                        showDestinationMarker();
                    } else {
                        // update pickup and zoom for the first time only
                        updatePickupMarker();
                        zoomToMarkers();
                    }
                }
            }

            // continue the trip details task
            continueTripDetailsTask();
            firstTripDetailsReq = false;
        } else if (Const.ROUTE_CANCEL_TRIP.equals(tag)) {
            // cancel trip request
            // check response
            ServerResponse serverResponse = (ServerResponse) response;
            if (serverResponse.isSuccess()) {
                // remove active trip
                activeUserController.removeActiveTrip();

                // show msg and finish
                Utils.showShortToast(this, R.string.trip_cancelled_successfully);
                finish();
            } else {
                // show msg
                String msg = AppUtils.getResponseMsg(this, serverResponse, R.string.failed_cancelling_trip);
                Utils.showShortToast(this, msg);
            }
        } else if (Const.ROUTE_ARRIVED.equals(tag)) {
            // arrived trip request
            // check response
            ServerResponse serverResponse = (ServerResponse) response;
            if (serverResponse.isSuccess()) {
                // show msg
                Utils.showShortToast(this, R.string.arrived_successfully);

                // update active trip
                activeUserController.updateActiveTripStatus(id, TripStatus.DRIVER_ARRIVED);

                // update status and update ui
                tripStatus = TripStatus.DRIVER_ARRIVED.getValue();
                updateTripStatusUI();
                updateUI();
            } else {
                // show msg
                String msg = AppUtils.getResponseMsg(this, serverResponse, R.string.failed_arriving);
                Utils.showShortToast(this, msg);
            }
        } else if (Const.ROUTE_START_TRIP.equals(tag)) {
            // start trip request
            // check response
            ServerResponse serverResponse = (ServerResponse) response;
            if (serverResponse.isSuccess()) {
                // show msg
                Utils.showShortToast(this, R.string.trip_started_successfully);

                // update active trip
                activeUserController.updateActiveTripStatus(id, TripStatus.STARTED);

                // update status and update ui
                tripStatus = TripStatus.STARTED.getValue();
                updateTripStatusUI();
                updateUI();

                // show destination marker
                showDestinationMarker();
            } else {
                // show msg
                String msg = AppUtils.getResponseMsg(this, serverResponse, R.string.failed_starting_trip);
                Utils.showShortToast(this, msg);
            }
        } else if (Const.ROUTE_END_TRIP.equals(tag)) {
            // end trip request
            // check response
            ServerResponse serverResponse = (ServerResponse) response;
            if (serverResponse.isSuccess()) {
                // remove active trip
                activeUserController.removeActiveTrip();

                // open trip details activity
                Intent intent = new Intent(this, TripDetailsActivity.class);
                intent.putExtra(Const.KEY_ID, id);
                startActivity(intent);
                finish();
            } else {
                // show msg
                String msg = AppUtils.getResponseMsg(this, serverResponse, R.string.failed_ending_trip);
                Utils.showShortToast(this, msg);
            }
        }
    }

    @Override
    public void onFail(Exception ex, int statusCode, String tag) {
        hideProgressDialog();

        // check tag
        if (Const.ROUTE_GET_DETAILS_BY_ID.equals(tag)) {
            // check if first request
            if (firstTripDetailsReq) {
                super.onFail(ex, statusCode, tag);
                firstTripDetailsReq = false;
            }
        } else {
            super.onFail(ex, statusCode, tag);
        }
    }

    private void updateDriverMarker() {
        updateMarker(MARKER_SIGN_DRIVER);
    }

    private void updatePickupMarker() {
        updateMarker(MARKER_SIGN_PICKUP);
    }

    private void updateMarker(int markerSign) {
        try {
            // get marker
            Marker marker = markers[markerSign];

            // prepare objects and values
            LatLng latLng;
            float bearing = 0;
            BitmapDescriptor icon;
            if (markerSign == MARKER_SIGN_DRIVER) {
                latLng = locationController.createLatLng(trip.getDriver().getLocation());
                bearing = Utils.convertToFloat(trip.getDriver().getBearing());
                icon = BitmapDescriptorFactory.fromResource(R.drawable.map_car_icon);
            } else if (markerSign == MARKER_SIGN_PICKUP) {
                latLng = locationController.createLatLng(trip.getPickupLocation());
                icon = BitmapDescriptorFactory.fromResource(R.drawable.green_marker);
            } else {
                latLng = locationController.createLatLng(trip.getDestinationLocation());
                icon = BitmapDescriptorFactory.fromResource(R.drawable.red_marker);
            }

            // check marker
            if (marker == null) {
                // create the marker and add it to the map
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .rotation(bearing)
                        .icon(icon);
                marker = map.addMarker(options);
            } else {
                // animate to the new position
                MarkerUtils.animateMarkerToICSWithBearing(marker, latLng, bearing,
                        new MarkerUtils.LatLngInterpolator.LinearFixed());
            }

            // save the marker in the array
            markers[markerSign] = marker;
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    private void zoomToMarkers() {
        try {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                if (marker != null) {
                    builder.include(marker.getPosition());
                }
            }
            LatLngBounds bounds = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, MAP_PADDING);
            map.animateCamera(cameraUpdate);
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    private void showDestinationMarker() {
        if (trip.getDestinationLocation() != null) {
            // add destination marker
            updateMarker(MARKER_SIGN_DESTINATION);
        }

        // remove pickup marker
        Marker marker = markers[MARKER_SIGN_PICKUP];
        if (marker != null) marker.remove();
        markers[MARKER_SIGN_PICKUP] = null;

        // zoom to markers
        zoomToMarkers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // stop trip details handler
        tripDetailsHandler.removeCallbacks(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void run() {
        // send load details request
        loadTripDetails();
    }

    private void continueTripDetailsTask() {
        // continue drivers task after static time
        tripDetailsHandler.removeCallbacks(this);
        tripDetailsHandler.postDelayed(this, Const.MAP_REFRESH_RATE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop trip details handler
        tripDetailsHandler.removeCallbacks(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if can continue trip details handler
        if (!firstTripDetailsReq) {
            tripDetailsHandler.post(this);
        }
    }

    private void onMainActionButton() {
        // check status to do suitable action
        if (TripStatus.ACCEPTED.getValue().equals(tripStatus)) {
            preArrived();
        } else if (TripStatus.DRIVER_ARRIVED.getValue().equals(tripStatus)) {
            preStartTrip();
        } else if (TripStatus.STARTED.getValue().equals(tripStatus)) {
            preEndTrip();
        }
    }

    private void preArrived() {
        // check internet connection
        if (hasInternetConnection()) {
            showProgressDialog();
            arriveTrip();
        } else {
            Utils.showShortToast(this, R.string.no_internet_connection);
        }
    }

    private void arriveTrip() {
        // prepare params
        String driverId = activeUserController.getUser().getId();
        String carId = activeUserController.getCarId();

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.arriveTrip(this, this, driverId, carId, id);
        cancelWhenDestroyed(connectionHandler);
    }

    private void preStartTrip() {
        // check internet connection
        if (hasInternetConnection()) {
            showProgressDialog();
            startTrip();
        } else {
            Utils.showShortToast(this, R.string.no_internet_connection);
        }
    }

    private void startTrip() {
        // prepare params
        String driverId = activeUserController.getUser().getId();
        String carId = activeUserController.getCarId();

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.startTrip(this, this, driverId, carId, id);
        cancelWhenDestroyed(connectionHandler);
    }

    private void preEndTrip() {
        // prepare actual destination
        Location location = LocationUtils.getLastKnownLocation(this);
        if (location == null) {
            // show msg and exit
            Utils.showShortToast(this, R.string.couldnt_get_your_current_location);
            return;
        }

        // get address
        String address = LocationUtils.getAddress(this, location.getLatitude(), location.getLongitude());

        // check internet connection
        if (hasInternetConnection()) {
            showProgressDialog();
            endTrip(location.getLatitude(), location.getLongitude(), address);
        } else {
            Utils.showShortToast(this, R.string.no_internet_connection);
        }
    }

    private void endTrip(double lat, double lng, String address) {
        // prepare params
        String driverId = activeUserController.getUser().getId();
        String carId = activeUserController.getCarId();

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.endTrip(this, this, driverId, carId,
                id, lat, lng, address);
        cancelWhenDestroyed(connectionHandler);
    }

    private void preCancelTrip() {
        // check internet connection
        if (hasInternetConnection()) {
            showProgressDialog();
            cancelTrip();
        } else {
            Utils.showShortToast(this, R.string.no_internet_connection);
        }
    }

    private void cancelTrip() {
        // prepare params
        String driverId = activeUserController.getUser().getId();
        String carId = activeUserController.getCarId();

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.cancelTrip(this, this, driverId, carId, id);
        cancelWhenDestroyed(connectionHandler);
    }
}