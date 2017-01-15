package com.twsela.driver.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;

import java.util.List;
import java.util.Locale;

/**
 * Created by Shamyyoun on 11/12/16.
 */

public class LocationUtils {
    /**
     * method, used to check if gps  is enabled on the device or not
     *
     * @param context
     * @return boolean variable
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return false;
        } else {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
    }

    /**
     * method, used to open location settings activity
     *
     * @param context
     */
    public static void openLocationSettingsActivity(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * method, used to open location settings for result activity
     *
     * @param activity
     */
    public static void openLocationSettingsActivityForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * method, used to open location settings for result activity
     *
     * @param fragment
     */
    public static void openLocationSettingsActivityForResult(Fragment fragment, int requestCode) {
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * method, used to get location from GPS
     *
     * @param context
     * @return
     */
    public static Location getLastKnownLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location;
    }

    public static String getAddress(Context context, double latitude, double longitude) {
        try {
            // get the address
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            // prepare the address
            Address address = addresses.get(0);
            String addressStr;
            addressStr = address.getAddressLine(0);
            if (address.getAddressLine(1) != null) {
                addressStr += ", " + address.getAddressLine(1);
            }

            return addressStr;
        } catch (Exception e) {
            return null;
        }
    }
}
