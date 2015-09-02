package com.byteshatf.callrecorder.database;


public class DatabaseConstants {

    public static final String DATABASE_NAME = "Call_recording_database.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "call_recording";
    public static final String TITLE = "title";
    public static final String STATE = "state";
    public static final String CONTACTS = "contacts";
    public static final String ID_COLUMN = "ID";
    public static final String DATE_COLUMN = "DATETIME";

    public static final String TABLE_CREATE =
            "CREATE TABLE " +
                    TABLE_NAME + "(" +
                    ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " TEXT UNIQUE , " +
                    STATE + " TEXT , " +
                    CONTACTS + " TEXT, " +
                    DATE_COLUMN + " TEXT" + " ) ";
}
