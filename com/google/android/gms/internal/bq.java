package com.google.android.gms.internal;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.android.gms.internal.bp.C0147a;
import com.google.android.gms.internal.br.C0152a;
import com.google.android.gms.internal.bu.C0153a;
import com.google.android.gms.internal.cr.C0162a;
import org.json.JSONException;

public final class bq extends cg implements C0152a, C0162a {
    private final aw dZ;
    private final Object eJ = new Object();
    private ap eK;
    private final cq fG;
    private final C0147a gm;
    private final C0153a gn;
    private final C0193h go;
    private cg gp;
    private bw gq;
    private boolean gr = false;
    private an gs;
    private at gt;
    private final Context mContext;

    class C01481 implements Runnable {
        final /* synthetic */ bq gu;

        C01481(bq bqVar) {
            this.gu = bqVar;
        }

        public void run() {
            this.gu.onStop();
        }
    }

    class C01503 implements Runnable {
        final /* synthetic */ bq gu;

        C01503(bq bqVar) {
            this.gu = bqVar;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r7 = this;
            r0 = r7.gu;
            r6 = r0.eJ;
            monitor-enter(r6);
            r0 = r7.gu;	 Catch:{ all -> 0x005f }
            r0 = r0.gq;	 Catch:{ all -> 0x005f }
            r0 = r0.errorCode;	 Catch:{ all -> 0x005f }
            r1 = -2;
            if (r0 == r1) goto L_0x0014;
        L_0x0012:
            monitor-exit(r6);	 Catch:{ all -> 0x005f }
        L_0x0013:
            return;
        L_0x0014:
            r0 = r7.gu;	 Catch:{ all -> 0x005f }
            r0 = r0.fG;	 Catch:{ all -> 0x005f }
            r0 = r0.aw();	 Catch:{ all -> 0x005f }
            r1 = r7.gu;	 Catch:{ all -> 0x005f }
            r0.m314a(r1);	 Catch:{ all -> 0x005f }
            r0 = r7.gu;	 Catch:{ all -> 0x005f }
            r0 = r0.gq;	 Catch:{ all -> 0x005f }
            r0 = r0.errorCode;	 Catch:{ all -> 0x005f }
            r1 = -3;
            if (r0 != r1) goto L_0x0062;
        L_0x002e:
            r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005f }
            r0.<init>();	 Catch:{ all -> 0x005f }
            r1 = "Loading URL in WebView: ";
            r0 = r0.append(r1);	 Catch:{ all -> 0x005f }
            r1 = r7.gu;	 Catch:{ all -> 0x005f }
            r1 = r1.gq;	 Catch:{ all -> 0x005f }
            r1 = r1.fW;	 Catch:{ all -> 0x005f }
            r0 = r0.append(r1);	 Catch:{ all -> 0x005f }
            r0 = r0.toString();	 Catch:{ all -> 0x005f }
            com.google.android.gms.internal.cn.m298p(r0);	 Catch:{ all -> 0x005f }
            r0 = r7.gu;	 Catch:{ all -> 0x005f }
            r0 = r0.fG;	 Catch:{ all -> 0x005f }
            r1 = r7.gu;	 Catch:{ all -> 0x005f }
            r1 = r1.gq;	 Catch:{ all -> 0x005f }
            r1 = r1.fW;	 Catch:{ all -> 0x005f }
            r0.loadUrl(r1);	 Catch:{ all -> 0x005f }
        L_0x005d:
            monitor-exit(r6);	 Catch:{ all -> 0x005f }
            goto L_0x0013;
        L_0x005f:
            r0 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x005f }
            throw r0;
        L_0x0062:
            r0 = "Loading HTML in WebView.";
            com.google.android.gms.internal.cn.m298p(r0);	 Catch:{ all -> 0x005f }
            r0 = r7.gu;	 Catch:{ all -> 0x005f }
            r0 = r0.fG;	 Catch:{ all -> 0x005f }
            r1 = r7.gu;	 Catch:{ all -> 0x005f }
            r1 = r1.gq;	 Catch:{ all -> 0x005f }
            r1 = r1.fW;	 Catch:{ all -> 0x005f }
            r1 = com.google.android.gms.internal.ci.m274j(r1);	 Catch:{ all -> 0x005f }
            r2 = r7.gu;	 Catch:{ all -> 0x005f }
            r2 = r2.gq;	 Catch:{ all -> 0x005f }
            r2 = r2.gG;	 Catch:{ all -> 0x005f }
            r3 = "text/html";
            r4 = "UTF-8";
            r5 = 0;
            r0.loadDataWithBaseURL(r1, r2, r3, r4, r5);	 Catch:{ all -> 0x005f }
            goto L_0x005d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.bq.3.run():void");
        }
    }

    private static final class C0151a extends Exception {
        private final int gw;

        public C0151a(String str, int i) {
            super(str);
            this.gw = i;
        }

        public int getErrorCode() {
            return this.gw;
        }
    }

    public bq(Context context, C0153a c0153a, C0193h c0193h, cq cqVar, aw awVar, C0147a c0147a) {
        this.dZ = awVar;
        this.gm = c0147a;
        this.fG = cqVar;
        this.mContext = context;
        this.gn = c0153a;
        this.go = c0193h;
    }

    private void m909a(bu buVar, long j) throws C0151a {
        this.gs = new an(this.mContext, buVar, this.dZ, this.eK);
        this.gt = this.gs.m178a(j, 60000);
        switch (this.gt.fl) {
            case 0:
                return;
            case 1:
                throw new C0151a("No fill from any mediation ad networks.", 3);
            default:
                throw new C0151a("Unexpected mediation result: " + this.gt.fl, 0);
        }
    }

    private void ad() throws C0151a {
        if (this.gq.errorCode != -3) {
            if (TextUtils.isEmpty(this.gq.gG)) {
                throw new C0151a("No fill from ad server.", 3);
            } else if (this.gq.gI) {
                try {
                    this.eK = new ap(this.gq.gG);
                } catch (JSONException e) {
                    throw new C0151a("Could not parse mediation config: " + this.gq.gG, 0);
                }
            }
        }
    }

    private void m911b(long j) throws C0151a {
        cm.hO.post(new C01503(this));
        m915d(j);
    }

    private void m913c(long j) throws C0151a {
        while (m916e(j)) {
            if (this.gq != null) {
                this.gp = null;
                if (this.gq.errorCode != -2 && this.gq.errorCode != -3) {
                    throw new C0151a("There was a problem getting an ad response. ErrorCode: " + this.gq.errorCode, this.gq.errorCode);
                }
                return;
            }
        }
        throw new C0151a("Timed out waiting for ad response.", 2);
    }

    private void m915d(long j) throws C0151a {
        while (m916e(j)) {
            if (this.gr) {
                return;
            }
        }
        throw new C0151a("Timed out waiting for WebView to finish loading.", 2);
    }

    private boolean m916e(long j) throws C0151a {
        long elapsedRealtime = 60000 - (SystemClock.elapsedRealtime() - j);
        if (elapsedRealtime <= 0) {
            return false;
        }
        try {
            this.eJ.wait(elapsedRealtime);
            return true;
        } catch (InterruptedException e) {
            throw new C0151a("Ad request cancelled.", -1);
        }
    }

    public void mo809a(bw bwVar) {
        synchronized (this.eJ) {
            cn.m295m("Received ad response.");
            this.gq = bwVar;
            this.eJ.notify();
        }
    }

    public void mo798a(cq cqVar) {
        synchronized (this.eJ) {
            cn.m295m("WebView finished loading.");
            this.gr = true;
            this.eJ.notify();
        }
    }

    public void ac() {
        synchronized (this.eJ) {
            cn.m295m("AdLoaderBackgroundTask started.");
            bu buVar = new bu(this.gn, this.go.m666g().mo852a(this.mContext));
            int i = -2;
            try {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                this.gp = br.m223a(this.mContext, buVar, this);
                if (this.gp == null) {
                    throw new C0151a("Could not start the ad request service.", 0);
                }
                m913c(elapsedRealtime);
                ad();
                if (this.gq.gI) {
                    m909a(buVar, elapsedRealtime);
                } else {
                    m911b(elapsedRealtime);
                }
                final ce ceVar = new ce(buVar.gB, this.fG, this.gq.eW, i, this.gq.eX, this.gq.gK, this.gq.orientation, this.gq.fa, buVar.gE, this.gq.gI, this.gt != null ? this.gt.fm : null, this.gt != null ? this.gt.fn : null, this.gt != null ? this.gt.fo : null, this.eK, this.gt != null ? this.gt.fp : null, this.gq.gJ, this.gq.gH);
                cm.hO.post(new Runnable(this) {
                    final /* synthetic */ bq gu;

                    public void run() {
                        synchronized (this.gu.eJ) {
                            this.gu.gm.mo1783a(ceVar);
                        }
                    }
                });
            } catch (C0151a e) {
                i = e.getErrorCode();
                if (i == 3 || i == -1) {
                    cn.m297o(e.getMessage());
                } else {
                    cn.m299q(e.getMessage());
                }
                this.gq = new bw(i);
                cm.hO.post(new C01481(this));
            }
        }
    }

    public void onStop() {
        synchronized (this.eJ) {
            if (this.gp != null) {
                this.gp.cancel();
            }
            this.fG.stopLoading();
            ci.m264a(this.fG);
            if (this.gs != null) {
                this.gs.cancel();
            }
        }
    }
}
