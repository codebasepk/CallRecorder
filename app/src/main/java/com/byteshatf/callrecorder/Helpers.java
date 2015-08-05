package com.byteshatf.callrecorder;


import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class Helpers extends ContextWrapper {

    public Helpers(Context base) {
        super(base);
    }

    public static Cursor getAllContacts(ContentResolver cr) {
        return cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );
    }

    public static List<String> getAllContactNames() {
        List<String> contactNames = new ArrayList<>();
        Cursor cursor = getAllContacts(AppGlobals.getContext().getContentResolver());
        while (cursor.moveToNext()) {
            String name = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactNames.add(name);
        }
        cursor.close();
        return contactNames;
    }

    public static List<String> getAllContactNumbers() {
        List<String> contactNumbers = new ArrayList<>();
        Cursor cursor = getAllContacts(AppGlobals.getContext().getContentResolver());
        while (cursor.moveToNext()) {
            String number = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactNumbers.add(number);
        }
        cursor.close();
        return contactNumbers;
    }
}
