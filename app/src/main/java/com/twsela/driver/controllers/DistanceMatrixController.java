package com.twsela.driver.controllers;

import com.twsela.driver.models.entities.DistanceMatrixResult;
import com.twsela.driver.models.responses.DistanceMatrixResponse;

/**
 * Created by Shamyyoun on 1/16/17.
 */

public class DistanceMatrixController {

    public DistanceMatrixResult getDistanceResult(DistanceMatrixResponse response) {
        try {
            return response.getRows().get(0).getElements().get(0).getDistance();
        } catch (Exception e) {
            return null;
        }
    }
}
