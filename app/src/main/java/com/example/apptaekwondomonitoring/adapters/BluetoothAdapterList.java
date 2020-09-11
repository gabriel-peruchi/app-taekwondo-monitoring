package com.example.apptaekwondomonitoring.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.apptaekwondomonitoring.R;
import com.example.apptaekwondomonitoring.interfaces.AdapterItemClick;

import java.util.List;

public class BluetoothAdapterList extends BaseAdapter {

    private List<BluetoothDevice> bluetoothDeviceList;
    private Activity activity;
    private AdapterItemClick<BluetoothDevice> adapterItemClick;

    private LayoutInflater layoutInflater;

    public BluetoothAdapterList(List<BluetoothDevice> bluetoothDevices, Activity activity, AdapterItemClick<BluetoothDevice> adapterItemClick) {
        this.bluetoothDeviceList = bluetoothDevices;
        this.activity = activity;
        this.adapterItemClick = adapterItemClick;
    }

    @Override
    public int getCount() {
        return bluetoothDeviceList.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return bluetoothDeviceList.get(position);
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
        @SuppressLint("ViewHolder") View row_bluetooth_device = layoutInflater.inflate(R.layout.row_bluetooth_device, parent, false);

        TextView txt_name_bluetooth_device = row_bluetooth_device.findViewById(R.id.txt_name_bluetooth_device);
        TextView txt_address_bluetooth_device = row_bluetooth_device.findViewById(R.id.txt_address_bluetooth_device);

        txt_name_bluetooth_device.setText(bluetoothDeviceList.get(position).getName());
        txt_address_bluetooth_device.setText(bluetoothDeviceList.get(position).getAddress());

        if (adapterItemClick != null) {
            row_bluetooth_device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterItemClick.onClick(bluetoothDeviceList.get(position));
                }
            });
        }

        return row_bluetooth_device;
    }
}
