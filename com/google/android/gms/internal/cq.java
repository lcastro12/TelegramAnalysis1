package com.google.android.gms.internal;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

public final class cq extends WebView implements DownloadListener {
    private final Object eJ = new Object();
    private C0771x fg;
    private final C0193h go;
    private final cr hT;
    private final MutableContextWrapper hU;
    private final co hV;
    private bf hW;
    private boolean hX;
    private boolean hY;

    private cq(MutableContextWrapper mutableContextWrapper, C0771x c0771x, boolean z, boolean z2, C0193h c0193h, co coVar) {
        super(mutableContextWrapper);
        this.hU = mutableContextWrapper;
        this.fg = c0771x;
        this.hX = z;
        this.go = c0193h;
        this.hV = coVar;
        setBackgroundColor(0);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        ci.m261a((Context) mutableContextWrapper, coVar.hP, settings);
        if (VERSION.SDK_INT >= 17) {
            ck.m284a(getContext(), settings);
        } else if (VERSION.SDK_INT >= 11) {
            cj.m278a(getContext(), settings);
        }
        setDownloadListener(this);
        if (VERSION.SDK_INT >= 11) {
            this.hT = new ct(this, z2);
        } else {
            this.hT = new cr(this, z2);
        }
        setWebViewClient(this.hT);
        if (VERSION.SDK_INT >= 14) {
            setWebChromeClient(new cu(this));
        } else if (VERSION.SDK_INT >= 11) {
            setWebChromeClient(new cs(this));
        }
        aA();
    }

    public static cq m303a(Context context, C0771x c0771x, boolean z, boolean z2, C0193h c0193h, co coVar) {
        return new cq(new MutableContextWrapper(context), c0771x, z, z2, c0193h, coVar);
    }

    private void aA() {
        synchronized (this.eJ) {
            if (this.hX || this.fg.ex) {
                if (VERSION.SDK_INT < 14) {
                    cn.m295m("Disabling hardware acceleration on an overlay.");
                    aB();
                } else {
                    cn.m295m("Enabling hardware acceleration on an overlay.");
                    aC();
                }
            } else if (VERSION.SDK_INT < 18) {
                cn.m295m("Disabling hardware acceleration on an AdView.");
                aB();
            } else {
                cn.m295m("Enabling hardware acceleration on an AdView.");
                aC();
            }
        }
    }

    private void aB() {
        synchronized (this.eJ) {
            if (!this.hY && VERSION.SDK_INT >= 11) {
                cj.m282c(this);
            }
            this.hY = true;
        }
    }

    private void aC() {
        synchronized (this.eJ) {
            if (this.hY && VERSION.SDK_INT >= 11) {
                cj.m283d(this);
            }
            this.hY = false;
        }
    }

    public void m304a(Context context, C0771x c0771x) {
        synchronized (this.eJ) {
            this.hU.setBaseContext(context);
            this.hW = null;
            this.fg = c0771x;
            this.hX = false;
            ci.m269b(this);
            loadUrl("about:blank");
            this.hT.reset();
        }
    }

    public void m305a(bf bfVar) {
        synchronized (this.eJ) {
            this.hW = bfVar;
        }
    }

