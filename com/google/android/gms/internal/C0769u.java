package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.C0115e;
import com.google.android.gms.dynamic.C0898c;
import com.google.android.gms.internal.ac.C0668a;
import com.google.android.gms.internal.ad.C0670a;

public final class C0769u extends C0115e<ad> {
    private static final C0769u er = new C0769u();

    private C0769u() {
        super("com.google.android.gms.ads.AdManagerCreatorImpl");
    }

    public static ac m1236a(Context context, C0771x c0771x, String str, av avVar) {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == 0) {
            ac b = er.m1237b(context, c0771x, str, avVar);
            if (b != null) {
                return b;
            }
        }
        cn.m295m("Using AdManager from the client jar.");
        return new C0934r(context, c0771x, str, avVar, new co(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, true));
    }

    private ac m1237b(Context context, C0771x c0771x, String str, av avVar) {
        try {
            return C0668a.m841f(((ad) m145t(context)).mo768a(C0898c.m1318g(context), c0771x, str, avVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE));
        } catch (Throwable e) {
            cn.m293b("Could not create remote AdManager.", e);
            return null;
        } catch (Throwable e2) {
            cn.m293b("Could not create remote AdManager.", e2);
            return null;
        }
    }

    protected ad m1238c(IBinder iBinder) {
        return C0670a.m843g(iBinder);
    }

    protected /* synthetic */ Object mo799d(IBinder iBinder) {
        return m1238c(iBinder);
    }
}
