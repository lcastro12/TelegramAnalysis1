package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw.C0183b;

public class dr implements SafeParcelable {
    public static final ds CREATOR = new ds();
    private final int iM;
    private final dt lt;

    dr(int i, dt dtVar) {
        this.iM = i;
        this.lt = dtVar;
    }

    private dr(dt dtVar) {
        this.iM = 1;
        this.lt = dtVar;
    }

    public static dr m986a(C0183b<?, ?> c0183b) {
        if (c0183b instanceof dt) {
            return new dr((dt) c0183b);
        }
        throw new IllegalArgumentException("Unsupported safe parcelable field converter class.");
    }

    dt bk() {
        return this.lt;
    }

    public C0183b<?, ?> bl() {
        if (this.lt != null) {
            return this.lt;
        }
        throw new IllegalStateException("There was no converter wrapped in this ConverterWrapper.");
    }

    public int describeContents() {
        ds dsVar = CREATOR;
        return 0;
    }

    int getVersionCode() {
        return this.iM;
    }

    public void writeToParcel(Parcel out, int flags) {
        ds dsVar = CREATOR;
        ds.m404a(this, out, flags);
    }
}
