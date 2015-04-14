package com.drl.lutz.lullabyapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.drl.lutz.lullabyapp.R;
import com.drl.lutz.lullabyapp.adapters.LocationCursorAdapter;
import com.drl.lutz.lullabyapp.database.Location;
import com.drl.lutz.lullabyapp.database.LocationDatabase;
import com.drl.lutz.lullabyapp.logic.FileUploader;
import com.drl.lutz.lullabyapp.views.LocationAutoCompleteView;

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
            this.showAlert("Error","Error: "+e.toString());
            return;
        }

        FileUploader uploader = new FileUploader(getApplicationContext(),soundFile,location);

        try {
            uploader.upload(UPLOAD_WEB_ADDRESS);
        } catch (Exception e) {
            this.showAlert("Error","Error uploading lullaby: "+e.toString());
            return;
        }
    }

}
