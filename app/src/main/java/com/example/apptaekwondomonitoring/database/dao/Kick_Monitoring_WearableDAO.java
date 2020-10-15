package com.example.apptaekwondomonitoring.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.apptaekwondomonitoring.database.DBOpenHelper;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring_Wearable;

import java.util.ArrayList;
import java.util.List;

public class Kick_Monitoring_WearableDAO extends AbstractDAO {

    private static final String TABLE_NAME = "tb_kick_monitoring_wearable";

    private static final String
            COLUMN_ID = "_id",
            COLUMN_KICK_MONITORING = "kick_monitoring",
            COLUMN_SECONDS = "seconds",
            COLUMN_ACCEL_X = "accel_x",
            COLUMN_ACCEL_Y = "accel_y",
            COLUMN_ACCEL_Z = "accel_z";

    public static final String
            CREATE_TABLE = "create table " + TABLE_NAME
            + "("
            + COLUMN_ID + " integer not null primary key autoincrement, "
            + COLUMN_KICK_MONITORING + " integer not null, "
            + COLUMN_SECONDS + " decimal(10,2), "
            + COLUMN_ACCEL_X + " decimal(10,2), "
            + COLUMN_ACCEL_Y + " decimal(10,2), "
            + COLUMN_ACCEL_Z + " decimal(10,2), "
            + "foreign key (" + COLUMN_KICK_MONITORING + ") REFERENCES tb_kick_monitoring ( " + Kick_MonitoringDAO.COLUMN_ID + ")"
            + ");";

    public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

    private final String[] colunas = {
            COLUMN_ID,
            COLUMN_KICK_MONITORING,
            COLUMN_SECONDS,
            COLUMN_ACCEL_X,
            COLUMN_ACCEL_Y,
            COLUMN_ACCEL_Z
    };

    private Kick_MonitoringDAO kick_monitoringDAO;

    public Kick_Monitoring_WearableDAO(Context context) {
        db_open_helper = new DBOpenHelper(context);
        kick_monitoringDAO = new Kick_MonitoringDAO(context);
    }

    public Long insert(Kick_Monitoring_Wearable kick_monitoring_wearable) {
        long _id = 0;

        try {

            open();

            _id = database.insert(TABLE_NAME, null, getValuesKickMonitoringWearable(kick_monitoring_wearable));

        } catch (Exception e) {
            System.out.println("DATABASE INSERT ERROR " + e.getMessage());
        } finally {
            close();
        }

        return _id;
    }

    public void insertAll(List<Kick_Monitoring_Wearable> kick_monitoring_wearableList) {
        String values = "";

        for (Kick_Monitoring_Wearable kick_monitoring_wearable : kick_monitoring_wearableList) {
            values = values.concat(
                    "(" + kick_monitoring_wearable.getKick_monitoring().get_id() + ", " +
                            kick_monitoring_wearable.getSeconds() + ", " +
                            kick_monitoring_wearable.getAccel_x() + ", " +
                            kick_monitoring_wearable.getAccel_y() + ", " +
                            kick_monitoring_wearable.getAccel_z()
                            + "),"
            );
        }

        values = values.substring(0, values.length() - 1);
        values = values.concat(";");

        String comandSQL = "INSERT INTO "
                + TABLE_NAME
                + " ("
                + COLUMN_KICK_MONITORING + ", "
                + COLUMN_SECONDS + ", "
                + COLUMN_ACCEL_X + ", "
                + COLUMN_ACCEL_Y + ", "
                + COLUMN_ACCEL_Z
                + ") "
                + "VALUES " + values;

        try {
            open();
            database.execSQL(comandSQL);
        } catch (Exception e) {
            System.out.println("DATABASE INSERT ERROR " + e.getMessage());
        } finally {
            close();
        }
    }

    public List<Kick_Monitoring_Wearable> selectByKickMonitoring(Kick_Monitoring kick_monitoring) {

        List<Kick_Monitoring_Wearable> kick_monitoring_wearable_list = new ArrayList<>();

        try {
            open();

            Cursor cursor = database.query(
                    TABLE_NAME,
                    colunas,
                    COLUMN_KICK_MONITORING + " = ? ",
                    new String[]{String.valueOf(kick_monitoring.get_id())},
                    null,
                    null,
                    null
            );

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                kick_monitoring_wearable_list.add(cursorToKickMonitoringWearable(cursor));
                cursor.moveToNext();
            }

            cursor.close();

        } catch (Exception e) {
            System.out.println("DATABASE SELECT BY KICK MONITORING ERROR " + e.getMessage());
        } finally {
            close();
        }

        return kick_monitoring_wearable_list;
    }

    public void deleteByKickMonitoring(Kick_Monitoring kick_monitoring) {
        try {
            open();
            database.delete(
                    TABLE_NAME,
                    COLUMN_KICK_MONITORING + "= ?",
                    new String[]{String.valueOf(kick_monitoring.get_id())}
            );
        } catch (Exception e) {
            System.out.println("DATABASE DELETE ERROR " + e.getMessage());
        } finally {
            close();
        }
    }

    private ContentValues getValuesKickMonitoringWearable(Kick_Monitoring_Wearable kick_monitoring_wearable) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_KICK_MONITORING, kick_monitoring_wearable.getKick_monitoring().get_id());
        contentValues.put(COLUMN_SECONDS, kick_monitoring_wearable.getSeconds());
        contentValues.put(COLUMN_ACCEL_X, kick_monitoring_wearable.getAccel_x());
        contentValues.put(COLUMN_ACCEL_Y, kick_monitoring_wearable.getAccel_y());
        contentValues.put(COLUMN_ACCEL_Z, kick_monitoring_wearable.getAccel_z());

        return contentValues;
    }

    private Kick_Monitoring_Wearable cursorToKickMonitoringWearable(Cursor cursor) {
        Kick_Monitoring_Wearable kick_monitoring_wearable = new Kick_Monitoring_Wearable();

        kick_monitoring_wearable.set_id(cursor.getLong(0));
        kick_monitoring_wearable.setKick_monitoring(kick_monitoringDAO.selectById(cursor.getLong(1)));
        kick_monitoring_wearable.setSeconds(cursor.getDouble(2));
        kick_monitoring_wearable.setAccel_x(cursor.getDouble(3));
        kick_monitoring_wearable.setAccel_y(cursor.getDouble(4));
        kick_monitoring_wearable.setAccel_z(cursor.getDouble(5));

        return kick_monitoring_wearable;
    }
}
