package com.wang.kyle.foodfinder.module;

/**
 * Created by Kyle on 5/5/2016.
 */
public class Review {
    private Aspect[] aspects;
    private String author_name;
    private String author_url;
    private String language;
    private float rating;
    private String text;
    private long time;

    public Aspect[] getAspects() {
        return aspects;
    }

    public void setAspects(Aspect[] aspects) {
        this.aspects = aspects;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public void setAuthor_url(String author_url) {
        this.author_url = author_url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private class Aspect{
        private float rating;
        private String type;

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
