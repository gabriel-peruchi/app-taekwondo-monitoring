package com.example.apptaekwondomonitoring.models;

import java.io.Serializable;

public class Athlete implements Serializable {

    private Long _id;
    private String name;
    private Double weight;
    private Double height;
    private String category;

    public Athlete() {

    }

    public Athlete(Long _id, String name, Double weight, Double height, String category) {
        this._id = _id;
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.category = category;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
