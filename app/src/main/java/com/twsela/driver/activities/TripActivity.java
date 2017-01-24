package com.twsela.driver.activities;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.twsela.driver.ApiRequests;
import com.twsela.driver.Const;
import com.twsela.driver.R;
import com.twsela.driver.TwselaApp;
import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.controllers.ActiveUserController;
import com.twsela.driver.controllers.DirectionsController;
import com.twsela.driver.controllers.DistanceMatrixController;
import com.twsela.driver.controllers.LocationController;
import com.twsela.driver.controllers.TripController;
import com.twsela.driver.models.entities.MongoLocation;
import com.twsela.driver.models.entities.Trip;
import com.twsela.driver.models.enums.TripStatus;
import com.twsela.driver.models.responses.DirectionsResponse;
import com.twsela.driver.models.responses.DistanceMatrixResponse;
import com.twsela.driver.models.responses.ServerResponse;
import com.twsela.driver.models.responses.TripResponse;
import com.twsela.driver.utils.AppUtils;
import com.twsela.driver.utils.LocationUtils;
import com.twsela.driver.utils.MarkerUtils;
import com.twsela.driver.utils.Utils;

import java.util.List;

public class TripActivity extends ParentActivity implements OnMapReadyCallback, Runnable {
    private static final int MAP_PADDING = 300;
    private static final int MARKER_SIGN_DRIVER = 0;
    private static final int MARKER_SIGN_PICKUP = 1;
    private static final int MARKER_SIGN_DESTINATION = 2;

    private String id;
    private String tripStatus;
    private TripController tripController;
    private LocationController locationController;
    private ActiveUserController activeUserController;
    private DirectionsController directionsController;
    private DistanceMatrixController distanceMatrixController;

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private TextView tvTripStatus;
    private TextView tvPassengerName;
    private Button btnMainAction;
    private Button btnCancel;
    private View layoutNavigation;
    private TextView tvAddress;
    private ImageButton ibNavigate;

    private Trip trip;
    private Handler tripDetailsHandler;
    private Marker[] markers;
    private boolean firstTripDetailsReq = true;
    private Polyline pathPolyline;
    private Location actualDestinationLocation;

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
        directionsController = new DirectionsController();
        distanceMatrixController = new DistanceMatrixController();
        markers = new Marker[3];

        // init views
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        tvTripStatus = (TextView) findViewById(R.id.tv_trip_status);
        tvPassengerName = (TextView) findViewById(R.id.tv_passenger_name);
        btnMainAction = (Button) findViewById(R.id.btn_main_action);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        layoutNavigation = findViewById(R.id.layout_navigation);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        ibNavigate = (ImageButton) findViewById(R.id.ib_navigate);

