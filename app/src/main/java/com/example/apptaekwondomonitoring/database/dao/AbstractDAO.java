package com.example.apptaekwondomonitoring.database.dao;

import android.database.sqlite.SQLiteDatabase;

import com.example.apptaekwondomonitoring.database.DBOpenHelper;

public class AbstractDAO {

    protected DBOpenHelper db_open_helper;
    protected SQLiteDatabase database;

    protected final void open() {
        database = db_open_helper.getWritableDatabase();
        System.out.println("database " + database);
    }

    protected final void close() {
        db_open_helper.close();
    }

}
