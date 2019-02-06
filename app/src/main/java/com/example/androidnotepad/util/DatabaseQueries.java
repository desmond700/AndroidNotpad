package com.example.androidnotepad.util;

import android.provider.BaseColumns;

public class DatabaseQueries implements BaseColumns {

    public static final String TABLE_NAME = "notepad";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_WRITTEN_DATE = "date";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_NOTE + " TEXT, " +
            COLUMN_WRITTEN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP );";


}
