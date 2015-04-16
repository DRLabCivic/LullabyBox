package com.drl.lutz.lullabyapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.drl.lutz.lullabyapp.R;
import com.drl.lutz.lullabyapp.database.Location;
import com.drl.lutz.lullabyapp.utils.FileUploader;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;

public class UploadActivity extends FullscreenActivity {

    private static final String UPLOAD_WEB_ADDRESS = "sahabe.mooo.com/lullaby/submit.php";

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

        uploadLullaby(soundFile,location);
    }

    public void uploadLullaby(File soundFile,Location location) {

        FileUploader uploader = new FileUploader(getApplicationContext(),soundFile,location);

        try {
            uploader.upload(UPLOAD_WEB_ADDRESS,  new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    ((TextView)findViewById(R.id.bodyText)).setText(R.string.upload_body_text_finished);
                    findViewById(R.id.doneButton).setEnabled(true);
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    showAlert("Error", "Failed to upload file to server: " + throwable.toString());
                }

                @Override
                public void onProgress(int position, int length) {
                    Log.d("UPLOAD", " progress: pos:" + position + " len:" + length);
                }
            });
        } catch (Exception e) {
            this.showAlert("Error","Error uploading lullaby: "+e.toString());
            return;
        }
    }

}
