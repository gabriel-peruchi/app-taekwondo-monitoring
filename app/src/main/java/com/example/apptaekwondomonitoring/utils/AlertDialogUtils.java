package com.example.apptaekwondomonitoring.utils;

import android.content.Context;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class AlertDialogUtils {

    static public SweetAlertDialog createDialog(Context context, Integer alertType, String title, String contentText) {
        return new SweetAlertDialog(context, alertType)
                .setTitleText(title)
                .setContentText(contentText);
    }
}
