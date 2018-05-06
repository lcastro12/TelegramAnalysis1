package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.internal.bu.C0153a;

public final class bp {

    public interface C0147a {
        void mo1783a(ce ceVar);
    }

    public static cg m221a(Context context, C0153a c0153a, C0193h c0193h, cq cqVar, aw awVar, C0147a c0147a) {
        cg bqVar = new bq(context, c0153a, c0193h, cqVar, awVar, c0147a);
        bqVar.start();
        return bqVar;
    }
}
