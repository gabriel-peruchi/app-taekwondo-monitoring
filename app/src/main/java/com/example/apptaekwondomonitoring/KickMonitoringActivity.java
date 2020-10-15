package com.example.apptaekwondomonitoring;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.APIlib;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.example.apptaekwondomonitoring.charts.AccelerationData;
import com.example.apptaekwondomonitoring.charts.ChartCartesian;
import com.example.apptaekwondomonitoring.charts.SpeedData;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_ImpactDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_SpeedDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_WearableDAO;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring_Accel;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring_Impact;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring_Speed;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring_Wearable;
import com.example.apptaekwondomonitoring.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

public class KickMonitoringActivity extends AppCompatActivity {

    private Kick_Monitoring_ImpactDAO kick_monitoring_impactDAO;
    private Kick_Monitoring_WearableDAO kick_monitoring_wearableDAO;
    private Kick_Monitoring_SpeedDAO kick_monitoring_speedDAO;

    private Kick_Monitoring kick_monitoring;

    private TextView txt_name_athlete;
    private TextView txt_name_kick;
    private TextView txt_max_value_impact_x;
    private TextView txt_max_value_impact_y;
    private TextView txt_max_value_impact_z;
    private TextView txt_max_value_accel_x;
    private TextView txt_max_value_accel_y;
    private TextView txt_max_value_accel_z;
    private TextView txt_max_value_velocity_x;
    private TextView txt_max_value_velocity_y;
    private TextView txt_max_value_velocity_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kick_monitoring);

        getSupportActionBar().setTitle(R.string.details);

        txt_name_athlete = findViewById(R.id.txt_kick_monitoring_name_athlete);
        txt_name_kick = findViewById(R.id.txt_kick_monitoring_name_kick);
        txt_max_value_impact_x = findViewById(R.id.txt_kick_monitoring_max_value_impact_x);
        txt_max_value_impact_y = findViewById(R.id.txt_kick_monitoring_max_value_impact_y);
        txt_max_value_impact_z = findViewById(R.id.txt_kick_monitoring_max_value_impact_z);
        txt_max_value_accel_x = findViewById(R.id.txt_kick_monitoring_max_value_accel_x);
        txt_max_value_accel_y = findViewById(R.id.txt_kick_monitoring_max_value_accel_y);
        txt_max_value_accel_z = findViewById(R.id.txt_kick_monitoring_max_value_accel_z);
        txt_max_value_velocity_x = findViewById(R.id.txt_kick_monitoring_max_value_velocity_x);
        txt_max_value_velocity_y = findViewById(R.id.txt_kick_monitoring_max_value_velocity_y);
        txt_max_value_velocity_z = findViewById(R.id.txt_kick_monitoring_max_value_velocity_z);

        kick_monitoring_impactDAO = new Kick_Monitoring_ImpactDAO(KickMonitoringActivity.this);
        kick_monitoring_wearableDAO = new Kick_Monitoring_WearableDAO(KickMonitoringActivity.this);
        kick_monitoring_speedDAO = new Kick_Monitoring_SpeedDAO(KickMonitoringActivity.this);

        kick_monitoring = (Kick_Monitoring) getIntent().getSerializableExtra("kick_monitoring");

        txt_name_athlete.setText(kick_monitoring.getMonitoring().getAthlete().getName());
        txt_name_kick.setText(String.valueOf(kick_monitoring.get_id()));

        //constructCardInfo();
        constructCharImpact();
        constructCharWearable();
        constructCharSpeed();
    }

    private void constructCardInfo() {

        Double max_impact_x = NumberUtils.toFixedTwo(kick_monitoring.getMax_impact_x());
        Double max_impact_y = NumberUtils.toFixedTwo(kick_monitoring.getMax_impact_y());
        Double max_impact_z = NumberUtils.toFixedTwo(kick_monitoring.getMax_impact_z());

        Double max_accel_kick_x = NumberUtils.toFixedTwo(kick_monitoring.getMax_accel_kick_x());
        Double max_accel_kick_y = NumberUtils.toFixedTwo(kick_monitoring.getMax_accel_kick_y());
        Double max_accel_kick_z = NumberUtils.toFixedTwo(kick_monitoring.getMax_accel_kick_z());

        Double max_velocity_kick_x = NumberUtils.toFixedTwo(kick_monitoring.getMax_velocity_kick_x());
        Double max_velocity_kick_y = NumberUtils.toFixedTwo(kick_monitoring.getMax_velocity_kick_y());
        Double max_velocity_kick_z = NumberUtils.toFixedTwo(kick_monitoring.getMax_velocity_kick_z());

        txt_max_value_impact_x.setText(String.valueOf(max_impact_x));
        txt_max_value_impact_y.setText(String.valueOf(max_impact_y));
        txt_max_value_impact_z.setText(String.valueOf(max_impact_z));
        txt_max_value_accel_x.setText(String.valueOf(max_accel_kick_x));
        txt_max_value_accel_y.setText(String.valueOf(max_accel_kick_y));
        txt_max_value_accel_z.setText(String.valueOf(max_accel_kick_z));
        txt_max_value_velocity_x.setText(String.valueOf(max_velocity_kick_x));
        txt_max_value_velocity_y.setText(String.valueOf(max_velocity_kick_y));
        txt_max_value_velocity_z.setText(String.valueOf(max_velocity_kick_z));
    }

    private void constructCharImpact() {

        AnyChartView chart_kick_monitoring_impact = findViewById(R.id.chart_kick_monitoring_impact);
        APIlib.getInstance().setActiveAnyChartView(chart_kick_monitoring_impact);

        ChartCartesian chartCartesian = new ChartCartesian();
        chartCartesian.createDefaultSettings("Valores de Impacto do Chute");
        chartCartesian.setLegends(getString(R.string.accel_meters_per_second), getString(R.string.time_second));
        chartCartesian.setView(chart_kick_monitoring_impact);

        List<Kick_Monitoring_Impact> kick_monitoring_impactList = kick_monitoring_impactDAO
                .selectByKickMonitoring(kick_monitoring);

        List<AccelerationData> values_impact = convertToAccelerationData(
                new ArrayList<Kick_Monitoring_Accel>(kick_monitoring_impactList)
        );

        chartCartesian.setData(new ArrayList<ValueDataEntry>(values_impact));
    }

    private void constructCharWearable() {
        AnyChartView chart_kick_monitoring_wearable = findViewById(R.id.chart_kick_monitoring_wearable);
        APIlib.getInstance().setActiveAnyChartView(chart_kick_monitoring_wearable);

        ChartCartesian chartCartesian = new ChartCartesian();
        chartCartesian.createDefaultSettings("Valores de Aceleração do Chute");
        chartCartesian.setLegends(getString(R.string.accel_meters_per_second), getString(R.string.time_second));
        chartCartesian.setView(chart_kick_monitoring_wearable);

        List<Kick_Monitoring_Wearable> kick_monitoring_wearableList = kick_monitoring_wearableDAO
                .selectByKickMonitoring(kick_monitoring);

        List<AccelerationData> values_wearable = convertToAccelerationData(
                new ArrayList<Kick_Monitoring_Accel>(kick_monitoring_wearableList)
        );

        chartCartesian.setData(new ArrayList<ValueDataEntry>(values_wearable));
    }

    private void constructCharSpeed() {
        AnyChartView chart_kick_monitoring_speed = findViewById(R.id.chart_kick_monitoring_speed);
        APIlib.getInstance().setActiveAnyChartView(chart_kick_monitoring_speed);

        ChartCartesian chartCartesian = new ChartCartesian();
        chartCartesian.createDefaultSettings("Valores de Velocidade do Chute");
        chartCartesian.setLegends(getString(R.string.speed_meters_per_second), getString(R.string.time_second));
        chartCartesian.setView(chart_kick_monitoring_speed);

        List<Kick_Monitoring_Speed> kick_monitoring_speedList = kick_monitoring_speedDAO
                .selectByKickMonitoring(kick_monitoring);

        List<SpeedData> values_speed = convertToSpeedData(
                new ArrayList<>(kick_monitoring_speedList)
        );

        chartCartesian.setData(new ArrayList<ValueDataEntry>(values_speed));
    }

    private List<AccelerationData> convertToAccelerationData(List<Kick_Monitoring_Accel> kick_monitoring_accelList) {
        List<AccelerationData> accelerationDataList = new ArrayList<>();

        for (Kick_Monitoring_Accel kick_monitoring_accel : kick_monitoring_accelList) {
            accelerationDataList.add(
                    new AccelerationData(
                            kick_monitoring_accel.getSeconds(),
                            kick_monitoring_accel.getAccel_x(),
                            kick_monitoring_accel.getAccel_y(),
                            kick_monitoring_accel.getAccel_z()
                    )
            );
        }

        return accelerationDataList;
    }

    private List<SpeedData> convertToSpeedData(List<Kick_Monitoring_Speed> kick_monitoring_speedList) {
        List<SpeedData> speedDataList = new ArrayList<>();

        for (Kick_Monitoring_Speed kick_monitoring_speed : kick_monitoring_speedList) {
            speedDataList.add(
                    new SpeedData(
                            kick_monitoring_speed.getSeconds(),
                            kick_monitoring_speed.getSpeed_x(),
                            kick_monitoring_speed.getSpeed_y(),
                            kick_monitoring_speed.getSpeed_z()
                    )
            );
        }

        return speedDataList;
    }
}
