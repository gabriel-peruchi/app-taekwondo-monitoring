package com.example.apptaekwondomonitoring;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.apptaekwondomonitoring.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class KickMonitoringActivity extends AppCompatActivity {

    private Kick_Monitoring_ImpactDAO kick_monitoring_impactDAO;
    private Kick_Monitoring_WearableDAO kick_monitoring_wearableDAO;
    private Kick_Monitoring_SpeedDAO kick_monitoring_speedDAO;

    private Kick_Monitoring kick_monitoring;

    private TextView txt_name_athlete;
    private TextView txt_name_kick;
    private TextView txt_max_value_impact;
    private TextView txt_max_value_accel;
    private TextView txt_max_value_velocity;
    private TextView txt_execution_time;

    private EditText edt_start_time;
    private EditText edt_end_time;

    private Button btn_recalculate_speed;
    private Button btn_save_recalculate_speed;

    private List<Kick_Monitoring_Impact> kick_monitoring_impactList;
    private List<Kick_Monitoring_Wearable> kick_monitoring_wearableList;
    private List<Kick_Monitoring_Speed> kick_monitoring_speedList;

    private AnyChartView chart_kick_monitoring_impact;
    private AnyChartView chart_kick_monitoring_wearable;
    private AnyChartView chart_kick_monitoring_speed;

    private ChartCartesian chart_cartesian_module_impact;
    private ChartCartesian chart_cartesian_module_wearable;
    private ChartCartesian chart_cartesian_speed_calculated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kick_monitoring);

        getSupportActionBar().setTitle(R.string.details);

        txt_name_athlete = findViewById(R.id.txt_kick_monitoring_name_athlete);
        txt_name_kick = findViewById(R.id.txt_kick_monitoring_name_kick);
        txt_max_value_impact = findViewById(R.id.txt_kick_monitoring_max_value_impact);
        txt_max_value_accel = findViewById(R.id.txt_kick_monitoring_max_value_accel);
        txt_max_value_velocity = findViewById(R.id.txt_kick_monitoring_max_value_velocity);
        txt_execution_time = findViewById(R.id.txt_kick_monitoring_execution_time);
        edt_start_time = findViewById(R.id.edt_start_time);
        edt_end_time = findViewById(R.id.edt_end_time);
        btn_recalculate_speed = findViewById(R.id.btn_recalculate_speed);
        btn_save_recalculate_speed = findViewById(R.id.btn_save_recalculate_speed);

        kick_monitoring_impactDAO = new Kick_Monitoring_ImpactDAO(KickMonitoringActivity.this);
        kick_monitoring_wearableDAO = new Kick_Monitoring_WearableDAO(KickMonitoringActivity.this);
        kick_monitoring_speedDAO = new Kick_Monitoring_SpeedDAO(KickMonitoringActivity.this);

        kick_monitoring = (Kick_Monitoring) getIntent().getSerializableExtra("kick_monitoring");

        txt_name_athlete.setText(kick_monitoring.getMonitoring().getAthlete().getName());
        txt_name_kick.setText(String.valueOf(kick_monitoring.get_id()));

        btn_recalculate_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recalculateSpeed();
            }
        });

        btn_save_recalculate_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecalculateSpeed();
            }
        });

        populateData();
        constructCardInfo();
        constructCharImpact();
        constructCharWearable();
        constructCharSpeed();
    }

    private void populateData() {
        kick_monitoring_impactList = kick_monitoring_impactDAO
                .selectByKickMonitoring(kick_monitoring);

        kick_monitoring_wearableList = kick_monitoring_wearableDAO
                .selectByKickMonitoring(kick_monitoring);

        kick_monitoring_speedList = kick_monitoring_speedDAO
                .selectByKickMonitoring(kick_monitoring);

        edt_end_time.setText(String.valueOf(kick_monitoring_speedList.get(kick_monitoring_speedList.size() - 1).getSeconds()));
        edt_start_time.setText(String.valueOf(kick_monitoring_speedList.get(0).getSeconds()));
    }

    private void constructCardInfo() {

        Double max_impact = MathUtils.toFixed(getMaxImpact(kick_monitoring_impactList), 4);
        Double max_accel = MathUtils.toFixed(getMaxAccel(kick_monitoring_wearableList), 4);
        Double max_velocity = MathUtils.toFixed(getMaxSpeed(kick_monitoring_speedList), 4);
        Double executionTime = MathUtils.toFixed(getExecutionTime(kick_monitoring_speedList, kick_monitoring_wearableList), 4);

        txt_max_value_impact.setText(String.valueOf(max_impact));
        txt_max_value_accel.setText(String.valueOf(max_accel));
        txt_max_value_velocity.setText(String.valueOf(max_velocity));
        txt_execution_time.setText(String.valueOf(executionTime));

    }

    private void constructCharImpact() {

        chart_kick_monitoring_impact = findViewById(R.id.chart_kick_monitoring_impact);
        APIlib.getInstance().setActiveAnyChartView(chart_kick_monitoring_impact);

        chart_cartesian_module_impact = new ChartCartesian();
        chart_cartesian_module_impact.createDefaultSettings("Valores de Impacto do Chute");
        chart_cartesian_module_impact.setLegends(getString(R.string.accel_meters_per_second), getString(R.string.time_second));
        chart_cartesian_module_impact.setView(chart_kick_monitoring_impact);

        List<AccelerationData> values_impact = convertToAccelerationData(
                new ArrayList<Kick_Monitoring_Accel>(kick_monitoring_impactList)
        );

        chart_cartesian_module_impact.setData(new ArrayList<ValueDataEntry>(values_impact));
    }

    private void constructCharWearable() {
        chart_kick_monitoring_wearable = findViewById(R.id.chart_kick_monitoring_wearable);
        APIlib.getInstance().setActiveAnyChartView(chart_kick_monitoring_wearable);

        chart_cartesian_module_wearable = new ChartCartesian();
        chart_cartesian_module_wearable.createDefaultSettings("Valores de Aceleração do Chute");
        chart_cartesian_module_wearable.setLegends(getString(R.string.accel_meters_per_second), getString(R.string.time_second));
        chart_cartesian_module_wearable.setView(chart_kick_monitoring_wearable);

        List<AccelerationData> values_wearable = convertToAccelerationData(
                new ArrayList<Kick_Monitoring_Accel>(kick_monitoring_wearableList)
        );

        chart_cartesian_module_wearable.setData(new ArrayList<ValueDataEntry>(values_wearable));
    }

    private void constructCharSpeed() {
        chart_kick_monitoring_speed = findViewById(R.id.chart_kick_monitoring_speed);
        APIlib.getInstance().setActiveAnyChartView(chart_kick_monitoring_speed);

        chart_cartesian_speed_calculated = new ChartCartesian();
        chart_cartesian_speed_calculated.createDefaultSettings("Valores de Velocidade do Chute");
        chart_cartesian_speed_calculated.setLegends(getString(R.string.speed_meters_per_second), getString(R.string.time_second));
        chart_cartesian_speed_calculated.setView(chart_kick_monitoring_speed);

        List<SpeedData> values_speed = convertToSpeedData(kick_monitoring_speedList);

        chart_cartesian_speed_calculated.setData(new ArrayList<ValueDataEntry>(values_speed));
    }

    private List<AccelerationData> convertToAccelerationData(List<Kick_Monitoring_Accel> kick_monitoring_accelList) {
        List<AccelerationData> accelerationDataList = new ArrayList<>();

        for (Kick_Monitoring_Accel kick_monitoring_accel : kick_monitoring_accelList) {
            accelerationDataList.add(
                    new AccelerationData(
                            kick_monitoring_accel.getSeconds(),
                            kick_monitoring_accel.getAccel_x(),
                            kick_monitoring_accel.getAccel_y(),
                            kick_monitoring_accel.getAccel_z(),
                            kick_monitoring_accel.getResulting()
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
                            kick_monitoring_speed.getSpeed_z(),
                            kick_monitoring_speed.getResulting()
                    )
            );
        }

        return speedDataList;
    }

    public Double getMaxImpact(List<Kick_Monitoring_Impact> kick_monitoring_impactList) {
        double maxImpact = 0.0;

        for (Kick_Monitoring_Impact kick_monitoring_impact : kick_monitoring_impactList) {
            if (kick_monitoring_impact.getResulting() > maxImpact) {
                maxImpact = kick_monitoring_impact.getResulting();
            }

            if (maxImpact > 50 && kick_monitoring_impact.getResulting() < maxImpact) {
                break;
            }
        }

        return maxImpact;
    }

    public Double getMaxAccel(List<Kick_Monitoring_Wearable> kick_monitoring_wearableList) {
        double maxAccel = 0.0;

        for (Kick_Monitoring_Wearable kick_monitoring_wearable : kick_monitoring_wearableList) {
            // Impacto
            if (kick_monitoring_wearable.getAccel_x() < -50) {
                break;
            }

            if (kick_monitoring_wearable.getResulting() > maxAccel) {
                maxAccel = kick_monitoring_wearable.getResulting();
            }
        }

        return maxAccel;
    }

    public Double getMaxSpeed(List<Kick_Monitoring_Speed> kick_monitoring_speedList) {
        double maxSpeed = 0.0;

        for (Kick_Monitoring_Speed kick_monitoring_speed : kick_monitoring_speedList) {
            if (kick_monitoring_speed.getResulting() > maxSpeed) {
                maxSpeed = kick_monitoring_speed.getResulting();
            }
        }

        return maxSpeed;
    }

    public Double getExecutionTime(List<Kick_Monitoring_Speed> kick_monitoring_speedList, List<Kick_Monitoring_Wearable> kick_monitoring_wearableList) {
        double timeBeforeImpact = kick_monitoring_speedList.get(kick_monitoring_speedList.size() - 1).getSeconds();

        double timeImpact = 0.0;

        for (int i = 0; i < kick_monitoring_wearableList.size(); i++) {
            if (timeBeforeImpact == kick_monitoring_wearableList.get(i).getSeconds()) {
                if (i + 1 == kick_monitoring_wearableList.size()) {
                    timeImpact = kick_monitoring_wearableList.get(i).getSeconds();
                    break;
                } else {
                    timeImpact = kick_monitoring_wearableList.get(i + 1).getSeconds();
                    break;
                }
            }
        }

        return timeImpact - kick_monitoring_speedList.get(0).getSeconds();
    }

    public void recalculateSpeed() {

        if (edt_start_time.getText().toString().isEmpty() || edt_end_time.getText().toString().isEmpty()) {
            return;
        }

        List<Kick_Monitoring_Speed> new_kick_monitoring_speedList = new ArrayList<>();

        double start_time = Double.parseDouble(edt_start_time.getText().toString());
        double end_time = Double.parseDouble(edt_end_time.getText().toString());

        boolean start_calculation = true;

        double speedResulting = 0.0;
        double speedX = 0.0;
        double speedY = 0.0;
        double speedZ = 0.0;

        for (int i = 1; i < kick_monitoring_wearableList.size(); i++) {

            Kick_Monitoring_Wearable kick_monitoring_wearable_before = kick_monitoring_wearableList.get(i - 1);
            Kick_Monitoring_Wearable kick_monitoring_wearable_current = kick_monitoring_wearableList.get(i);

            if (kick_monitoring_wearable_current.getSeconds() >= start_time && kick_monitoring_wearable_current.getSeconds() <= end_time) {

                if (kick_monitoring_wearable_current.getSeconds() == start_time || start_calculation) {
                    kick_monitoring_wearable_before = new Kick_Monitoring_Wearable(
                            kick_monitoring_wearable_before.get_id(),
                            kick_monitoring_wearable_before.getSeconds(),
                            0.0, 0.0, 0.0, 0.0
                    );
                    start_calculation = false;
                }

                double beforeTime = kick_monitoring_wearable_before.getSeconds();
                double currentTime = kick_monitoring_wearable_current.getSeconds();

                speedX += MathUtils.integrateTrapezoidal(beforeTime, currentTime, kick_monitoring_wearable_before.getAccel_x(), kick_monitoring_wearable_current.getAccel_x());
                speedY += MathUtils.integrateTrapezoidal(beforeTime, currentTime, kick_monitoring_wearable_before.getAccel_y(), kick_monitoring_wearable_current.getAccel_y());
                speedZ += MathUtils.integrateTrapezoidal(beforeTime, currentTime, kick_monitoring_wearable_before.getAccel_z(), kick_monitoring_wearable_current.getAccel_z());
                speedResulting = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2) + Math.pow(speedZ, 2));

                Kick_Monitoring_Speed kick_monitoring_speed = new Kick_Monitoring_Speed();

                kick_monitoring_speed.setKick_monitoring(kick_monitoring);
                kick_monitoring_speed.setSeconds(currentTime);
                kick_monitoring_speed.setSpeed_x(speedX);
                kick_monitoring_speed.setSpeed_y(speedY);
                kick_monitoring_speed.setSpeed_z(speedZ);
                kick_monitoring_speed.setResulting(speedResulting);

                new_kick_monitoring_speedList.add(kick_monitoring_speed);
            }
        }

        kick_monitoring_speedList.clear();
        kick_monitoring_speedList.addAll(new_kick_monitoring_speedList);

        List<SpeedData> values_speed = convertToSpeedData(kick_monitoring_speedList);

        APIlib.getInstance().setActiveAnyChartView(chart_kick_monitoring_speed);
        chart_cartesian_speed_calculated.setData(new ArrayList<ValueDataEntry>(values_speed));
        constructCardInfo();
    }

    public void saveRecalculateSpeed() {
        kick_monitoring_speedDAO.deleteByKickMonitoring(kick_monitoring);
        kick_monitoring_speedDAO.insertAll(kick_monitoring_speedList);
    }
}
