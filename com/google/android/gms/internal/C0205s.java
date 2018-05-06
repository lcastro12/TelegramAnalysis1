package com.google.android.gms.internal;

import java.lang.ref.WeakReference;

public final class C0205s {
    private final Runnable el;
    private C0770v em;
    private boolean en = false;

    public C0205s(final C0934r c0934r) {
        this.el = new Runnable(this) {
            private final WeakReference<C0934r> eo = new WeakReference(c0934r);
            final /* synthetic */ C0205s eq;

            public void run() {
                this.eq.en = false;
                C0934r c0934r = (C0934r) this.eo.get();
                if (c0934r != null) {
                    c0934r.m1561b(this.eq.em);
                }
            }
        };
    }

    public void m695a(C0770v c0770v, long j) {
        if (this.en) {
            cn.m299q("An ad refresh is already scheduled.");
            return;
        }
        cn.m297o("Scheduling ad refresh " + j + " milliseconds from now.");
        this.em = c0770v;
        this.en = true;
        cm.hO.postDelayed(this.el, j);
    }

    public void cancel() {
        cm.hO.removeCallbacks(this.el);
    }

    public void m696d(C0770v c0770v) {
        m695a(c0770v, 60000);
    }
}
