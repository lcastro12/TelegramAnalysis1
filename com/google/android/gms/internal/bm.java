package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.gms.dynamic.C0115e;
import com.google.android.gms.dynamic.C0898c;
import com.google.android.gms.internal.bn.C0700a;
import com.google.android.gms.internal.bo.C0702a;

public final class bm extends C0115e<bo> {
    private static final bm gl = new bm();

    private static final class C0146a extends Exception {
        public C0146a(String str) {
            super(str);
        }
    }

    private bm() {
        super("com.google.android.gms.ads.AdOverlayCreatorImpl");
    }

    public static bn m900a(Activity activity) {
        try {
            if (!m901b(activity)) {
                return gl.m902c(activity);
            }
            cn.m295m("Using AdOverlay from the client jar.");
            return new bf(activity);
        } catch (C0146a e) {
            cn.m299q(e.getMessage());
            return null;
        }
    }

    private static boolean m901b(Activity activity) throws C0146a {
        Intent intent = activity.getIntent();
        if (intent.hasExtra("com.google.android.gms.ads.internal.overlay.useClientJar")) {
            return intent.getBooleanExtra("com.google.android.gms.ads.internal.overlay.useClientJar", false);
        }
        throw new C0146a("Ad overlay requires the useClientJar flag in intent extras.");
    }

    private bn m902c(Activity activity) {
        try {
            return C0700a.m905m(((bo) m145t(activity)).mo808a(C0898c.m1318g(activity)));
        } catch (Throwable e) {
            cn.m293b("Could not create remote AdOverlay.", e);
            return null;
        } catch (Throwable e2) {
            cn.m293b("Could not create remote AdOverlay.", e2);
            return null;
        }
    }

    protected /* synthetic */ Object mo799d(IBinder iBinder) {
        return m904l(iBinder);
    }

    protected bo m904l(IBinder iBinder) {
        return C0702a.m907n(iBinder);
    }
}
