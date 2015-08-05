package com.byteshatf.callrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class IncomingCallListener extends PhoneStateListener {
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:

                break;
            case TelephonyManager.CALL_STATE_IDLE:

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                break;
        }
    }

    BroadcastReceiver mOutgoingCall = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }
    };
}
