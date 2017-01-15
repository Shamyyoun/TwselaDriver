
package com.twsela.driver.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twsela.driver.models.entities.Driver;

public class LoginResponse extends ServerResponse {
    @SerializedName("content")
    @Expose
    private Driver content;

    /**
     * @return The content
     */
    public Driver getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    public void setContent(Driver content) {
        this.content = content;
    }
}
