package com.wang.kyle.foodfinder.module;

import java.io.Serializable;

/**
 * Created by Kyle on 5/4/2016.
 */
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 6316486389993069646L;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
