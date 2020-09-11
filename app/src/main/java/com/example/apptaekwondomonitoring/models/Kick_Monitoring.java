package com.example.apptaekwondomonitoring.models;

import java.io.Serializable;

public class Kick_Monitoring implements Serializable {

    private Long _id;
    private Monitoring monitoring;
    private Double max_impact_x;
    private Double max_impact_y;
    private Double max_impact_z;
    private Double max_accel_kick_x;
    private Double max_accel_kick_y;
    private Double max_accel_kick_z;
    private Double max_velocity_kick_x;
    private Double max_velocity_kick_y;
    private Double max_velocity_kick_z;

    public Kick_Monitoring() {

    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Monitoring getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(Monitoring monitoring) {
        this.monitoring = monitoring;
    }

    public Double getMax_impact_x() {
        return max_impact_x;
    }

    public void setMax_impact_x(Double max_impact_x) {
        this.max_impact_x = max_impact_x;
    }

    public Double getMax_impact_y() {
        return max_impact_y;
    }

    public void setMax_impact_y(Double max_impact_y) {
        this.max_impact_y = max_impact_y;
    }

    public Double getMax_impact_z() {
        return max_impact_z;
    }

    public void setMax_impact_z(Double max_impact_z) {
        this.max_impact_z = max_impact_z;
    }

    public Double getMax_accel_kick_x() {
        return max_accel_kick_x;
    }

    public void setMax_accel_kick_x(Double max_accel_kick_x) {
        this.max_accel_kick_x = max_accel_kick_x;
    }

    public Double getMax_accel_kick_y() {
        return max_accel_kick_y;
    }

    public void setMax_accel_kick_y(Double max_accel_kick_y) {
        this.max_accel_kick_y = max_accel_kick_y;
    }

    public Double getMax_accel_kick_z() {
        return max_accel_kick_z;
    }

    public void setMax_accel_kick_z(Double max_accel_kick_z) {
        this.max_accel_kick_z = max_accel_kick_z;
    }

    public Double getMax_velocity_kick_x() {
        return max_velocity_kick_x;
    }

    public void setMax_velocity_kick_x(Double max_velocity_kick_x) {
        this.max_velocity_kick_x = max_velocity_kick_x;
    }

    public Double getMax_velocity_kick_y() {
        return max_velocity_kick_y;
    }

    public void setMax_velocity_kick_y(Double max_velocity_kick_y) {
        this.max_velocity_kick_y = max_velocity_kick_y;
    }

    public Double getMax_velocity_kick_z() {
        return max_velocity_kick_z;
    }

    public void setMax_velocity_kick_z(Double max_velocity_kick_z) {
        this.max_velocity_kick_z = max_velocity_kick_z;
    }
}
