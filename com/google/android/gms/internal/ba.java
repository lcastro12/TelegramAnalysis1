package com.google.android.gms.internal;

import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;

public final class ba<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> implements MediationBannerListener, MediationInterstitialListener {
    private final ay ft;

    class C01321 implements Runnable {
        final /* synthetic */ ba fu;

        C01321(ba baVar) {
            this.fu = baVar;
        }

        public void run() {
            try {
                this.fu.ft.mo785y();
            } catch (Throwable e) {
                cn.m293b("Could not call onAdClicked.", e);
            }
        }
    }

    class C01332 implements Runnable {
        final /* synthetic */ ba fu;

        C01332(ba baVar) {
            this.fu = baVar;
        }

        public void run() {
            try {
                this.fu.ft.onAdOpened();
            } catch (Throwable e) {
                cn.m293b("Could not call onAdOpened.", e);
            }
        }
    }

    class C01343 implements Runnable {
        final /* synthetic */ ba fu;

        C01343(ba baVar) {
            this.fu = baVar;
        }

        public void run() {
            try {
                this.fu.ft.onAdLoaded();
            } catch (Throwable e) {
                cn.m293b("Could not call onAdLoaded.", e);
            }
        }
    }

    class C01354 implements Runnable {
        final /* synthetic */ ba fu;

        C01354(ba baVar) {
            this.fu = baVar;
        }

        public void run() {
            try {
                this.fu.ft.onAdClosed();
            } catch (Throwable e) {
                cn.m293b("Could not call onAdClosed.", e);
            }
        }
    }

    class C01376 implements Runnable {
        final /* synthetic */ ba fu;

        C01376(ba baVar) {
            this.fu = baVar;
        }

        public void run() {
            try {
                this.fu.ft.onAdLeftApplication();
            } catch (Throwable e) {
                cn.m293b("Could not call onAdLeftApplication.", e);
            }
        }
    }

    class C01387 implements Runnable {
        final /* synthetic */ ba fu;

        C01387(ba baVar) {
            this.fu = baVar;
        }

        public void run() {
            try {
                this.fu.ft.onAdOpened();
            } catch (Throwable e) {
                cn.m293b("Could not call onAdOpened.", e);
            }
        }
    }

    class C01398 implements Runnable {
        final /* synthetic */ ba fu;

        C01398(ba baVar) {
            this.fu = baVar;
        }

        public void run() {
            try {
                this.fu.ft.onAdLoaded();
            } catch (Throwable e) {
                cn.m293b("Could not call onAdLoaded.", e);
            }
        }
    }

    class C01409 implements Runnable {
        final /* synthetic */ ba fu;

        C01409(ba baVar) {
            this.fu = baVar;
        }

        public void run() {
            try {
                this.fu.ft.onAdClosed();
            } catch (Throwable e) {
                cn.m293b("Could not call onAdClosed.", e);
            }
        }
    }

    public ba(ay ayVar) {
        this.ft = ayVar;
    }

    public void onClick(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        cn.m295m("Adapter called onClick.");
        if (cm.ar()) {
            try {
                this.ft.mo785y();
                return;
            } catch (Throwable e) {
                cn.m293b("Could not call onAdClicked.", e);
                return;
            }
        }
        cn.m299q("onClick must be called on the main UI thread.");
        cm.hO.post(new C01321(this));
    }

    public void onDismissScreen(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        cn.m295m("Adapter called onDismissScreen.");
        if (cm.ar()) {
            try {
                this.ft.onAdClosed();
                return;
            } catch (Throwable e) {
                cn.m293b("Could not call onAdClosed.", e);
                return;
            }
        }
        cn.m299q("onDismissScreen must be called on the main UI thread.");
        cm.hO.post(new C01354(this));
    }

    public void onDismissScreen(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        cn.m295m("Adapter called onDismissScreen.");
        if (cm.ar()) {
            try {
                this.ft.onAdClosed();
                return;
            } catch (Throwable e) {
                cn.m293b("Could not call onAdClosed.", e);
                return;
            }
        }
        cn.m299q("onDismissScreen must be called on the main UI thread.");
        cm.hO.post(new C01409(this));
    }

