package com.google.android.gms.internal;

import android.content.Context;
import android.os.SystemClock;
import com.google.android.gms.dynamic.C0898c;
import com.google.android.gms.internal.at.C0128a;

public final class as implements C0128a {
    private final aw dZ;
    private final Object eJ = new Object();
    private final C0770v em;
    private final String fd;
    private final long fe;
    private final ao ff;
    private final C0771x fg;
    private ax fh;
    private int fi = -2;
    private final Context mContext;

    public as(Context context, String str, aw awVar, ap apVar, ao aoVar, C0770v c0770v, C0771x c0771x) {
        this.mContext = context;
        this.fd = str;
        this.dZ = awVar;
        this.fe = apVar.eV != -1 ? apVar.eV : 10000;
        this.ff = aoVar;
        this.em = c0770v;
        this.fg = c0771x;
    }

    private ax m860P() {
        cn.m297o("Instantiating mediation adapter: " + this.fd);
        try {
            return this.dZ.mo774g(this.fd);
        } catch (Throwable e) {
            cn.m292a("Could not instantiate mediation adapter: " + this.fd, e);
            return null;
        }
    }

    private void m863a(long j, long j2, long j3, long j4) {
        while (this.fi == -2) {
            m867b(j, j2, j3, j4);
        }
    }

    private void m864a(ar arVar) {
        try {
            if (this.fg.ex) {
                this.fh.mo775a(C0898c.m1318g(this.mContext), this.em, this.ff.eS, arVar);
            } else {
                this.fh.mo776a(C0898c.m1318g(this.mContext), this.fg, this.em, this.ff.eS, arVar);
            }
        } catch (Throwable e) {
            cn.m293b("Could not request ad from mediation adapter.", e);
            mo773d(5);
        }
    }

    private void m867b(long j, long j2, long j3, long j4) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j5 = j2 - (elapsedRealtime - j);
        elapsedRealtime = j4 - (elapsedRealtime - j3);
        if (j5 <= 0 || elapsedRealtime <= 0) {
            cn.m297o("Timed out waiting for adapter.");
            this.fi = 3;
            return;
        }
        try {
            this.eJ.wait(Math.min(j5, elapsedRealtime));
        } catch (InterruptedException e) {
            this.fi = -1;
        }
    }

    public at m870b(long j, long j2) {
        at atVar;
        synchronized (this.eJ) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            final ar arVar = new ar();
            cm.hO.post(new Runnable(this) {
                final /* synthetic */ as fk;

                public void run() {
                    synchronized (this.fk.eJ) {
                        if (this.fk.fi != -2) {
                            return;
                        }
                        this.fk.fh = this.fk.m860P();
                        if (this.fk.fh == null) {
                            this.fk.mo773d(4);
                            return;
                        }
                        arVar.m1327a(this.fk);
                        this.fk.m864a(arVar);
                    }
                }
            });
            m863a(elapsedRealtime, this.fe, j, j2);
            atVar = new at(this.ff, this.fh, this.fd, arVar, this.fi);
        }
        return atVar;
    }

    public void cancel() {
        synchronized (this.eJ) {
            try {
                if (this.fh != null) {
                    this.fh.destroy();
                }
            } catch (Throwable e) {
                cn.m293b("Could not destroy mediation adapter.", e);
            }
            this.fi = -1;
            this.eJ.notify();
        }
    }

    public void mo773d(int i) {
        synchronized (this.eJ) {
            this.fi = i;
            this.eJ.notify();
        }
    }
}
