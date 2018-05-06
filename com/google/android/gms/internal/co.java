package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class co implements SafeParcelable {
    public static final cp CREATOR = new cp();
    public String hP;
    public int hQ;
    public int hR;
    public boolean hS;
    public final int versionCode;

    public co(int i, int i2, boolean z) {
        this(1, "afma-sdk-a-v" + i + "." + i2 + "." + (z ? "0" : "1"), i, i2, z);
    }

    co(int i, String str, int i2, int i3, boolean z) {
        this.versionCode = i;
        this.hP = str;
        this.hQ = i2;
        this.hR = i3;
        this.hS = z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        cp.m300a(this, out, flags);
    }
}
