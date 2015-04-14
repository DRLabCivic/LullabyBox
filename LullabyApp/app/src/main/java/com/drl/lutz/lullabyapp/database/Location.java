package com.drl.lutz.lullabyapp.database;

import android.database.Cursor;

/**
 * Created by lutz on 12/04/15.
 */
public class Location {

    public long _id;

    public String city;
    //public String accent_city;
    //public String region;
    public String country;

    public int population;
    public double longitude;
    public double latitude;

    public Location(Cursor cursor) {
        _id = cursor.getInt(cursor.getColumnIndex("_id"));

        city = cursor.getString(cursor.getColumnIndex("city"));
        //accent_city = cursor.getString(cursor.getColumnIndex("accent_city"));
        //region = cursor.getString(cursor.getColumnIndex("region"));
        country = cursor.getString(cursor.getColumnIndex("country"));

        population = cursor.getInt(cursor.getColumnIndex("population"));
        latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
        longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
    }

    public Location(int id, String city, String country, int population, double latitude, double longitude) {
        this._id = id;

        this.city = city;
        //this.accent_city = accent_city;
        //this.region = region;
        this.country = country;

        this.population = population;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
