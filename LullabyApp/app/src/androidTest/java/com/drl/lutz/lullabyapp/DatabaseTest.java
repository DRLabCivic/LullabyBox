package com.drl.lutz.lullabyapp;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.drl.lutz.lullabyapp.database.Location;
import com.drl.lutz.lullabyapp.database.LocationDatabase;

import java.util.ArrayList;

/**
 * Created by lutz on 12/04/15.
 */
public class DatabaseTest extends AndroidTestCase {
    private LocationDatabase db;

    @Override
    public void setUp(){
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = LocationDatabase.getInstance(context);

        // insert test data

        db.onUpgrade(db.getWritableDatabase(), 1, 1);
        ArrayList<Location> locations = new ArrayList<Location>();
        locations.add(new Location(0,"Belzig","de",40000,52.32,12.43));
        locations.add(new Location(0,"Berlin","de",3600000,23.3434,45.234785));
        locations.add(new Location(0,"Gothenburg","se",600000,56.32,32.43));
        locations.add(new Location(0,"Beelitz","de",20000,52.32,12.43));
        locations.add(new Location(0,"Götebörgé §'.,-213'é","de",40000,52.32,12.43));
        db.insertLocations(db.getWritableDatabase(), locations);
        db.buildSearchTable(db.getWritableDatabase());

    }

    public void testSearch(){
        Cursor cursor = db.searchCity("be");

        cursor.moveToFirst();
        Log.d("TEST", DatabaseUtils.dumpCursorToString(cursor));

        assertTrue("Searching pattern 'be': Column count should be 3",cursor.getCount() == 3);

        cursor.close();
    }

    /*public void testSearchAccent(){
        Cursor cursor = db.searchCity("gö");

        cursor.moveToFirst();
        Log.d("DATABASE", DatabaseUtils.dumpCursorToString(cursor));

        assertTrue("Searching accent pattern 'gö': Column count should be 1",cursor.getCount() == 1);
        assertTrue("Searching accent pattern 'gö': accent_name should be göteborg, is: "+cursor.getString(cursor.getColumnIndex("accent_city")), cursor.getString(2).equals("Göteborg"));

        // make sure to close the cursor
        cursor.close();
    }*/

    public void testSearchResultOrder(){
        Cursor cursor = db.searchCity("be");

        cursor.moveToFirst();
        Log.d("TEST", DatabaseUtils.dumpCursorToString(cursor));

        assertTrue("Checking search order, first city should be Berlin",cursor.getString(cursor.getColumnIndex("city")).equals("Berlin"));
        cursor.moveToNext();
        assertTrue("Checking search order, second city should be Belzig",cursor.getString(cursor.getColumnIndex("city")).equals("Belzig"));
        cursor.moveToNext();
        assertTrue("Checking search order, third city should be Beelitz",cursor.getString(cursor.getColumnIndex("city")).equals("Beelitz"));

        // make sure to close the cursor
        cursor.close();
    }

    /*public void testImport(){

        DatabaseImporter importer = new DatabaseImporter(db);

        int rowsBefore = db.numberOfRows();

        try {
            importer.importCsvFile("cities_test.sql","\t");
        } catch (Exception e) {
            assertTrue("Error importing file: " + e.toString(), false);
            e.printStackTrace();
        }

        assertTrue("Db size should be greater after import",rowsBefore<db.numberOfRows());
    }*/

    public void testSpecialCharacters(){

        Cursor cursor = db.searchCity("Götebö");
        //Cursor cursor = db.getAllLocations();

        cursor.moveToFirst();
        Log.d("TEST", DatabaseUtils.dumpCursorToString(cursor));

        assertTrue("Inserting special character string and searching pattern 'Goteborge': Column count should be 1",cursor.getCount() == 1);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }
}
