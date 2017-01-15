package com.twsela.driver.models.enums;

/**
 * Created by Shamyyoun on 1/14/17.
 */

public enum NotificationKey {
    TRIP_REQUEST("Passenger Request Trip");

    private String value;

    NotificationKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
