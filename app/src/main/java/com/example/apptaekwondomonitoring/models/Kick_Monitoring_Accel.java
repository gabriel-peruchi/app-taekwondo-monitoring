package com.example.apptaekwondomonitoring.models;

public class Kick_Monitoring_Accel {

    private Long _id;
    private Kick_Monitoring kick_monitoring;
    private Double seconds;
    private Double accel_x;
    private Double accel_y;
    private Double accel_z;

    public Kick_Monitoring_Accel() {

    }

    public Kick_Monitoring_Accel(Long _id, Double seconds, Double accel_x, Double accel_y, Double accel_z) {
        this._id = _id;
        this.seconds = seconds;
        this.accel_x = accel_x;
        this.accel_y = accel_y;
        this.accel_z = accel_z;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Kick_Monitoring getKick_monitoring() {
        return kick_monitoring;
    }

    public void setKick_monitoring(Kick_Monitoring kick_monitoring) {
        this.kick_monitoring = kick_monitoring;
    }

    public Double getSeconds() {
        return seconds;
    }

    public void setSeconds(Double seconds) {
        this.seconds = seconds;
    }

    public Double getAccel_x() {
        return accel_x;
    }

    public void setAccel_x(Double accel_x) {
        this.accel_x = accel_x;
    }

    public Double getAccel_y() {
        return accel_y;
    }

    public void setAccel_y(Double accel_y) {
        this.accel_y = accel_y;
    }

    public Double getAccel_z() {
        return accel_z;
    }

    public void setAccel_z(Double accel_z) {
        this.accel_z = accel_z;
    }
}
