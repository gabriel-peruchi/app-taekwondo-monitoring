package com.example.apptaekwondomonitoring.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.apptaekwondomonitoring.R;
import com.example.apptaekwondomonitoring.interfaces.AdapterItemClick;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring;

import java.util.List;

public class KickMonitoringAdapterList extends BaseAdapter {

    private List<Kick_Monitoring> kick_monitorings;
    private Activity activity;
    private AdapterItemClick<Kick_Monitoring> adapterItemClick;

    private LayoutInflater layoutInflater;

    public KickMonitoringAdapterList(List<Kick_Monitoring> kick_monitorings, Activity activity, AdapterItemClick<Kick_Monitoring> adapterItemClick) {
        this.kick_monitorings = kick_monitorings;
        this.activity = activity;
        this.adapterItemClick = adapterItemClick;
    }

    @Override
    public int getCount() {
        return kick_monitorings.size();
    }

    @Override
    public Kick_Monitoring getItem(int position) {
        return kick_monitorings.get(position);
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
        @SuppressLint("ViewHolder") View row_kick_monitoring = layoutInflater.inflate(
                R.layout.row_kick_monitoring, parent, false
        );

        TextView txt_id_monitoring = row_kick_monitoring.findViewById(R.id.txt_desc_kick_monitoring);

        String name_kick = "Chute " + (position + 1); //kick_monitorings.get(position).get_id();

        txt_id_monitoring.setText(name_kick);

        if (adapterItemClick != null) {
            row_kick_monitoring.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterItemClick.onClick(kick_monitorings.get(position));
                }
            });
        }

        return row_kick_monitoring;
    }
}
