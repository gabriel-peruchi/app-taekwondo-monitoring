package com.example.apptaekwondomonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptaekwondomonitoring.adapters.KickMonitoringAdapterList;
import com.example.apptaekwondomonitoring.database.dao.Kick_MonitoringDAO;
import com.example.apptaekwondomonitoring.interfaces.AdapterItemClick;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring;
import com.example.apptaekwondomonitoring.models.Monitoring;
import com.example.apptaekwondomonitoring.utils.DateUtils;
import com.example.apptaekwondomonitoring.utils.NumberUtils;

import java.util.List;

public class ResumeMonitoringActivity extends AppCompatActivity {

    private ListView list_view_kick_monitorings;
    private TextView txt_monitoring_code;
    private TextView txt_monitoring_date;
    private TextView txt_monitoring_name_athlete;
    private TextView txt_monitoring_category_athlete;
    private TextView txt_monitoring_weight_athlete;
    private TextView txt_monitoring_height_athlete;
    private TextView txt_monitoring_num_kicks;
    private TextView txt_max_value_impact_x;
    private TextView txt_max_value_impact_y;
    private TextView txt_max_value_impact_z;
    private TextView txt_max_value_accel_x;
    private TextView txt_max_value_accel_y;
    private TextView txt_max_value_accel_z;
    private TextView txt_max_value_velocity_x;
    private TextView txt_max_value_velocity_y;
    private TextView txt_max_value_velocity_z;

