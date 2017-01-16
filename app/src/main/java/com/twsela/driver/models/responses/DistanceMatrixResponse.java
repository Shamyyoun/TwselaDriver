package com.twsela.driver.models.responses;

import com.google.gson.annotations.SerializedName;
import com.twsela.driver.models.entities.DistanceMatrixRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 16/01/2017.
 */
public class DistanceMatrixResponse {
    @SerializedName("origin_addresses")
    public List<String> originAddresses = new ArrayList<>();
    @SerializedName("destination_addresses")
    public List<String> destinationAddresses = new ArrayList<>();
    public List<DistanceMatrixRow> rows = new ArrayList<>();
    public String status;

    public List<String> getOriginAddresses() {
        return originAddresses;
    }

    public void setOriginAddresses(List<String> originAddresses) {
        this.originAddresses = originAddresses;
    }

    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(List<String> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    public List<DistanceMatrixRow> getRows() {
        return rows;
    }

    public void setRows(List<DistanceMatrixRow> rows) {
        this.rows = rows;
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

