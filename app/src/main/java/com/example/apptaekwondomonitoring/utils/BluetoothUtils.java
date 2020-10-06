package com.example.apptaekwondomonitoring.utils;

import com.example.apptaekwondomonitoring.charts.AccelerationData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BluetoothUtils {

    // PROTOCOLO -> a{segundos|accelX|accelY|accelZ}a{millisegundos|accelX|accelY|accelZ}
    public static List<AccelerationData> convertDataToList(StringBuilder data) {

        List<AccelerationData> accelerationDataList = new ArrayList<>();

        String[] values = data.toString().split("a");

        // [{segundos|accelX|accelY|accelZ}]
        for (String value : values) {

            // Verifica se os dados estão completos
            if (!value.isEmpty() && value.charAt(0) == '{' && value.charAt(value.length() - 1) == '}') {
                value = value.substring(1, value.length() - 1);

                String[] dataAccelerations = value.split("\\|");

                Number seconds = Double.parseDouble(dataAccelerations[0]);
                Number accelX = Double.parseDouble(dataAccelerations[1]) * 9.8;
                Number accelY = Double.parseDouble(dataAccelerations[2]) * 9.8;
                Number accelZ = Double.parseDouble(dataAccelerations[3]) > 0
                        ? (Double.parseDouble(dataAccelerations[3]) * 9.8) - 9.8
                        : (Double.parseDouble(dataAccelerations[3]) * 9.8); // Retira a aceleração da gravidade

                AccelerationData accelerationData = new AccelerationData(seconds, accelX, accelY, accelZ);

                accelerationDataList.add(accelerationData);
            }
        }

        // Efeito de segurança (Ordena por segundos)
        Collections.sort(accelerationDataList);

        return accelerationDataList;
    }

}
