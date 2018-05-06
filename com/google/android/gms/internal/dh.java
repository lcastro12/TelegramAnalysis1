package com.google.android.gms.internal;

import android.util.Log;

public final class dh {
    private final String li;

    public dh(String str) {
        this.li = (String) dm.m392e(str);
    }

    public void m363a(String str, String str2, Throwable th) {
        if (m368x(6)) {
            Log.e(str, str2, th);
        }
    }

    public void m364b(String str, String str2) {
        if (m368x(3)) {
            Log.d(str, str2);
        }
    }

    public void m365c(String str, String str2) {
        if (m368x(5)) {
            Log.w(str, str2);
        }
    }

    public void m366d(String str, String str2) {
        if (m368x(6)) {
            Log.e(str, str2);
        }
    }

    public void m367e(String str, String str2) {
        if (!m368x(4)) {
        }
    }

    public boolean m368x(int i) {
        return Log.isLoggable(this.li, i);
    }
}
