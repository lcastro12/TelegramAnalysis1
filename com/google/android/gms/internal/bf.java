package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.AdActivity;
import com.google.android.gms.internal.bn.C0700a;
import com.google.android.gms.internal.cr.C0162a;

public final class bf extends C0700a {
    private final Activity fD;
    private bh fE;
    private bj fF;
    private cq fG;
    private C0143b fH;
    private bk fI;
    private FrameLayout fJ;
    private CustomViewCallback fK;
    private boolean fL = false;
    private boolean fM = false;
    private RelativeLayout fN;

    private static final class C0142a extends Exception {
        public C0142a(String str) {
            super(str);
        }
    }

    private static final class C0143b {
        public final LayoutParams fP;
        public final ViewGroup fQ;
        public final int index;

        public C0143b(cq cqVar) throws C0142a {
            this.fP = cqVar.getLayoutParams();
            ViewParent parent = cqVar.getParent();
            if (parent instanceof ViewGroup) {
                this.fQ = (ViewGroup) parent;
                this.index = this.fQ.indexOfChild(cqVar);
                this.fQ.removeView(cqVar);
                cqVar.m307i(true);
                return;
            }
            throw new C0142a("Could not get the parent of the WebView for an overlay.");
        }
    }

    class C06981 implements C0162a {
        final /* synthetic */ bf fO;

        C06981(bf bfVar) {
            this.fO = bfVar;
        }

        public void mo798a(cq cqVar) {
            cqVar.at();
        }
    }

    public bf(Activity activity) {
        this.fD = activity;
    }

    private void m1335T() {
        if (this.fD.isFinishing() && !this.fM) {
            this.fM = true;
            if (this.fD.isFinishing()) {
                if (this.fG != null) {
                    this.fG.as();
                    this.fN.removeView(this.fG);
                    if (this.fH != null) {
                        this.fG.m307i(false);
                        this.fH.fQ.addView(this.fG, this.fH.index, this.fH.fP);
                    }
                }
                if (this.fE != null && this.fE.fT != null) {
                    this.fE.fT.mo1776B();
                }
            }
        }
    }

