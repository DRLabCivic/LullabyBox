package com.drl.lutz.lullabyapp.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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

        this.recreate();

        super.onStop();

    }

    public void onScreenClicked(View view) {

        final View moon = findViewById(R.id.moon);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_moon);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                View layout = findViewById(R.id.mainLayout);
                layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        moon.startAnimation(animation);

        View moonText = findViewById(R.id.moonText);
        moonText.setVisibility(View.GONE);
    }

    public void onNextButtonClicked(View view) {


        startActivity(new Intent(this, RecorderActivity.class));
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }

    public void upgradeDatabase() {
        startActivity(new Intent(this, DatabaseUpdateActivity.class));
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }
}
