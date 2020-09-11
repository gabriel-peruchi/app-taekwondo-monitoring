package com.example.apptaekwondomonitoring.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.apptaekwondomonitoring.database.DBOpenHelper;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring;
import com.example.apptaekwondomonitoring.models.Monitoring;

import java.util.ArrayList;
import java.util.List;

public class Kick_MonitoringDAO extends AbstractDAO {

    private static final String TABLE_NAME = "tb_kick_monitoring";

    public static final String
            COLUMN_ID = "_id",
            COLUMN_MONITORING = "monitoring",
            COLUMN_MAX_IMPACT_X = "max_impact_x",
            COLUMN_MAX_IMPACT_Y = "max_impact_y",
            COLUMN_MAX_IMPACT_Z = "max_impact_Z",
            COLUMN_MAX_ACCEL_KICK_X = "max_accel_kick_x",
            COLUMN_MAX_ACCEL_KICK_Y = "max_accel_kick_y",
            COLUMN_MAX_ACCEL_KICK_Z = "max_accel_kick_z",
            COLUMN_MAX_VELOCITY_KICK_X = "max_velocity_kick_x",
            COLUMN_MAX_VELOCITY_KICK_Y = "max_velocity_kick_y",
            COLUMN_MAX_VELOCITY_KICK_Z = "max_velocity_kick_z";

    public static final String
            CREATE_TABLE = "create table " + TABLE_NAME
            + "("
            + COLUMN_ID + " integer not null primary key autoincrement, "
            + COLUMN_MONITORING + " integer not null, "
            + COLUMN_MAX_IMPACT_X + " decimal(10,2), "
            + COLUMN_MAX_IMPACT_Y + " decimal(10,2), "
            + COLUMN_MAX_IMPACT_Z + " decimal(10,2), "
            + COLUMN_MAX_ACCEL_KICK_X + " decimal(10,2), "
            + COLUMN_MAX_ACCEL_KICK_Y + " decimal(10,2), "
            + COLUMN_MAX_ACCEL_KICK_Z + " decimal(10,2), "
            + COLUMN_MAX_VELOCITY_KICK_X + " decimal(10,2), "
            + COLUMN_MAX_VELOCITY_KICK_Y + " decimal(10,2), "
            + COLUMN_MAX_VELOCITY_KICK_Z + " decimal(10,2), "
            + "foreign key (" + COLUMN_MONITORING + ") REFERENCES tb_monitoring ( " + MonitoringDAO.COLUMN_ID + ")"
            + ");";

    public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

    private final String[] colunas = {
            COLUMN_ID,
            COLUMN_MONITORING,
            COLUMN_MAX_IMPACT_X,
            COLUMN_MAX_IMPACT_Y,
            COLUMN_MAX_IMPACT_Z,
            COLUMN_MAX_ACCEL_KICK_X,
            COLUMN_MAX_ACCEL_KICK_Y,
            COLUMN_MAX_ACCEL_KICK_Z,
            COLUMN_MAX_VELOCITY_KICK_X,
            COLUMN_MAX_VELOCITY_KICK_Y,
            COLUMN_MAX_VELOCITY_KICK_Z
    };

    private MonitoringDAO monitoringDAO;

    public Kick_MonitoringDAO(Context context) {
        db_open_helper = new DBOpenHelper(context);
        monitoringDAO = new MonitoringDAO(context);
    }

    public Long insert(Kick_Monitoring kick_monitoring) {
        long _id = 0;

        try {

            open();

            _id = database.insert(TABLE_NAME, null, getValuesKickMonitoring(kick_monitoring));

        } catch (Exception e) {
            System.out.println("DATABASE INSERT ERROR " + e.getMessage());
        } finally {
            close();
        }

        return _id;
    }

    public List<Kick_Monitoring> selectAll() {

        List<Kick_Monitoring> kick_monitorings = new ArrayList<>();

        try {
            open();

            Cursor cursor = database.query(
                    TABLE_NAME,
                    colunas,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                kick_monitorings.add(cursorToKickMonitoring(cursor));
                cursor.moveToNext();
            }

            cursor.close();

        } catch (Exception e) {
            System.out.println("DATABASE SELECT ALL ERROR " + e.getMessage());
        } finally {
            close();
        }

        return kick_monitorings;
    }

