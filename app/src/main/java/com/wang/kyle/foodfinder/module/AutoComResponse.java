package com.wang.kyle.foodfinder.module;

import java.util.List;

/**
 * Created by Kyle on 5/4/2016.
 */
public class AutoComResponse extends BaseResponse {
    private List<Prediction> predictions;

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }
}
