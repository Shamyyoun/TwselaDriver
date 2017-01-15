package com.twsela.driver.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Driver {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("location")
    @Expose
    private MongoLocation location;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("bearing")
    @Expose
    private String bearing;
    @SerializedName("is_online")
    @Expose
    private boolean isOnline;
    @SerializedName("car_id")
    @Expose
    private List<String> carId = null;
    @SerializedName("gcm")
    @Expose
    private String gcm;
    @SerializedName("active_trip")
    @Expose
    private Trip activeTrip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MongoLocation getLocation() {
        return location;
    }

    public void setLocation(MongoLocation location) {
        this.location = location;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public List<String> getCarId() {
        return carId;
    }

    public void setCarId(List<String> carId) {
        this.carId = carId;
    }

    public String getGcm() {
        return gcm;
    }

    public void setGcm(String gcm) {
        this.gcm = gcm;
    }

    public Trip getActiveTrip() {
        return activeTrip;
    }

    public void setActiveTrip(Trip activeTrip) {
        this.activeTrip = activeTrip;
    }

}