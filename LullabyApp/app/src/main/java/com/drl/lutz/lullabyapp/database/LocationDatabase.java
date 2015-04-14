package com.drl.lutz.lullabyapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.Normalizer;
import java.util.ArrayList;

/**
 * Created by lutz on 10/04/15.
 * Singleton for Database Access
 */
public class LocationDatabase extends SQLiteOpenHelper {

    private static LocationDatabase _instance;

    private Context context;

    public static final String DATABASE_NAME = "lullabieApp.db";
    public static final String CITIES_TABLE_NAME = "cities";
    public static final String CITIES_TABLE_NAME_FTS = "cities_fts";

    public synchronized static LocationDatabase getInstance(Context context)
    {
        if (_instance == null)
            _instance = new LocationDatabase(context);
        return _instance;
    }

    private LocationDatabase(Context context) {
        super(context, DATABASE_NAME , null, 8);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create primary table
        db.execSQL(
            "CREATE TABLE "+CITIES_TABLE_NAME +
            "(_id INTEGER PRIMARY KEY, " +
            "city VARCHAR(255), "+
            "country VARCHAR(3), "+
            "population INTEGER, "+
            "longitude NUMERIC, "+
            "latitude NUMERIC)"
        );

        buildSearchTable(db);
    }

    public void buildSearchTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+CITIES_TABLE_NAME_FTS);
        db.execSQL(
            "CREATE VIRTUAL TABLE "+CITIES_TABLE_NAME_FTS+" USING fts4" +
            "('_id', 'city')");

        //copy data to search table
        db.execSQL(
            "INSERT INTO "+CITIES_TABLE_NAME_FTS+" (_id, city) " +
            "SELECT _id, LOWER(city) FROM "+CITIES_TABLE_NAME);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateDatabase(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateDatabase(db);
    }

    public void recreateDatabase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+CITIES_TABLE_NAME);
        onCreate(db);
    }

    public synchronized void insertLocations(SQLiteDatabase db, ArrayList<Location> locations) {

        String query = "INSERT INTO "+CITIES_TABLE_NAME+" " +
                "(city,country,population,longitude,latitude) VALUES ";

        for (Location location : locations) {
            query += "("+escape(location.city)+"," +
                    escape(location.country)+"," +
                    escape(Integer.toString(location.population))+"," +
                    escape(Double.toString(location.longitude))+"," +
                    escape(Double.toString(location.latitude))+"),";
        }

        //delete last comma
        query = query.substring(0,query.length()-1);

        //insert data
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        cursor.close();
    }

    public synchronized Cursor getLocation(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+CITIES_TABLE_NAME+" WHERE _id='"+id+"'",null);
        return cursor;
    }

    public synchronized Cursor getAllLocations() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+CITIES_TABLE_NAME,null);
        return cursor;
    }

    public synchronized Cursor searchCity (String prefix) {
        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor cursor = db.rawQuery("SELECT * FROM "+CITIES_TABLE_NAME_FTS,null);
        Cursor cursor = db.rawQuery(
                "SELECT data.* FROM "+CITIES_TABLE_NAME_FTS+" AS fts " +
                        "JOIN "+CITIES_TABLE_NAME+" AS data ON fts._id = data._id " +
                        "WHERE fts.city MATCH "+escape(prefix+"*")+" " +
                        "ORDER BY data.population DESC LIMIT 20",
                null);
        return cursor;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM "+CITIES_TABLE_NAME,null);
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    static public String escape(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\x00-\\x7F]", ""); //only allow asci characters
        return DatabaseUtils.sqlEscapeString(str);
    }

}
