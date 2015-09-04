package com.byteshatf.callrecorder;

import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.widget.Toast;
import android.util.Log;
import java.io.IOException;

public class CallRecording {

    public static boolean isRecording;
    private MediaRecorder mediaRecorder;
    private SharedPreferences sharedPreferences;
    Helpers mHelpers;

    public void startRecord() {
        mHelpers = new Helpers(AppGlobals.getContext());
        sharedPreferences = mHelpers.getPreferenceManager();
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
            mediaRecorder.setAudioEncodingBitRate(96000);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(AppGlobals.getDataDirectory("CallRec") + "/" + AppGlobals.sCurrentNumber + "_" + Helpers.getTimeStamp() + ".aac");
            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.i(AppGlobals.getLogTag(getClass()), "Recording started.....");
            isRecording = true;
        } catch (Exception e) {
            Toast.makeText(AppGlobals.getContext(), "Error: Recording source incompatible", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopRecording() {
        if (CallRecording.isRecording) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        }
    }
}
