package com.example.apptaekwondomonitoring.utils;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {

    public static Double toFixedTwo(Double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static void calculateVelocity() {
        TrapezoidIntegrator trapezoidIntegrator = new TrapezoidIntegrator();

        UnivariateFunction univariateFunction = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        };

        double result = trapezoidIntegrator.integrate(0, univariateFunction, 0, 0);
    }
}
