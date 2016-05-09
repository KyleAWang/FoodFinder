package com.wang.kyle.foodfinder.module;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Kyle on 5/4/2016.
 */
public class Prediction implements Serializable{

    private static final long serialVersionUID = 2666145306966448589L;
    private String description;
    private String id;
    private List<Map<String, Integer>> matched_substrings;
    private String place_id;
    private String reference;
    private List<Map<String, Object>> terms;
    private List<String> types;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Map<String, Integer>> getMatched_substrings() {
        return matched_substrings;
    }

    public void setMatched_substrings(List<Map<String, Integer>> matched_substrings) {
        this.matched_substrings = matched_substrings;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<Map<String, Object>> getTerms() {
        return terms;
    }

    public void setTerms(List<Map<String, Object>> terms) {
        this.terms = terms;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
