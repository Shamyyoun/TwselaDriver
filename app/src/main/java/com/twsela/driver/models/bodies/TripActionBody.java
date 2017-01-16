
package com.twsela.driver.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twsela.driver.models.entities.MongoLocation;

public class TripActionBody {

    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("car_id")
    @Expose
    private String carId;
    @SerializedName("trip_id")
    @Expose
    private String tripId;
    @SerializedName("actual_destination_location")
    @Expose
    private MongoLocation actualDestinationLocation;
    @SerializedName("actual_destination_address")
    @Expose
    private String actualDestinationAddress;
    @SerializedName("total_distance_km")
    @Expose
    private float totalDistanceKm;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public MongoLocation getActualDestinationLocation() {
        return actualDestinationLocation;
    }

    public void setActualDestinationLocation(MongoLocation actualDestinationLocation) {
        this.actualDestinationLocation = actualDestinationLocation;
    }

    public String getActualDestinationAddress() {
        return actualDestinationAddress;
    }

    public void setActualDestinationAddress(String actualDestinationAddress) {
        this.actualDestinationAddress = actualDestinationAddress;
    }

    public float getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(float totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }
}
