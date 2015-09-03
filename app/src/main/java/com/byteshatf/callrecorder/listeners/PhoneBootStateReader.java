package com.byteshatf.callrecorder.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshatf.callrecorder.services.CallRecordingService;

public class PhoneBootStateReader extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, CallRecordingService.class));
    }
}
