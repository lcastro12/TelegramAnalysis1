package com.google.android.gms.internal;

import android.content.Context;
import android.webkit.WebSettings;

public final class ck {
    public static void m284a(Context context, WebSettings webSettings) {
        cj.m278a(context, webSettings);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
    }

    public static String getDefaultUserAgent(Context context) {
        return WebSettings.getDefaultUserAgent(context);
    }
}
