
package com.twsela.driver.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    @SerializedName("actual_destination_address")
    @Expose
    private String actualDestinationAddress;
    @SerializedName("actual_destination_location")
    @Expose
    private MongoLocation actualDestinationLocation;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("driver")
    @Expose
    private Driver driver;
    @SerializedName("passenger")
    @Expose
    private Passenger passenger;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("cost")
    @Expose
    private float cost;
    @SerializedName("route_points")
    @Expose
    private List<MongoLocation> routePoints = null;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getActualDestinationAddress() {
        return actualDestinationAddress;
    }

    public void setActualDestinationAddress(String actualDestinationAddress) {
        this.actualDestinationAddress = actualDestinationAddress;
    }

    public MongoLocation getActualDestinationLocation() {
        return actualDestinationLocation;
    }

    public void setActualDestinationLocation(MongoLocation actualDestinationLocation) {
        this.actualDestinationLocation = actualDestinationLocation;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public List<MongoLocation> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<MongoLocation> routePoints) {
        this.routePoints = routePoints;
    }
}
