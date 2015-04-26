package com.drl.lutz.lullabyapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.drl.lutz.lullabyapp.R;
import com.drl.lutz.lullabyapp.database.Location;
import com.drl.lutz.lullabyapp.database.LocationDatabase;

import java.io.UnsupportedEncodingException;

/**
 * Created by lutz on 12/04/15.
 */
public class LocationCursorAdapter extends CursorAdapter {

    private Context context;

    public LocationCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
    }



    //I store the autocomplete text view in a layout xml.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.location_cursor_layout, null);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Location loc = new Location(cursor);

        TextView cityText = (TextView)view.findViewById(R.id.cityText);
        cityText.setText(loc.city);

        TextView countryText = (TextView)view.findViewById(R.id.countryText);
        countryText.setText(loc.country);
    }

    //you need to override this to return the string value when
    //selecting an item from the autocomplete suggestions
    //just do cursor.getstring(whatevercolumn);
    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor.toString();
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        String searchString = "";
        if (constraint != null)
            searchString = constraint.toString();

        // return cursor from location database
        Cursor cursor = LocationDatabase.getInstance(this.context).searchCity(searchString);
        return cursor;
    }
}
