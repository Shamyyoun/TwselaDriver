package com.twsela.driver.controllers;

import com.twsela.driver.models.entities.DistanceMatrixRow;
import com.twsela.driver.models.responses.DistanceMatrixResponse;

import java.util.List;

/**
 * Created by Shamyyoun on 1/16/17.
 */

public class DistanceMatrixController {

    public long getTotalDistance(DistanceMatrixResponse response) {
        try {
            long distance = 0;

            List<DistanceMatrixRow> rows = response.getRows();
            for (int i = 0; i < rows.size(); i++) {
                long tempDistance = rows.get(i).getElements().get(i).getDistance().getValue();
                distance += tempDistance;
            }

            return distance;
        } catch (Exception e) {
            return 0;
        }
    }
}
