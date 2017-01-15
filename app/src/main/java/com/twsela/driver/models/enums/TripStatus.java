package com.twsela.driver.models.enums;

/**
 * Created by Shamyyoun on 1/14/17.
 */

public enum TripStatus {
    REQUEST_TRIP("Passenger Request Trip"), ACCEPTED("Driver Accepted Trip"),
    DRIVER_ARRIVED("Driver Arrived"), STARTED("Driver Start Trip"),
    ENDED("Driver End Trip");

    private String value;

    TripStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
