
package com.twsela.driver.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MongoLocation {

    @SerializedName("coordinates")
    @Expose
    private List<Double> coordinates = null;
    @SerializedName("type")
    @Expose
    private String type = "Point";

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        try {
            return "Latitude: " + coordinates.get(0) + " - Longitude: " + coordinates.get(1);
        } catch (Exception e) {
            return super.toString();
        }
    }
}
