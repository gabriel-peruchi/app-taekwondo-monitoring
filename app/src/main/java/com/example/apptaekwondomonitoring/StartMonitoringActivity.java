package com.example.apptaekwondomonitoring;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.APIlib;
import com.anychart.AnyChartView;
import com.example.apptaekwondomonitoring.charts.AccelerationData;
import com.example.apptaekwondomonitoring.charts.ChartCartesian;
import com.example.apptaekwondomonitoring.database.dao.Kick_MonitoringDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_ImpactDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_WearableDAO;
import com.example.apptaekwondomonitoring.dialog.DialogConnectionModules;
import com.example.apptaekwondomonitoring.interfaces.Constants;
import com.example.apptaekwondomonitoring.models.Athlete;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring_Impact;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring_Wearable;
import com.example.apptaekwondomonitoring.models.Monitoring;
import com.example.apptaekwondomonitoring.services.BluetoothConnection;
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

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothConnection bluetooth_connection_impact_module;
    private BluetoothConnection bluetooth_connection_wearable_module;

    private Athlete athlete;
    private Monitoring monitoring;

    private Kick_MonitoringDAO kick_monitoringDAO;
    private Kick_Monitoring_WearableDAO kick_monitoring_wearableDAO;
    private Kick_Monitoring_ImpactDAO kick_monitoring_impactDAO;

    private FloatingActionButton button_finished_monitoring;
    private FloatingActionButton button_kick_collection;
    private FloatingActionButton button_kick_save;
    private FloatingActionButton button_kick_discard;
    private Button button_calculate_speed;
    private EditText edt_start_time;
    private EditText edt_end_time;
    private TextView txt_result_x_velocity;
    private TextView txt_result_y_velocity;
    private TextView txt_result_z_velocity;

    private StringBuilder data_module_impact = new StringBuilder();
    private List<AccelerationData> accelerations_data_module_impact = new ArrayList<>();

    private StringBuilder data_module_wearable = new StringBuilder();
    private List<AccelerationData> accelerations_data_module_wearable = new ArrayList<>();

    private ChartCartesian chart_cartesian_module_impact;
    private ChartCartesian chart_cartesian_module_wearable;

    private AnyChartView any_chart_view_module_impact;
    private AnyChartView any_chart_view_module_wearable;

    private boolean receiveData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_monitoring);
        getSupportActionBar().setTitle(R.string.monitoring);

        athlete = (Athlete) getIntent().getSerializableExtra("athlete");
        monitoring = (Monitoring) getIntent().getSerializableExtra("monitoring");

        kick_monitoringDAO = new Kick_MonitoringDAO(StartMonitoringActivity.this);
        kick_monitoring_wearableDAO = new Kick_Monitoring_WearableDAO(StartMonitoringActivity.this);
        kick_monitoring_impactDAO = new Kick_Monitoring_ImpactDAO(StartMonitoringActivity.this);

        button_calculate_speed = findViewById(R.id.btn_calculate_speed);
        button_finished_monitoring = findViewById(R.id.btn_finish_monitoring);
        button_kick_collection = findViewById(R.id.btn_kick_collection);
        button_kick_save = findViewById(R.id.btn_kick_save);
        button_kick_discard = findViewById(R.id.btn_kick_discard);
        edt_start_time = findViewById(R.id.edt_start_time);
        edt_end_time = findViewById(R.id.edt_end_time);
        txt_result_x_velocity = findViewById(R.id.txt_result_x_velocity);
        txt_result_y_velocity = findViewById(R.id.txt_result_y_velocity);
        txt_result_z_velocity = findViewById(R.id.txt_result_z_velocity);

        button_calculate_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCalculateVelocity();
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

        bluetooth_connection_impact_module.write(receiveData ? "1" : "0");
        bluetooth_connection_wearable_module.write(receiveData ? "1" : "0");

        if (!receiveData) {
            accelerations_data_module_impact = BluetoothUtils.convertDataToList(data_module_impact);
            APIlib.getInstance().setActiveAnyChartView(any_chart_view_module_impact);
            chart_cartesian_module_impact.setData(accelerations_data_module_impact);
            data_module_impact.delete(0, data_module_impact.length());

            accelerations_data_module_wearable = BluetoothUtils.convertDataToList(data_module_wearable);
            APIlib.getInstance().setActiveAnyChartView(any_chart_view_module_wearable);
            chart_cartesian_module_wearable.setData(accelerations_data_module_wearable);
            data_module_wearable.delete(0, data_module_wearable.length());
        }
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
        chart_cartesian_module_impact.clearData();
        chart_cartesian_module_impact.setView(any_chart_view_module_impact);
    }

    private void constructChartModuleWearable() {
        any_chart_view_module_wearable = findViewById(R.id.chart_module_wearable);
        APIlib.getInstance().setActiveAnyChartView(any_chart_view_module_wearable);

        chart_cartesian_module_wearable = new ChartCartesian();
        chart_cartesian_module_wearable.createDefaultSettings("Valores de Aceleração do Golpe");
        chart_cartesian_module_wearable.clearData();
        chart_cartesian_module_wearable.setView(any_chart_view_module_wearable);
    }

    public void buttonKickSaveListener() {
        if (!(accelerations_data_module_impact.size() > 0) && !(accelerations_data_module_wearable.size() > 0)) {
            return;
        }

        double max_impact_x = 0;
        double max_impact_y = 0;
        double max_impact_z = 0;
        double max_accel_kick_x = 0;
        double max_accel_kick_y = 0;
        double max_accel_kick_z = 0;
        double max_velocity_kick_x = 0;
        double max_velocity_kick_y = 0;
        double max_velocity_kick_z = 0;

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

        // Seta os valores de velocidade calculados pelo usuário
        max_velocity_kick_x = Double.parseDouble(txt_result_x_velocity.getText().toString());
        max_velocity_kick_y = Double.parseDouble(txt_result_y_velocity.getText().toString());
        max_velocity_kick_z = Double.parseDouble(txt_result_z_velocity.getText().toString());

        Kick_Monitoring kick_monitoring = new Kick_Monitoring();

        kick_monitoring.setMonitoring(monitoring);
        kick_monitoring.setMax_impact_x(max_impact_x);
        kick_monitoring.setMax_impact_y(max_impact_y);
        kick_monitoring.setMax_impact_z(max_impact_z);
        kick_monitoring.setMax_accel_kick_x(max_accel_kick_x);
        kick_monitoring.setMax_accel_kick_y(max_accel_kick_y);
        kick_monitoring.setMax_accel_kick_z(max_accel_kick_z);
        kick_monitoring.setMax_velocity_kick_x(max_velocity_kick_x);
        kick_monitoring.setMax_velocity_kick_y(max_velocity_kick_y);
        kick_monitoring.setMax_velocity_kick_z(max_velocity_kick_z);

        long id_kick_monitoring = kick_monitoringDAO.insert(kick_monitoring);

        kick_monitoring.set_id(id_kick_monitoring);

        // Salva todos os valores de impacto no banco
        for (AccelerationData accelerationDataImpact : accelerations_data_module_impact) {

            Kick_Monitoring_Impact kick_monitoring_impact = new Kick_Monitoring_Impact();

            kick_monitoring_impact.setSeconds(accelerationDataImpact.getSeconds());
            kick_monitoring_impact.setAccel_x(accelerationDataImpact.getAccelX());
            kick_monitoring_impact.setAccel_y(accelerationDataImpact.getAccelY());
            kick_monitoring_impact.setAccel_z(accelerationDataImpact.getAccelZ());
            kick_monitoring_impact.setKick_monitoring(kick_monitoring);

            kick_monitoring_impactDAO.insert(kick_monitoring_impact);
        }

        // Salva todos os valores de aceleração do chute no banco
        for (AccelerationData accelerationDataWearable : accelerations_data_module_wearable) {

            Kick_Monitoring_Wearable kick_monitoring_wearable = new Kick_Monitoring_Wearable();

            kick_monitoring_wearable.setSeconds(accelerationDataWearable.getSeconds());
            kick_monitoring_wearable.setAccel_x(accelerationDataWearable.getAccelX());
            kick_monitoring_wearable.setAccel_y(accelerationDataWearable.getAccelY());
            kick_monitoring_wearable.setAccel_z(accelerationDataWearable.getAccelZ());
            kick_monitoring_wearable.setKick_monitoring(kick_monitoring);

            kick_monitoring_wearableDAO.insert(kick_monitoring_wearable);
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

    public void buttonCalculateVelocity() {
        if (edt_start_time.getText().toString().isEmpty()) {
            edt_start_time.setError("Campo Obrigatório");
            return;
        }

        if (edt_end_time.getText().toString().isEmpty()) {
            edt_end_time.setError("Campo Obrigatório");
            return;
        }

        // TODO: Calculo da velocidade
        txt_result_x_velocity.setText("0");
        txt_result_y_velocity.setText("0");
        txt_result_z_velocity.setText("0");
    }

    public void clearMonitoring() {
        accelerations_data_module_impact.clear();
        accelerations_data_module_wearable.clear();

        APIlib.getInstance().setActiveAnyChartView(any_chart_view_module_impact);
        chart_cartesian_module_impact.clearData();

        APIlib.getInstance().setActiveAnyChartView(any_chart_view_module_wearable);
        chart_cartesian_module_wearable.clearData();

        edt_start_time.setText("");
        edt_end_time.setText("");

        txt_result_x_velocity.setText("0");
        txt_result_y_velocity.setText("0");
        txt_result_z_velocity.setText("0");
    }

    @Override
    public void onBackPressed() {
        buttonFinishedMonitoring();
    }
}
