package com.twsela.driver.models.entities;

/**
 * Created by Shamyyoun on 16/01/2017.
 */
public class DistanceMatrixResult {
    public String text;
    public int value; // in meters

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
