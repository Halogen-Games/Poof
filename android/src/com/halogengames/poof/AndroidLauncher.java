package com.halogengames.poof;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.halogengames.poof.dataLoaders.GameData;
import com.halogengames.poof.screens.GameOverScreen;

import advertisements.AndroidAd;
import amazonDB.AndroidScoreDB;

public class AndroidLauncher extends AndroidApplication {

    // Declare a DynamoDBMapper object (need to set it static)
    public static DynamoDBMapper dynamoDBMapper;
    public static InterstitialAd mInterstitialAd;

    private Poof gameInstance;


    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.initAWS();

        //init admob
        MobileAds.initialize(this, "ca-app-pub-8345561730745636~6055051977");

        //init interstitial ad
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AndroidLauncher.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        setupInterstitialAdEvents();

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		config.numSamples = 3;

        RelativeLayout layout = new RelativeLayout(this);

        //initialize(new Poof(new AndroidScoreDB()), config);
        gameInstance = new Poof(new AndroidScoreDB(),new AndroidAd());
        View gameView = initializeForView(gameInstance, config);
        layout.addView(gameView);

        AndroidAd.addBannerViewToLayout(this, layout);

        setContentView(layout);
	}

    private void initAWS(){
        // AWSMobileClient enables AWS user credentials to access the table
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                initDynamoDB();
            }
        }).execute();
    }

	private void initDynamoDB(){
        // Add code to instantiate an AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        AndroidLauncher.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(
                        AWSMobileClient.getInstance().getConfiguration())
                .build();
    }

    private void setupInterstitialAdEvents(){
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                GameData.gamesPlayed = 0;
                gameInstance.setScreen(new GameOverScreen(gameInstance));
                AndroidLauncher.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }
}
