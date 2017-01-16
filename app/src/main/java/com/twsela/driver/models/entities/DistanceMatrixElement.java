package com.twsela.driver.models.entities;

/**
 * Created by Basim Alamuddin on 27/03/2016.
 */
public class DistanceMatrixElement {
    public DistanceMatrixResult distance;
    public DistanceMatrixResult duration;
    public String status;

    public DistanceMatrixResult getDistance() {
        return distance;
    }

    public void setDistance(DistanceMatrixResult distance) {
        this.distance = distance;
    }

    public DistanceMatrixResult getDuration() {
        return duration;
    }

    public void setDuration(DistanceMatrixResult duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOk() {
        return status.equalsIgnoreCase("OK");
    }

}
