package com.drl.lutz.lullabyapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.drl.lutz.lullabyapp.R;
import com.drl.lutz.lullabyapp.database.LocationDatabase;

public class MainActivity extends FullscreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //check if database is populated, if not upgrade db
        LocationDatabase database = LocationDatabase.getInstance(getApplicationContext());

        if (database.numberOfRows() == 0)
            upgradeDatabase();

        /*
        //start in animation
        View bottomLayout = findViewById(R.id.bottomLayout);
        bottomLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_in));
        View topLayout = findViewById(R.id.topLayout);
        topLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_in));*/
    }

    @Override
    protected void onStop() {

        /*
        //start out animation
        View bottomLayout = findViewById(R.id.bottomLayout);
        bottomLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_out));
        View topLayout = findViewById(R.id.topLayout);
        topLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_out));*/

        super.onStop();

    }

    public void onNextButtonClicked(View view) {
        startActivity(new Intent(this, RecorderActivity.class));
        //startActivity(new Intent(this, LocationActivity.class));
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }

    public void upgradeDatabase() {
        startActivity(new Intent(this, DatabaseUpdateActivity.class));
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }
}
