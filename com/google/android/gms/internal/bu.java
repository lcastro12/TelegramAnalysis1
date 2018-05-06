package com.google.android.gms.internal;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class bu implements SafeParcelable {
    public static final bv CREATOR = new bv();
    public final String adUnitId;
    public final ApplicationInfo applicationInfo;
    public final C0771x ed;
    public final co eg;
    public final Bundle gA;
    public final C0770v gB;
    public final PackageInfo gC;
    public final String gD;
    public final String gE;
    public final String gF;
    public final int versionCode;

    public static final class C0153a {
        public final String adUnitId;
        public final ApplicationInfo applicationInfo;
        public final C0771x ed;
        public final co eg;
        public final Bundle gA;
        public final C0770v gB;
        public final PackageInfo gC;
        public final String gE;
        public final String gF;

        public C0153a(Bundle bundle, C0770v c0770v, C0771x c0771x, String str, ApplicationInfo applicationInfo, PackageInfo packageInfo, String str2, String str3, co coVar) {
            this.gA = bundle;
            this.gB = c0770v;
            this.ed = c0771x;
            this.adUnitId = str;
            this.applicationInfo = applicationInfo;
            this.gC = packageInfo;
            this.gE = str2;
            this.gF = str3;
            this.eg = coVar;
        }
    }

    bu(int i, Bundle bundle, C0770v c0770v, C0771x c0771x, String str, ApplicationInfo applicationInfo, PackageInfo packageInfo, String str2, String str3, String str4, co coVar) {
        this.versionCode = i;
        this.gA = bundle;
        this.gB = c0770v;
        this.ed = c0771x;
        this.adUnitId = str;
        this.applicationInfo = applicationInfo;
        this.gC = packageInfo;
        this.gD = str2;
        this.gE = str3;
        this.gF = str4;
        this.eg = coVar;
    }

    public bu(Bundle bundle, C0770v c0770v, C0771x c0771x, String str, ApplicationInfo applicationInfo, PackageInfo packageInfo, String str2, String str3, String str4, co coVar) {
        this(1, bundle, c0770v, c0771x, str, applicationInfo, packageInfo, str2, str3, str4, coVar);
    }

    public bu(C0153a c0153a, String str) {
        this(c0153a.gA, c0153a.gB, c0153a.ed, c0153a.adUnitId, c0153a.applicationInfo, c0153a.gC, str, c0153a.gE, c0153a.gF, c0153a.eg);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        bv.m226a(this, out, flags);
    }
}
