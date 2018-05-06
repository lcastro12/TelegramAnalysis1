package com.google.android.gms.maps.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.C0898c;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.C0212c.C0798a;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public class C0226q {
    private static Context pW;
    private static C0212c pX;

    private static <T> T m727a(ClassLoader classLoader, String str) {
        try {
            return C0226q.m728c(((ClassLoader) dm.m392e(classLoader)).loadClass(str));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to find dynamic class " + str);
        }
    }

    private static <T> T m728c(Class<?> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException("Unable to instantiate the dynamic class " + cls.getName());
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Unable to call the default constructor of " + cls.getName());
        }
    }

    private static boolean cI() {
        return C0226q.cJ() != null;
    }

    private static Class<?> cJ() {
        try {
            return Class.forName("com.google.android.gms.maps.internal.CreatorImpl");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static Context getRemoteContext(Context context) {
        if (pW == null) {
            if (C0226q.cI()) {
                pW = context;
            } else {
                pW = GooglePlayServicesUtil.getRemoteContext(context);
            }
        }
        return pW;
    }

    public static C0212c m729u(Context context) throws GooglePlayServicesNotAvailableException {
        dm.m392e(context);
        if (pX != null) {
            return pX;
        }
        C0226q.m730v(context);
        pX = C0226q.m731w(context);
        try {
            pX.mo1177a(C0898c.m1318g(C0226q.getRemoteContext(context).getResources()), (int) GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
            return pX;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    private static void m730v(Context context) throws GooglePlayServicesNotAvailableException {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        switch (isGooglePlayServicesAvailable) {
            case 0:
                return;
            default:
                throw new GooglePlayServicesNotAvailableException(isGooglePlayServicesAvailable);
        }
    }

    private static C0212c m731w(Context context) {
        if (!C0226q.cI()) {
            return C0798a.m1265J((IBinder) C0226q.m727a(C0226q.getRemoteContext(context).getClassLoader(), "com.google.android.gms.maps.internal.CreatorImpl"));
        }
        Log.i(C0226q.class.getSimpleName(), "Making Creator statically");
        return (C0212c) C0226q.m728c(C0226q.cJ());
    }
}
