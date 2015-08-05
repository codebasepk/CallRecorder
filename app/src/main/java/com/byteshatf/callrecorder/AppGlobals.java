package com.byteshatf.callrecorder;

import android.app.Application;
import android.content.Context;


public class AppGlobals extends Application {

    static Context sContext;
    public static final int RESULT_OK = 200;
    public static final int REQUEST_CODE = 199;

    static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
}
