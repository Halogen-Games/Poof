package advertisements;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.halogengames.poof.AndroidLauncher;
import com.halogengames.poof.Poof;
import com.halogengames.poof.advertisement.AdInterface;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import static com.google.ads.consent.ConsentInformation.getInstance;

public class AndroidAd implements AdInterface{
    private AdHandler handler = new AdHandler(this);
    private AdView adView;
    public Context context;
    private AndroidLauncher contextLauncher;
    private AdRequest adRequest;

    public Poof game;

    //GDPR Data
    public boolean isEALocation;

    public AndroidAd(Context context, AndroidLauncher contextLauncher){
        this.context = context;
        this.contextLauncher = contextLauncher;

        //to test EU region behavior, uncomment bellow
        //ConsentInformation.getInstance(context).addTestDevice("525C2A500DB3DFAAF563CBCD22C6DF25");
        //getInstance(context).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
        isEALocation = true;
    }

    public void addBannerViewToLayout() {
        RelativeLayout layout = this.contextLauncher.layout;
        adView = new AdView(this.context);
        adView.setAdSize(AdSize.SMART_BANNER);

        String originalBannerID = "ca-app-pub-5290404360165098/6745742149";
        String testBannerID = "ca-app-pub-3940256099942544/6300978111";
        String currentBannerID = testBannerID;

        adView.setAdUnitId(currentBannerID);

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        layout.addView(adView, adParams);
    }

    private void reloadBannerAd(){
        adView.loadAd(adRequest);
    }

    private void setupInterstitialAds(){
        //init interstitial ad
        String originalInterstitialID = "ca-app-pub-5290404360165098/8302366393";
        String testInterstitialID = "ca-app-pub-3940256099942544/1033173712";
        String currentInterstitialID = testInterstitialID;

        //todo: separate the initialization and loading just like banner ad
        AndroidLauncher.mInterstitialAd = new InterstitialAd(this.context);
        AndroidLauncher.mInterstitialAd.setAdUnitId(currentInterstitialID);
        AndroidLauncher.mInterstitialAd.loadAd(adRequest);

        AndroidLauncher.mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                System.out.println("Ad failed to load, reloading");

                AndroidLauncher.mInterstitialAd.loadAd(adRequest);
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
                // Code to be executed when the interstitial ad is closed.
                AndroidLauncher.mInterstitialAd.loadAd(adRequest);
            }
        });
    }

    @Override
    public void showInterstitialAd() {
        handler.sendEmptyMessage(SHOW_INTERSTITIAL);
    }

    @Override
    public boolean isEURegion(){
        return isEALocation;
    }

    public void genConsentFormIfNeeded(){
        ConsentInformation consentInformation = getInstance(this.context);
        String[] publisherIds = {"pub-5290404360165098"};
        ConsentListener consentListener =  new ConsentListener(this);

        //get the current consent status
        consentInformation.requestConsentInfoUpdate(publisherIds, consentListener);
    }

    public void setConsentStatus(int status){
        System.out.println("Setting personal Status: " + status);
        if(status == NON_PERSONALIZED_AD) {
            handler.sendEmptyMessage(NON_PERSONALIZED_AD);
        }else{
            handler.sendEmptyMessage(PERSONALIZED_AD);
        }
    }

    public void setPersonalizedAd(){
        System.out.println("Setting personalized ad");
        getInstance(context).setConsentStatus(ConsentStatus.PERSONALIZED);

        //create a personalized ad request
        adRequest = new AdRequest.Builder().build();
        reloadBannerAd();
        setupInterstitialAds();
    }

    public void setNonPersonalizedAd(){
        System.out.println("Setting non-personalized ad");
        getInstance(context).setConsentStatus(ConsentStatus.NON_PERSONALIZED);

        //create non personalized ad request
        Bundle extras = new Bundle();
        extras.putString("npa", "1");

        adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
        reloadBannerAd();
        setupInterstitialAds();
    }

    public void setGameHandle(Poof game){
        this.game = game;
    }
}
