package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class be implements SafeParcelable {
    public static final bd CREATOR = new bd();
    public final String fA;
    public final String fB;
    public final String fC;
    public final String fy;
    public final String fz;
    public final String mimeType;
    public final String packageName;
    public final int versionCode;

    public be(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.versionCode = i;
        this.fy = str;
        this.fz = str2;
        this.mimeType = str3;
        this.packageName = str4;
        this.fA = str5;
        this.fB = str6;
        this.fC = str7;
    }

    public be(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this(1, str, str2, str3, str4, str5, str6, str7);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        bd.m200a(this, out, flags);
    }
}
