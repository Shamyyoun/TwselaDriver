package com.twsela.driver.models.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 16/01/2017.
 */
public class DistanceMatrixRow {
    public List<DistanceMatrixElement> elements = new ArrayList<>();

    public List<DistanceMatrixElement> getElements() {
        return elements;
    }

    public void setElements(List<DistanceMatrixElement> elements) {
        this.elements = elements;
    }
}
