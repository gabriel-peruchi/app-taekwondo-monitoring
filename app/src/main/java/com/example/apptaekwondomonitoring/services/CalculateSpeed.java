package com.example.apptaekwondomonitoring.services;

import com.example.apptaekwondomonitoring.charts.AccelerationData;
import com.example.apptaekwondomonitoring.charts.SpeedData;
import com.example.apptaekwondomonitoring.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class CalculateSpeed {

    public static List<SpeedData> calculate(final List<AccelerationData> accelerationDataList) {
        List<SpeedData> speedDataList = new ArrayList<>();

        double speedSumX = 0.0;
        double speedSumY = 0.0;
        double speedSumZ = 0.0;
        double resultingSum = 0.0;

        boolean movement = false;
        boolean startCalculation = true;

        double finalSecondsKick = getFinalSecondsKick(accelerationDataList);

        for (int i = 1; i < accelerationDataList.size(); i++) {

            AccelerationData accelerationDataBefore = accelerationDataList.get(i - 1);
            AccelerationData accelerationDataCurrent = accelerationDataList.get(i);

            // Identificação de movimento (Levantar do Pé)
            if (accelerationDataCurrent.getAccelZ() > 3) {
                movement = true;
            }

            if (!movement) {
                continue;
            }

            // Calcula até o momento do impacto
            if (accelerationDataCurrent.getSeconds() > finalSecondsKick) {
                break;
            }

            if (startCalculation) {
                accelerationDataBefore = new AccelerationData(accelerationDataBefore.getSeconds(), 0, 0, 0, 0);
                startCalculation = false;
            }

            double beforeTime = accelerationDataBefore.getSeconds();
            double currentTime = accelerationDataCurrent.getSeconds();

            speedSumX += MathUtils.integrateTrapezoidal(beforeTime, currentTime, accelerationDataBefore.getAccelX(), accelerationDataCurrent.getAccelX());
            speedSumY += MathUtils.integrateTrapezoidal(beforeTime, currentTime, accelerationDataBefore.getAccelY(), accelerationDataCurrent.getAccelY());
            speedSumZ += MathUtils.integrateTrapezoidal(beforeTime, currentTime, accelerationDataBefore.getAccelZ(), accelerationDataCurrent.getAccelZ());
            resultingSum = Math.sqrt(Math.pow(speedSumX, 2) + Math.pow(speedSumY, 2) + Math.pow(speedSumZ, 2));

            SpeedData speedData = new SpeedData(currentTime, speedSumX, speedSumY, speedSumZ, resultingSum);

            speedDataList.add(speedData);
        }

        return speedDataList;
    }

    private static double getFinalSecondsKick(List<AccelerationData> accelerationDataList) {
        double seconds = 0.0;

        for (AccelerationData accelerationData : accelerationDataList) {
            // Impacto
            if (accelerationData.getAccelX() < -70) {
                break;
            }
            seconds = accelerationData.getSeconds();
        }

        return seconds;
    }
}
