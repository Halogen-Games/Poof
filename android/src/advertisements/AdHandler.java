package advertisements;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.halogengames.poof.AndroidLauncher;

public class AdHandler extends Handler {

    private AndroidAd myAd;

    AdHandler(AndroidAd andAd) {
        myAd = andAd;
    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what) {
            case AndroidAd.SHOW_INTERSTITIAL:
            {
                if (AndroidLauncher.mInterstitialAd.isLoaded()) {
                    AndroidLauncher.mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                break;
            }
            case AndroidAd.PERSONALIZED_AD:
            {
                myAd.setPersonalizedAd();
                break;
            }
            case AndroidAd.NON_PERSONALIZED_AD:
            {
                myAd.setNonPersonalizedAd();
                break;
            }
        }
    }
}
