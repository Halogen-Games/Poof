package advertisements;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.halogengames.poof.AndroidLauncher;
import com.halogengames.poof.advertisement.AdInterface;

import java.lang.ref.WeakReference;

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

    public static void addBannerViewToLayout(Context context, RelativeLayout layout) {
        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        //test banner for now
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        layout.addView(adView, adParams);
    }

    @Override
    public void showInterstitialAd() {
        handler.sendEmptyMessage(SHOW_INTERSTITIAL);
    }
}
