package com.google.ads.mediation.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.google.ads.AdRequest.Gender;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import com.google.android.gms.internal.bb;
import com.google.android.gms.internal.cm;
import java.util.Date;
import java.util.Set;

public final class AdMobAdapter implements MediationBannerAdapter<AdMobExtras, AdMobServerParameters>, MediationInterstitialAdapter<AdMobExtras, AdMobServerParameters> {
    private AdView f73h;
    private InterstitialAd f74i;

    private static final class C0642a extends AdListener {
        private final AdMobAdapter f51j;
        private final MediationBannerListener f52k;

        public C0642a(AdMobAdapter adMobAdapter, MediationBannerListener mediationBannerListener) {
            this.f51j = adMobAdapter;
            this.f52k = mediationBannerListener;
        }

        public void onAdClosed() {
            this.f52k.onDismissScreen(this.f51j);
        }

        public void onAdFailedToLoad(int errorCode) {
            this.f52k.onFailedToReceiveAd(this.f51j, bb.m198f(errorCode));
        }

        public void onAdLeftApplication() {
            this.f52k.onLeaveApplication(this.f51j);
        }

        public void onAdLoaded() {
            this.f52k.onReceivedAd(this.f51j);
        }

        public void onAdOpened() {
            this.f52k.onClick(this.f51j);
            this.f52k.onPresentScreen(this.f51j);
        }
    }

    private static final class C0643b extends AdListener {
        private final AdMobAdapter f53j;
        private final MediationInterstitialListener f54l;

        public C0643b(AdMobAdapter adMobAdapter, MediationInterstitialListener mediationInterstitialListener) {
            this.f53j = adMobAdapter;
            this.f54l = mediationInterstitialListener;
        }

        public void onAdClosed() {
            this.f54l.onDismissScreen(this.f53j);
        }

        public void onAdFailedToLoad(int errorCode) {
            this.f54l.onFailedToReceiveAd(this.f53j, bb.m198f(errorCode));
        }

        public void onAdLeftApplication() {
            this.f54l.onLeaveApplication(this.f53j);
        }

        public void onAdLoaded() {
            this.f54l.onReceivedAd(this.f53j);
        }

        public void onAdOpened() {
            this.f54l.onPresentScreen(this.f53j);
        }
    }

    private static AdRequest m1310a(Context context, MediationAdRequest mediationAdRequest, AdMobExtras adMobExtras, AdMobServerParameters adMobServerParameters) {
        NetworkExtras adMobExtras2;
        Builder builder = new Builder();
        Date birthday = mediationAdRequest.getBirthday();
        if (birthday != null) {
            builder.setBirthday(birthday);
        }
        Gender gender = mediationAdRequest.getGender();
        if (gender != null) {
            builder.setGender(bb.m194a(gender));
        }
        Set<String> keywords = mediationAdRequest.getKeywords();
        if (keywords != null) {
            for (String addKeyword : keywords) {
                builder.addKeyword(addKeyword);
            }
        }
        if (mediationAdRequest.isTesting()) {
            builder.addTestDevice(cm.m290l(context));
        }
        if (adMobServerParameters.tagForChildDirectedTreatment != -1) {
            builder.tagForChildDirectedTreatment(adMobServerParameters.tagForChildDirectedTreatment == 1);
        }
        if (adMobExtras == null) {
            adMobExtras2 = new AdMobExtras(new Bundle());
        }
        Bundle extras = adMobExtras2.getExtras();
        extras.putInt("gw", 1);
        extras.putString("mad_hac", adMobServerParameters.allowHouseAds);
        extras.putBoolean("_noRefresh", true);
        builder.addNetworkExtras(adMobExtras2);
        return builder.build();
    }

    public void destroy() {
        if (this.f73h != null) {
            this.f73h.destroy();
            this.f73h = null;
        }
        if (this.f74i != null) {
            this.f74i = null;
        }
    }

    public Class<AdMobExtras> getAdditionalParametersType() {
        return AdMobExtras.class;
    }

    public View getBannerView() {
        return this.f73h;
    }

    public Class<AdMobServerParameters> getServerParametersType() {
        return AdMobServerParameters.class;
    }

    public void requestBannerAd(MediationBannerListener bannerListener, Activity activity, AdMobServerParameters serverParameters, AdSize adSize, MediationAdRequest mediationAdRequest, AdMobExtras extras) {
        this.f73h = new AdView(activity);
        this.f73h.setAdSize(new com.google.android.gms.ads.AdSize(adSize.getWidth(), adSize.getHeight()));
        this.f73h.setAdUnitId(serverParameters.adUnitId);
        this.f73h.setAdListener(new C0642a(this, bannerListener));
        this.f73h.loadAd(m1310a(activity, mediationAdRequest, extras, serverParameters));
    }

    public void requestInterstitialAd(MediationInterstitialListener interstitialListener, Activity activity, AdMobServerParameters serverParameters, MediationAdRequest mediationAdRequest, AdMobExtras extras) {
        this.f74i = new InterstitialAd(activity);
        this.f74i.setAdUnitId(serverParameters.adUnitId);
        this.f74i.setAdListener(new C0643b(this, interstitialListener));
        this.f74i.loadAd(m1310a(activity, mediationAdRequest, extras, serverParameters));
    }

    public void showInterstitial() {
        this.f74i.show();
    }
}
