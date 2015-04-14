package com.drl.lutz.lullabyapp.logic;

import android.content.Context;
import android.net.Uri;

import java.io.File;

/**
 * Created by lutz on 13/04/15.
 */
abstract public class SoundRecorder {

    protected Context context;

    public SoundRecorder(Context context) {
        this.context = context;
    }
    public void prepare() throws Exception { };
    abstract public void startRecording() throws Exception;
    abstract public void stopRecording();
    abstract public void reset();
    abstract public File save() throws Exception;
    abstract public void release();

}
