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
                // Converte de g para m/s²
                double accelX = Double.parseDouble(dataAccelerations[1]) * 9.8;
                double accelY = Double.parseDouble(dataAccelerations[2]) * 9.8;
                double accelZ = Double.parseDouble(dataAccelerations[3]) > 0
                        ? (Double.parseDouble(dataAccelerations[3]) * 9.8) - 9.8
                        : (Double.parseDouble(dataAccelerations[3]) * 9.8); // Retira a aceleração da gravidade

                Number resulting = Math.sqrt(Math.pow(accelX, 2) + Math.pow(accelY, 2) + Math.pow(accelZ, 2));

                AccelerationData accelerationData = new AccelerationData(seconds, accelX, accelY, accelZ, resulting);

                accelerationDataList.add(accelerationData);
            }
        }

        // Efeito de segurança (Ordena por segundos)
        Collections.sort(accelerationDataList);

        return accelerationDataList;
    }

}
