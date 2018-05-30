package advertisements;

import android.os.Bundle;

import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;

import static com.google.ads.consent.ConsentInformation.getInstance;

public class ConsentListener implements ConsentInfoUpdateListener {
        public ConsentStatus status;
        private AndroidAd adHandle;

        ConsentListener(AndroidAd adHandle){
            this.adHandle = adHandle;
        }

        @Override
        public void onConsentInfoUpdated(ConsentStatus consentStatus) {

            adHandle.isEALocation = getInstance(adHandle.context).isRequestLocationInEeaOrUnknown();
            System.out.println("Is EEA:" + adHandle.isEALocation);
            System.out.println("Consent:"+consentStatus);

            //act according to consent status
            if(consentStatus == ConsentStatus.PERSONALIZED || !getInstance(adHandle.context).isRequestLocationInEeaOrUnknown()){
                adHandle.setPersonalizedAd();
            }else if(consentStatus == ConsentStatus.NON_PERSONALIZED){
                //create non personalized ad request
                adHandle.setNonPersonalizedAd();
            }else{
                adHandle.game.showGDPRConsentPage();
            }
        }

        @Override
        public void onFailedToUpdateConsentInfo(String errorDescription) {
            System.out.println("s\ns\ns\ns\ns\ns\ns");
        }
}
