package com.example.apptaekwondomonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(R.string.home_menu);

        Button button_new_monitoring = findViewById(R.id.btn_new_monitoring);
        Button button_list_monitorings = findViewById(R.id.btn_list_monitorings);

        button_new_monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonNewMonitoringListener();
            }
        });

        button_list_monitorings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListMonitoringListener();
            }
        });

    }

    public void buttonNewMonitoringListener() {
        startActivity(new Intent(MainActivity.this, NewMonitoringActivity.class));
    }

    public void buttonListMonitoringListener() {
        startActivity(new Intent(MainActivity.this, ListMonitoringActivity.class));
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
