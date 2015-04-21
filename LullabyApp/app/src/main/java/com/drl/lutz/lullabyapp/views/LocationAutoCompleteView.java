package com.drl.lutz.lullabyapp.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.drl.lutz.lullabyapp.R;
import com.drl.lutz.lullabyapp.database.Location;
import com.drl.lutz.lullabyapp.database.LocationDatabase;

/**
 * TODO: document your custom view class.
 */
public class LocationAutoCompleteView extends AutoCompleteTextView {


    public interface OnLocationSelectListener {
        public void onSelected(boolean selected, Location location);
    }

    private OnLocationSelectListener listener = null;

    private Location location = null;

    View loadingIndidicatorView;

    public LocationAutoCompleteView(Context context) {
        super(context);
        init();
    }

    public LocationAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LocationAutoCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        //show dropdown when clicked
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropDown();
            }
        });

        //if clicked on an item
        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LocationDatabase db = LocationDatabase.getInstance(getContext());
                Cursor cursor = db.getLocation(id);
                cursor.moveToFirst();

                setLocation(new Location(cursor));
            }
        });

        //if manualy changed input
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before)
                    setLocation(null);
                showDropDown();


                //tell the activity that there was a user interaction
                ((Activity)getContext()).onUserInteraction();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loadingIndidicatorView = null;

    }

    @Override
    public void onFilterComplete(int count) {
        super.onFilterComplete(count);
        loadingIndidicatorView.setVisibility(INVISIBLE);
    }

    @Override
    public void performFiltering(CharSequence text, int keyCode) {
        super.performFiltering(text, keyCode);
        loadingIndidicatorView.setVisibility(VISIBLE);
    }

    public void setLocation(Location loc) {

        if (loc != null) {
            this.setText(loc.city);
            this.location = loc;

            if (listener != null)
                listener.onSelected(true,loc);

        } else {
            this.location = null;

            if (listener != null)
                listener.onSelected(false,null);
        }
    }


    public void setOnLocationSelectListener(OnLocationSelectListener listener) {
        this.listener = listener;
    }

    public void setLoadingIndicator(View loadingIndidicatorView) {
        this.loadingIndidicatorView = loadingIndidicatorView;
        this.loadingIndidicatorView.setVisibility(INVISIBLE);
    }

}
