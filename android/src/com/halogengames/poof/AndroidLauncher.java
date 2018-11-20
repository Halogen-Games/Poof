package com.halogengames.poof;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import advertisements.ChartboostAd;
import googlePlayServices.AndroidLeaderboardManager;
import googlePlayServices.PlayGamesHelper;

import com.chartboost.sdk.Chartboost;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class AndroidLauncher extends AndroidApplication {

    private PlayGamesHelper playGamesHelper;
    public Preferences prefs;

    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		config.numSamples = 3;

        ChartboostAd androidAd = new ChartboostAd(this);
        Chartboost.setDelegate(androidAd.cbDelegateObject);

        playGamesHelper = new PlayGamesHelper(this);
        playGamesHelper.signInSilently();

        AndroidLeaderboardManager leaderboardManager = new AndroidLeaderboardManager(this, playGamesHelper);

        //create a relative layout that can handle both the views
        RelativeLayout layout = new RelativeLayout(this);

        //Add game view
        Poof gameInstance = new Poof(leaderboardManager, androidAd);
        //initialize(new Poof(new AndroidScoreDB()), config);
        View gameView = initializeForView(gameInstance, config);
        layout.addView(gameView);

        //add playGame popups view
        View view = new View(this);
        playGamesHelper.setPopUpView(view);
        layout.addView(view);

        setContentView(layout);
	}

    @Override
    public void onStart() {
        super.onStart();
        Chartboost.onStart(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Chartboost.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Chartboost.onPause(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Chartboost.onStop(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Chartboost.onDestroy(this);
    }

    @Override
    public void onBackPressed() {
        // If an interstitial is on screen, close it.
        if (!Chartboost.onBackPressed())
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PlayGamesHelper.RC_SIGN_IN) {
            playGamesHelper.handleSignInIntentResult(data);
        }
    }
}
