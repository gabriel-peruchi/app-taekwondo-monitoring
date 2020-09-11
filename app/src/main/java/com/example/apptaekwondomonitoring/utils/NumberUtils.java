package com.example.apptaekwondomonitoring.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {

    public static Double toFixedTwo(Double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
