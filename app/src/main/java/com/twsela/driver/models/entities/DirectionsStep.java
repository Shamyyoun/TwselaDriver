
package com.twsela.driver.models.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DirectionsStep {

    @SerializedName("end_location")
    @Expose
    private GoogleLocation endLocation;
    @SerializedName("html_instructions")
    @Expose
    private String htmlInstructions;
    @SerializedName("start_location")
    @Expose
    private GoogleLocation startLocation;
    @SerializedName("travel_mode")
    @Expose
    private String travelMode;
    @SerializedName("maneuver")
    @Expose
    private String maneuver;
    @SerializedName("polyline")
    @Expose
    private DirectionsPolyline polyline;

    public GoogleLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(GoogleLocation endLocation) {
        this.endLocation = endLocation;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public GoogleLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(GoogleLocation startLocation) {
        this.startLocation = startLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    public DirectionsPolyline getPolyline() {
        return polyline;
    }

    public void setPolyline(DirectionsPolyline polyline) {
        this.polyline = polyline;
    }
}
