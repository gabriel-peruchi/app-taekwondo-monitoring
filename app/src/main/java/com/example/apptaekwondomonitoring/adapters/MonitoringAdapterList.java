package com.example.apptaekwondomonitoring.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.apptaekwondomonitoring.R;
import com.example.apptaekwondomonitoring.interfaces.AdapterItemClick;
import com.example.apptaekwondomonitoring.models.Monitoring;
import com.example.apptaekwondomonitoring.utils.DateUtils;

import java.util.List;

public class MonitoringAdapterList extends BaseAdapter {

    private List<Monitoring> monitorings;
    private Activity activity;
    private AdapterItemClick<Monitoring> adapterItemClick;
    private AdapterItemClick<Monitoring> adapterDeleteItemClick;

    private LayoutInflater layoutInflater;

    public MonitoringAdapterList(List<Monitoring> monitorings, Activity activity, AdapterItemClick<Monitoring> adapterItemClick, AdapterItemClick<Monitoring> adapterDeleteItemClick) {
        this.monitorings = monitorings;
        this.activity = activity;
        this.adapterItemClick = adapterItemClick;
        this.adapterDeleteItemClick = adapterDeleteItemClick;
    }

    @Override
    public int getCount() {
        return monitorings.size();
    }

    @Override
    public Monitoring getItem(int position) {
        return monitorings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -999;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        assert layoutInflater != null;
        @SuppressLint("ViewHolder") View row_monitoring = layoutInflater.inflate(R.layout.card_monitoring, parent, false);

        TextView txt_id_monitoring = row_monitoring.findViewById(R.id.txt_id_monitoring);
        TextView txt_athlete_monitoring = row_monitoring.findViewById(R.id.txt_athlete_monitoring);
        TextView txt_data_monitoring = row_monitoring.findViewById(R.id.txt_date_monitoring);
        ImageButton imageButton = row_monitoring.findViewById(R.id.img_btn_delete_monitoring);

        txt_id_monitoring.setText(String.valueOf(monitorings.get(position).get_id()));
        txt_athlete_monitoring.setText(monitorings.get(position).getAthlete().getName());
        txt_data_monitoring.setText(DateUtils.dateFormat(monitorings.get(position).getDate()));

        if (adapterItemClick != null) {
            row_monitoring.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterItemClick.onClick(monitorings.get(position));
                }
            });
        }


        if (adapterDeleteItemClick != null) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterDeleteItemClick.onClick(monitorings.get(position));
                }
            });
        }

        return row_monitoring;
    }
}
