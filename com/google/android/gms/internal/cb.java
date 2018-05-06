package com.google.android.gms.internal;

import com.google.android.gms.plus.PlusShare;
import java.util.Map;

public final class cb {
    private final Object eJ = new Object();
    private cq fG;
    private String gT;
    public final ai gU = new C07051(this);
    public final ai gV = new C07062(this);
    private int gw = -2;

    class C07051 implements ai {
        final /* synthetic */ cb gW;

        C07051(cb cbVar) {
            this.gW = cbVar;
        }

        public void mo770a(cq cqVar, Map<String, String> map) {
            synchronized (this.gW.eJ) {
                String str = (String) map.get("errors");
                cn.m299q("Invalid " + ((String) map.get("type")) + " request error: " + str);
                this.gW.gw = 1;
                this.gW.eJ.notify();
            }
        }
    }

    class C07062 implements ai {
        final /* synthetic */ cb gW;

        C07062(cb cbVar) {
            this.gW = cbVar;
        }

        public void mo770a(cq cqVar, Map<String, String> map) {
            synchronized (this.gW.eJ) {
                String str = (String) map.get(PlusShare.KEY_CALL_TO_ACTION_URL);
                if (str == null) {
                    cn.m299q("URL missing in loadAdUrl GMSG.");
                    return;
                }
                this.gW.gT = str;
                this.gW.eJ.notify();
            }
        }
    }

    public String aj() {
        String str;
        synchronized (this.eJ) {
            while (this.gT == null && this.gw == -2) {
                try {
                    this.eJ.wait();
                } catch (InterruptedException e) {
                    cn.m299q("Ad request service was interrupted.");
                    str = null;
                }
            }
            str = this.gT;
        }
        return str;
    }

    public void m241b(cq cqVar) {
        synchronized (this.eJ) {
            this.fG = cqVar;
        }
    }

    public int getErrorCode() {
        int i;
        synchronized (this.eJ) {
            i = this.gw;
        }
        return i;
    }
}
