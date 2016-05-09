package com.wang.kyle.foodfinder.module;

/**
 * Created by Kyle on 5/4/2016.
 */
public class AutoComRequest {

    private String latitude;
    private String longitude;
    private String input;

    public String getLanguage() {
        return language;
    }


    public int getOffset() {
        return offset;
    }

    private int offset = 3;
    private String language = "en";

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
