package com.halogengames.poof.advertisement;

public interface AdInterface {
    int SHOW_INTERSTITIAL = -1;
    int PERSONALIZED_AD = -2;
    int NON_PERSONALIZED_AD = -3;

    void showInterstitialAd();
    boolean isEURegion();
    void genConsentFormIfNeeded();
    void setConsentStatus(int status);
    void setInterstitialRate(int rate);
}
