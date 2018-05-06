package com.google.android.gms.internal;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.internal.ab.C0666a;

public final class C0935t extends C0666a {
    private final AdListener dT;

    public C0935t(AdListener adListener) {
        this.dT = adListener;
    }

    public void onAdClosed() {
        this.dT.onAdClosed();
    }

    public void onAdFailedToLoad(int errorCode) {
        this.dT.onAdFailedToLoad(errorCode);
    }

    public void onAdLeftApplication() {
        this.dT.onAdLeftApplication();
    }

    public void onAdLoaded() {
        this.dT.onAdLoaded();
    }

    public void onAdOpened() {
        this.dT.onAdOpened();
    }
}
