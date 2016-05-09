package com.wang.kyle.foodfinder.module;

import java.util.List;

/**
 * Created by Kyle on 5/4/2016.
 */
public class PlaceSearchResponse extends  BaseResponse{
    private  List<Place> results;

    public List<Place> getResults() {
        return results;
    }
}
