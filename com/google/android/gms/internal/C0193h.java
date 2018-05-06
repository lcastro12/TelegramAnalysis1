package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.view.MotionEvent;

public class C0193h {
    private final C0155c dA = new C0155c();
    private String dw = "googleads.g.doubleclick.net";
    private String dx = "/pagead/ads";
    private String[] dy = new String[]{".doubleclick.net", ".googleadservices.com", ".googlesyndication.com"};
    private C0174d dz;

    public C0193h(C0174d c0174d) {
        this.dz = c0174d;
    }

    private Uri m661a(Uri uri, Context context, String str, boolean z) throws C0194i {
        try {
            if (uri.getQueryParameter("ms") != null) {
                throw new C0194i("Query parameter already exists: ms");
            }
            return m662a(uri, "ms", z ? this.dz.mo853a(context, str) : this.dz.mo852a(context));
        } catch (UnsupportedOperationException e) {
            throw new C0194i("Provided Uri is not in a valid state");
        }
    }

    private Uri m662a(Uri uri, String str, String str2) throws UnsupportedOperationException {
        String uri2 = uri.toString();
        int indexOf = uri2.indexOf("&adurl");
        if (indexOf == -1) {
            indexOf = uri2.indexOf("?adurl");
        }
        return indexOf != -1 ? Uri.parse(new StringBuilder(uri2.substring(0, indexOf + 1)).append(str).append("=").append(str2).append("&").append(uri2.substring(indexOf + 1)).toString()) : uri.buildUpon().appendQueryParameter(str, str2).build();
    }

    public Uri m663a(Uri uri, Context context) throws C0194i {
        try {
            return m661a(uri, context, uri.getQueryParameter("ai"), true);
        } catch (UnsupportedOperationException e) {
            throw new C0194i("Provided Uri is not in a valid state");
        }
    }

    public void m664a(MotionEvent motionEvent) {
        this.dz.mo855a(motionEvent);
    }

    public boolean m665a(Uri uri) {
        if (uri == null) {
            throw new NullPointerException();
        }
        try {
            String host = uri.getHost();
            for (String endsWith : this.dy) {
                if (host.endsWith(endsWith)) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public C0174d m666g() {
        return this.dz;
    }
}
