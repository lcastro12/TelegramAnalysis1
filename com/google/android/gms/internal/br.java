package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.bs.C0907a;
import com.google.android.gms.internal.bs.C0908b;

public final class br {

    public interface C0152a {
        void mo809a(bw bwVar);
    }

    public static cg m223a(Context context, bu buVar, C0152a c0152a) {
        return buVar.eg.hS ? m224b(context, buVar, c0152a) : m225c(context, buVar, c0152a);
    }

    private static cg m224b(Context context, bu buVar, C0152a c0152a) {
        cn.m295m("Fetching ad response from local ad request service.");
        cg c0907a = new C0907a(context, buVar, c0152a);
        c0907a.start();
        return c0907a;
    }

    private static cg m225c(Context context, bu buVar, C0152a c0152a) {
        cn.m295m("Fetching ad response from remote ad request service.");
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0) {
            return new C0908b(context, buVar, c0152a);
        }
        cn.m299q("Failed to connect to remote ad request service.");
        return null;
    }
}
