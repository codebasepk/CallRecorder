package com.byteshatf.callrecorder.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.byteshatf.callrecorder.AppGlobals;
import com.byteshatf.callrecorder.Helpers;
import com.byteshatf.callrecorder.listeners.IncomingCallListener;


public class CallRecordingService extends Service {

    private IncomingCallListener mIncomingCallListener;
    private TelephonyManager mTelephonyManager;
    private Helpers mHelpers;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIncomingCallListener = new IncomingCallListener(getApplicationContext());
        mHelpers = new Helpers(getApplicationContext());
        mTelephonyManager = mHelpers.getTelephonyManager();
        mTelephonyManager.listen(mIncomingCallListener, PhoneStateListener.LISTEN_CALL_STATE);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(mIncomingCallListener.mOutgoingCall, intentFilter);
        Log.i(AppGlobals.getLogTag(getClass()), "service started ....");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mIncomingCallListener.mOutgoingCall);
    }
}
