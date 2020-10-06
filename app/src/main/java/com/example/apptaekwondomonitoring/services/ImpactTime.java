package com.example.apptaekwondomonitoring.services;

import com.example.apptaekwondomonitoring.charts.AccelerationData;

import java.util.List;

public class ImpactTime {

    public static double getTimeImpact(List<AccelerationData> impactValues, List<AccelerationData> accelerationKickValues) {

        double timeImpact = 0.0;

        for (AccelerationData impactValue : impactValues) {
            if (impactValue.getAccelX() > 1) {
                timeImpact = impactValue.getSeconds();
                break;
            }
        }

        return timeImpact;
    }

}
