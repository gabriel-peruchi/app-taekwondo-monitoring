package com.example.apptaekwondomonitoring.models;

import java.io.Serializable;

public class Kick_Monitoring_Wearable extends Kick_Monitoring_Accel implements Serializable {
    public Kick_Monitoring_Wearable() {
        super();
    }

    public Kick_Monitoring_Wearable(Long _id, Double seconds, Double accel_x, Double accel_y, Double accel_z, Double resulting) {
        super(_id, seconds, accel_x, accel_y, accel_z, resulting);
    }
}
