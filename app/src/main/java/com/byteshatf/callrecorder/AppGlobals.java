package com.byteshatf.callrecorder;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;


public class AppGlobals extends Application {

    public static final int RESULT_OK = 200;
    public static final int REQUEST_CODE = 199;
    static Context sContext;
    public static String path = "CallRec" + "/" + Helpers.getTimeStamp() + ".aac";

    public static Context getContext() {
        return sContext;
    }

    public static String getDataDirectory(String type) {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dataDirectory = sdcard + "/Android/data/";
        String directoryPath = dataDirectory
                + sContext.getPackageName()
                + File.separator
                + type
                + File.separator;
        File file = new File(directoryPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
}
