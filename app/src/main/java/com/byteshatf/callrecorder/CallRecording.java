package com.byteshatf.callrecorder;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class CallRecording {

    public static boolean isRecording;
    private MediaRecorder mediaRecorder;

    public void startRecord() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setAudioEncodingBitRate(96000);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(AppGlobals.getDataDirectory("CallRec") + "/" + Helpers.getTimeStamp()
                +"_"+ AppGlobals.sCurrentNumber+ ".aac");
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.i(AppGlobals.getLogTag(getClass()), "Recording started .....");
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
        isRecording = false;
    }
}
