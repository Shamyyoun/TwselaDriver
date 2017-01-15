package com.twsela.driver.controllers;

import android.content.Context;

import com.twsela.driver.Const;
import com.twsela.driver.models.entities.Driver;
import com.twsela.driver.models.entities.Trip;
import com.twsela.driver.models.enums.TripStatus;
import com.twsela.driver.utils.SharedPrefs;
import com.twsela.driver.utils.Utils;

/**
 * Created by Shamyyoun on 8/27/16.
 */
public class ActiveUserController {
    private static Driver user;
    private Context context;
    private SharedPrefs<Driver> userPrefs;

    public ActiveUserController(Context context) {
        this.context = context;
        userPrefs = new SharedPrefs(context, Driver.class);
    }

    public void save() {
        userPrefs.save(user, Const.SP_USER);
    }

    public Driver getUser() {
        if (user == null) {
            user = userPrefs.load(Const.SP_USER);
        }

        return user;
    }

    public void setUser(Driver user) {
        this.user = user;
    }

    public boolean hasLoggedInUser() {
        return getUser() != null;
    }

    public void logout() {
        userPrefs.remove(Const.SP_USER);
        setUser(null);
    }

    public void updateActiveTripStatus(String id, TripStatus status) {
        // create new one if required
        Trip activeTrip = getUser().getActiveTrip();
        if (activeTrip == null) {
            activeTrip = new Trip();
        }

        // set values
        activeTrip.setId(id);
        activeTrip.setStatus(status.getValue());

        user.setActiveTrip(activeTrip);
        save();
    }

    public void removeActiveTrip() {
        getUser().setActiveTrip(null);
        save();
    }

    public Trip getActiveTrip() {
        if (getUser() != null) {
            return user.getActiveTrip();
        } else {
            return null;
        }
    }

    public String getCarId() {
        if (getUser() == null) {
            return null;
        }

        if (Utils.isNullOrEmpty(user.getCarId())) {
            return null;
        }

        return user.getCarId().get(0);
    }
}
