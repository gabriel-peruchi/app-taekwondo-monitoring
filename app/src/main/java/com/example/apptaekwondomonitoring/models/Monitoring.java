package com.example.apptaekwondomonitoring.models;

import java.io.Serializable;
import java.util.Date;

public class Monitoring implements Serializable {

    private Long _id;
    private Date date;
    private Athlete athlete;

    public Monitoring() {

    }

    public Monitoring(Long _id, Date date, Athlete athlete) {
        this._id = _id;
        this.date = date;
        this.athlete = athlete;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }
}
