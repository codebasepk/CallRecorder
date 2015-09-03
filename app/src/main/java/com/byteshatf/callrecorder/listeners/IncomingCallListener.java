package com.byteshatf.callrecorder.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.byteshatf.callrecorder.AppGlobals;
import com.byteshatf.callrecorder.CallRecording;
import com.byteshatf.callrecorder.Helpers;
import com.byteshatf.callrecorder.database.DatabaseConstants;
import com.byteshatf.callrecorder.database.DatabaseHelpers;

public class IncomingCallListener extends PhoneStateListener {

    private CallRecording callRecording = new CallRecording();
    private SharedPreferences mSharedPreferences;
    private Helpers mHelpers;

    public IncomingCallListener() {
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                if (incomingNumber != null) {
                    AppGlobals.sCurrentNumber = incomingNumber;
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (CallRecording.isRecording) {
                    callRecording.stopRecording();
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                    startRecording(AppGlobals.sCurrentNumber);
                break;
        }
    }

    public BroadcastReceiver mOutgoingCall = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AppGlobals.sCurrentNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            switch (getSpinnerValue()) {
                case 1:
                case 2:
                    if (!CallRecording.isRecording) {
                        callRecording.startRecord();
                    }
                    break;
                case 4:
                    if (checkIfCurrentNumberExistInDatabase(AppGlobals.sCurrentNumber)[0].equals("true")) {
                        String values = checkIfCurrentNumberExistInDatabase(AppGlobals.sCurrentNumber)[1];
                        if (mHelpers.getSwitchState((AppGlobals.sSwitchState+values.trim()))) {
                            int spinnerValue = mHelpers.getValuesFromSharedPreferences(values, 0);
                            switch (spinnerValue) {
                                case 1:
                                    if (!CallRecording.isRecording) {
                                        callRecording.startRecord();
                                    }
                                    break;
                            }
                        }
                    }
                    break;
            }

        }
    };

    private void startRecording(String number) {
        switch (getSpinnerValue()) {
            case 0:
                if (!CallRecording.isRecording) {
                    callRecording.startRecord();
                }
                break;
            case 2:
                if (!CallRecording.isRecording) {
                    callRecording.startRecord();
                }
                break;
            case 3:
                if (!mHelpers.contactExists(AppGlobals.sCurrentNumber)) {
                    if (!CallRecording.isRecording) {
                        callRecording.startRecord();
                    }
                }


                break;
            case 4:
                if (checkIfCurrentNumberExistInDatabase(number)[0].equals("true")) {
                    String values = checkIfCurrentNumberExistInDatabase(number)[1];
                    if (mHelpers.getSwitchState((AppGlobals.sSwitchState + values))) {
                        int spinnerValue = mHelpers.getValuesFromSharedPreferences(values, 0);
                       switch (spinnerValue) {
                           case 0:
                               if (!CallRecording.isRecording) {
                                   callRecording.startRecord();
                               }
                               break;
                           case 2:
                               if (!CallRecording.isRecording) {
                                   callRecording.startRecord();
                               }
                               break;
                       }
                    }
                }
                break;
        }
    }

    private int getSpinnerValue() {
        mHelpers = new Helpers(AppGlobals.getContext());
        mSharedPreferences = mHelpers.getPreferenceManager();
        return mSharedPreferences.getInt(AppGlobals.MAIN_SPINNER_KEY, 0);

    }

    public String[] checkIfCurrentNumberExistInDatabase(String number) {
        DatabaseHelpers databaseHelpers = new DatabaseHelpers(AppGlobals.getContext());
        String[] finalResult = new String[2];
        SQLiteDatabase sqLiteDatabase = databaseHelpers.getReadableDatabase();
        String query="SELECT * FROM "+ DatabaseConstants.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        while (cursor.moveToNext()) {
            String allContacts = cursor.getString(cursor.getColumnIndex(DatabaseConstants.CONTACTS));
            String[] contacts = allContacts.split(",");
            for (String item: contacts) {
                System.out.println("OK");
                Log.i("this", number);
                if (PhoneNumberUtils.compare(item, number)) {
                    System.out.println("ContactMatch");
                    finalResult[0] = "true";
                    finalResult[1] = cursor.getString(cursor.getColumnIndex(DatabaseConstants.TITLE));
                    return finalResult;
                }
            }
        }
        sqLiteDatabase.close();
        finalResult[0] = "false";
        return finalResult;
    }


}