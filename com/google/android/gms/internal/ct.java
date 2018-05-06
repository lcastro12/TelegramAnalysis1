package com.google.android.gms.internal;

import android.content.Context;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class ct extends cr {
    public ct(cq cqVar, boolean z) {
        super(cqVar, z);
    }

    private static WebResourceResponse m924b(Context context, String str, String str2) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str2).openConnection();
        try {
            ci.m263a(context, str, true, httpURLConnection);
            httpURLConnection.connect();
            WebResourceResponse webResourceResponse = new WebResourceResponse("application/javascript", "UTF-8", new ByteArrayInputStream(ci.m257a(new InputStreamReader(httpURLConnection.getInputStream())).getBytes("UTF-8")));
            return webResourceResponse;
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
        try {
            if (!"mraid.js".equalsIgnoreCase(new File(url).getName())) {
                return super.shouldInterceptRequest(webView, url);
            }
            if (webView instanceof cq) {
                cq cqVar = (cq) webView;
                cqVar.aw().m312S();
                if (cqVar.av().ex) {
                    cn.m298p("shouldInterceptRequest(http://media.admob.com/mraid/v1/mraid_app_interstitial.js)");
                    return m924b(cqVar.getContext(), this.fG.ay().hP, "http://media.admob.com/mraid/v1/mraid_app_interstitial.js");
                } else if (cqVar.az()) {
                    cn.m298p("shouldInterceptRequest(http://media.admob.com/mraid/v1/mraid_app_expanded_banner.js)");
                    return m924b(cqVar.getContext(), this.fG.ay().hP, "http://media.admob.com/mraid/v1/mraid_app_expanded_banner.js");
                } else {
                    cn.m298p("shouldInterceptRequest(http://media.admob.com/mraid/v1/mraid_app_banner.js)");
                    return m924b(cqVar.getContext(), this.fG.ay().hP, "http://media.admob.com/mraid/v1/mraid_app_banner.js");
                }
            }
            cn.m299q("Tried to intercept request from a WebView that wasn't an AdWebView.");
            return super.shouldInterceptRequest(webView, url);
        } catch (IOException e) {
            cn.m299q("Could not fetching MRAID JS. " + e.getMessage());
            return super.shouldInterceptRequest(webView, url);
        }
    }
}
