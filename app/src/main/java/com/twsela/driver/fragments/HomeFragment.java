package com.twsela.driver.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.twsela.driver.ApiRequests;
import com.twsela.driver.Const;
import com.twsela.driver.R;
import com.twsela.driver.connection.ConnectionHandler;
import com.twsela.driver.controllers.ActiveUserController;
import com.twsela.driver.models.entities.Driver;
import com.twsela.driver.models.responses.ServerResponse;
import com.twsela.driver.services.UpdateLocationService;
import com.twsela.driver.utils.AppUtils;
import com.twsela.driver.utils.DialogUtils;
import com.twsela.driver.utils.LocationUtils;
import com.twsela.driver.utils.PermissionUtil;
import com.twsela.driver.utils.Utils;

/**
 * Created by Shamyyoun on 5/28/16.
 */
public class HomeFragment extends ParentFragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private ActiveUserController activeUserController;
    private LocationManager locationManager;

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private Button btnChangeStatus;

    private AlertDialog gpsDialog;
    private boolean mapLoaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create main objects
        activeUserController = new ActiveUserController(activity);
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // init views
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        btnChangeStatus = (Button) findViewById(R.id.btn_change_status);

        // add listeners
        btnChangeStatus.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // update status ui
        updateStatusUI();

        // check location permission
        if (!isLocationPermGranted()) {
            // request permission from user
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Const.PERM_REQ_LOCATION);
            return;
        }

        // check gps
        if (!LocationUtils.isGpsEnabled(activity)) {
            showEnableGPSDialog();
            return;
        }

        // check internet
        if (!hasInternetConnection()) {
            // show msg
            Utils.showShortToast(activity, R.string.enable_internet_connection_to_use_app);
        }

        // then get the map async if all settings are ok
        mapFragment.getMapAsync(this);
    }

    private void updateStatusUI() {
        Driver user = activeUserController.getUser();
        if (user.isOnline()) {
            btnChangeStatus.setText(R.string.go_offline);
            btnChangeStatus.setBackgroundResource(R.drawable.gray_rect_button_bg);
        } else {
            btnChangeStatus.setText(R.string.go_online);
            btnChangeStatus.setBackgroundResource(R.drawable.green_rect_button_bg);
        }
    }

    private boolean isLocationPermGranted() {
        return activity.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        // customize the map
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        // get suitable location
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        // add from location and set its address if possible
        if (location != null) {
            // zoom to this location
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, Const.INITIAL_ZOOM_LEVEL);
            map.animateCamera(cameraUpdate);
        }

        // update the flag
        mapLoaded = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Const.PERM_REQ_LOCATION) {
            // check grant result
            if (PermissionUtil.isAllGranted(grantResults)) {
                // check gps
                if (!LocationUtils.isGpsEnabled(activity)) {
                    showEnableGPSDialog();
                } else {
                    // all settings are ok
                    // get the map async
                    mapFragment.getMapAsync(this);
                }
            } else {
                // show msg and finish
                Utils.showLongToast(activity, R.string.location_perm_refuse_msg);
                activity.finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showEnableGPSDialog() {
        // show confirm dialog
        gpsDialog = DialogUtils.showConfirmDialog(activity, R.string.enable_gps_to_use_app, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // open location settings activity
                LocationUtils.openLocationSettingsActivityForResult(fragment, Const.REQ_ENABLE_GPS);
            }
        }, R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // finish the activity
                dialogInterface.dismiss();
                activity.finish();
            }
        }, R.string.close);
        gpsDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_status:
                preChangeStatus();
                break;

            default:
                super.onClick(v);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.REQ_ENABLE_GPS) {
            // check gps
            if (LocationUtils.isGpsEnabled(activity)) {
                // all settings are ok
                // get the map async
                mapFragment.getMapAsync(this);
            } else {
                // show msg and finish
                Utils.showShortToast(activity, R.string.gps_refuse_msg);
                activity.finish();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void preChangeStatus() {
        // check internet connection
        if (hasInternetConnection()) {
            showProgressDialog();
            changeStatus();
        } else {
            Utils.showShortToast(activity, R.string.no_internet_connection);
        }
    }

    private void changeStatus() {
        // get user
        Driver user = activeUserController.getUser();

        // send the request
        ConnectionHandler connectionHandler = ApiRequests.updateStatus(activity, this, user.getId(), !user.isOnline());
        cancelWhenDestroyed(connectionHandler);
    }

    @Override
    public void onSuccess(Object response, int statusCode, String tag) {
        hideProgressDialog();

        // check response
        ServerResponse serverResponse = (ServerResponse) response;
        if (serverResponse.isSuccess()) {
            // update the status
            Driver user = activeUserController.getUser();
            user.setOnline(!user.isOnline());
            activeUserController.save();
            updateStatusUI();

            // check the new status
            if (user.isOnline()) {
                // start update location service
                activity.startService(new Intent(activity, UpdateLocationService.class));
            } else {
                // stop update location service
                activity.stopService(new Intent(activity, UpdateLocationService.class));
            }
        } else {
            // show msg
            String msg = AppUtils.getResponseMsg(activity, serverResponse, R.string.failed_changing_your_status);
            Utils.showShortToast(activity, msg);
        }
    }
}
