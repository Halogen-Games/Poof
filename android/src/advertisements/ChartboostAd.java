package advertisements;

import android.app.Activity;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.halogengames.poof.Poof;
import com.halogengames.poof.advertisement.AdInterface;

public class ChartboostAd implements AdInterface {

    private int interstitialRequests;
    private int interstitialRate;

    private Poof game;

    //GDPR Data
    private boolean isEALocation;
    private GDPRConsentManager consentManager;

    private Activity activity;

    public ChartboostAd(Activity activity){
        this.activity = activity;

        consentManager = new GDPRConsentManager(activity);

        //Todo:Set EU Region Correctly
        isEALocation = true;

        if(!consentManager.isEURegion() || consentManager.getConsentStatus() == GDPRConsentManager.ConsentStat.Personalized){
            setPersonalizedAd();
        }else{
            setNonPersonalizedAd();
        }

        Chartboost.startWithAppId(this.activity, "5b25530109167b0cb1cff48c", "94df37e9dcc845d5d409b7437c5fe408de8ec57f");
        Chartboost.onCreate(this.activity);

        Chartboost.cacheInterstitial(CBLocation.LOCATION_DEFAULT);
        Chartboost.cacheRewardedVideo(CBLocation.LOCATION_DEFAULT);

        interstitialRequests = 0;
        interstitialRate = 1;
    }

    @Override
    public void showInterstitialAd(){
        interstitialRequests++;
        if (Chartboost.hasInterstitial(CBLocation.LOCATION_DEFAULT)) {
            if(interstitialRequests >= interstitialRate) {
                interstitialRequests = 0;
                System.out.println("Interstitial Showing");
                Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);
            }
        }else{
            System.out.println("Interstitial not ready");
            Chartboost.cacheInterstitial(CBLocation.LOCATION_DEFAULT);
        }
    }

    @Override
    public boolean rewardAdReady(){
        System.out.println(Chartboost.hasRewardedVideo(CBLocation.LOCATION_DEFAULT));
        if(!Chartboost.hasRewardedVideo(CBLocation.LOCATION_DEFAULT)){
            System.out.println("Reward ad not ready");
            Chartboost.cacheRewardedVideo(CBLocation.LOCATION_DEFAULT);
        }

        return Chartboost.hasRewardedVideo(CBLocation.LOCATION_DEFAULT);
    }

    @Override
    public void showRewardAd(){
        Chartboost.showRewardedVideo(CBLocation.LOCATION_DEFAULT);
        Chartboost.cacheRewardedVideo(CBLocation.LOCATION_DEFAULT);
    }

    @Override
    public boolean isEURegion(){
        return consentManager.isEURegion();
    }

    @Override
    public boolean isConsentFormNeeded() {
        return consentManager.isConsentFormNeeded();
    }

    @Override
    public void setPersonalizedAdStatus(boolean isPersonalized) {
        if(isPersonalized){
            consentManager.setConsentStatus(GDPRConsentManager.ConsentStat.Personalized);
        }else{
            consentManager.setConsentStatus(GDPRConsentManager.ConsentStat.NonPersonalized);
        }
    }

    @Override
    public void setPersonalizedAd(){
        System.out.println("Setting personalized ads");
        Chartboost.restrictDataCollection(activity,false);
    }

    @Override
    public void setNonPersonalizedAd(){
        System.out.println("Setting non-personalized ads");
        Chartboost.restrictDataCollection(activity,true);
    }

    @Override
    public void setInterstitialRate(int rate) {
        //number of requests for one ad
        interstitialRate = rate;
    }

    @Override
    public void setGameHandle(Poof game){
        this.game = game;
    }
}
