package com.example.apptaekwondomonitoring.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.apptaekwondomonitoring.database.DBOpenHelper;
import com.example.apptaekwondomonitoring.models.Athlete;

import java.util.ArrayList;
import java.util.List;

public class AthleteDAO extends AbstractDAO {

    private static final String TABLE_NAME = "tb_athlete";

    public static final String
            COLUMN_ID = "_id",
            COLUMN_NAME = "name",
            COLUMN_WEIGHT = "weight",
            COLUMN_HEIGHT = "height",
            COLUMN_CATEGORY = "category";

    public static final String
            CREATE_TABLE = "create table " + TABLE_NAME
            + "("
            + COLUMN_ID + " integer not null primary key autoincrement, "
            + COLUMN_NAME + " varchar(100) not null, "
            + COLUMN_WEIGHT + " decimal(10,2) not null, "
            + COLUMN_HEIGHT + " decimal(10,2) not null, "
            + COLUMN_CATEGORY + " varchar(100) not null"
            + ");";

    public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;

    private final String[] colunas = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_WEIGHT,
            COLUMN_HEIGHT,
            COLUMN_CATEGORY,
    };

    public AthleteDAO(Context context) {
        db_open_helper = new DBOpenHelper(context);
    }

    public Long insert(Athlete athlete) {
        long _id = 0;

        try {

            open();

            _id = database.insert(TABLE_NAME, null, getValuesAthlete(athlete));

        } catch (Exception e) {
            System.out.println("DATABASE INSERT ERROR " + e.getMessage());
        } finally {
            close();
        }

        return _id;
    }

    public Athlete selectById(Long _id) {
        List<Athlete> athletes = new ArrayList<>();

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
                athletes.add(cursorToAthlete(cursor));
                cursor.moveToNext();
            }

            cursor.close();

        } catch (Exception e) {
            System.out.println("DATABASE SELECT BY ID ERROR " + e.getMessage());
        } finally {
            close();
        }

        return athletes.get(0);
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

    private ContentValues getValuesAthlete(Athlete athlete) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, athlete.getName());
        contentValues.put(COLUMN_WEIGHT, athlete.getWeight());
        contentValues.put(COLUMN_HEIGHT, athlete.getHeight());
        contentValues.put(COLUMN_CATEGORY, athlete.getCategory());

        return contentValues;
    }

    private Athlete cursorToAthlete(Cursor cursor) {
        Athlete athlete = new Athlete();

        athlete.set_id(cursor.getLong(0));
        athlete.setName(cursor.getString(1));
        athlete.setWeight(cursor.getDouble(2));
        athlete.setHeight(cursor.getDouble(3));
        athlete.setCategory(cursor.getString(4));

        return athlete;
    }
}
