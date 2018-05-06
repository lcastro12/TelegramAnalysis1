package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.internal.by.C0704a;
import com.google.android.gms.location.LocationStatusCodes;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public final class bz extends C0704a {
    private static final Object gL = new Object();
    private static bz gM;
    private final al gN;
    private final Context mContext;

    private bz(Context context, al alVar) {
        this.mContext = context;
        this.gN = alVar;
    }

    private static bw m1350a(final Context context, al alVar, final bu buVar) {
        cn.m295m("Starting ad request from service.");
        alVar.init();
        cd cdVar = new cd(context);
        if (cdVar.hs == -1) {
            cn.m295m("Device is offline.");
            return new bw(2);
        }
        final cb cbVar = new cb();
        final String a = ca.m233a(buVar, cdVar, alVar.mo771a(250));
        if (a == null) {
            return new bw(0);
        }
        cm.hO.post(new Runnable() {
            public void run() {
                cq a = cq.m303a(context, new C0771x(), false, false, null, buVar.eg);
                a.setWillNotDraw(true);
                cbVar.m241b(a);
                cr aw = a.aw();
                aw.m316a("/invalidRequest", cbVar.gU);
                aw.m316a("/loadAdURL", cbVar.gV);
                aw.m316a("/log", ah.eE);
                cn.m295m("Getting the ad request URL.");
                a.loadDataWithBaseURL("http://googleads.g.doubleclick.net", "<!DOCTYPE html><html><head><script src=\"http://googleads.g.doubleclick.net/mads/static/sdk/native/sdk-core-v40.js\"></script><script>AFMA_buildAdURL(" + a + ");</script></head><body></body></html>", "text/html", "UTF-8", null);
            }
        });
        a = cbVar.aj();
        return TextUtils.isEmpty(a) ? new bw(cbVar.getErrorCode()) : m1351a(context, buVar.eg.hP, a);
    }

    private static bw m1351a(Context context, String str, String str2) {
        HttpURLConnection httpURLConnection;
        try {
            int responseCode;
            bw bwVar;
            cc ccVar = new cc();
            URL url = new URL(str2);
            int i = 0;
            while (true) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                ci.m263a(context, str, false, httpURLConnection);
                responseCode = httpURLConnection.getResponseCode();
                Map headerFields = httpURLConnection.getHeaderFields();
                if (responseCode < 200 || responseCode >= 300) {
                    m1353a(url.toString(), headerFields, null, responseCode);
                    if (responseCode < 300 || responseCode >= 400) {
                        break;
                    }
                    Object headerField = httpURLConnection.getHeaderField("Location");
                    if (TextUtils.isEmpty(headerField)) {
                        cn.m299q("No location header to follow redirect.");
                        bwVar = new bw(0);
                        httpURLConnection.disconnect();
                        return bwVar;
                    }
                    url = new URL(headerField);
                    i++;
                    if (i > 5) {
                        cn.m299q("Too many redirects.");
                        bwVar = new bw(0);
                        httpURLConnection.disconnect();
                        return bwVar;
                    }
                    ccVar.m252d(headerFields);
                    httpURLConnection.disconnect();
                } else {
                    String url2 = url.toString();
                    String a = ci.m257a(new InputStreamReader(httpURLConnection.getInputStream()));
                    m1353a(url2, headerFields, a, responseCode);
                    ccVar.m251a(url2, headerFields, a);
                    bwVar = ccVar.ak();
                    httpURLConnection.disconnect();
                    return bwVar;
                }
            }
            cn.m299q("Received error HTTP response code: " + responseCode);
            bwVar = new bw(0);
            httpURLConnection.disconnect();
            return bwVar;
        } catch (IOException e) {
            cn.m299q("Error while connecting to ad server: " + e.getMessage());
            return new bw(2);
        } catch (Throwable th) {
            httpURLConnection.disconnect();
        }
    }

    public static bz m1352a(Context context, al alVar) {
        bz bzVar;
        synchronized (gL) {
            if (gM == null) {
                gM = new bz(context.getApplicationContext(), alVar);
            }
            bzVar = gM;
        }
        return bzVar;
    }

    private static void m1353a(String str, Map<String, List<String>> map, String str2, int i) {
        if (cn.m294k(2)) {
            cn.m298p("Http Response: {\n  URL:\n    " + str + "\n  Headers:");
            if (map != null) {
                for (String str3 : map.keySet()) {
                    cn.m298p("    " + str3 + ":");
                    for (String str32 : (List) map.get(str32)) {
                        cn.m298p("      " + str32);
                    }
                }
            }
            cn.m298p("  Body:");
            if (str2 != null) {
                for (int i2 = 0; i2 < Math.min(str2.length(), 100000); i2 += LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                    cn.m298p(str2.substring(i2, Math.min(str2.length(), i2 + LocationStatusCodes.GEOFENCE_NOT_AVAILABLE)));
                }
            } else {
                cn.m298p("    null");
            }
            cn.m298p("  Response Code:\n    " + i + "\n}");
        }
    }

    public bw mo812a(bu buVar) {
        return m1350a(this.mContext, this.gN, buVar);
    }
}
