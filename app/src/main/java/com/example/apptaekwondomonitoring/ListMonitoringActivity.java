package com.example.apptaekwondomonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptaekwondomonitoring.adapters.MonitoringAdapterList;
import com.example.apptaekwondomonitoring.database.dao.AthleteDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_MonitoringDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_ImpactDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_WearableDAO;
import com.example.apptaekwondomonitoring.database.dao.MonitoringDAO;
import com.example.apptaekwondomonitoring.interfaces.AdapterItemClick;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring;
import com.example.apptaekwondomonitoring.models.Monitoring;
import com.example.apptaekwondomonitoring.utils.AlertDialogUtils;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.List;

public class ListMonitoringActivity extends AppCompatActivity {

    private AthleteDAO athleteDAO;
    private MonitoringDAO monitoringDAO;
    private Kick_MonitoringDAO kick_monitoringDAO;
    private Kick_Monitoring_ImpactDAO kick_monitoring_impactDAO;
    private Kick_Monitoring_WearableDAO kick_monitoring_wearableDAO;

    private ListView list_view_monitorings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_monitoring);

        getSupportActionBar().setTitle(R.string.list_monitorings);

        list_view_monitorings = findViewById(R.id.list_view_monitorings);

        athleteDAO = new AthleteDAO(ListMonitoringActivity.this);
        monitoringDAO = new MonitoringDAO(ListMonitoringActivity.this);
        kick_monitoringDAO = new Kick_MonitoringDAO(ListMonitoringActivity.this);
        kick_monitoring_impactDAO = new Kick_Monitoring_ImpactDAO(ListMonitoringActivity.this);
        kick_monitoring_wearableDAO = new Kick_Monitoring_WearableDAO(ListMonitoringActivity.this);

        constructAdpter();
    }

    public void deleteMonitoring(Monitoring monitoring) {

        List<Kick_Monitoring> kick_monitoringList = kick_monitoringDAO.selectByMonitoring(monitoring);

        for (Kick_Monitoring kick_monitoring : kick_monitoringList) {
            kick_monitoring_impactDAO.deleteByKickMonitoring(kick_monitoring);
            kick_monitoring_wearableDAO.deleteByKickMonitoring(kick_monitoring);
        }

        kick_monitoringDAO.deleteByMonitoring(monitoring);
        monitoringDAO.deleteById(monitoring.get_id());
        athleteDAO.deleteById(monitoring.getAthlete().get_id());
    }

    public void showAlertConfirmationDelete(final Monitoring monitoring) {
        final SweetAlertDialog alertDialog = AlertDialogUtils.createDialog(
                ListMonitoringActivity.this,
                SweetAlertDialog.WARNING_TYPE,
                "Confirmação",
                "Deseja exluir este monitoramento?"
        );

        alertDialog.setConfirmText("Confirmar");
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                deleteMonitoring(monitoring);
                alertDialog.dismiss();
                constructAdpter();
            }
        });

        alertDialog.setCancelText("Cancelar");
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void constructAdpter() {
        List<Monitoring> monitorings = monitoringDAO.selectAll();

        AdapterItemClick<Monitoring> adapterItemClick = new AdapterItemClick<Monitoring>() {
            @Override
            public void onClick(Monitoring monitoring) {
                Intent intent = new Intent(ListMonitoringActivity.this, ResumeMonitoringActivity.class);
                intent.putExtra("monitoring", monitoring);
                startActivity(intent);
            }
        };

        AdapterItemClick<Monitoring> adapterDeleteItemClick = new AdapterItemClick<Monitoring>() {
            @Override
            public void onClick(Monitoring monitoring) {
                showAlertConfirmationDelete(monitoring);
            }
        };

        MonitoringAdapterList monitoringAdapterList = new MonitoringAdapterList(
                monitorings,
                ListMonitoringActivity.this,
                adapterItemClick,
                adapterDeleteItemClick
        );

        list_view_monitorings.setAdapter(monitoringAdapterList);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ListMonitoringActivity.this, MainActivity.class));
    }
}
