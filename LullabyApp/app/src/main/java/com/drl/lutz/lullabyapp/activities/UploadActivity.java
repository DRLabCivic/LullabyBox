package com.drl.lutz.lullabyapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.drl.lutz.lullabyapp.R;
import com.drl.lutz.lullabyapp.database.Location;
import com.drl.lutz.lullabyapp.utils.FileUploader;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;

public class UploadActivity extends FullscreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        File soundFile = null;
        Location location = null;

        //receive soundfile and location from location activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            soundFile = (File)extras.get("soundFile");
            location = (Location)extras.get("location");
        }

        try {
            Log.e("UPLOAD", "Location: " + location.toJsonString());
            Log.e("UPLOAD", "File: " + soundFile.getAbsolutePath().toString());
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.toString());
            return;
        }

        setIdleCloseTimer(getResources().getInteger(R.integer.idleTime));

        //start upload to web server automatically
        uploadLullaby(soundFile,location);

    }

    public void uploadLullaby(File soundFile,Location location) {

        final FileUploader uploader = new FileUploader(getApplicationContext(),soundFile,location);

        String uploadUrl = getResources().getString(R.string.UploadWebUrl);

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);

        try {
            uploader.upload(uploadUrl,  new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    ((TextView)findViewById(R.id.bodyText)).setText(R.string.upload_body_text_finished);
                    findViewById(R.id.doneButton).setEnabled(true);
                    progressBar.setProgress(100);
                    pauseIdleTimer(false);

                    //delete file after succesfull upload
                    uploader.deleteFile();
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    throwable.printStackTrace();
                    showAlert("Error", "Failed to upload file to server: " + throwable.toString());
                }

                @Override
                public void onProgress(int position, int length) {
                    progressBar.setProgress((int) (position / (float) length * 100));
                }
            });

            pauseIdleTimer(true);

        } catch (Exception e) {
            this.showAlert("Error","Error uploading lullaby: "+e.toString());
            return;
        }
    }

    public void onAcceptButtonClicked(View view) {
        finish();
    }
}
