package com.byteshatf.callrecorder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.byteshatf.callrecorder.AppGlobals;
import com.byteshatf.callrecorder.Helpers;

import java.util.ArrayList;


public class DatabaseHelpers extends SQLiteOpenHelper {

    public DatabaseHelpers(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstants.TABLE_CREATE);
        Log.i(AppGlobals.getLogTag(getClass()), "Database created !!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DatabaseConstants.TABLE_NAME);
        onCreate(db);
    }

    public void createNewEntry(String title, String state, String contacts) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.TITLE, title);
        values.put(DatabaseConstants.STATE, state);
        values.put(DatabaseConstants.CONTACTS, contacts);
        values.put(DatabaseConstants.DATE_COLUMN, Helpers.getTimeStampForDatabase());
        sqLiteDatabase.insert(DatabaseConstants.TABLE_NAME, null, values);
        Log.i(AppGlobals.getLogTag(getClass()), "created New Entry");
        sqLiteDatabase.close();
    }

    public ArrayList<String> getAllPresentNotes() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME + " ORDER BY " +
                DatabaseConstants.DATE_COLUMN + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        ArrayList<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemname = cursor.getString(cursor.getColumnIndex(
                    DatabaseConstants.TITLE));
            if (itemname != null) {
                System.out.println(itemname);
                arrayList.add(itemname);
            }
        }
        sqLiteDatabase.close();
        return arrayList;
    }

}
