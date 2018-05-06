package com.google.android.gms.internal;

import com.google.android.gms.internal.at.C0128a;
import com.google.android.gms.internal.ay.C0685a;

public final class ar extends C0685a {
    private final Object eJ = new Object();
    private C0128a fb;
    private aq fc;

    public void m1326a(aq aqVar) {
        synchronized (this.eJ) {
            this.fc = aqVar;
        }
    }

    public void m1327a(C0128a c0128a) {
        synchronized (this.eJ) {
            this.fb = c0128a;
        }
    }

    public void onAdClosed() {
        synchronized (this.eJ) {
            if (this.fc != null) {
                this.fc.mo1779E();
            }
        }
    }

    public void onAdFailedToLoad(int error) {
        synchronized (this.eJ) {
            if (this.fb != null) {
                this.fb.mo773d(error == 3 ? 1 : 2);
                this.fb = null;
            }
        }
    }

    public void onAdLeftApplication() {
        synchronized (this.eJ) {
            if (this.fc != null) {
                this.fc.mo1780F();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onAdLoaded() {
        /*
        r3 = this;
        r1 = r3.eJ;
        monitor-enter(r1);
        r0 = r3.fb;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x0012;
    L_0x0007:
        r0 = r3.fb;	 Catch:{ all -> 0x001d }
        r2 = 0;
        r0.mo773d(r2);	 Catch:{ all -> 0x001d }
        r0 = 0;
        r3.fb = r0;	 Catch:{ all -> 0x001d }
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
    L_0x0011:
        return;
    L_0x0012:
        r0 = r3.fc;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x001b;
    L_0x0016:
        r0 = r3.fc;	 Catch:{ all -> 0x001d }
        r0.mo1782H();	 Catch:{ all -> 0x001d }
    L_0x001b:
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
        goto L_0x0011;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.ar.onAdLoaded():void");
    }

    public void onAdOpened() {
        synchronized (this.eJ) {
            if (this.fc != null) {
                this.fc.mo1781G();
            }
        }
    }

    public void mo785y() {
        synchronized (this.eJ) {
            if (this.fc != null) {
                this.fc.mo1778D();
            }
        }
    }
}
