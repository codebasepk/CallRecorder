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
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
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

    public void createNewEntry(String title, String contacts) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.TITLE, title);
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

    public String[] retrieveNoteDetails(String value) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String whereClause = DatabaseConstants.TITLE + " = ?";
        String[] whereArgs = new String[]{value};
        Cursor cursor = sqLiteDatabase.query(DatabaseConstants.TABLE_NAME, null, whereClause, whereArgs,
                null, null, null);
        String[] list = new String[3];
        while (cursor.moveToNext()) {
            list[0] = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.ID_COLUMN)));
            list[1] = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.CONTACTS)));
            Log.i(AppGlobals.getLogTag(getClass()), " Data retrieved ....");
        }
        sqLiteDatabase.close();
        return list;
    }

    public void updateCategory(String id, String title, String contacts) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.TITLE, title);
        values.put(DatabaseConstants.CONTACTS, contacts);
        values.put(DatabaseConstants.DATE_COLUMN, Helpers.getTimeStampForDatabase());
        sqLiteDatabase.update(DatabaseConstants.TABLE_NAME, values, DatabaseConstants.ID_COLUMN + "=" + id, null);
        Log.i(AppGlobals.getLogTag(getClass()), "Updated.......");
        sqLiteDatabase.close();
    }

    public void deleteCategory(String value) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(DatabaseConstants.TABLE_NAME, DatabaseConstants.TITLE +
                "=?", new String[] { value });
        sqLiteDatabase.close();
    }
}
