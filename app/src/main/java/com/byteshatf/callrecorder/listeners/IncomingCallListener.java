package com.byteshatf.callrecorder.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.byteshatf.callrecorder.AppGlobals;
import com.byteshatf.callrecorder.CallRecording;
import com.byteshatf.callrecorder.Helpers;
import com.byteshatf.callrecorder.database.DatabaseHelpers;

public class IncomingCallListener extends PhoneStateListener {

    private CallRecording callRecording = new CallRecording();
    private SharedPreferences mSharedPreferences;
    private Helpers mHelpers;
    private Context mContext;
    public static String sCurrentNumber;

    public IncomingCallListener(Context context) {
        mContext = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        sCurrentNumber = incomingNumber;
        Log.i(AppGlobals.getLogTag(getClass()), sCurrentNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (CallRecording.isRecording) {
                    callRecording.stopRecording();
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                startRecording();
                break;
        }
    }

    public BroadcastReceiver mOutgoingCall = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            switch (getSpinnerValue()) {
                case 1:
                case 2:
                    if (!CallRecording.isRecording) {
                        callRecording.startRecord();
                    }
                    break;
                case 4:
                    DatabaseHelpers databaseHelpers = new DatabaseHelpers(mContext);
                    if (databaseHelpers.checkIfCurrentNumberExistInDatabase()[0].equals("true")) {
                        String values = databaseHelpers.checkIfCurrentNumberExistInDatabase()[1];
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

    private void startRecording() {
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


                break;
            case 4:
                DatabaseHelpers databaseHelpers = new DatabaseHelpers(mContext);
                if (databaseHelpers.checkIfCurrentNumberExistInDatabase()[0].equals("true")) {
                    String values = databaseHelpers.checkIfCurrentNumberExistInDatabase()[1];
                    if (mHelpers.getSwitchState((AppGlobals.sSwitchState+values))) {
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
        mHelpers = new Helpers(mContext);
        mSharedPreferences = mHelpers.getPreferenceManager();
        return mSharedPreferences.getInt(AppGlobals.MAIN_SPINNER_KEY, 0);

    }


}