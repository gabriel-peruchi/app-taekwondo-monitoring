package com.example.apptaekwondomonitoring.utils;

import android.util.Log;

import com.anychart.chart.common.dataentry.DataEntry;
import com.example.apptaekwondomonitoring.charts.AccelerationData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BluetoothUtils {

    // PROTOCOLO -> a{millisegundos|accelX|accelY|accelZ}a{millisegundos|accelX|accelY|accelZ}
    public static List<AccelerationData> convertDataToList(StringBuilder data) {

        List<AccelerationData> accelerationDataList = new ArrayList<>();

//        Log.d("DATA", data.toString());

        String[] values = data.toString().split("a");

//        Log.d("DATA SPLIT", Arrays.toString(values));

        // [{millisegundos|accelX|accelY|accelZ}]
        for (String value : values) {

            // Dados completos
            if (!value.isEmpty() && value.charAt(0) == '{' && value.charAt(value.length() - 1) == '}') {
                value = value.substring(1, value.length() - 1);

//                Log.d("VALUE", value);

                String[] dataAccelerations = value.split("\\|");

//                Log.d("DATA ACCELERATIONS", Arrays.toString(dataAccelerations));

                Number seconds = Double.parseDouble(dataAccelerations[0]) / 1000;
                Number accelX = Double.parseDouble(dataAccelerations[1]) * 9.80665;
                Number accelY = Double.parseDouble(dataAccelerations[2]) * 9.80665;
                Number accelZ = Double.parseDouble(dataAccelerations[3]) * 9.80665;

//                Log.d("CONVERTIDOS", " "
//                    + seconds
//                    + " "
//                    + accelX
//                    + " "
//                    + accelY
//                    + " "
//                    + accelZ);

                AccelerationData accelerationData = new AccelerationData(seconds, accelX, accelY, accelZ);

//                Log.d("accel DATA", " "
//                        + accelerationData.getSeconds()
//                        + " "
//                        + accelerationData.getAccelX()
//                        + " "
//                        + accelerationData.getAccelY()
//                        + " "
//                        + accelerationData.getAccelY());

                accelerationDataList.add(accelerationData);

            }
        }

        // Efeito de segurança (Ordena por segundos)
        Collections.sort(accelerationDataList);

        return accelerationDataList;
    }

}
