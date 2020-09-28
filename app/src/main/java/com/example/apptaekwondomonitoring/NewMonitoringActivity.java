package com.example.apptaekwondomonitoring;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apptaekwondomonitoring.adapters.BluetoothAdapterList;
import com.example.apptaekwondomonitoring.database.dao.AthleteDAO;
import com.example.apptaekwondomonitoring.database.dao.MonitoringDAO;
import com.example.apptaekwondomonitoring.dialog.DialogSelector;
import com.example.apptaekwondomonitoring.models.Athlete;
import com.example.apptaekwondomonitoring.models.Monitoring;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewMonitoringActivity extends AppCompatActivity {

    private static final int ENABLE_BLUETOOTH = 1;

    private BluetoothAdapter bluetoothAdapter;
    private boolean bluetoothAble = false;

    private Button button_bluetooth_devices;
    private Button button_start_monitoring;
    private EditText edit_name_athlete;
    private EditText edit_weight_athlete;
    private EditText edit_height_athlete;
    private EditText edit_category_athlete;

    private AthleteDAO athleteDAO;
    private MonitoringDAO monitoringDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_monitoring);

        getSupportActionBar().setTitle(R.string.new_monitoring);

        athleteDAO = new AthleteDAO(NewMonitoringActivity.this);
        monitoringDAO = new MonitoringDAO(NewMonitoringActivity.this);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        button_bluetooth_devices = findViewById(R.id.btn_bluetooth_devices);
        button_start_monitoring = findViewById(R.id.btn_start_monitoring);
        edit_name_athlete = findViewById(R.id.edit_name_athlete);
        edit_weight_athlete = findViewById(R.id.edit_weight_athlete);
        edit_height_athlete = findViewById(R.id.edit_height_athlete);
        edit_category_athlete = findViewById(R.id.edit_category_athlete);

        button_bluetooth_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonBluetoothDevicesListener();
            }
        });

        button_start_monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateBluetoothAble();
                buttonStarMonitoringListener();
            }
        });
    }

    private void buttonBluetoothDevicesListener() {
        List<BluetoothDevice> bluetoothDevices = new ArrayList<>(bluetoothAdapter.getBondedDevices());

        BluetoothAdapterList bluetoothAdapterList = new BluetoothAdapterList(
                (List<BluetoothDevice>) bluetoothDevices, NewMonitoringActivity.this, null
        );

        DialogSelector<BluetoothDevice> bluetoothDeviceDialogSelector = new DialogSelector<>(
                NewMonitoringActivity.this, bluetoothAdapterList
        );

        bluetoothDeviceDialogSelector.show();
    }

    private void buttonStarMonitoringListener() {
        if (!validateInputs()) {
            return;
        }

        if (!bluetoothAble) {
            validateBluetoothAble();
            return;
        }

        Athlete athlete = new Athlete();

        athlete.setName(edit_name_athlete.getText().toString());
        athlete.setWeight(Double.parseDouble(edit_weight_athlete.getText().toString()));
        athlete.setHeight(Double.parseDouble(edit_height_athlete.getText().toString()));
        athlete.setCategory(edit_category_athlete.getText().toString());

        long id_athlete = athleteDAO.insert(athlete);

        athlete.set_id(id_athlete);

        Monitoring monitoring = new Monitoring();

        monitoring.setDate(new Date());
        monitoring.setAthlete(athlete);

        long id_monitoring = monitoringDAO.insert(monitoring);

        monitoring.set_id(id_monitoring);

        Intent intent = new Intent(NewMonitoringActivity.this, StartMonitoringActivity.class);

        intent.putExtra("athlete", athlete);
        intent.putExtra("monitoring", monitoring);

        startActivity(intent);
    }

    private boolean validateInputs() {
        if (edit_name_athlete.getText().toString().isEmpty()) {
            edit_name_athlete.setError("Campo Obrigatório");
            return false;
        }

        if (edit_weight_athlete.getText().toString().isEmpty()) {
            edit_weight_athlete.setError("Campo Obrigatório");
            return false;
        }

        if (edit_height_athlete.getText().toString().isEmpty()) {
            edit_height_athlete.setError("Campo Obrigatório");
            return false;
        }

        if (edit_category_athlete.getText().toString().isEmpty()) {
            edit_category_athlete.setError("Campo Obrigatório");
            return false;
        }

        return true;
    }

    private void validateBluetoothAble() {
        if (bluetoothAdapter == null) {
            bluetoothAble = false;
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, ENABLE_BLUETOOTH);
            buttonStarMonitoringListener();
        } else {
            bluetoothAble = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_BLUETOOTH && resultCode == Activity.RESULT_OK) {
            bluetoothAble = true;
        } else {
            Toast.makeText(this, "O Bluetooth precisa estar ativado!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NewMonitoringActivity.this, MainActivity.class));
    }
}
