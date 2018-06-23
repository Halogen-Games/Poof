package com.halogengames.poof.advertisement;

import com.halogengames.poof.Poof;

public interface AdInterface {
    void showInterstitialAd();
    boolean rewardAdReady();
    void showRewardAd();

    boolean isEURegion();
    boolean isConsentFormNeeded();
    void setPersonalizedAdStatus(boolean isPersonalized);

    void setInterstitialRate(int rate);

    void setGameHandle(Poof game);

    void setPersonalizedAd();
    void setNonPersonalizedAd();
}
