package com.example.apptaekwondomonitoring;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.APIlib;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.example.apptaekwondomonitoring.charts.AccelerationData;
import com.example.apptaekwondomonitoring.charts.ChartCartesian;
import com.example.apptaekwondomonitoring.charts.SpeedData;
import com.example.apptaekwondomonitoring.database.dao.Kick_MonitoringDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_ImpactDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_SpeedDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_WearableDAO;
import com.example.apptaekwondomonitoring.dialog.DialogConnectionModules;
import com.example.apptaekwondomonitoring.interfaces.Constants;
import com.example.apptaekwondomonitoring.models.Athlete;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring_Impact;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring_Speed;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring_Wearable;
import com.example.apptaekwondomonitoring.models.Monitoring;
import com.example.apptaekwondomonitoring.services.BluetoothConnection;
import com.example.apptaekwondomonitoring.services.CalculateSpeed;
import com.example.apptaekwondomonitoring.utils.AlertDialogUtils;
import com.example.apptaekwondomonitoring.utils.BluetoothUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartMonitoringActivity extends AppCompatActivity {

    private static final String ADDRESS_IMPACT_MODULE = "00:18:91:D8:3B:4E";
    private static final String ADDRESS_WEARABLE_MODULE = "00:19:08:00:50:C6";
//    private static final String ADDRESS_WEARABLE_MODULE = "00:18:91:D8:3D:97"; // Módulo Vestível Reserva

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothConnection bluetooth_connection_impact_module;
    private BluetoothConnection bluetooth_connection_wearable_module;

    private Athlete athlete;
    private Monitoring monitoring;

    private Kick_MonitoringDAO kick_monitoringDAO;
    private Kick_Monitoring_WearableDAO kick_monitoring_wearableDAO;
    private Kick_Monitoring_ImpactDAO kick_monitoring_impactDAO;
    private Kick_Monitoring_SpeedDAO kick_monitoring_speedDAO;

    private FloatingActionButton button_finished_monitoring;
    private FloatingActionButton button_kick_collection;
    private FloatingActionButton button_kick_save;
    private FloatingActionButton button_kick_discard;
    private TextView txt_tollbal_start_monitoring;
    private ImageButton image_button_disconnect_bluetooth;

    private StringBuilder data_module_impact = new StringBuilder();
    private List<AccelerationData> accelerations_data_module_impact = new ArrayList<>();

    private StringBuilder data_module_wearable = new StringBuilder();
    private List<AccelerationData> accelerations_data_module_wearable = new ArrayList<>();

    private List<SpeedData> speed_data_calculated = new ArrayList<>();

    private ChartCartesian chart_cartesian_module_impact;
    private ChartCartesian chart_cartesian_module_wearable;
    private ChartCartesian chart_cartesian_speed_calculated;

    private AnyChartView any_chart_view_module_impact;
    private AnyChartView any_chart_view_module_wearable;
    private AnyChartView any_chart_view_speed_calculated;

    private boolean receiveData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_monitoring);
        getSupportActionBar().hide();

        athlete = (Athlete) getIntent().getSerializableExtra("athlete");
        monitoring = (Monitoring) getIntent().getSerializableExtra("monitoring");

        kick_monitoringDAO = new Kick_MonitoringDAO(StartMonitoringActivity.this);
        kick_monitoring_wearableDAO = new Kick_Monitoring_WearableDAO(StartMonitoringActivity.this);
        kick_monitoring_impactDAO = new Kick_Monitoring_ImpactDAO(StartMonitoringActivity.this);
        kick_monitoring_speedDAO = new Kick_Monitoring_SpeedDAO(StartMonitoringActivity.this);

        button_finished_monitoring = findViewById(R.id.btn_finish_monitoring);
        button_kick_collection = findViewById(R.id.btn_kick_collection);
        button_kick_save = findViewById(R.id.btn_kick_save);
        button_kick_discard = findViewById(R.id.btn_kick_discard);
        txt_tollbal_start_monitoring = findViewById(R.id.txt_toolbar_start_monitoring);
        image_button_disconnect_bluetooth = findViewById(R.id.image_button_disconnect_bluetooth);

        txt_tollbal_start_monitoring.setText(R.string.monitoring);

        image_button_disconnect_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect_bluetooth();
            }
        });

        button_finished_monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFinishedMonitoring();
            }
        });

        button_kick_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonKickCollectionListener();
            }
        });

        button_kick_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonKickSaveListener();
            }
        });

        button_kick_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMonitoring();
            }
        });

        constructChartModuleImpact();
        constructChartModuleWearable();
        constructChartSpeedCalculated();
        settingsConnectionBluetoothModules();
    }


    private void settingsConnectionBluetoothModules() {
        bluetooth_connection_impact_module = new BluetoothConnection(
                bluetoothAdapter.getRemoteDevice(ADDRESS_IMPACT_MODULE),
                getHandlerBluetoothImpact()
        );

        bluetooth_connection_wearable_module = new BluetoothConnection(
                bluetoothAdapter.getRemoteDevice(ADDRESS_WEARABLE_MODULE),
                getHandlerBluetoothWearable()
        );

        new DialogConnectionModules(
                this,
                bluetooth_connection_wearable_module,
                bluetooth_connection_impact_module
        ).show();
    }

    private void buttonKickCollectionListener() {
        if (!bluetooth_connection_impact_module.isConnected() || !bluetooth_connection_wearable_module.isConnected()) {
            return;
        }

        receiveData = !receiveData;

        button_kick_collection.setImageResource(
                receiveData ? R.drawable.ic_pause_white_44dp : R.drawable.ic_play_arrow_white_44dp
        );

        String comando = receiveData ? "1" : "0";

        // Garante o envio
        bluetooth_connection_impact_module.write(comando);
        bluetooth_connection_impact_module.write(comando);
        bluetooth_connection_wearable_module.write(comando);
        bluetooth_connection_wearable_module.write(comando);

        if (!receiveData) {
            accelerations_data_module_impact = BluetoothUtils.convertDataToList(data_module_impact);
            accelerations_data_module_impact = getSignificantValues(accelerations_data_module_impact, 9, 70, 70);
            APIlib.getInstance().setActiveAnyChartView(any_chart_view_module_impact);
            chart_cartesian_module_impact.setData(new ArrayList<ValueDataEntry>(accelerations_data_module_impact));
            data_module_impact.delete(0, data_module_impact.length());

            accelerations_data_module_wearable = BluetoothUtils.convertDataToList(data_module_wearable);
            accelerations_data_module_wearable = getSignificantValues(accelerations_data_module_wearable, 5, 100, 250);
            APIlib.getInstance().setActiveAnyChartView(any_chart_view_module_wearable);
            chart_cartesian_module_wearable.setData(new ArrayList<ValueDataEntry>(accelerations_data_module_wearable));
            data_module_wearable.delete(0, data_module_wearable.length());

            speed_data_calculated = CalculateSpeed.calculate(accelerations_data_module_wearable);
            APIlib.getInstance().setActiveAnyChartView(any_chart_view_speed_calculated);
            chart_cartesian_speed_calculated.setData(new ArrayList<ValueDataEntry>(speed_data_calculated));
        }
    }

    // Retorna somente valores significativos
    public List<AccelerationData> getSignificantValues(List<AccelerationData> accelerationDataList, double limit, int intervalLeft, int intervalRight) {
        List<AccelerationData> significantValues = new ArrayList<>();

        AccelerationData accelerationDataLimit = null;
        int indexAccelerationDataLimit = 0;

        for (int i = 0; i < accelerationDataList.size(); i++) {
            if (Math.abs(accelerationDataList.get(i).getAccelX()) >= limit ||
                    Math.abs(accelerationDataList.get(i).getAccelY()) >= limit ||
                    Math.abs(accelerationDataList.get(i).getAccelZ()) >= limit
            ) {
                accelerationDataLimit = accelerationDataList.get(i);
                indexAccelerationDataLimit = i;
                break;
            }
        }

        if (accelerationDataLimit == null) {
            return accelerationDataList;
        }

        try {
            int initialIndex = intervalLeft <= indexAccelerationDataLimit ? indexAccelerationDataLimit - intervalLeft : 0;

            for (int i = initialIndex; i < indexAccelerationDataLimit; i++) {
                significantValues.add(accelerationDataList.get(i));
            }

            int finalIndex = Math.min(intervalRight + indexAccelerationDataLimit, accelerationDataList.size() - 1);

            for (int i = indexAccelerationDataLimit; i <= finalIndex; i++) {
                significantValues.add(accelerationDataList.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return significantValues.size() > 0 ? significantValues : accelerationDataList;
    }

    @SuppressLint("HandlerLeak")
    private Handler getHandlerBluetoothImpact() {
        return new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == Constants.MESSAGE_READ && receiveData) {
                    data_module_impact.append((String) msg.obj);
                }
            }
        };
    }

    @SuppressLint("HandlerLeak")
    private Handler getHandlerBluetoothWearable() {
        return new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == Constants.MESSAGE_READ && receiveData) {
                    data_module_wearable.append((String) msg.obj);
                }
            }
        };
    }

    private void constructChartModuleImpact() {
        any_chart_view_module_impact = findViewById(R.id.chart_module_impact);
        APIlib.getInstance().setActiveAnyChartView(any_chart_view_module_impact);

        chart_cartesian_module_impact = new ChartCartesian();
        chart_cartesian_module_impact.createDefaultSettings("Valores de Impacto no Saco de Pancadas");
        chart_cartesian_module_impact.setLegends(getString(R.string.accel_meters_per_second), getString(R.string.time_second));
        chart_cartesian_module_impact.clearData();
        chart_cartesian_module_impact.setView(any_chart_view_module_impact);
    }

    private void constructChartModuleWearable() {
        any_chart_view_module_wearable = findViewById(R.id.chart_module_wearable);
        APIlib.getInstance().setActiveAnyChartView(any_chart_view_module_wearable);

        chart_cartesian_module_wearable = new ChartCartesian();
        chart_cartesian_module_wearable.createDefaultSettings("Valores de Aceleração do Golpe");
        chart_cartesian_module_wearable.setLegends(getString(R.string.accel_meters_per_second), getString(R.string.time_second));
        chart_cartesian_module_wearable.clearData();
        chart_cartesian_module_wearable.setView(any_chart_view_module_wearable);
    }

    private void constructChartSpeedCalculated() {
        any_chart_view_speed_calculated = findViewById(R.id.chart_speed_calculated);
        APIlib.getInstance().setActiveAnyChartView(any_chart_view_speed_calculated);

        chart_cartesian_speed_calculated = new ChartCartesian();
        chart_cartesian_speed_calculated.createDefaultSettings("Valores de Velocidade do Golpe");
        chart_cartesian_speed_calculated.setLegends(getString(R.string.speed_meters_per_second), getString(R.string.time_second));
        chart_cartesian_speed_calculated.clearData();
        chart_cartesian_speed_calculated.setView(any_chart_view_speed_calculated);
    }

    public void buttonKickSaveListener() {
        if (accelerations_data_module_impact.size() == 0 || accelerations_data_module_wearable.size() == 0) {
            return;
        }

        double max_impact_x = 0;
        double max_impact_y = 0;
        double max_impact_z = 0;
        double max_accel_kick_x = 0;
        double max_accel_kick_y = 0;
        double max_accel_kick_z = 0;
        double max_speed_kick_x = 0;
        double max_speed_kick_y = 0;
        double max_speed_kick_z = 0;

        // Seta os valores máximos encontrados de impacto
        for (AccelerationData accelerationDataImpact : accelerations_data_module_impact) {

            if (accelerationDataImpact.getAccelX() > max_impact_x) {
                max_impact_x = accelerationDataImpact.getAccelX();
            }

            if (accelerationDataImpact.getAccelY() > max_impact_y) {
                max_impact_y = accelerationDataImpact.getAccelY();
            }

            if (accelerationDataImpact.getAccelZ() > max_impact_z) {
                max_impact_z = accelerationDataImpact.getAccelZ();
            }
        }

        // Seta os valores máximos encontrados de aceleração do chute
        for (AccelerationData accelerationDataWearable : accelerations_data_module_wearable) {

            if (accelerationDataWearable.getAccelX() > max_accel_kick_x) {
                max_accel_kick_x = accelerationDataWearable.getAccelX();
            }

            if (accelerationDataWearable.getAccelY() > max_accel_kick_y) {
                max_accel_kick_y = accelerationDataWearable.getAccelY();
            }

            if (accelerationDataWearable.getAccelZ() > max_accel_kick_z) {
                max_accel_kick_z = accelerationDataWearable.getAccelZ();
            }
        }

        // Seta os valores máximos encontrados de velocidade do chute
        for (SpeedData speedData : speed_data_calculated) {

            if (speedData.getSpeedX() > max_speed_kick_x) {
                max_speed_kick_x = speedData.getSpeedX();
            }

            if (speedData.getSpeedY() > max_speed_kick_y) {
                max_speed_kick_y = speedData.getSpeedY();
            }

            if (speedData.getSpeedZ() > max_speed_kick_z) {
                max_speed_kick_z = speedData.getSpeedZ();
            }
        }

        Kick_Monitoring kick_monitoring = new Kick_Monitoring();
        kick_monitoring.setMonitoring(monitoring);
        kick_monitoring.setMax_impact_x(max_impact_x);
        kick_monitoring.setMax_impact_y(max_impact_y);
        kick_monitoring.setMax_impact_z(max_impact_z);
        kick_monitoring.setMax_accel_kick_x(max_accel_kick_x);
        kick_monitoring.setMax_accel_kick_y(max_accel_kick_y);
        kick_monitoring.setMax_accel_kick_z(max_accel_kick_z);
        kick_monitoring.setMax_velocity_kick_x(max_speed_kick_x);
        kick_monitoring.setMax_velocity_kick_y(max_speed_kick_y);
        kick_monitoring.setMax_velocity_kick_z(max_speed_kick_z);

        long id_kick_monitoring = kick_monitoringDAO.insert(kick_monitoring);

        kick_monitoring.set_id(id_kick_monitoring);

        List<Kick_Monitoring_Impact> kick_monitoring_impactList = new ArrayList<>();

        // Salva todos os valores de impacto no banco
        for (AccelerationData accelerationDataImpact : accelerations_data_module_impact) {

            Kick_Monitoring_Impact kick_monitoring_impact = new Kick_Monitoring_Impact();
            kick_monitoring_impact.setSeconds(accelerationDataImpact.getSeconds());
            kick_monitoring_impact.setAccel_x(accelerationDataImpact.getAccelX());
            kick_monitoring_impact.setAccel_y(accelerationDataImpact.getAccelY());
            kick_monitoring_impact.setAccel_z(accelerationDataImpact.getAccelZ());
            kick_monitoring_impact.setResulting(accelerationDataImpact.getResulting());
            kick_monitoring_impact.setKick_monitoring(kick_monitoring);

            kick_monitoring_impactList.add(kick_monitoring_impact);
        }

        kick_monitoring_impactDAO.insertAll(kick_monitoring_impactList);

        List<Kick_Monitoring_Wearable> kick_monitoring_wearableList = new ArrayList<>();

        // Salva todos os valores de aceleração do chute no banco
        for (AccelerationData accelerationDataWearable : accelerations_data_module_wearable) {

            Kick_Monitoring_Wearable kick_monitoring_wearable = new Kick_Monitoring_Wearable();
            kick_monitoring_wearable.setSeconds(accelerationDataWearable.getSeconds());
            kick_monitoring_wearable.setAccel_x(accelerationDataWearable.getAccelX());
            kick_monitoring_wearable.setAccel_y(accelerationDataWearable.getAccelY());
            kick_monitoring_wearable.setAccel_z(accelerationDataWearable.getAccelZ());
            kick_monitoring_wearable.setResulting(accelerationDataWearable.getResulting());
            kick_monitoring_wearable.setKick_monitoring(kick_monitoring);

            kick_monitoring_wearableList.add(kick_monitoring_wearable);
        }

        kick_monitoring_wearableDAO.insertAll(kick_monitoring_wearableList);

        List<Kick_Monitoring_Speed> kick_monitoring_speedList = new ArrayList<>();

        // Salva os valores de velocidade do chute calculados no banco
        for (SpeedData speedData : speed_data_calculated) {

            Kick_Monitoring_Speed kick_monitoring_speed = new Kick_Monitoring_Speed();
            kick_monitoring_speed.setSeconds(speedData.getSeconds());
            kick_monitoring_speed.setSpeed_x(speedData.getSpeedX());
            kick_monitoring_speed.setSpeed_y(speedData.getSpeedY());
            kick_monitoring_speed.setSpeed_z(speedData.getSpeedZ());
            kick_monitoring_speed.setResulting(speedData.getResulting());
            kick_monitoring_speed.setKick_monitoring(kick_monitoring);

            kick_monitoring_speedList.add(kick_monitoring_speed);
        }

        if (speed_data_calculated.size() > 0) {
            kick_monitoring_speedDAO.insertAll(kick_monitoring_speedList);
        }

        clearMonitoring();

        AlertDialogUtils.createDialog(
                StartMonitoringActivity.this,
                SweetAlertDialog.SUCCESS_TYPE,
                "Golpe Monitoriado",
                "Salvo com Sucesso!"
        ).show();
    }

    public void buttonFinishedMonitoring() {
        final SweetAlertDialog alertDialogConfirmation = AlertDialogUtils.createDialog(
                StartMonitoringActivity.this,
                SweetAlertDialog.WARNING_TYPE,
                "Confirmação",
                "Deseja finalizar monitoramento?"
        );

        alertDialogConfirmation.setConfirmText("Sim");
        alertDialogConfirmation.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                try {
                    bluetooth_connection_impact_module.close();
                    bluetooth_connection_wearable_module.close();
                    startActivity(new Intent(StartMonitoringActivity.this, MainActivity.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        alertDialogConfirmation.setCancelText("Não");
        alertDialogConfirmation.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });

        alertDialogConfirmation.show();
    }

    public void clearMonitoring() {
        accelerations_data_module_impact.clear();
        accelerations_data_module_wearable.clear();
        speed_data_calculated.clear();

        APIlib.getInstance().setActiveAnyChartView(any_chart_view_module_impact);
        chart_cartesian_module_impact.clearData();

        APIlib.getInstance().setActiveAnyChartView(any_chart_view_module_wearable);
        chart_cartesian_module_wearable.clearData();

        APIlib.getInstance().setActiveAnyChartView(any_chart_view_speed_calculated);
        chart_cartesian_speed_calculated.clearData();
    }

    public void disconnect_bluetooth() {
        try {
            bluetooth_connection_impact_module.close();
            bluetooth_connection_wearable_module.close();
            settingsConnectionBluetoothModules();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        buttonFinishedMonitoring();
    }
}
