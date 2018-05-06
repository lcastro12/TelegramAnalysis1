package com.google.android.gms.ads;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.gms.internal.bm;
import com.google.android.gms.internal.bn;
import com.google.android.gms.internal.cn;

public final class AdActivity extends Activity {
    public static final String CLASS_NAME = "com.google.android.gms.ads.AdActivity";
    public static final String SIMPLE_CLASS_NAME = "AdActivity";
    private bn dH;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dH = bm.m900a(this);
        if (this.dH == null) {
            cn.m299q("Could not create ad overlay.");
            finish();
            return;
        }
        try {
            this.dH.onCreate(savedInstanceState);
        } catch (Throwable e) {
            cn.m293b("Could not forward onCreate to ad overlay:", e);
            finish();
        }
    }

    protected void onDestroy() {
        try {
            if (this.dH != null) {
                this.dH.onDestroy();
            }
        } catch (Throwable e) {
            cn.m293b("Could not forward onDestroy to ad overlay:", e);
        }
        super.onDestroy();
    }

    protected void onPause() {
        try {
            if (this.dH != null) {
                this.dH.onPause();
            }
        } catch (Throwable e) {
            cn.m293b("Could not forward onPause to ad overlay:", e);
            finish();
        }
        super.onPause();
    }

    protected void onRestart() {
        super.onRestart();
        try {
            if (this.dH != null) {
                this.dH.onRestart();
            }
        } catch (Throwable e) {
            cn.m293b("Could not forward onRestart to ad overlay:", e);
            finish();
        }
    }

    protected void onResume() {
        super.onResume();
        try {
            if (this.dH != null) {
                this.dH.onResume();
            }
        } catch (Throwable e) {
            cn.m293b("Could not forward onResume to ad overlay:", e);
            finish();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        try {
            if (this.dH != null) {
                this.dH.onSaveInstanceState(outState);
            }
        } catch (Throwable e) {
            cn.m293b("Could not forward onSaveInstanceState to ad overlay:", e);
            finish();
        }
        super.onSaveInstanceState(outState);
    }

    protected void onStart() {
        super.onStart();
        try {
            if (this.dH != null) {
                this.dH.onStart();
            }
        } catch (Throwable e) {
            cn.m293b("Could not forward onStart to ad overlay:", e);
            finish();
        }
    }

    protected void onStop() {
        try {
            if (this.dH != null) {
                this.dH.onStop();
            }
        } catch (Throwable e) {
            cn.m293b("Could not forward onStop to ad overlay:", e);
            finish();
        }
        super.onStop();
    }
}
