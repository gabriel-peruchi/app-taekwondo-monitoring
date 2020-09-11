package com.example.apptaekwondomonitoring.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.apptaekwondomonitoring.R;

public class DialogSelector<T> {

    private Activity activity;
    private AlertDialog alertDialog;
    private BaseAdapter adapter;

    public DialogSelector(Activity activity, BaseAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void show() {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this.activity);

        View view = this.activity.getLayoutInflater().inflate(R.layout.dialog_selector_bluetooth, null);

        final ListView listView_itens = view.findViewById(R.id.list_selector_bluetooth);

//        listView_itens.setTextFilterEnabled(true);

        listView_itens.setAdapter(adapter);

        alertBuilder.setView(view);

        alertDialog = alertBuilder.create();

        alertDialog.show();
    }

    public void close() {
        alertDialog.dismiss();
    }
}
