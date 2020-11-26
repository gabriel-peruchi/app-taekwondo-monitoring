package com.example.apptaekwondomonitoring.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.apptaekwondomonitoring.database.dao.AthleteDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_MonitoringDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_ImpactDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_SpeedDAO;
import com.example.apptaekwondomonitoring.database.dao.Kick_Monitoring_WearableDAO;
import com.example.apptaekwondomonitoring.database.dao.MonitoringDAO;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "banco.db";
    private static final Integer DATABASE_VERSION = 8;

    public DBOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AthleteDAO.CREATE_TABLE);
        db.execSQL(MonitoringDAO.CREATE_TABLE);
        db.execSQL(Kick_MonitoringDAO.CREATE_TABLE);
        db.execSQL(Kick_Monitoring_ImpactDAO.CREATE_TABLE);
        db.execSQL(Kick_Monitoring_WearableDAO.CREATE_TABLE);
        db.execSQL(Kick_Monitoring_SpeedDAO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 7) {
            String commandKickMonitoringImpact = "ALTER TABLE " + Kick_Monitoring_ImpactDAO.TABLE_NAME + " ADD COLUMN resulting decimal(10,2);";
            String commandKickMonitoringWearable = "ALTER TABLE " + Kick_Monitoring_WearableDAO.TABLE_NAME + " ADD COLUMN resulting decimal(10,2);";
            String commandKickMonitoringSpeed = "ALTER TABLE " + Kick_Monitoring_SpeedDAO.TABLE_NAME + " ADD COLUMN resulting decimal(10,2);";

            db.execSQL(commandKickMonitoringImpact);
            db.execSQL(commandKickMonitoringWearable);
            db.execSQL(commandKickMonitoringSpeed);
        }
    }
}
