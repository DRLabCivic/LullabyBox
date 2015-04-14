package com.drl.lutz.lullabyapp.logic;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by lutz on 13/04/15.
 */
public class SoundRecorderExt extends SoundRecorder {


    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";

    ExtAudioRecorder recorder;

    public SoundRecorderExt(Context context) {
        super(context);

        recorder = ExtAudioRecorder.getInstance(false);
    }

    @Override
    public void startRecording() throws Exception {
        recorder.setOutputFile(getFilename());
        recorder.prepare();
        recorder.start();
    }

    @Override
    public void stopRecording() {
        recorder.stop();
    }

    @Override
    public void reset() {

    }

    @Override
    public Uri save() {
        return null;
    }

    @Override
    public void release() {
        recorder.release();
    }

    private String getFilename (){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() +
                AUDIO_RECORDER_FILE_EXT_WAV);
    }
}
