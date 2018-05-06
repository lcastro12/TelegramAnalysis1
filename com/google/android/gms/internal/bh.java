package com.google.android.gms.internal;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.C0112b.C0655a;
import com.google.android.gms.dynamic.C0898c;

public final class bh implements SafeParcelable {
    public static final bg CREATOR = new bg();
    public final co eg;
    public final be fR;
    public final C0202q fS;
    public final bi fT;
    public final cq fU;
    public final ag fV;
    public final String fW;
    public final boolean fX;
    public final String fY;
    public final bl fZ;
    public final String fz;
    public final int ga;
    public final int orientation;
    public final int versionCode;

    bh(int i, be beVar, IBinder iBinder, IBinder iBinder2, IBinder iBinder3, IBinder iBinder4, String str, boolean z, String str2, IBinder iBinder5, int i2, int i3, String str3, co coVar) {
        this.versionCode = i;
        this.fR = beVar;
        this.fS = (C0202q) C0898c.m1317b(C0655a.m828z(iBinder));
        this.fT = (bi) C0898c.m1317b(C0655a.m828z(iBinder2));
        this.fU = (cq) C0898c.m1317b(C0655a.m828z(iBinder3));
        this.fV = (ag) C0898c.m1317b(C0655a.m828z(iBinder4));
        this.fW = str;
        this.fX = z;
        this.fY = str2;
        this.fZ = (bl) C0898c.m1317b(C0655a.m828z(iBinder5));
        this.orientation = i2;
        this.ga = i3;
        this.fz = str3;
        this.eg = coVar;
    }

    public bh(be beVar, C0202q c0202q, bi biVar, bl blVar, co coVar) {
        this.versionCode = 1;
        this.fR = beVar;
        this.fS = c0202q;
        this.fT = biVar;
        this.fU = null;
        this.fV = null;
        this.fW = null;
        this.fX = false;
        this.fY = null;
        this.fZ = blVar;
        this.orientation = -1;
        this.ga = 4;
        this.fz = null;
        this.eg = coVar;
    }

    public bh(C0202q c0202q, bi biVar, ag agVar, bl blVar, cq cqVar, boolean z, int i, String str, co coVar) {
        this.versionCode = 1;
        this.fR = null;
        this.fS = c0202q;
        this.fT = biVar;
        this.fU = cqVar;
        this.fV = agVar;
        this.fW = null;
        this.fX = z;
        this.fY = null;
        this.fZ = blVar;
        this.orientation = i;
        this.ga = 3;
        this.fz = str;
        this.eg = coVar;
    }

    public bh(C0202q c0202q, bi biVar, ag agVar, bl blVar, cq cqVar, boolean z, int i, String str, String str2, co coVar) {
        this.versionCode = 1;
        this.fR = null;
        this.fS = c0202q;
        this.fT = biVar;
        this.fU = cqVar;
        this.fV = agVar;
        this.fW = str2;
        this.fX = z;
        this.fY = str;
        this.fZ = blVar;
        this.orientation = i;
        this.ga = 3;
        this.fz = null;
        this.eg = coVar;
    }

    public bh(C0202q c0202q, bi biVar, bl blVar, cq cqVar, int i, co coVar) {
        this.versionCode = 1;
        this.fR = null;
        this.fS = c0202q;
        this.fT = biVar;
        this.fU = cqVar;
        this.fV = null;
        this.fW = null;
        this.fX = false;
        this.fY = null;
        this.fZ = blVar;
        this.orientation = i;
        this.ga = 1;
        this.fz = null;
        this.eg = coVar;
    }

    public bh(C0202q c0202q, bi biVar, bl blVar, cq cqVar, boolean z, int i, co coVar) {
        this.versionCode = 1;
        this.fR = null;
        this.fS = c0202q;
        this.fT = biVar;
        this.fU = cqVar;
        this.fV = null;
        this.fW = null;
        this.fX = z;
        this.fY = null;
        this.fZ = blVar;
        this.orientation = i;
        this.ga = 2;
        this.fz = null;
        this.eg = coVar;
    }

    public static bh m893a(Intent intent) {
        try {
            Bundle bundleExtra = intent.getBundleExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
            bundleExtra.setClassLoader(bh.class.getClassLoader());
            return (bh) bundleExtra.getParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
        } catch (Exception e) {
            return null;
        }
    }

    public static void m894a(Intent intent, bh bhVar) {
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", bhVar);
        intent.putExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", bundle);
    }

    IBinder m895U() {
        return C0898c.m1318g(this.fS).asBinder();
    }

    IBinder m896V() {
        return C0898c.m1318g(this.fT).asBinder();
    }

    IBinder m897W() {
        return C0898c.m1318g(this.fU).asBinder();
    }

    IBinder m898X() {
        return C0898c.m1318g(this.fV).asBinder();
    }

    IBinder m899Y() {
        return C0898c.m1318g(this.fZ).asBinder();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        bg.m203a(this, out, flags);
    }
}