        // add listeners
        btnMainAction.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        ibNavigate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_main_action) {
            onMainActionButton();
        } else if (v.getId() == R.id.btn_cancel) {
            preCancelTrip();
        } else if (v.getId() == R.id.ib_navigate) {
            onNavigate();
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

        // make the activity stay waked up
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // customize map
        this.map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
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
                    setNavigationAddress(); // set the suitable navigation

                    // check status to load the path
                    if (TripStatus.ACCEPTED.getValue().equals(tripStatus)) {
                        loadPickupDirections();
                    } else if (TripStatus.STARTED.getValue().equals(tripStatus)) {
                        loadDestinationDirections();
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

                // hide navigation layout and clear map path
                hideNavigationLayout();
                clearPath();
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

                // set navigation address and load the path
                setNavigationAddress();
                loadDestinationDirections();
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
        } else if (Const.TAG_DIRECTIONS.equals(tag)) {
            // get points list
            DirectionsResponse directionsResponse = (DirectionsResponse) response;
            List<LatLng> points = directionsController.getPoints(directionsResponse);

            // draw path if possible
            if (points != null) {
                drawPath(points);
            }
        } else if (Const.TAG_DISTANCE_MATRIX.equals(tag)) {
            // get distance
            DistanceMatrixResponse distanceMatrixResponse = (DistanceMatrixResponse) response;
            long distanceMeters = distanceMatrixController.getTotalDistance(distanceMatrixResponse);
            float distanceKm = distanceMeters / 1000f;

            // send end trip request
            showProgressDialog();
            endTrip(distanceKm);
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
        } else if (!Const.TAG_DIRECTIONS.equals(tag)) {
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
        actualDestinationLocation = LocationUtils.getLastKnownLocation(this);
        if (actualDestinationLocation == null) {
            // show msg and exit
            Utils.showShortToast(this, R.string.couldnt_get_your_current_location);
            return;
        }

        // check internet connection
        if (hasInternetConnection()) {
            showProgressDialog();
            // load distance first
            loadDistanceMatrix();
        } else {
            Utils.showShortToast(this, R.string.no_internet_connection);
        }
    }

    private void endTrip(float distance) {
        // prepare params
        String driverId = activeUserController.getUser().getId();
        String carId = activeUserController.getCarId();
        double lat = actualDestinationLocation.getLatitude();
        double lng = actualDestinationLocation.getLongitude();
        String address = LocationUtils.getAddress(this, lat, lng);

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.endTrip(this, this, driverId, carId,
                id, lat, lng, address, distance);
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

    private void setNavigationAddress() {

        // check trip status to prepare suitable address
        String address = null;
        if (TripStatus.ACCEPTED.getValue().equals(tripStatus)) {
            // pickup address
            if (trip.getPickupAddress() != null) {
                address = getString(R.string.pickup_c) + " " + trip.getPickupAddress();
            } else if (trip.getPickupLocation() != null) {
                MongoLocation location = trip.getPickupLocation();
                address = LocationUtils.getAddress(this, locationController.getLatitude(location),
                        locationController.getLongitude(location));
                address = getString(R.string.pickup_c) + " " + address;
            }
        } else if (TripStatus.STARTED.getValue().equals(tripStatus)) {
            // pickup address
            if (trip.getDestinationAddress() != null) {
                address = getString(R.string.destination_c) + " " + trip.getDestinationAddress();
            } else if (trip.getDestinationLocation() != null) {
                MongoLocation location = trip.getDestinationLocation();
                address = LocationUtils.getAddress(this, locationController.getLatitude(location),
                        locationController.getLongitude(location));
                address = getString(R.string.destination_c) + " " + address;
            }
        }

        // check address
        if (address != null) {
            // set address and show navigation layout
            tvAddress.setText(address);
            layoutNavigation.setVisibility(View.VISIBLE);
        } else {
            // hide navigation layout
            layoutNavigation.setVisibility(View.GONE);
        }
    }

    private void hideNavigationLayout() {
        layoutNavigation.setVisibility(View.GONE);
    }

    private void onNavigate() {
        // check status to prepare suitable location
        MongoLocation location = null;
        if (TripStatus.ACCEPTED.getValue().equals(tripStatus)) {
            location = trip.getPickupLocation();
        } else if (TripStatus.STARTED.getValue().equals(tripStatus)) {
            location = trip.getDestinationLocation();
        }

        // open navigation if possible
        if (location != null) {
            double lat = locationController.getLatitude(location);
            double lng = locationController.getLongitude(location);

            Utils.openGoogleMapsNavigation(this, lat, lng);
        }
    }

    private void loadPickupDirections() {
        // clear current path
        clearPath();

        // load the path if possible
        MongoLocation location = trip.getPickupLocation();
        if (location != null) {
            loadDirections(locationController.getLatitude(location), locationController.getLongitude(location));
        }
    }

    private void loadDestinationDirections() {
        // clear current path
        clearPath();

        // load the path if possible
        MongoLocation location = trip.getDestinationLocation();
        if (location != null) {
            loadDirections(locationController.getLatitude(location), locationController.getLongitude(location));
        }
    }

    private void loadDirections(double toLat, double toLng) {
        // check current location
        Location location = LocationUtils.getLastKnownLocation(this);
        if (location == null) {
            // exit
            return;
        }

        // prepare params
        String apiKey = getString(R.string.google_api_server_key);
        String language = TwselaApp.getLanguage(this);

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.getDirections(this, this, location.getLatitude(),
                location.getLongitude(), toLat, toLng, apiKey, language);
        cancelWhenDestroyed(connectionHandler);
    }

    private void drawPath(List<LatLng> points) {
        // check map
        if (map == null) {
            return;
        }

        // prepare polyline options obj
        PolylineOptions options = new PolylineOptions()
                .addAll(points)
                .width(12)
                .color(Color.DKGRAY)
                .geodesic(true);

        // draw the polyline
        pathPolyline = map.addPolyline(options);
    }

    private void clearPath() {
        // check map
        if (map != null && pathPolyline != null) {
            pathPolyline.remove();
            return;
        }
    }

    private void loadDistanceMatrix() {
        // prepare origins and destinations params
        String origins = "";
        String destinations = "";

        // check route points
        List<MongoLocation> points = trip.getRoutePoints();
        if (points != null && points.size() > 1) {
            // prepare using points list
            for (int i = 0; i < points.size() - 1; i++) {
                // get locations
                MongoLocation location1 = points.get(i);
                MongoLocation location2 = points.get(i + 1);

                // get values
                double lat1 = locationController.getLatitude(location1);
                double lng1 = locationController.getLongitude(location1);
                double lat2 = locationController.getLatitude(location2);
                double lng2 = locationController.getLongitude(location2);

                if (i != 0) {
                    origins += "|";
                    destinations += "|";
                }

                origins += lat1 + "," + lng1;
                destinations += lat2 + "," + lng2;
            }


        } else {
            // prepare using pickup and actual destination points
            double originLat = locationController.getLatitude(trip.getPickupLocation());
            double originLng = locationController.getLongitude(trip.getPickupLocation());
            double destLat = actualDestinationLocation.getLatitude();
            double destLng = actualDestinationLocation.getLongitude();

            origins = originLat + "," + originLng;
            destinations = destLat + "," + destLng;
        }


        String apiKey = getString(R.string.google_api_server_key);
        String language = TwselaApp.getLanguage(this);

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.getDistanceMatrix(this, this, origins, destinations, apiKey, language);
        cancelWhenDestroyed(connectionHandler);
    }
}