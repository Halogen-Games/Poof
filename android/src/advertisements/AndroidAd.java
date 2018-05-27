package advertisements;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.halogengames.poof.AndroidLauncher;
import com.halogengames.poof.advertisement.AdInterface;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

public class AndroidAd implements AdInterface{
    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     * Hence creating a static class to hold the handler
     * as it can potentially outlive the main activity causing memory leaks
     */
    private static class MyHandler extends Handler {

        private final WeakReference<AndroidAd> myAd;

        private MyHandler(AndroidAd andAd) {
            myAd = new WeakReference<>(andAd);
        }

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_INTERSTITIAL:
                {
                    if (AndroidLauncher.mInterstitialAd.isLoaded()) {
                        AndroidLauncher.mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    break;
                }
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    public void addBannerViewToLayout(Context context, RelativeLayout layout) {
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);

        String originalBannerID = "ca-app-pub-5290404360165098/6745742149";
        String testBannerID = "ca-app-pub-3940256099942544/6300978111";
        String currentBannerID = testBannerID;

        adView.setAdUnitId(currentBannerID);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        layout.addView(adView, adParams);
    }

    public void setupInterstitialAds(Context context){
        //init interstitial ad
        String originalInterstitialID = "ca-app-pub-5290404360165098/8302366393";
        String testInterstitialID = "ca-app-pub-3940256099942544/1033173712";
        String currentInterstitialID = testInterstitialID;

        AndroidLauncher.mInterstitialAd = new InterstitialAd(context);
        AndroidLauncher.mInterstitialAd.setAdUnitId(currentInterstitialID);
        AndroidLauncher.mInterstitialAd.loadAd(new AdRequest.Builder().build());

        AndroidLauncher.mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                System.out.println("Ad failed to load, reloading");
                AndroidLauncher.mInterstitialAd.loadAd(new AdRequest.Builder().build());
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
                AndroidLauncher.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    @Override
    public void showInterstitialAd() {
        handler.sendEmptyMessage(SHOW_INTERSTITIAL);
    }

//    public void getEEAConsent(final Context context){
//        ConsentInformation consentInformation = ConsentInformation.getInstance(context);
//        consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
//        String[] publisherIds = {"pub-5290404360165098"};
//        showConsentForm(context);
//        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
//            @Override
//            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
//                // User's consent status successfully updated.
//                if(consentStatus == ConsentStatus.PERSONALIZED){
//
//                }else if(consentStatus == ConsentStatus.NON_PERSONALIZED){
//
//                }else{
//
//                }
//            }
//
//            @Override
//            public void onFailedToUpdateConsentInfo(String errorDescription) {
//                // User's consent status failed to update.
//            }
//        });
//
//    }
//
//    private void showConsentForm(Context context){
//        URL privURL = null;
//        try {
//            // TODO: Replace with your app's privacy policy URL.
//            privURL = new URL("https://www.wishfie.com/private-policy");
//        } catch (MalformedURLException e) {
//            System.out.println("Bad URL");
//            // Handle error.
//        }
//
//        ConsentForm form = new ConsentForm.Builder(context, privURL)
//                .withListener(new ConsentFormListener() {
//                    @Override
//                    public void onConsentFormLoaded() {
//                        // Consent form loaded successfully.
//                    }
//
//                    @Override
//                    public void onConsentFormOpened() {
//                        // Consent form was displayed.
//                    }
//
//                    @Override
//                    public void onConsentFormClosed(
//                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
//                        // Consent form was closed.
//                    }
//
//                    @Override
//                    public void onConsentFormError(String errorDescription) {
//                        // Consent form error.
//                    }
//                })
//                .withPersonalizedAdsOption()
//                .withNonPersonalizedAdsOption()
//                .withAdFreeOption()
//                .build();
//
//        form.load();
//        form.show();
//        System.out.println(ConsentInformation.getInstance(context).getDebugGeography());
//    }
}
