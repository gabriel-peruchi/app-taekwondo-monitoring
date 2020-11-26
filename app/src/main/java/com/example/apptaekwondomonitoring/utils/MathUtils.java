package com.example.apptaekwondomonitoring.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    public static Double toFixed(Double value, int scale) {
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static double integrateTrapezoidal(double beforeX, double currentX, double beforeY, double currentY) {
        // Altura do Trapézio (Espacamento entre as divisões)
        double h = (currentX - beforeX);

        // Calculando a soma do primeiro e último termo
        double s = beforeY + currentY;

        // Multiplicando h / 2 com s.
        return (h / 2) * s;
    }
}
