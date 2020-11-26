package com.example.apptaekwondomonitoring.charts;

import androidx.annotation.NonNull;

import com.anychart.chart.common.dataentry.ValueDataEntry;

public class AccelerationData extends ValueDataEntry implements Comparable<AccelerationData> {

    public AccelerationData(Number seconds, Number accelX, Number accelY, Number accelZ, Number resulting) {
        super(seconds, accelX);
        setValue("value2", accelY);
        setValue("value3", accelZ);
        setValue("value4", resulting);
    }

    public Double getSeconds() {
        return Double.parseDouble((String) getValue("x"));
    }

    public Double getAccelX() {
        return Double.parseDouble((String) getValue("value"));
    }

    public Double getAccelY() {
        return Double.parseDouble((String) getValue("value2"));
    }

    public Double getAccelZ() {
        return Double.parseDouble((String) getValue("value3"));
    }

    public Double getResulting() {
        return Double.parseDouble((String) getValue("value4"));
    }

    @Override
    public int compareTo(AccelerationData otherAcclerationData) {
        return this.getSeconds().compareTo(otherAcclerationData.getSeconds());
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + getSeconds() + " | " + getAccelX() + " | " + getAccelY() + " | " + getAccelZ() + " | " + getResulting() + "}";
    }
}