    public void onFailedToReceiveAd(MediationBannerAdapter<?, ?> mediationBannerAdapter, final ErrorCode errorCode) {
        cn.m295m("Adapter called onFailedToReceiveAd with error. " + errorCode);
        if (cm.ar()) {
            try {
                this.ft.onAdFailedToLoad(bb.m193a(errorCode));
                return;
            } catch (Throwable e) {
                cn.m293b("Could not call onAdFailedToLoad.", e);
                return;
            }
        }
        cn.m299q("onFailedToReceiveAd must be called on the main UI thread.");
        cm.hO.post(new Runnable(this) {
            final /* synthetic */ ba fu;

            public void run() {
                try {
                    this.fu.ft.onAdFailedToLoad(bb.m193a(errorCode));
                } catch (Throwable e) {
                    cn.m293b("Could not call onAdFailedToLoad.", e);
                }
            }
        });
    }

    public void onFailedToReceiveAd(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter, final ErrorCode errorCode) {
        cn.m295m("Adapter called onFailedToReceiveAd with error " + errorCode + ".");
        if (cm.ar()) {
            try {
                this.ft.onAdFailedToLoad(bb.m193a(errorCode));
                return;
            } catch (Throwable e) {
                cn.m293b("Could not call onAdFailedToLoad.", e);
                return;
            }
        }
        cn.m299q("onFailedToReceiveAd must be called on the main UI thread.");
        cm.hO.post(new Runnable(this) {
            final /* synthetic */ ba fu;

            public void run() {
                try {
                    this.fu.ft.onAdFailedToLoad(bb.m193a(errorCode));
                } catch (Throwable e) {
                    cn.m293b("Could not call onAdFailedToLoad.", e);
                }
            }
        });
    }

    public void onLeaveApplication(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        cn.m295m("Adapter called onLeaveApplication.");
        if (cm.ar()) {
            try {
                this.ft.onAdLeftApplication();
                return;
            } catch (Throwable e) {
                cn.m293b("Could not call onAdLeftApplication.", e);
                return;
            }
        }
        cn.m299q("onLeaveApplication must be called on the main UI thread.");
        cm.hO.post(new C01376(this));
    }

    public void onLeaveApplication(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        cn.m295m("Adapter called onLeaveApplication.");
        if (cm.ar()) {
            try {
                this.ft.onAdLeftApplication();
                return;
            } catch (Throwable e) {
                cn.m293b("Could not call onAdLeftApplication.", e);
                return;
            }
        }
        cn.m299q("onLeaveApplication must be called on the main UI thread.");
        cm.hO.post(new Runnable(this) {
            final /* synthetic */ ba fu;

            {
                this.fu = r1;
            }

            public void run() {
                try {
                    this.fu.ft.onAdLeftApplication();
                } catch (Throwable e) {
                    cn.m293b("Could not call onAdLeftApplication.", e);
                }
            }
        });
    }

    public void onPresentScreen(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        cn.m295m("Adapter called onPresentScreen.");
        if (cm.ar()) {
            try {
                this.ft.onAdOpened();
                return;
            } catch (Throwable e) {
                cn.m293b("Could not call onAdOpened.", e);
                return;
            }
        }
        cn.m299q("onPresentScreen must be called on the main UI thread.");
        cm.hO.post(new C01387(this));
    }

    public void onPresentScreen(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        cn.m295m("Adapter called onPresentScreen.");
        if (cm.ar()) {
            try {
                this.ft.onAdOpened();
                return;
            } catch (Throwable e) {
                cn.m293b("Could not call onAdOpened.", e);
                return;
            }
        }
        cn.m299q("onPresentScreen must be called on the main UI thread.");
        cm.hO.post(new C01332(this));
    }

    public void onReceivedAd(MediationBannerAdapter<?, ?> mediationBannerAdapter) {
        cn.m295m("Adapter called onReceivedAd.");
        if (cm.ar()) {
            try {
                this.ft.onAdLoaded();
                return;
            } catch (Throwable e) {
                cn.m293b("Could not call onAdLoaded.", e);
                return;
            }
        }
        cn.m299q("onReceivedAd must be called on the main UI thread.");
        cm.hO.post(new C01398(this));
    }

    public void onReceivedAd(MediationInterstitialAdapter<?, ?> mediationInterstitialAdapter) {
        cn.m295m("Adapter called onReceivedAd.");
        if (cm.ar()) {
            try {
                this.ft.onAdLoaded();
                return;
            } catch (Throwable e) {
                cn.m293b("Could not call onAdLoaded.", e);
                return;
            }
        }
        cn.m299q("onReceivedAd must be called on the main UI thread.");
        cm.hO.post(new C01343(this));
    }
}