    private Kick_MonitoringDAO kick_monitoringDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_monitoring);

        getSupportActionBar().setTitle(R.string.resume_monitoring);

        list_view_kick_monitorings = findViewById(R.id.list_view_kick_monitorings);
        txt_monitoring_code = findViewById(R.id.txt_monitoring_code);
        txt_monitoring_date = findViewById(R.id.txt_monitoring_date);
        txt_monitoring_name_athlete = findViewById(R.id.txt_monitoring_name_athlete);
        txt_monitoring_category_athlete = findViewById(R.id.txt_monitoring_category_athlete);
        txt_monitoring_weight_athlete = findViewById(R.id.txt_monitoring_weight_athlete);
        txt_monitoring_height_athlete = findViewById(R.id.txt_monitoring_height_athlete);
        txt_monitoring_num_kicks = findViewById(R.id.txt_monitoring_num_kicks);
        txt_max_value_impact_x = findViewById(R.id.txt_resume_monitoring_max_value_impact_x);
        txt_max_value_impact_y = findViewById(R.id.txt_resume_monitoring_max_value_impact_y);
        txt_max_value_impact_z = findViewById(R.id.txt_resume_monitoring_max_value_impact_z);
        txt_max_value_accel_x = findViewById(R.id.txt_resume_monitoring_max_value_accel_x);
        txt_max_value_accel_y = findViewById(R.id.txt_resume_monitoring_max_value_accel_y);
        txt_max_value_accel_z = findViewById(R.id.txt_resume_monitoring_max_value_accel_z);
        txt_max_value_velocity_x = findViewById(R.id.txt_resume_monitoring_max_value_velocity_x);
        txt_max_value_velocity_y = findViewById(R.id.txt_resume_monitoring_max_value_velocity_y);
        txt_max_value_velocity_z = findViewById(R.id.txt_resume_monitoring_max_value_velocity_z);

        kick_monitoringDAO = new Kick_MonitoringDAO(ResumeMonitoringActivity.this);

        Monitoring monitoring = (Monitoring) getIntent().getSerializableExtra("monitoring");

        setInfoComponents(monitoring);
    }

    public void setInfoComponents(Monitoring monitoring) {

        List<Kick_Monitoring> kick_monitorings = kick_monitoringDAO.selectByMonitoring(monitoring);

        double max_impact_x = 0;
        double max_impact_y = 0;
        double max_impact_z = 0;
        double max_accel_kick_x = 0;
        double max_accel_kick_y = 0;
        double max_accel_kick_z = 0;
        double max_velocity_kick_x = 0;
        double max_velocity_kick_y = 0;
        double max_velocity_kick_z = 0;

        for (Kick_Monitoring kick_monitoring : kick_monitorings) {

            if (kick_monitoring.getMax_impact_x() > max_impact_x) {
                max_impact_x = kick_monitoring.getMax_impact_x();
            }

            if (kick_monitoring.getMax_impact_y() > max_impact_y) {
                max_impact_y = kick_monitoring.getMax_impact_y();
            }

            if (kick_monitoring.getMax_impact_z() > max_impact_z) {
                max_impact_z = kick_monitoring.getMax_impact_z();
            }

            if (kick_monitoring.getMax_accel_kick_x() > max_accel_kick_x) {
                max_accel_kick_x = kick_monitoring.getMax_accel_kick_x();
            }

            if (kick_monitoring.getMax_accel_kick_y() > max_accel_kick_y) {
                max_accel_kick_y = kick_monitoring.getMax_accel_kick_y();
            }

            if (kick_monitoring.getMax_accel_kick_z() > max_accel_kick_z) {
                max_accel_kick_z = kick_monitoring.getMax_accel_kick_z();
            }

            if (kick_monitoring.getMax_velocity_kick_x() > max_velocity_kick_x) {
                max_velocity_kick_x = kick_monitoring.getMax_velocity_kick_x();
            }

            if (kick_monitoring.getMax_velocity_kick_y() > max_velocity_kick_y) {
                max_velocity_kick_y = kick_monitoring.getMax_velocity_kick_y();
            }

            if (kick_monitoring.getMax_velocity_kick_z() > max_velocity_kick_z) {
                max_velocity_kick_z = kick_monitoring.getMax_velocity_kick_z();
            }
        }

        max_impact_x = NumberUtils.toFixedTwo(max_impact_x);
        max_impact_y = NumberUtils.toFixedTwo(max_impact_y);
        max_impact_z = NumberUtils.toFixedTwo(max_impact_z);
        max_accel_kick_x = NumberUtils.toFixedTwo(max_accel_kick_x);
        max_accel_kick_y = NumberUtils.toFixedTwo(max_accel_kick_y);
        max_accel_kick_z = NumberUtils.toFixedTwo(max_accel_kick_z);
        max_velocity_kick_x = NumberUtils.toFixedTwo(max_velocity_kick_x);
        max_velocity_kick_y = NumberUtils.toFixedTwo(max_velocity_kick_y);
        max_velocity_kick_z = NumberUtils.toFixedTwo(max_velocity_kick_z);

        txt_monitoring_code.setText(String.valueOf(monitoring.get_id()));
        txt_monitoring_date.setText(DateUtils.dateFormat(monitoring.getDate()));
        txt_monitoring_name_athlete.setText(monitoring.getAthlete().getName());
        txt_monitoring_category_athlete.setText(monitoring.getAthlete().getCategory());
        txt_monitoring_weight_athlete.setText(String.valueOf(monitoring.getAthlete().getWeight()));
        txt_monitoring_height_athlete.setText(String.valueOf(monitoring.getAthlete().getHeight()));
        txt_monitoring_num_kicks.setText(String.valueOf(kick_monitorings.size()));
        txt_max_value_impact_x.setText(String.valueOf(max_impact_x));
        txt_max_value_impact_y.setText(String.valueOf(max_impact_y));
        txt_max_value_impact_z.setText(String.valueOf(max_impact_z));
        txt_max_value_accel_x.setText(String.valueOf(max_accel_kick_x));
        txt_max_value_accel_y.setText(String.valueOf(max_accel_kick_y));
        txt_max_value_accel_z.setText(String.valueOf(max_accel_kick_z));
        txt_max_value_velocity_x.setText(String.valueOf(max_velocity_kick_x));
        txt_max_value_velocity_y.setText(String.valueOf(max_velocity_kick_y));
        txt_max_value_velocity_z.setText(String.valueOf(max_velocity_kick_z));


        KickMonitoringAdapterList kickMonitoringAdapterList = new KickMonitoringAdapterList(
                kick_monitorings,
                ResumeMonitoringActivity.this,
                new AdapterItemClick<Kick_Monitoring>() {
                    @Override
                    public void onClick(Kick_Monitoring kick_monitoring) {
                        Intent intent = new Intent(ResumeMonitoringActivity.this, KickMonitoringActivity.class);
                        intent.putExtra("kick_monitoring", kick_monitoring);
                        startActivity(intent);
                    }
                }
        );

        list_view_kick_monitorings.setAdapter(kickMonitoringAdapterList);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResumeMonitoringActivity.this, ListMonitoringActivity.class));
    }
}
