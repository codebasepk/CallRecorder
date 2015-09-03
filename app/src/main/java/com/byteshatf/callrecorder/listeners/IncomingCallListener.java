package com.byteshatf.callrecorder.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.byteshatf.callrecorder.CallRecording;

public class IncomingCallListener extends PhoneStateListener {

    CallRecording callRecording = new CallRecording();

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                if (CallRecording.isRecording) {
                    callRecording.stopRecording();
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (!CallRecording.isRecording) {
                    callRecording.startRecord();
                }
                break;
        }
    }

    public BroadcastReceiver mOutgoingCall = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            System.out.println(number);
        }
    };
}