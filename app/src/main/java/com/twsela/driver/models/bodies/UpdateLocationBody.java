
package com.twsela.driver.models.bodies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twsela.driver.models.entities.MongoLocation;

public class UpdateLocationBody {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("location")
    @Expose
    private MongoLocation location;
    @SerializedName("bearing")
    @Expose
    private String bearing;
    @SerializedName("trip_id")
    @Expose
    private String tripId;

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

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

}
