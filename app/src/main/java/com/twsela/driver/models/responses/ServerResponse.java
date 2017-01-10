
package com.twsela.driver.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ServerResponse {

    @SerializedName("status")
    @Expose
    private boolean success;
    @SerializedName("validation")
    @Expose
    private List<String> validation = new ArrayList<String>();

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getValidation() {
        return validation;
    }

    public void setValidation(List<String> validation) {
        this.validation = validation;
    }

}
