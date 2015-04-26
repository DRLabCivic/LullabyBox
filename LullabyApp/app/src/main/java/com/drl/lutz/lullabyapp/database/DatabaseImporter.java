package com.drl.lutz.lullabyapp.database;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;


import com.drl.lutz.lullabyapp.database.Location;
import com.drl.lutz.lullabyapp.database.LocationDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by lutz on 13/04/15.
 */
public class DatabaseImporter {

    public interface OnDatabaseImportListener {
        public void onProgress(float progress);
        public void onBuildSearchTable();
        public void onFinish();
    }

    LocationDatabase db;

    private OnDatabaseImportListener listener = null;

    public DatabaseImporter(LocationDatabase db) {
        this.db = db;
    }

    public void importCsvFile(String filePath, String seperator) throws IOException {
        Log.e("IMPORT", "Importing...");

        //File file = new File(filePath);

        File file = new File(Environment.getExternalStorageDirectory().getPath(),filePath);

        long fileSize = file.length();
        long bytesRead = 0;

        //Read text from file
        InputStreamReader inputStream = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)),"UTF-8");
        BufferedReader reader = new BufferedReader(inputStream);
        //BufferedReader br = new BufferedReader(new FileReader(file));

        if (listener != null)
            listener.onProgress(0.0F);

        //skip first line
        reader.readLine();

        ArrayList<Location> locations = new ArrayList<Location>();

        //open database
        SQLiteDatabase sqlDb = db.getWritableDatabase();

        String line;
        while ((line = reader.readLine()) != null) {

            bytesRead += line.length(); //approximately 1 byte per utf character

            String[] values = line.split(seperator);
            // order in file:   combined	population	country_code	region	latitude	longitude

            String city = values[0];
            String country = values[2];
            int pop;
            try {
                pop = Integer.parseInt(values[1]);
            } catch (NumberFormatException e) {
                pop = 0;
            }
            double latitude;
            double longitude;
            try {
                latitude = Double.parseDouble(values[4]);
                longitude = Double.parseDouble(values[5]);
            } catch (NumberFormatException e) {
                //no coordinates, skip this line
                continue;
            }

            locations.add(new Location(0, city, country, pop, latitude, longitude));

            //insert batches of 100 locations
            if (locations.size() > 100) {
                db.insertLocations(sqlDb, locations);
                locations.clear();

                if (listener != null)
                    listener.onProgress(bytesRead/(float)fileSize);

                //Log.e("Import","Progress:"+bytesRead/(float)fileSize);
            }
        }

        //insert the rest
        db.insertLocations(sqlDb, locations);

        Log.e("IMPORT","End Importing");

        if (listener != null) {
            listener.onProgress(1.0F);
            listener.onBuildSearchTable();
        }

        db.buildSearchTable(sqlDb);

        sqlDb.close();

        Log.e("IMPORT","End Building Search table");

        if (listener != null) {

            listener.onFinish();
        }
    }

    public void setOnDatabaseImportListener(OnDatabaseImportListener listener) {
        this.listener = listener;
    }
}
