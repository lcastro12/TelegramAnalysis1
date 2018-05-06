package com.google.android.gms.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.google.android.gms.ads.AdActivity;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ci {
    private static final Object gL = new Object();
    private static boolean hJ = true;
    private static String hK;
    private static boolean hL = false;

    private static final class C0160a extends BroadcastReceiver {
        private C0160a() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.USER_PRESENT".equals(intent.getAction())) {
                ci.hJ = true;
            } else if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                ci.hJ = false;
            }
        }
    }

    public static String m257a(Readable readable) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        CharSequence allocate = CharBuffer.allocate(2048);
        while (true) {
            int read = readable.read(allocate);
            if (read == -1) {
                return stringBuilder.toString();
            }
            allocate.flip();
            stringBuilder.append(allocate, 0, read);
        }
    }

    private static JSONArray m258a(Collection<?> collection) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        for (Object a : collection) {
            m265a(jSONArray, a);
        }
        return jSONArray;
    }

    private static JSONArray m259a(Object[] objArr) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        for (Object a : objArr) {
            m265a(jSONArray, a);
        }
        return jSONArray;
    }

    private static JSONObject m260a(Bundle bundle) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        for (String str : bundle.keySet()) {
            m266a(jSONObject, str, bundle.get(str));
        }
        return jSONObject;
    }

    public static void m261a(Context context, String str, WebSettings webSettings) {
        webSettings.setUserAgentString(m268b(context, str));
    }

    public static void m262a(Context context, String str, List<String> list) {
        for (String clVar : list) {
            new cl(context, str, clVar).start();
        }
    }

    public static void m263a(Context context, String str, boolean z, HttpURLConnection httpURLConnection) {
        httpURLConnection.setConnectTimeout(60000);
        httpURLConnection.setInstanceFollowRedirects(z);
        httpURLConnection.setReadTimeout(60000);
        httpURLConnection.setRequestProperty("User-Agent", m268b(context, str));
        httpURLConnection.setUseCaches(false);
    }

    public static void m264a(WebView webView) {
        if (VERSION.SDK_INT >= 11) {
            cj.m280a(webView);
        }
    }

    private static void m265a(JSONArray jSONArray, Object obj) throws JSONException {
        if (obj instanceof Bundle) {
            jSONArray.put(m260a((Bundle) obj));
        } else if (obj instanceof Map) {
            jSONArray.put(m277l((Map) obj));
        } else if (obj instanceof Collection) {
            jSONArray.put(m258a((Collection) obj));
        } else if (obj instanceof Object[]) {
            jSONArray.put(m259a((Object[]) obj));
        } else {
            jSONArray.put(obj);
        }
    }

    private static void m266a(JSONObject jSONObject, String str, Object obj) throws JSONException {
        if (obj instanceof Bundle) {
            jSONObject.put(str, m260a((Bundle) obj));
        } else if (obj instanceof Map) {
            jSONObject.put(str, m277l((Map) obj));
        } else if (obj instanceof Collection) {
            if (str == null) {
                str = "null";
            }
            jSONObject.put(str, m258a((Collection) obj));
        } else if (obj instanceof Object[]) {
            jSONObject.put(str, m258a(Arrays.asList((Object[]) obj)));
        } else {
            jSONObject.put(str, obj);
        }
    }

    public static boolean m267a(PackageManager packageManager, String str, String str2) {
        return packageManager.checkPermission(str2, str) == 0;
    }

    public static boolean am() {
        return hJ;
    }

    public static int an() {
        return VERSION.SDK_INT >= 9 ? 6 : 0;
    }

    public static int ao() {
        return VERSION.SDK_INT >= 9 ? 7 : 1;
    }

    private static String m268b(final Context context, String str) {
        String str2;
        synchronized (gL) {
            if (hK != null) {
                str2 = hK;
            } else {
                if (VERSION.SDK_INT >= 17) {
                    hK = ck.getDefaultUserAgent(context);
                } else if (cm.ar()) {
                    hK = m273j(context);
                } else {
                    cm.hO.post(new Runnable() {
                        public void run() {
                            synchronized (ci.gL) {
                                ci.hK = ci.m273j(context);
                                ci.gL.notifyAll();
                            }
                        }
                    });
                    while (hK == null) {
                        try {
                            gL.wait();
                        } catch (InterruptedException e) {
                            str2 = hK;
                        }
                    }
                }
                hK += " (Mobile; " + str + ")";
                str2 = hK;
            }
        }
        return str2;
    }

    public static void m269b(WebView webView) {
        if (VERSION.SDK_INT >= 11) {
            cj.m281b(webView);
        }
    }

    public static boolean m270h(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context, AdActivity.CLASS_NAME);
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 65536);
        if (resolveActivity == null || resolveActivity.activityInfo == null) {
            cn.m299q("Could not find com.google.android.gms.ads.AdActivity, please make sure it is declared in AndroidManifest.xml.");
            return false;
        }
        boolean z;
        String str = "com.google.android.gms.ads.AdActivity requires the android:configChanges value to contain \"%s\".";
        if ((resolveActivity.activityInfo.configChanges & 16) == 0) {
            cn.m299q(String.format(str, new Object[]{"keyboard"}));
            z = false;
        } else {
            z = true;
        }
        if ((resolveActivity.activityInfo.configChanges & 32) == 0) {
            cn.m299q(String.format(str, new Object[]{"keyboardHidden"}));
            z = false;
        }
        if ((resolveActivity.activityInfo.configChanges & 128) == 0) {
            cn.m299q(String.format(str, new Object[]{"orientation"}));
            z = false;
        }
        if ((resolveActivity.activityInfo.configChanges & 256) == 0) {
            cn.m299q(String.format(str, new Object[]{"screenLayout"}));
            z = false;
        }
        if ((resolveActivity.activityInfo.configChanges & 512) == 0) {
            cn.m299q(String.format(str, new Object[]{"uiMode"}));
            z = false;
        }
        if ((resolveActivity.activityInfo.configChanges & 1024) == 0) {
            cn.m299q(String.format(str, new Object[]{"screenSize"}));
            z = false;
        }
        if ((resolveActivity.activityInfo.configChanges & 2048) != 0) {
            return z;
        }
        cn.m299q(String.format(str, new Object[]{"smallestScreenSize"}));
        return false;
    }

    public static void m272i(Context context) {
        if (!hL) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            context.getApplicationContext().registerReceiver(new C0160a(), intentFilter);
            hL = true;
        }
    }

    private static String m273j(Context context) {
        return new WebView(context).getSettings().getUserAgentString();
    }

    public static String m274j(String str) {
        return Uri.parse(str).buildUpon().query(null).build().toString();
    }

    public static JSONObject m277l(Map<String, ?> map) throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            for (String str : map.keySet()) {
                m266a(jSONObject, str, map.get(str));
            }
            return jSONObject;
        } catch (ClassCastException e) {
            throw new JSONException("Could not convert map to JSON: " + e.getMessage());
        }
    }
}
