package com.byteshatf.callrecorder;

import android.media.MediaRecorder;

import java.io.IOException;

public class CallRecording extends MediaRecorder {

    public static boolean isRecording;

    public void startRecord() {
        setAudioSource(AudioSource.MIC);
        setAudioEncodingBitRate(96000);
        setOutputFormat(OutputFormat.DEFAULT);
        setAudioEncoder(AudioEncoder.AAC);
        setOutputFile(AppGlobals.getDataDirectory(AppGlobals.path));
        try {
            prepare();
            start();
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        stop();
        reset();
        release();
        isRecording = false;

    }
}
