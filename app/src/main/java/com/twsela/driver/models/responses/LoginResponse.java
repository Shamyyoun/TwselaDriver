
package com.twsela.driver.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twsela.driver.models.entities.User;

public class LoginResponse extends ServerResponse {
    @SerializedName("content")
    @Expose
    private User content;

    /**
     * @return The content
     */
    public User getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    public void setContent(User content) {
        this.content = content;
    }
}
