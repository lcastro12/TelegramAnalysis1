package com.google.android.gms.internal;

import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.net.UrlQuerySanitizer.ParameterValuePair;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.HashMap;
import java.util.Map;

public class cr extends WebViewClient {
    private final Object eJ = new Object();
    private ag ey;
    protected final cq fG;
    private final HashMap<String, ai> hZ = new HashMap();
    private C0202q ia;
    private bi ib;
    private C0162a ic;
    private boolean id = false;
    private boolean ie;
    private bl f48if;

    public interface C0162a {
        void mo798a(cq cqVar);
    }

    public cr(cq cqVar, boolean z) {
        this.fG = cqVar;
        this.ie = z;
    }

    private void m309a(bh bhVar) {
        bf.m1337a(this.fG.getContext(), bhVar);
    }

    private static boolean m310b(Uri uri) {
        String scheme = uri.getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }

    private void m311c(Uri uri) {
        String path = uri.getPath();
        ai aiVar = (ai) this.hZ.get(path);
        if (aiVar != null) {
            Map hashMap = new HashMap();
            UrlQuerySanitizer urlQuerySanitizer = new UrlQuerySanitizer();
            urlQuerySanitizer.setAllowUnregisteredParamaters(true);
            urlQuerySanitizer.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
            urlQuerySanitizer.parseUrl(uri.toString());
            for (ParameterValuePair parameterValuePair : urlQuerySanitizer.getParameterList()) {
                hashMap.put(parameterValuePair.mParameter, parameterValuePair.mValue);
            }
            if (cn.m294k(2)) {
                cn.m298p("Received GMSG: " + path);
                for (String str : hashMap.keySet()) {
                    cn.m298p("  " + str + ": " + ((String) hashMap.get(str)));
                }
            }
            aiVar.mo770a(this.fG, hashMap);
            return;
        }
        cn.m299q("No GMSG handler found for GMSG: " + uri);
    }

    public final void m312S() {
        synchronized (this.eJ) {
            this.id = false;
            this.ie = true;
            final bf au = this.fG.au();
            if (au != null) {
                if (cm.ar()) {
                    au.m1341S();
                } else {
                    cm.hO.post(new Runnable(this) {
                        final /* synthetic */ cr ih;

                        public void run() {
                            au.m1341S();
                        }
                    });
                }
            }
        }
    }

    public final void m313a(be beVar) {
        bi biVar = null;
        boolean az = this.fG.az();
        C0202q c0202q = (!az || this.fG.av().ex) ? this.ia : null;
        if (!az) {
            biVar = this.ib;
        }
        m309a(new bh(beVar, c0202q, biVar, this.f48if, this.fG.ay()));
    }

    public final void m314a(C0162a c0162a) {
        this.ic = c0162a;
    }

    public void m315a(C0202q c0202q, bi biVar, ag agVar, bl blVar, boolean z) {
        m316a("/appEvent", new af(agVar));
        m316a("/canOpenURLs", ah.ez);
        m316a("/click", ah.eA);
        m316a("/close", ah.eB);
        m316a("/customClose", ah.eC);
        m316a("/httpTrack", ah.eD);
        m316a("/log", ah.eE);
        m316a("/open", ah.eF);
        m316a("/touch", ah.eG);
        m316a("/video", ah.eH);
        this.ia = c0202q;
        this.ib = biVar;
        this.ey = agVar;
        this.f48if = blVar;
        m320j(z);
    }

    public final void m316a(String str, ai aiVar) {
        this.hZ.put(str, aiVar);
    }

    public final void m317a(boolean z, int i) {
        C0202q c0202q = (!this.fG.az() || this.fG.av().ex) ? this.ia : null;
        m309a(new bh(c0202q, this.ib, this.f48if, this.fG, z, i, this.fG.ay()));
    }

    public final void m318a(boolean z, int i, String str) {
        bi biVar = null;
        boolean az = this.fG.az();
        C0202q c0202q = (!az || this.fG.av().ex) ? this.ia : null;
        if (!az) {
            biVar = this.ib;
        }
        m309a(new bh(c0202q, biVar, this.ey, this.f48if, this.fG, z, i, str, this.fG.ay()));
    }

    public final void m319a(boolean z, int i, String str, String str2) {
        bi biVar = null;
        boolean az = this.fG.az();
        C0202q c0202q = (!az || this.fG.av().ex) ? this.ia : null;
        if (!az) {
            biVar = this.ib;
        }
        m309a(new bh(c0202q, biVar, this.ey, this.f48if, this.fG, z, i, str, str2, this.fG.ay()));
    }

    public boolean aD() {
        boolean z;
        synchronized (this.eJ) {
            z = this.ie;
        }
        return z;
    }

    public final void m320j(boolean z) {
        this.id = z;
    }

    public final void onPageFinished(WebView webView, String url) {
        if (this.ic != null) {
            this.ic.mo798a(this.fG);
            this.ic = null;
        }
    }

    public final void reset() {
        synchronized (this.eJ) {
            this.hZ.clear();
            this.ia = null;
            this.ib = null;
            this.ic = null;
            this.ey = null;
            this.id = false;
            this.ie = false;
            this.f48if = null;
        }
    }

    public final boolean shouldOverrideUrlLoading(WebView webView, String url) {
        cn.m298p("AdWebView shouldOverrideUrlLoading: " + url);
        Uri parse = Uri.parse(url);
        if ("gmsg".equalsIgnoreCase(parse.getScheme()) && "mobileads.google.com".equalsIgnoreCase(parse.getHost())) {
            m311c(parse);
        } else if (this.id && m310b(parse)) {
            return super.shouldOverrideUrlLoading(webView, url);
        } else {
            if (this.fG.willNotDraw()) {
                cn.m299q("AdWebView unable to handle URL: " + url);
            } else {
                Uri uri;
                try {
                    C0193h ax = this.fG.ax();
                    if (ax != null && ax.m665a(parse)) {
                        parse = ax.m663a(parse, this.fG.getContext());
                    }
                    uri = parse;
                } catch (C0194i e) {
                    cn.m299q("Unable to append parameter to URL: " + url);
                    uri = parse;
                }
                m313a(new be("android.intent.action.VIEW", uri.toString(), null, null, null, null, null));
            }
        }
        return true;
    }
}