    public void m306a(String str, Map<String, ?> map) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("javascript:AFMA_ReceiveMessage('");
        stringBuilder.append(str);
        stringBuilder.append("'");
        if (map != null) {
            try {
                String jSONObject = ci.m277l(map).toString();
                stringBuilder.append(",");
                stringBuilder.append(jSONObject);
            } catch (JSONException e) {
                cn.m299q("Could not convert AFMA event parameters to JSON.");
                return;
            }
        }
        stringBuilder.append(");");
        cn.m298p("Dispatching AFMA event: " + stringBuilder);
        loadUrl(stringBuilder.toString());
    }

    public void as() {
        Map hashMap = new HashMap(1);
        hashMap.put("version", this.hV.hP);
        m306a("onhide", hashMap);
    }

    public void at() {
        Map hashMap = new HashMap(1);
        hashMap.put("version", this.hV.hP);
        m306a("onshow", hashMap);
    }

    public bf au() {
        bf bfVar;
        synchronized (this.eJ) {
            bfVar = this.hW;
        }
        return bfVar;
    }

    public C0771x av() {
        C0771x c0771x;
        synchronized (this.eJ) {
            c0771x = this.fg;
        }
        return c0771x;
    }

    public cr aw() {
        return this.hT;
    }

    public C0193h ax() {
        return this.go;
    }

    public co ay() {
        return this.hV;
    }

    public boolean az() {
        boolean z;
        synchronized (this.eJ) {
            z = this.hX;
        }
        return z;
    }

    public void m307i(boolean z) {
        synchronized (this.eJ) {
            this.hX = z;
            aA();
        }
    }

    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long size) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse(url), mimeType);
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            cn.m295m("Couldn't find an Activity to view url/mimetype: " + url + " / " + mimeType);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onMeasure(int r10, int r11) {
        /*
        r9 = this;
        r0 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r7 = 8;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r4 = r9.eJ;
        monitor-enter(r4);
        r1 = r9.isInEditMode();	 Catch:{ all -> 0x008e }
        if (r1 != 0) goto L_0x0016;
    L_0x0012:
        r1 = r9.hX;	 Catch:{ all -> 0x008e }
        if (r1 == 0) goto L_0x001b;
    L_0x0016:
        super.onMeasure(r10, r11);	 Catch:{ all -> 0x008e }
        monitor-exit(r4);	 Catch:{ all -> 0x008e }
    L_0x001a:
        return;
    L_0x001b:
        r2 = android.view.View.MeasureSpec.getMode(r10);	 Catch:{ all -> 0x008e }
        r3 = android.view.View.MeasureSpec.getSize(r10);	 Catch:{ all -> 0x008e }
        r5 = android.view.View.MeasureSpec.getMode(r11);	 Catch:{ all -> 0x008e }
        r1 = android.view.View.MeasureSpec.getSize(r11);	 Catch:{ all -> 0x008e }
        if (r2 == r6) goto L_0x002f;
    L_0x002d:
        if (r2 != r8) goto L_0x00a7;
    L_0x002f:
        r2 = r3;
    L_0x0030:
        if (r5 == r6) goto L_0x0034;
    L_0x0032:
        if (r5 != r8) goto L_0x0035;
    L_0x0034:
        r0 = r1;
    L_0x0035:
        r5 = r9.fg;	 Catch:{ all -> 0x008e }
        r5 = r5.widthPixels;	 Catch:{ all -> 0x008e }
        if (r5 > r2) goto L_0x0041;
    L_0x003b:
        r2 = r9.fg;	 Catch:{ all -> 0x008e }
        r2 = r2.heightPixels;	 Catch:{ all -> 0x008e }
        if (r2 <= r0) goto L_0x0091;
    L_0x0041:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008e }
        r0.<init>();	 Catch:{ all -> 0x008e }
        r2 = "Not enough space to show ad. Needs ";
        r0 = r0.append(r2);	 Catch:{ all -> 0x008e }
        r2 = r9.fg;	 Catch:{ all -> 0x008e }
        r2 = r2.widthPixels;	 Catch:{ all -> 0x008e }
        r0 = r0.append(r2);	 Catch:{ all -> 0x008e }
        r2 = "x";
        r0 = r0.append(r2);	 Catch:{ all -> 0x008e }
        r2 = r9.fg;	 Catch:{ all -> 0x008e }
        r2 = r2.heightPixels;	 Catch:{ all -> 0x008e }
        r0 = r0.append(r2);	 Catch:{ all -> 0x008e }
        r2 = ", but only has ";
        r0 = r0.append(r2);	 Catch:{ all -> 0x008e }
        r0 = r0.append(r3);	 Catch:{ all -> 0x008e }
        r2 = "x";
        r0 = r0.append(r2);	 Catch:{ all -> 0x008e }
        r0 = r0.append(r1);	 Catch:{ all -> 0x008e }
        r0 = r0.toString();	 Catch:{ all -> 0x008e }
        com.google.android.gms.internal.cn.m299q(r0);	 Catch:{ all -> 0x008e }
        r0 = r9.getVisibility();	 Catch:{ all -> 0x008e }
        if (r0 == r7) goto L_0x0087;
    L_0x0083:
        r0 = 4;
        r9.setVisibility(r0);	 Catch:{ all -> 0x008e }
    L_0x0087:
        r0 = 0;
        r1 = 0;
        r9.setMeasuredDimension(r0, r1);	 Catch:{ all -> 0x008e }
    L_0x008c:
        monitor-exit(r4);	 Catch:{ all -> 0x008e }
        goto L_0x001a;
    L_0x008e:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x008e }
        throw r0;
    L_0x0091:
        r0 = r9.getVisibility();	 Catch:{ all -> 0x008e }
        if (r0 == r7) goto L_0x009b;
    L_0x0097:
        r0 = 0;
        r9.setVisibility(r0);	 Catch:{ all -> 0x008e }
    L_0x009b:
        r0 = r9.fg;	 Catch:{ all -> 0x008e }
        r0 = r0.widthPixels;	 Catch:{ all -> 0x008e }
        r1 = r9.fg;	 Catch:{ all -> 0x008e }
        r1 = r1.heightPixels;	 Catch:{ all -> 0x008e }
        r9.setMeasuredDimension(r0, r1);	 Catch:{ all -> 0x008e }
        goto L_0x008c;
    L_0x00a7:
        r2 = r0;
        goto L_0x0030;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.cq.onMeasure(int, int):void");
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.go != null) {
            this.go.m664a(event);
        }
        return super.onTouchEvent(event);
    }

    public void setContext(Context context) {
        this.hU.setBaseContext(context);
    }
}
