package com.example.apptaekwondomonitoring.models;

import java.io.Serializable;

public class Athlete implements Serializable {

    private Long _id;
    private String name;
    private Long birth_date;
    private Double weight;
    private Double height;
    private String category;
    private Double training_time;

    public Athlete() {

    }

    public Athlete(Long _id, String name, Long birth_date, Double weight, Double height, String category, Double training_time) {
        this._id = _id;
        this.name = name;
        this.birth_date = birth_date;
        this.weight = weight;
        this.height = height;
        this.category = category;
        this.training_time = training_time;
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

    public Long getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Long birth_date) {
        this.birth_date = birth_date;
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

    public Double getTraining_time() {
        return training_time;
    }

    public void setTraining_time(Double training_time) {
        this.training_time = training_time;
    }
}
