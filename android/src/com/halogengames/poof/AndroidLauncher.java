package com.halogengames.poof;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.InterstitialAd;

import advertisements.ChartboostAd;
import amazonAWS.amazonDB.AndroidScoreDB;
import amazonAWS.analytics.AWSPinpointManager;
import amazonAWS.analytics.AbstractApplicationLifeCycleHelper;

import com.chartboost.sdk.Chartboost;

import java.io.IOException;

public class AndroidLauncher extends AndroidApplication {

    public static PinpointManager pinpointManager;
    private AbstractApplicationLifeCycleHelper applicationLifeCycleHelper;

    // Declare a DynamoDBMapper object (need to set it static)
    public static DynamoDBMapper dynamoDBMapper;
    public static InterstitialAd mInterstitialAd;

    private Poof gameInstance;
    private ChartboostAd androidAd;

    public RelativeLayout layout;


    @Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.initAWS();
		this.initPinpointManager();

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		config.numSamples = 3;


        layout = new RelativeLayout(this);

        androidAd = new ChartboostAd(this);
        Chartboost.setDelegate(androidAd.cbDelegateObject);

        gameInstance = new Poof(new AndroidScoreDB(), androidAd, new AWSPinpointManager());

        View gameView = initializeForView(gameInstance, config);
        layout.addView(gameView);

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

    private void initAWS(){
        // AWSMobileClient enables AWS user credentials to access the table
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                initDynamoDB();
            }
        }).execute();
    }

    private void initPinpointManager(){
        AWSConfiguration awsConfiguration = new AWSConfiguration(this);

        if (IdentityManager.getDefaultIdentityManager() == null) {
            final IdentityManager identityManager = new IdentityManager(getApplicationContext(), awsConfiguration);
            IdentityManager.setDefaultIdentityManager(identityManager);
        }

        try {
            final PinpointConfiguration config =
                    new PinpointConfiguration(this,
                            IdentityManager.getDefaultIdentityManager().getCredentialsProvider(),
                            awsConfiguration);
            AndroidLauncher.pinpointManager = new PinpointManager(config);
        } catch (final AmazonClientException ex) {
            Log.e("Amazon Pinpoint", "Unable to initialize PinpointManager. " + ex.getMessage(), ex);
        }

        //setup session lifecycle manager
        applicationLifeCycleHelper = new AbstractApplicationLifeCycleHelper(this.getApplication()) {
            @Override
            protected void applicationEnteredForeground() {
                AndroidLauncher.pinpointManager.getSessionClient().startSession();
                // handle any events that should occur when your app has come to the foreground...
            }

            @Override
            protected void applicationEnteredBackground() {
                Log.d("Amazon Pinpoint", "Detected application has entered the background.");
                AndroidLauncher.pinpointManager.getSessionClient().stopSession();
                AndroidLauncher.pinpointManager.getAnalyticsClient().submitEvents();
                // handle any events that should occur when your app has gone into the background...
            }
        };
    }

    //todo: ficure the use of below func from amazon pinpoint guide(Managing Sessions)
//    private void updateGCMToken() {
//        try {
//            final String gcmToken = InstanceID.getInstance(this).getToken(
//                    "YOUR_SENDER_ID",
//                    GoogleCloudMessaging.INSTANCE_ID_SCOPE
//            );
//            AndroidLauncher.pinpointManager.getNotificationClient().registerGCMDeviceToken(gcmToken);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onTrimMemory(final int level) {
        Log.d("Amazon Pinpoint", "onTrimMemory " + level);
        applicationLifeCycleHelper.handleOnTrimMemory(level);
        super.onTrimMemory(level);
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
}
