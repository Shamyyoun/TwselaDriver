
package com.twsela.driver.models.payloads;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twsela.driver.models.entities.MongoLocation;

public class TripRequestPayload {

    @SerializedName("request_time")
    @Expose
    private String requestTime;
    @SerializedName("destination_address")
    @Expose
    private String destinationAddress;
    @SerializedName("pickup_location")
    @Expose
    private MongoLocation pickupLocation;
    @SerializedName("passenger_id")
    @Expose
    private String passengerId;
    @SerializedName("destination_location")
    @Expose
    private MongoLocation destinationLocation;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public MongoLocation getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(MongoLocation pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public MongoLocation getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(MongoLocation destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
