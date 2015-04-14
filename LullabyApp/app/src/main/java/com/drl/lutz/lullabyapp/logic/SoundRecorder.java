package com.drl.lutz.lullabyapp.logic;

import android.content.Context;
import android.net.Uri;

/**
 * Created by lutz on 13/04/15.
 */
abstract public class SoundRecorder {

    protected Context context;

    public SoundRecorder(Context context) {
        this.context = context;
    }
    public void prepare(String outputFile) throws Exception { };
    abstract public void startRecording() throws Exception;
    abstract public void stopRecording();
    abstract public void reset();
    abstract public Uri save();
    abstract public void release();

}
