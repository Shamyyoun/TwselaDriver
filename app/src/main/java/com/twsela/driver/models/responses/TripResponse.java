
package com.twsela.driver.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twsela.driver.models.entities.Trip;

public class TripResponse extends ServerResponse {
    @SerializedName("content")
    @Expose
    private Trip content;

    public Trip getContent() {
        return content;
    }

    public void setContent(Trip content) {
        this.content = content;
    }
}
