package com.google.android.gms.internal;

import android.content.Context;

public final class an {
    private final aw dZ;
    private final bu eI;
    private final Object eJ = new Object();
    private final ap eK;
    private boolean eL = false;
    private as eM;
    private final Context mContext;

    public an(Context context, bu buVar, aw awVar, ap apVar) {
        this.mContext = context;
        this.eI = buVar;
        this.dZ = awVar;
        this.eK = apVar;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.gms.internal.at m178a(long r12, long r14) {
        /*
        r11 = this;
        r0 = "Starting mediation.";
        com.google.android.gms.internal.cn.m295m(r0);
        r0 = r11.eK;
        r0 = r0.eU;
        r8 = r0.iterator();
    L_0x000d:
        r0 = r8.hasNext();
        if (r0 == 0) goto L_0x008a;
    L_0x0013:
        r5 = r8.next();
        r5 = (com.google.android.gms.internal.ao) r5;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Trying mediation network: ";
        r0 = r0.append(r1);
        r1 = r5.eP;
        r0 = r0.append(r1);
        r0 = r0.toString();
        com.google.android.gms.internal.cn.m297o(r0);
        r0 = r5.eQ;
        r9 = r0.iterator();
    L_0x0037:
        r0 = r9.hasNext();
        if (r0 == 0) goto L_0x000d;
    L_0x003d:
        r2 = r9.next();
        r2 = (java.lang.String) r2;
        r10 = r11.eJ;
        monitor-enter(r10);
        r0 = r11.eL;	 Catch:{ all -> 0x0078 }
        if (r0 == 0) goto L_0x0052;
    L_0x004a:
        r0 = new com.google.android.gms.internal.at;	 Catch:{ all -> 0x0078 }
        r1 = -1;
        r0.<init>(r1);	 Catch:{ all -> 0x0078 }
        monitor-exit(r10);	 Catch:{ all -> 0x0078 }
    L_0x0051:
        return r0;
    L_0x0052:
        r0 = new com.google.android.gms.internal.as;	 Catch:{ all -> 0x0078 }
        r1 = r11.mContext;	 Catch:{ all -> 0x0078 }
        r3 = r11.dZ;	 Catch:{ all -> 0x0078 }
        r4 = r11.eK;	 Catch:{ all -> 0x0078 }
        r6 = r11.eI;	 Catch:{ all -> 0x0078 }
        r6 = r6.gB;	 Catch:{ all -> 0x0078 }
        r7 = r11.eI;	 Catch:{ all -> 0x0078 }
        r7 = r7.ed;	 Catch:{ all -> 0x0078 }
        r0.<init>(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ all -> 0x0078 }
        r11.eM = r0;	 Catch:{ all -> 0x0078 }
        monitor-exit(r10);	 Catch:{ all -> 0x0078 }
        r0 = r11.eM;
        r0 = r0.m870b(r12, r14);
        r1 = r0.fl;
        if (r1 != 0) goto L_0x007b;
    L_0x0072:
        r1 = "Adapter succeeded.";
        com.google.android.gms.internal.cn.m295m(r1);
        goto L_0x0051;
    L_0x0078:
        r0 = move-exception;
        monitor-exit(r10);	 Catch:{ all -> 0x0078 }
        throw r0;
    L_0x007b:
        r1 = r0.fn;
        if (r1 == 0) goto L_0x0037;
    L_0x007f:
        r1 = com.google.android.gms.internal.cm.hO;
        r2 = new com.google.android.gms.internal.an$1;
        r2.<init>(r11, r0);
        r1.post(r2);
        goto L_0x0037;
    L_0x008a:
        r0 = new com.google.android.gms.internal.at;
        r1 = 1;
        r0.<init>(r1);
        goto L_0x0051;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.an.a(long, long):com.google.android.gms.internal.at");
    }

    public void cancel() {
        synchronized (this.eJ) {
            this.eL = true;
            if (this.eM != null) {
                this.eM.cancel();
            }
        }
    }
}
