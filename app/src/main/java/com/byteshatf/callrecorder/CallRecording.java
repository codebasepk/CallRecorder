package com.byteshatf.callrecorder;

import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.widget.Toast;

import java.io.IOException;

public class CallRecording {

    public static boolean isRecording;
    private MediaRecorder mediaRecorder;
    private SharedPreferences sharedPreferences;
    Helpers mHelpers;

    public void startRecord() {
        mHelpers = new Helpers(AppGlobals.getContext());
        int recordingSource = sharedPreferences.getInt("radio_int", 0);
        mediaRecorder = new MediaRecorder();
        try {
            switch (recordingSource) {
                case 0:
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    break;
                case 1:
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                    break;
                case 2:
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_DOWNLINK);
                    break;
                case 3:
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_UPLINK);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(AppGlobals.getContext(), "Error: Recording source incompatible", Toast.LENGTH_SHORT).show();
        }
        mediaRecorder.setAudioEncodingBitRate(96000);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(AppGlobals.getDataDirectory("CallRec") + "/" + AppGlobals.sCurrentNumber + "_" + Helpers.getTimeStamp() + ".aac");
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
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
