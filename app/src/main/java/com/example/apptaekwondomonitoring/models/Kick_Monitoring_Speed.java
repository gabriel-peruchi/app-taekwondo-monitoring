package com.example.apptaekwondomonitoring.models;

public class Kick_Monitoring_Speed {

    private Long _id;
    private Kick_Monitoring kick_monitoring;
    private Double seconds;
    private Double speed_x;
    private Double speed_y;
    private Double speed_z;
    private Double resulting;

    public Kick_Monitoring_Speed() {

    }

    public Kick_Monitoring_Speed(Long _id, Double seconds, Double speed_x, Double speed_y, Double speed_z, Double resulting) {
        this._id = _id;
        this.seconds = seconds;
        this.speed_x = speed_x;
        this.speed_y = speed_y;
        this.speed_z = speed_z;
        this.resulting = resulting;
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

    public Double getSpeed_x() {
        return speed_x;
    }

    public void setSpeed_x(Double speed_x) {
        this.speed_x = speed_x;
    }

    public Double getSpeed_y() {
        return speed_y;
    }

    public void setSpeed_y(Double speed_y) {
        this.speed_y = speed_y;
    }

    public Double getSpeed_z() {
        return speed_z;
    }

    public void setSpeed_z(Double speed_z) {
        this.speed_z = speed_z;
    }

    public Double getResulting() {
        return resulting;
    }

    public void setResulting(Double resulting) {
        this.resulting = resulting;
    }
}
