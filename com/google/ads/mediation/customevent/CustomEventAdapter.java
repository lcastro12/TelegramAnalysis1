package com.google.ads.mediation.customevent;

import android.app.Activity;
import android.view.View;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.android.gms.ads.mediation.customevent.CustomEventExtras;
import com.google.android.gms.internal.cn;

public final class CustomEventAdapter implements MediationBannerAdapter<CustomEventExtras, CustomEventServerParameters>, MediationInterstitialAdapter<CustomEventExtras, CustomEventServerParameters> {
    private View f80m;
    private CustomEventBanner f81n;
    private CustomEventInterstitial f82o;

    private static final class C0894a implements CustomEventBannerListener {
        private final MediationBannerListener f75k;
        private final CustomEventAdapter f76p;

        public C0894a(CustomEventAdapter customEventAdapter, MediationBannerListener mediationBannerListener) {
            this.f76p = customEventAdapter;
            this.f75k = mediationBannerListener;
        }

        public void onClick() {
            cn.m295m("Custom event adapter called onFailedToReceiveAd.");
            this.f75k.onClick(this.f76p);
        }

        public void onDismissScreen() {
            cn.m295m("Custom event adapter called onFailedToReceiveAd.");
            this.f75k.onDismissScreen(this.f76p);
        }

        public void onFailedToReceiveAd() {
            cn.m295m("Custom event adapter called onFailedToReceiveAd.");
            this.f75k.onFailedToReceiveAd(this.f76p, ErrorCode.NO_FILL);
        }

        public void onLeaveApplication() {
            cn.m295m("Custom event adapter called onFailedToReceiveAd.");
            this.f75k.onLeaveApplication(this.f76p);
        }

        public void onPresentScreen() {
            cn.m295m("Custom event adapter called onFailedToReceiveAd.");
            this.f75k.onPresentScreen(this.f76p);
        }

        public void onReceivedAd(View view) {
            cn.m295m("Custom event adapter called onReceivedAd.");
            this.f76p.m1312a(view);
            this.f75k.onReceivedAd(this.f76p);
        }
    }

    private class C0895b implements CustomEventInterstitialListener {
        private final MediationInterstitialListener f77l;
        private final CustomEventAdapter f78p;
        final /* synthetic */ CustomEventAdapter f79q;

        public C0895b(CustomEventAdapter customEventAdapter, CustomEventAdapter customEventAdapter2, MediationInterstitialListener mediationInterstitialListener) {
            this.f79q = customEventAdapter;
            this.f78p = customEventAdapter2;
            this.f77l = mediationInterstitialListener;
        }

        public void onDismissScreen() {
            cn.m295m("Custom event adapter called onDismissScreen.");
            this.f77l.onDismissScreen(this.f78p);
        }

        public void onFailedToReceiveAd() {
            cn.m295m("Custom event adapter called onFailedToReceiveAd.");
            this.f77l.onFailedToReceiveAd(this.f78p, ErrorCode.NO_FILL);
        }

        public void onLeaveApplication() {
            cn.m295m("Custom event adapter called onLeaveApplication.");
            this.f77l.onLeaveApplication(this.f78p);
        }

        public void onPresentScreen() {
            cn.m295m("Custom event adapter called onPresentScreen.");
            this.f77l.onPresentScreen(this.f78p);
        }

        public void onReceivedAd() {
            cn.m295m("Custom event adapter called onReceivedAd.");
            this.f77l.onReceivedAd(this.f79q);
        }
    }

    private static <T> T m1311a(String str) {
        try {
            return Class.forName(str).newInstance();
        } catch (Throwable th) {
            cn.m299q("Could not instantiate custom event adapter: " + str + ". " + th.getMessage());
            return null;
        }
    }

    private void m1312a(View view) {
        this.f80m = view;
    }

    public void destroy() {
        if (this.f81n != null) {
            this.f81n.destroy();
        }
        if (this.f82o != null) {
            this.f82o.destroy();
        }
    }

    public Class<CustomEventExtras> getAdditionalParametersType() {
        return CustomEventExtras.class;
    }

    public View getBannerView() {
        return this.f80m;
    }

    public Class<CustomEventServerParameters> getServerParametersType() {
        return CustomEventServerParameters.class;
    }

    public void requestBannerAd(MediationBannerListener listener, Activity activity, CustomEventServerParameters serverParameters, AdSize adSize, MediationAdRequest mediationAdRequest, CustomEventExtras customEventExtras) {
        this.f81n = (CustomEventBanner) m1311a(serverParameters.className);
        if (this.f81n == null) {
            listener.onFailedToReceiveAd(this, ErrorCode.INTERNAL_ERROR);
        } else {
            this.f81n.requestBannerAd(new C0894a(this, listener), activity, serverParameters.label, serverParameters.parameter, adSize, mediationAdRequest, customEventExtras == null ? null : customEventExtras.getExtra(serverParameters.label));
        }
    }

    public void requestInterstitialAd(MediationInterstitialListener listener, Activity activity, CustomEventServerParameters serverParameters, MediationAdRequest mediationAdRequest, CustomEventExtras customEventExtras) {
        this.f82o = (CustomEventInterstitial) m1311a(serverParameters.className);
        if (this.f82o == null) {
            listener.onFailedToReceiveAd(this, ErrorCode.INTERNAL_ERROR);
        } else {
            this.f82o.requestInterstitialAd(new C0895b(this, this, listener), activity, serverParameters.label, serverParameters.parameter, mediationAdRequest, customEventExtras == null ? null : customEventExtras.getExtra(serverParameters.label));
        }
    }

    public void showInterstitial() {
        this.f82o.showInterstitial();
    }
}
