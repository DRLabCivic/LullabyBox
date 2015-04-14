package com.drl.lutz.lullabyapp.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.drl.lutz.lullabyapp.R;
import com.drl.lutz.lullabyapp.database.Location;
import com.drl.lutz.lullabyapp.adapters.LocationCursorAdapter;
import com.drl.lutz.lullabyapp.database.LocationDatabase;
import com.drl.lutz.lullabyapp.views.LocationAutoCompleteView;

import java.io.File;

public class LocationActivity extends FullscreenActivity {

    Location choosenLocation = null;

    File soundFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //receive soundfile from recorder activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.soundFile = (File)extras.get("soundFile");
        }

        LocationCursorAdapter lcadapter = new LocationCursorAdapter(this, null, 0);

        final LocationAutoCompleteView searchInput = (LocationAutoCompleteView)findViewById(R.id.searchTextInput);
        searchInput.setAdapter(lcadapter);

        searchInput.setOnLocationSelectListener(new LocationAutoCompleteView.OnLocationSelectListener() {
            @Override
            public void onSelected(boolean selected, Location location) {
                setLocation(location);
            }
        });

    }

    public void setLocation(Location loc) {

        if (loc != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            Button nextButton = (Button)findViewById(R.id.nextButton);
            nextButton.setEnabled(true);

            findViewById(R.id.helpText).setVisibility(View.INVISIBLE);

            choosenLocation = loc;
        } else {
            Button nextButton = (Button)findViewById(R.id.nextButton);
            nextButton.setEnabled(false);

            findViewById(R.id.helpText).setVisibility(View.VISIBLE);

            choosenLocation = null;
        }
    }

    public void onNextButtonClicked(View view) {

        Intent intent = new Intent(getBaseContext(), UploadActivity.class);
        intent.putExtra("soundFile", this.soundFile);
        intent.putExtra("location", this.choosenLocation);
        startActivity(intent);
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);

        finish();
    }
}
