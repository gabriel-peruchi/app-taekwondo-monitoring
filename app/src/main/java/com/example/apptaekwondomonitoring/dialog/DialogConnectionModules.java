package com.example.apptaekwondomonitoring.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.apptaekwondomonitoring.R;
import com.example.apptaekwondomonitoring.services.BluetoothConnection;

import java.io.IOException;

public class DialogConnectionModules {

    private Activity activity;
    private AlertDialog alertDialog;
    private BluetoothConnection connection_module_wearable;
    private BluetoothConnection connection_module_impact;

    private Button button_connected_wearable_module;
    private Button button_connected_impact_module;
    private ImageView img_result_connected_wearable_module;
    private ImageView img_result_connected_impact_module;
    private ProgressBar progress_connected_wearable_module;
    private ProgressBar progress_connected_impact_module;

    public DialogConnectionModules(Activity activity, BluetoothConnection connection_module_wearable, BluetoothConnection connection_module_impact) {
        this.activity = activity;
        this.connection_module_wearable = connection_module_wearable;
        this.connection_module_impact = connection_module_impact;
        construct();
    }

    public void show() {
        alertDialog.show();
    }

    public void close() {
        alertDialog.dismiss();
    }

    private void construct() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this.activity);

        @SuppressLint("InflateParams") View view = this.activity.getLayoutInflater().inflate(
                R.layout.dialog_connection_bluetooth, null
        );

        button_connected_wearable_module = view.findViewById(R.id.btn_connect_module_wearable);
        button_connected_impact_module = view.findViewById(R.id.btn_connect_module_impact);
        img_result_connected_wearable_module = view.findViewById(R.id.img_result_connected_wearable_module);
        img_result_connected_impact_module = view.findViewById(R.id.img_result_connected_impact_module);
        progress_connected_wearable_module = view.findViewById(R.id.progress_connected_wearable_module);
        progress_connected_impact_module = view.findViewById(R.id.progress_connected_impact_module);

        button_connected_impact_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    button_connected_impact_module.setEnabled(false);
                    Thread progressConnection = progressConnection(
                            connection_module_impact,
                            progress_connected_impact_module,
                            img_result_connected_impact_module
                    );
                    progressConnection.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button_connected_wearable_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    button_connected_wearable_module.setEnabled(false);
                    Thread progressConnection = progressConnection(
                            connection_module_wearable,
                            progress_connected_wearable_module,
                            img_result_connected_wearable_module
                    );
                    progressConnection.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        alertBuilder.setView(view);
        alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
    }

    private Thread progressConnection(final BluetoothConnection bluetoothConnection, final ProgressBar progressBar, final ImageView imageView) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    });

                    try {
                        bluetoothConnection.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bluetoothConnection.isConnected()) {
                                imageView.setImageResource(R.drawable.ic_bluetooth_connected_green_64dp);
                            } else {
                                imageView.setImageResource(R.drawable.ic_bluetooth_disconnected_red_64dp);
                            }
                            imageView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            verifyTerminateConnection();
                        }
                    });
                }
            }
        });
    }

    private void verifyTerminateConnection() {
        if (connection_module_impact.isConnected() && connection_module_wearable.isConnected()) {
            close();
            return;
        }

        if (!connection_module_impact.isConnected()) {
            button_connected_impact_module.setEnabled(true);
        }

        if (!connection_module_wearable.isConnected()) {
            button_connected_wearable_module.setEnabled(true);
        }
    }
}
