
package com.twsela.driver.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trip {

    @SerializedName("passenger_id")
    @Expose
    private String passengerId;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("pickup_location")
    @Expose
    private MongoLocation pickupLocation;
    @SerializedName("destination_address")
    @Expose
    private String destinationAddress;
    @SerializedName("destination_location")
    @Expose
    private MongoLocation destinationLocation;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("request_time")
    @Expose
    private String requestTime;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("driver")
    @Expose
    private Driver driver;
    @SerializedName("passenger")
    @Expose
    private Passenger passenger;

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public MongoLocation getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(MongoLocation pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public MongoLocation getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(MongoLocation destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
}
