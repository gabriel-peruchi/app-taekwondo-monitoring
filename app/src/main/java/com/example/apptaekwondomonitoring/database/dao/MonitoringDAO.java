package com.example.apptaekwondomonitoring.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.apptaekwondomonitoring.database.DBOpenHelper;
import com.example.apptaekwondomonitoring.models.Monitoring;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonitoringDAO extends AbstractDAO {

    private static final String TABLE_NAME = "tb_monitoring";

    public static final String
            COLUMN_ID = "_id",
            COLUMN_DATE = "date",
            COLUMN_ATHLETE = "athlete";

    public static final String
            CREATE_TABLE = "create table " + TABLE_NAME
            + "("
            + COLUMN_ID + " integer not null primary key autoincrement, "
            + COLUMN_DATE + " integer not null, "
            + COLUMN_ATHLETE + " integer not null, "
            + "foreign key (" + COLUMN_ATHLETE + ") REFERENCES tb_athlete ( " + AthleteDAO.COLUMN_ID + ")"
            + ");";

    public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

    private final String[] colunas = {
            COLUMN_ID,
            COLUMN_DATE,
            COLUMN_ATHLETE,
    };

    private AthleteDAO athleteDAO;

    public MonitoringDAO(Context context) {
        db_open_helper = new DBOpenHelper(context);
        athleteDAO = new AthleteDAO(context);
    }

    public Long insert(Monitoring monitoring) {
        long _id = 0;

        try {

            open();

            _id = database.insert(TABLE_NAME, null, getValuesMonitoring(monitoring));

        } catch (Exception e) {
            System.out.println("DATABASE INSERT ERROR " + e.getMessage());
        } finally {
            close();
        }

        return _id;
    }

    public List<Monitoring> selectAll() {

        List<Monitoring> monitorings = new ArrayList<>();

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
                monitorings.add(cursorToMonitoring(cursor));
                cursor.moveToNext();
            }

            cursor.close();

        } catch (Exception e) {
            System.out.println("DATABASE SELECT ALL ERROR " + e.getMessage());
        } finally {
            close();
        }

        return monitorings;
    }

    public Monitoring selectById(Long _id) {
        List<Monitoring> monitorings = new ArrayList<>();

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
                monitorings.add(cursorToMonitoring(cursor));
                cursor.moveToNext();
            }

            cursor.close();

        } catch (Exception e) {
            System.out.println("DATABASE SELECT BY ID ERROR " + e.getMessage());
        } finally {
            close();
        }

        return monitorings.get(0);
    }

    public void deleteById(Long _id) {
        try {
            open();
            database.delete(
                    TABLE_NAME,
                    COLUMN_ID + "= ?",
                    new String[]{String.valueOf(_id)}
            );
        } catch (Exception e) {
            System.out.println("DATABASE DELETE ERROR " + e.getMessage());
        } finally {
            close();
        }
    }

    private ContentValues getValuesMonitoring(Monitoring monitoring) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_DATE, monitoring.getDate().getTime());
        contentValues.put(COLUMN_ATHLETE, monitoring.getAthlete().get_id());

        return contentValues;
    }

    private Monitoring cursorToMonitoring(Cursor cursor) {
        Monitoring monitoring = new Monitoring();

        monitoring.set_id(cursor.getLong(0));
        monitoring.setDate(new Date(cursor.getLong(1)));
        monitoring.setAthlete(athleteDAO.selectById(cursor.getLong(2)));

        return monitoring;
    }
}
