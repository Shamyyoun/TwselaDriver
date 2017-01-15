package com.twsela.driver.activities;

import android.content.Intent;
import android.os.Bundle;

import com.twsela.driver.R;
import com.twsela.driver.controllers.ActiveUserController;
import com.twsela.driver.utils.PlayServicesUtils;
import com.twsela.driver.utils.Utils;

public class SplashActivity extends ParentActivity {
    private ActiveUserController activeUserController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // check google play services
        if (!PlayServicesUtils.isPlayServicesAvailable(this)) {
            // show msg and finish
            Utils.showLongToast(this, R.string.install_google_play_services);
            finish();
            return;
        }

        // create the controller
        activeUserController = new ActiveUserController(this);

        // check logged in user
        if (activeUserController.hasLoggedInUser()) {
            // open main activity
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

//            // check active trip TODO
//            Trip activeTrip = activeUserController.getActiveTrip();
//            if (activeTrip != null) {
//                // open trip details activity
//                Intent tripIntent = new Intent(this, TripDetailsActivity.class);
//                tripIntent.putExtra(Const.KEY_ID, activeTrip.getId());
//                tripIntent.putExtra(Const.KEY_STATUS, activeTrip.getStatus());
//
//                startActivity(tripIntent);
//            }

        } else {
            // open login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        // finish
        finish();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}