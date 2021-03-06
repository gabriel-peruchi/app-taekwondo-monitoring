package com.example.apptaekwondomonitoring.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.apptaekwondomonitoring.database.DBOpenHelper;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring;
import com.example.apptaekwondomonitoring.models.Kick_Monitoring_Speed;

import java.util.ArrayList;
import java.util.List;

public class Kick_Monitoring_SpeedDAO extends AbstractDAO {

    public static final String TABLE_NAME = "tb_kick_monitoring_speed";

    private static final String
            COLUMN_ID = "_id",
            COLUMN_KICK_MONITORING = "kick_monitoring",
            COLUMN_SECONDS = "seconds",
            COLUMN_SPEED_X = "speed_x",
            COLUMN_SPEED_Y = "speed_y",
            COLUMN_SPEED_Z = "speed_z",
            COLUMN_RESULTING = "resulting";

    public static final String
            CREATE_TABLE = "create table " + TABLE_NAME
            + "("
            + COLUMN_ID + " integer not null primary key autoincrement, "
            + COLUMN_KICK_MONITORING + " integer not null, "
            + COLUMN_SECONDS + " decimal(10,2), "
            + COLUMN_SPEED_X + " decimal(10,2), "
            + COLUMN_SPEED_Y + " decimal(10,2), "
            + COLUMN_SPEED_Z + " decimal(10,2), "
            + COLUMN_RESULTING + " decimal(10,2), "
            + "foreign key (" + COLUMN_KICK_MONITORING + ") REFERENCES tb_kick_monitoring ( " + Kick_MonitoringDAO.COLUMN_ID + ")"
            + ");";

    public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

    private final String[] colunas = {
            COLUMN_ID,
            COLUMN_KICK_MONITORING,
            COLUMN_SECONDS,
            COLUMN_SPEED_X,
            COLUMN_SPEED_Y,
            COLUMN_SPEED_Z,
            COLUMN_RESULTING
    };

    private Kick_MonitoringDAO kick_monitoringDAO;

    public Kick_Monitoring_SpeedDAO(Context context) {
        db_open_helper = new DBOpenHelper(context);
        kick_monitoringDAO = new Kick_MonitoringDAO(context);
    }

    public Long insert(Kick_Monitoring_Speed kick_monitoring_speed) {
        long _id = 0;

        try {

            open();

            _id = database.insert(TABLE_NAME, null, getValuesKickMonitoringSpeed(kick_monitoring_speed));

        } catch (Exception e) {
            System.out.println("DATABASE INSERT ERROR " + e.getMessage());
        } finally {
            close();
        }

        return _id;
    }

    public void insertAll(List<Kick_Monitoring_Speed> kick_monitoring_speedList) {
        String values = "";

        for (Kick_Monitoring_Speed kick_monitoring_speed : kick_monitoring_speedList) {
            values = values.concat(
                    "(" + kick_monitoring_speed.getKick_monitoring().get_id() + ", " +
                            kick_monitoring_speed.getSeconds() + ", " +
                            kick_monitoring_speed.getSpeed_x() + ", " +
                            kick_monitoring_speed.getSpeed_y() + ", " +
                            kick_monitoring_speed.getSpeed_z() + ", " +
                            kick_monitoring_speed.getResulting()
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
                + COLUMN_SPEED_X + ", "
                + COLUMN_SPEED_Y + ", "
                + COLUMN_SPEED_Z + ", "
                + COLUMN_RESULTING
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

    public Long update(Kick_Monitoring_Speed kick_monitoring_speed) {

        long linhasAfetadas = 0;

        try {

            open();

            linhasAfetadas = database.update(
                    Kick_Monitoring_SpeedDAO.TABLE_NAME, getValuesKickMonitoringSpeed(kick_monitoring_speed),
                    Kick_Monitoring_SpeedDAO.COLUMN_ID + "= ?",
                    new String[]{String.valueOf(kick_monitoring_speed.get_id())}
            );


        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e.getMessage());
        } finally {
            close();
        }

        return linhasAfetadas;
    }

    public List<Kick_Monitoring_Speed> selectByKickMonitoring(Kick_Monitoring kick_monitoring) {

        List<Kick_Monitoring_Speed> kick_monitoring_speeds = new ArrayList<>();

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
                kick_monitoring_speeds.add(cursorToKickMonitoringSpeed(cursor));
                cursor.moveToNext();
            }

            cursor.close();

        } catch (Exception e) {
            System.out.println("DATABASE SELECT BY KICK MONITORING ERROR " + e.getMessage());
        } finally {
            close();
        }

        return kick_monitoring_speeds;
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

    private ContentValues getValuesKickMonitoringSpeed(Kick_Monitoring_Speed kick_monitoring_speed) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_KICK_MONITORING, kick_monitoring_speed.getKick_monitoring().get_id());
        contentValues.put(COLUMN_SECONDS, kick_monitoring_speed.getSeconds());
        contentValues.put(COLUMN_SPEED_X, kick_monitoring_speed.getSpeed_x());
        contentValues.put(COLUMN_SPEED_Y, kick_monitoring_speed.getSpeed_y());
        contentValues.put(COLUMN_SPEED_Z, kick_monitoring_speed.getSpeed_z());
        contentValues.put(COLUMN_RESULTING, kick_monitoring_speed.getResulting());

        return contentValues;
    }

    private Kick_Monitoring_Speed cursorToKickMonitoringSpeed(Cursor cursor) {
        Kick_Monitoring_Speed kick_monitoring_speed = new Kick_Monitoring_Speed();

        kick_monitoring_speed.set_id(cursor.getLong(0));
        kick_monitoring_speed.setKick_monitoring(kick_monitoringDAO.selectById(cursor.getLong(1)));
        kick_monitoring_speed.setSeconds(cursor.getDouble(2));
        kick_monitoring_speed.setSpeed_x(cursor.getDouble(3));
        kick_monitoring_speed.setSpeed_y(cursor.getDouble(4));
        kick_monitoring_speed.setSpeed_z(cursor.getDouble(5));
        kick_monitoring_speed.setResulting(cursor.getDouble(6));

        return kick_monitoring_speed;
    }
}
