package com.wang.kyle.foodfinder.module;

import java.io.Serializable;

/**
 * Created by Kyle on 5/4/2016.
 */
public class PlaceSearchRequest implements Serializable{
    private static final long serialVersionUID = -1984043990786474816L;
    private double latitude;
    private double longitude;
    private double radius = 1500;
    private String keyword;
    private String language = "en";
    private String name;
    private String type;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
