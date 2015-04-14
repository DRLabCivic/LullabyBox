package com.drl.lutz.lullabyapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.drl.lutz.lullabyapp.R;
import com.drl.lutz.lullabyapp.database.DatabaseImporter;
import com.drl.lutz.lullabyapp.database.LocationDatabase;

import java.io.IOException;

public class DatabaseUpdateActivity extends FullscreenActivity {

    private final String DATABASE_FILE_PATH = "cities.sql";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_update);

    }

    public void runUpdate() {

        LocationDatabase database = LocationDatabase.getInstance(getApplicationContext());
        final DatabaseImporter importer = new DatabaseImporter(database);

        database.recreateDatabase(database.getWritableDatabase());

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(100);

        importer.setOnDatabaseImportListener(new DatabaseImporter.OnDatabaseImportListener() {
            @Override
            public void onProgress(float progress) {
                progressBar.setProgress((int)(progress*100));
            }

            @Override
            public void onBuildSearchTable() {
                progressBar.setProgress(100);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setIndeterminate(true);
                        ((TextView) findViewById(R.id.bodyText)).setText(R.string.update_body_text_2);
                    }
                });

            }

            @Override
            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setIndeterminate(false);
                        findViewById(R.id.acceptButton).setEnabled(true);
                        ((TextView)findViewById(R.id.bodyText)).setText(R.string.update_body_text_finished);
                    }
                });

            }
        });

        //run update in Thread
        new Thread(new Runnable() {
            public void run() {
                try {
                    importer.importCsvFile(DATABASE_FILE_PATH,"\t");
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView)findViewById(R.id.bodyText)).setText(R.string.update_body_text_error);
                        }
                    });
                }
            }
        }).start();
    }

    public void OnStartButtonClicked(View view) {

        findViewById(R.id.startButton).setVisibility(View.GONE);
        findViewById(R.id.acceptButton).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.bodyText)).setText(R.string.update_body_text);

        runUpdate();
    }

    public void onAcceptButtonClicked(View view) {
        super.onBackPressed();
    }


}
