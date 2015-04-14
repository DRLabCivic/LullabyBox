package com.drl.lutz.lullabyapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.drl.lutz.lullabyapp.R;
import com.drl.lutz.lullabyapp.logic.SoundRecorder;
import com.drl.lutz.lullabyapp.logic.SoundRecorderExt;
import com.drl.lutz.lullabyapp.logic.SoundRecorderHq;
import com.drl.lutz.lullabyapp.logic.SoundRecorderWav;
import com.drl.lutz.lullabyapp.logic.SoundRecorderWav2;


public class RecorderActivity extends FullscreenActivity {

    RecorderState state = RecorderState.INIT;

    CountDownTimer timer = null;

    SoundRecorder recorder;

    public enum RecorderState {
        INIT, RECORDING, STOPPED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        recorder = new SoundRecorderWav2(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recorder.release();
    }


    public void onScreenClick(View view) {
        if (state == RecorderState.INIT)
            startRecording();
        else if (state == RecorderState.RECORDING)
            stopRecording();
    }

    public void startRecording() {
        //hide Mic Icon
        findViewById(R.id.micIcon).setVisibility(View.GONE);

        //switch Text
        ((TextView) findViewById(R.id.recordTimeHelpText)).setText(R.string.recordHelpTextStop);

        //show Record Icon
        final View recordIcon = findViewById(R.id.recordIcon);
        recordIcon.setVisibility(View.VISIBLE);

        //start flashing record icon
        final Animation flashAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.flash);
        recordIcon.startAnimation(flashAnimation);

        //start Counting Time
        timer = new CountDownTimer(1000 * 600, 10) {

            long startTime = System.currentTimeMillis();

            public void onTick(long millisUntilFinished) {
                long elapsedTime = System.currentTimeMillis() - startTime;

                //update time
                TextView view = (TextView)findViewById(R.id.recordTimeView);
                view.setText(convertTimeString(elapsedTime));
            }

            public void onFinish() {
                stopRecording();
            }
        }.start();

        try {
            recorder.prepare();
            recorder.startRecording();
        } catch (Exception e) {
            stopRecording();
            showAlert("Error", "Error: " + e.toString());
        }

        state = RecorderState.RECORDING;
    }

    public void stopRecording() {

        //switch Text
        ((TextView) findViewById(R.id.recordTimeHelpText)).setText(R.string.recordHelpTextFinished);

        //hide Record Icon
        View recordIcon = findViewById(R.id.recordIcon);
        recordIcon.clearAnimation();
        recordIcon.setVisibility(View.GONE);

        //show Buttons
        findViewById(R.id.restartButton).setVisibility(View.VISIBLE);
        findViewById(R.id.acceptButton).setVisibility(View.VISIBLE);

        //stop timer
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        recorder.stopRecording();

        state = RecorderState.STOPPED;
    }

    public void resetRecording() {

        //reset Text
        ((TextView) findViewById(R.id.recordTimeHelpText)).setText(R.string.recordHelpTextStart);

        //reset Time
        TextView timeView = (TextView)findViewById(R.id.recordTimeView);
        timeView.setText(convertTimeString(0));

        //hide Buttons
        findViewById(R.id.restartButton).setVisibility(View.GONE);
        findViewById(R.id.acceptButton).setVisibility(View.GONE);

        //show microphone
        findViewById(R.id.micIcon).setVisibility(View.VISIBLE);

        recorder.reset();
        state = RecorderState.INIT;
    }

    private String convertTimeString(long milliseconds) {

        long hsecs= (milliseconds % 1000) / 100;
        long secs = milliseconds / 1000;
        //long mins = milliseconds / (1000 * 60);

        String secString = Long.toString(secs);
        if (secs < 10)
            secString = "00"+secString;
        else if (secs < 100)
            secString = "0"+secString;

        String hsecString = Long.toString(hsecs);

        //String minsString = Long.toString(mins);

        return(secString+"."+hsecString);
    }

    public void onRestartButtonClicked(View view) {
        resetRecording();
    }

    public void onAcceptButtonClicked(View view) {
        startActivity(new Intent(this, LocationActivity.class));
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);

        Uri recordedFile = recorder.save();
    }
}
