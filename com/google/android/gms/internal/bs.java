package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.internal.br.C0152a;

public abstract class bs extends cg {
    private final bu eI;
    private final C0152a gx;

    public static final class C0907a extends bs {
        private final Context mContext;

        public C0907a(Context context, bu buVar, C0152a c0152a) {
            super(buVar, c0152a);
            this.mContext = context;
        }

        public void ae() {
        }

        public by af() {
            return bz.m1352a(this.mContext, new am());
        }
    }

    public static final class C0908b extends bs implements ConnectionCallbacks, OnConnectionFailedListener {
        private final Object eJ = new Object();
        private final C0152a gx;
        private final bt gy;

        public C0908b(Context context, bu buVar, C0152a c0152a) {
            super(buVar, c0152a);
            this.gx = c0152a;
            this.gy = new bt(context, this, this, buVar.eg.hR);
            this.gy.connect();
        }

        public void ae() {
            synchronized (this.eJ) {
                if (this.gy.isConnected() || this.gy.isConnecting()) {
                    this.gy.disconnect();
                }
            }
        }

        public by af() {
            by ai;
            synchronized (this.eJ) {
                try {
                    ai = this.gy.ai();
                } catch (IllegalStateException e) {
                    ai = null;
                }
            }
            return ai;
        }

        public void onConnected(Bundle connectionHint) {
            start();
        }

        public void onConnectionFailed(ConnectionResult result) {
            this.gx.mo809a(new bw(0));
        }

        public void onDisconnected() {
            cn.m295m("Disconnected from remote ad request service.");
        }
    }

    public bs(bu buVar, C0152a c0152a) {
        this.eI = buVar;
        this.gx = c0152a;
    }

    private static bw m919a(by byVar, bu buVar) {
        try {
            return byVar.mo812a(buVar);
        } catch (Throwable e) {
            cn.m293b("Could not fetch ad response from ad request service.", e);
            return null;
        }
    }

    public final void ac() {
        try {
            bw bwVar;
            by af = af();
            if (af == null) {
                bwVar = new bw(0);
            } else {
                bwVar = m919a(af, this.eI);
                if (bwVar == null) {
                    bwVar = new bw(0);
                }
            }
            ae();
            this.gx.mo809a(bwVar);
        } catch (Throwable th) {
            ae();
        }
    }

    public abstract void ae();

    public abstract by af();

    public final void onStop() {
        ae();
    }
}