    public Kick_Monitoring selectById(Long _id) {
        List<Kick_Monitoring> kick_monitorings = new ArrayList<>();

        try {
            open();

            Cursor cursor = database.query(
                    TABLE_NAME, colunas,
                    COLUMN_ID + " = ? ",
                    new String[]{String.valueOf(_id)},
                    null,
                    null,
                    null
            );

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                kick_monitorings.add(cursorToKickMonitoring(cursor));
                cursor.moveToNext();
            }

            cursor.close();

        } catch (Exception e) {
            System.out.println("DATABASE SELECT BY ID ERROR " + e.getMessage());
        } finally {
            close();
        }

        return kick_monitorings.get(0);
    }

    public List<Kick_Monitoring> selectByMonitoring(Monitoring monitoring) {
        List<Kick_Monitoring> kick_monitorings = new ArrayList<>();

        try {
            open();

            Cursor cursor = database.query(
                    TABLE_NAME, colunas,
                    COLUMN_MONITORING + " = ? ",
                    new String[]{String.valueOf(monitoring.get_id())},
                    null,
                    null,
                    null
            );

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                kick_monitorings.add(cursorToKickMonitoring(cursor));
                cursor.moveToNext();
            }

            cursor.close();

        } catch (Exception e) {
            System.out.println("DATABASE SELECT BY ID ERROR " + e.getMessage());
        } finally {
            close();
        }

        return kick_monitorings;
    }

    public void deleteByMonitoring(Monitoring monitoring) {
        try {
            open();
            database.delete(
                    TABLE_NAME,
                    COLUMN_MONITORING + "= ?",
                    new String[]{String.valueOf(monitoring.get_id())}
            );
        } catch (Exception e) {
            System.out.println("DATABASE DELETE ERROR " + e.getMessage());
        } finally {
            close();
        }
    }

    private ContentValues getValuesKickMonitoring(Kick_Monitoring kick_monitoring) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_MONITORING, kick_monitoring.getMonitoring().get_id());
        contentValues.put(COLUMN_MAX_IMPACT_X, kick_monitoring.getMax_impact_x());
        contentValues.put(COLUMN_MAX_IMPACT_Y, kick_monitoring.getMax_impact_y());
        contentValues.put(COLUMN_MAX_IMPACT_Z, kick_monitoring.getMax_impact_z());
        contentValues.put(COLUMN_MAX_ACCEL_KICK_X, kick_monitoring.getMax_accel_kick_x());
        contentValues.put(COLUMN_MAX_ACCEL_KICK_Y, kick_monitoring.getMax_accel_kick_y());
        contentValues.put(COLUMN_MAX_ACCEL_KICK_Z, kick_monitoring.getMax_accel_kick_z());
        contentValues.put(COLUMN_MAX_VELOCITY_KICK_X, kick_monitoring.getMax_velocity_kick_x());
        contentValues.put(COLUMN_MAX_VELOCITY_KICK_Y, kick_monitoring.getMax_velocity_kick_y());
        contentValues.put(COLUMN_MAX_VELOCITY_KICK_Z, kick_monitoring.getMax_velocity_kick_z());

        return contentValues;
    }

    private Kick_Monitoring cursorToKickMonitoring(Cursor cursor) {
        Kick_Monitoring kick_monitoring = new Kick_Monitoring();

        kick_monitoring.set_id(cursor.getLong(0));
        kick_monitoring.setMonitoring(monitoringDAO.selectById(cursor.getLong(1)));
        kick_monitoring.setMax_impact_x(cursor.getDouble(2));
        kick_monitoring.setMax_impact_y(cursor.getDouble(3));
        kick_monitoring.setMax_impact_z(cursor.getDouble(4));
        kick_monitoring.setMax_accel_kick_x(cursor.getDouble(5));
        kick_monitoring.setMax_accel_kick_y(cursor.getDouble(6));
        kick_monitoring.setMax_accel_kick_z(cursor.getDouble(7));
        kick_monitoring.setMax_velocity_kick_x(cursor.getDouble(8));
        kick_monitoring.setMax_velocity_kick_y(cursor.getDouble(9));
        kick_monitoring.setMax_velocity_kick_z(cursor.getDouble(10));

        return kick_monitoring;
    }
}
