package com.wang.kyle.foodfinder.module;

import java.io.Serializable;

/**
 * Created by Kyle on 5/5/2016.
 */
public class PlaceDetailRequest implements Serializable {
    private static final long serialVersionUID = 7521954274234709604L;
    private String placeid;

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }
}
