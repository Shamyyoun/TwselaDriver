
package com.twsela.driver.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twsela.driver.models.entities.DirectionsRoute;

import java.util.List;

public class DirectionsResponse {

    @SerializedName("routes")
    @Expose
    private List<DirectionsRoute> routes = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<DirectionsRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<DirectionsRoute> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
