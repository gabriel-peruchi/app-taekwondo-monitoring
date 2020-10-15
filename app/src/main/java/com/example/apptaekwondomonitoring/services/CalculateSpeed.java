package com.example.apptaekwondomonitoring.services;

import com.example.apptaekwondomonitoring.charts.AccelerationData;
import com.example.apptaekwondomonitoring.charts.SpeedData;
import com.example.apptaekwondomonitoring.interfaces.FunctionIntegrate;

import java.util.ArrayList;
import java.util.List;

public class CalculateSpeed {

    // Número de trapézios dentro de um intervalo (Quanto maior, mais precisão)
    private final static Integer NUMBER_DIVISION = 1;

    public static List<SpeedData> calculate(final List<AccelerationData> accelerationDataList) {
        List<SpeedData> speedDataList = new ArrayList<>();

        double speedSumX = 0.0;
        double speedSumY = 0.0;
        double speedSumZ = 0.0;

        boolean movimento = false;

        double finalSecondsKick = getFinalSecondsKick(accelerationDataList);

        // Aceleração Inicial
        accelerationDataList.add(0, new AccelerationData(0, 0, 0, 0));

        for (int i = 1; i < accelerationDataList.size(); i++) {

            AccelerationData accelerationData = accelerationDataList.get(i);

            // Identificação de movimento
            if (Math.abs(accelerationData.getAccelX()) > 4.9 || Math.abs(accelerationData.getAccelY()) > 4.9 || Math.abs(accelerationData.getAccelZ()) > 4.9) {
                movimento = true;
            }

            if (!movimento) {
                continue;
            }

            // Calcula até o momento do impacto
            if (accelerationData.getSeconds() > finalSecondsKick) {
                break;
            }

            double beforeTime = accelerationDataList.get(i - 1).getSeconds();
            double currentTime = accelerationData.getSeconds();

            double speedX = 0.0;
            double speedY = 0.0;
            double speedZ = 0.0;

            speedX = integrateTrapezoidal(beforeTime, currentTime, NUMBER_DIVISION, new FunctionIntegrate() {
                @Override
                public double value(double x) {
                    return getAcclerationDataForSeconds(accelerationDataList, x).getAccelX();
                }
            });

            speedY = integrateTrapezoidal(beforeTime, currentTime, NUMBER_DIVISION, new FunctionIntegrate() {
                @Override
                public double value(double x) {
                    return getAcclerationDataForSeconds(accelerationDataList, x).getAccelY();
                }
            });

            speedZ = integrateTrapezoidal(beforeTime, currentTime, NUMBER_DIVISION, new FunctionIntegrate() {
                @Override
                public double value(double x) {
                    return getAcclerationDataForSeconds(accelerationDataList, x).getAccelZ();
                }
            });

            speedSumX += Math.round(speedX * 10000.0) / 10000.0;
            speedSumY += Math.round(speedY * 10000.0) / 10000.0;
            speedSumZ += Math.round(speedZ * 10000.0) / 10000.0;

            SpeedData speedData = new SpeedData(currentTime, speedSumX, speedSumY, speedSumZ);

            speedDataList.add(speedData);
        }

        return speedDataList;
    }

    private static double getFinalSecondsKick(List<AccelerationData> accelerationDataList) {
        double seconds = 0.0;
        double accelZ = 0.0;

        for (AccelerationData accelerationData : accelerationDataList) {
            if (accelerationData.getAccelZ() >= accelZ) {
                // Impacto
                if (accelerationData.getAccelX() < -70) {
                    break;
                }
                accelZ = accelerationData.getAccelZ();
                seconds = accelerationData.getSeconds();
            }
        }

        return seconds;
    }

    private static AccelerationData getAcclerationDataForSeconds(List<AccelerationData> accelerationDataList, double seconds) {
        AccelerationData accelerationData = null;

        for (AccelerationData accelData : accelerationDataList) {
            if (accelData.getSeconds() == seconds) {
                accelerationData = accelData;
                break;
            }
        }

        return accelerationData;
    }

    // Este código é fornecido por Anant Agarwal em https://www.geeksforgeeks.org/trapezoidal-rule-for-approximate-value-of-definite-integral/
    private static double integrateTrapezoidal(double a, double b, double n, FunctionIntegrate y) {
        // Altura do Trapézio (Espacamento entre as divisões)
        double h = (b - a) / n;

        // Calculando a soma do primeiro e último termo na fórmula acima
        double s = y.value(a) + y.value(b);

        // Adicionando termos do meio na fórmula acima
        for (int i = 1; i < n; i++)
            s += 2 * y.value(a + (i * h));

        // h / 2 indica (b-a) / 2n.
        // Multiplicando h / 2 com s.
        return (h / 2) * s;
    }
}