    private static RelativeLayout.LayoutParams m1336a(int i, int i2, int i3, int i4) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i3, i4);
        layoutParams.setMargins(i, i2, 0, 0);
        layoutParams.addRule(10);
        layoutParams.addRule(9);
        return layoutParams;
    }

    public static void m1337a(Context context, bh bhVar) {
        Intent intent = new Intent();
        intent.setClassName(context, AdActivity.CLASS_NAME);
        intent.putExtra("com.google.android.gms.ads.internal.overlay.useClientJar", bhVar.eg.hS);
        bh.m894a(intent, bhVar);
        intent.addFlags(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END);
        context.startActivity(intent);
    }

    private void m1338e(boolean z) throws C0142a {
        this.fD.requestWindowFeature(1);
        Window window = this.fD.getWindow();
        window.setFlags(1024, 1024);
        setRequestedOrientation(this.fE.orientation);
        if (VERSION.SDK_INT >= 11) {
            cn.m295m("Enabling hardware acceleration on the AdActivity window.");
            cj.m279a(window);
        }
        this.fN = new RelativeLayout(this.fD);
        this.fN.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.fD.setContentView(this.fN);
        boolean aD = this.fE.fU.aw().aD();
        if (z) {
            this.fG = cq.m303a(this.fD, this.fE.fU.av(), true, aD, null, this.fE.eg);
            this.fG.aw().m315a(null, null, this.fE.fV, this.fE.fZ, true);
            this.fG.aw().m314a(new C06981(this));
            if (this.fE.fz != null) {
                this.fG.loadUrl(this.fE.fz);
            } else if (this.fE.fY != null) {
                this.fG.loadDataWithBaseURL(this.fE.fW, this.fE.fY, "text/html", "UTF-8", null);
            } else {
                throw new C0142a("No URL or HTML to display in ad overlay.");
            }
        }
        this.fG = this.fE.fU;
        this.fG.setContext(this.fD);
        this.fG.m305a(this);
        this.fN.addView(this.fG, -1, -1);
        if (!z) {
            this.fG.at();
        }
        m1345c(aD);
    }

    public bj m1339Q() {
        return this.fF;
    }

    public void m1340R() {
        if (this.fE != null) {
            setRequestedOrientation(this.fE.orientation);
        }
        if (this.fJ != null) {
            this.fD.setContentView(this.fN);
            this.fJ.removeAllViews();
            this.fJ = null;
        }
        if (this.fK != null) {
            this.fK.onCustomViewHidden();
            this.fK = null;
        }
    }

    public void m1341S() {
        this.fN.removeView(this.fI);
        m1345c(true);
    }

    public void m1342a(View view, CustomViewCallback customViewCallback) {
        this.fJ = new FrameLayout(this.fD);
        this.fJ.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.fJ.addView(view, -1, -1);
        this.fD.setContentView(this.fJ);
        this.fK = customViewCallback;
    }

    public void m1343b(int i, int i2, int i3, int i4) {
        if (this.fF != null) {
            this.fF.setLayoutParams(m1336a(i, i2, i3, i4));
        }
    }

    public void m1344c(int i, int i2, int i3, int i4) {
        if (this.fF == null) {
            this.fF = new bj(this.fD, this.fG);
            this.fN.addView(this.fF, 0, m1336a(i, i2, i3, i4));
            this.fG.aw().m320j(false);
        }
    }

    public void m1345c(boolean z) {
        this.fI = new bk(this.fD, z ? 50 : 32);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(z ? 11 : 9);
        this.fI.m217d(this.fE.fX);
        this.fN.addView(this.fI, layoutParams);
    }

    public void close() {
        this.fD.finish();
    }

    public void m1346d(boolean z) {
        if (this.fI != null) {
            this.fI.m217d(z);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        boolean z = false;
        if (savedInstanceState != null) {
            z = savedInstanceState.getBoolean("com.google.android.gms.ads.internal.overlay.hasResumed", false);
        }
        this.fL = z;
        try {
            this.fE = bh.m893a(this.fD.getIntent());
            if (this.fE == null) {
                throw new C0142a("Could not get info for ad overlay.");
            }
            if (savedInstanceState == null) {
                if (this.fE.fT != null) {
                    this.fE.fT.mo1777C();
                }
                if (!(this.fE.ga == 1 || this.fE.fS == null)) {
                    this.fE.fS.mo1785y();
                }
            }
            switch (this.fE.ga) {
                case 1:
                    m1338e(false);
                    return;
                case 2:
                    this.fH = new C0143b(this.fE.fU);
                    m1338e(false);
                    return;
                case 3:
                    m1338e(true);
                    return;
                case 4:
                    if (this.fL) {
                        this.fD.finish();
                        return;
                    } else if (!bc.m199a(this.fD, this.fE.fR, this.fE.fZ)) {
                        this.fD.finish();
                        return;
                    } else {
                        return;
                    }
                default:
                    throw new C0142a("Could not determine ad overlay type.");
            }
        } catch (C0142a e) {
            cn.m299q(e.getMessage());
            this.fD.finish();
        }
    }

    public void onDestroy() {
        if (this.fF != null) {
            this.fF.destroy();
        }
        if (this.fG != null) {
            this.fN.removeView(this.fG);
        }
        m1335T();
    }

    public void onPause() {
        if (this.fF != null) {
            this.fF.pause();
        }
        m1340R();
        if (this.fG != null && (!this.fD.isFinishing() || this.fH == null)) {
            ci.m264a(this.fG);
        }
        m1335T();
    }

    public void onRestart() {
    }

    public void onResume() {
        if (this.fE != null && this.fE.ga == 4) {
            if (this.fL) {
                this.fD.finish();
            } else {
                this.fL = true;
            }
        }
        if (this.fG != null) {
            ci.m269b(this.fG);
        }
    }

    public void onSaveInstanceState(Bundle outBundle) {
        outBundle.putBoolean("com.google.android.gms.ads.internal.overlay.hasResumed", this.fL);
    }

    public void onStart() {
    }

    public void onStop() {
        m1335T();
    }

    public void setRequestedOrientation(int requestedOrientation) {
        this.fD.setRequestedOrientation(requestedOrientation);
    }
}
