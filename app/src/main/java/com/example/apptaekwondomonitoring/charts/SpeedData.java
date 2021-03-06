package com.example.apptaekwondomonitoring.charts;

import com.anychart.chart.common.dataentry.ValueDataEntry;

public class SpeedData extends ValueDataEntry implements Comparable<SpeedData> {

    public SpeedData(Number seconds, Number speedX, Number speedY, Number speedZ, Number resulting) {
        super(seconds, speedX);
        setValue("value2", speedY);
        setValue("value3", speedZ);
        setValue("value4", resulting);
    }

    public Double getSeconds() {
        return Double.parseDouble((String) getValue("x"));
    }

    public Double getSpeedX() {
        return Double.parseDouble((String) getValue("value"));
    }

    public Double getSpeedY() {
        return Double.parseDouble((String) getValue("value2"));
    }

    public Double getSpeedZ() {
        return Double.parseDouble((String) getValue("value3"));
    }

    public Double getResulting() {
        return Double.parseDouble((String) getValue("value4"));
    }

    @Override
    public int compareTo(SpeedData o) {
        return this.getSeconds().compareTo(o.getSeconds());
    }
}
