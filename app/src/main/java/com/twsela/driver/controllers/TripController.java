package com.twsela.driver.controllers;


import com.twsela.driver.models.entities.Trip;

/**
 * Created by Shamyyoun on 1/14/17.
 */

public class TripController {

    public String getPassengerUsername(Trip trip) {
        try {
            return trip.getPassenger().getUserName();
        } catch (Exception e) {
            return null;
        }
    }
}
