package com.google.android.gms.internal;

import android.util.Log;
import com.google.ads.AdRequest;

public final class cn {
    public static void m292a(String str, Throwable th) {
        if (m294k(3)) {
            Log.d(AdRequest.LOGTAG, str, th);
        }
    }

    public static void m293b(String str, Throwable th) {
        if (m294k(5)) {
            Log.w(AdRequest.LOGTAG, str, th);
        }
    }

    public static boolean m294k(int i) {
        return (i >= 5 || Log.isLoggable(AdRequest.LOGTAG, i)) && i != 2;
    }

    public static void m295m(String str) {
        if (m294k(3)) {
            Log.d(AdRequest.LOGTAG, str);
        }
    }

    public static void m296n(String str) {
        if (m294k(6)) {
            Log.e(AdRequest.LOGTAG, str);
        }
    }

    public static void m297o(String str) {
        if (m294k(4)) {
            Log.i(AdRequest.LOGTAG, str);
        }
    }

    public static void m298p(String str) {
        if (m294k(2)) {
            Log.v(AdRequest.LOGTAG, str);
        }
    }

    public static void m299q(String str) {
        if (m294k(5)) {
            Log.w(AdRequest.LOGTAG, str);
        }
    }
}
