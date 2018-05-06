package com.google.android.gms.common.data;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class C0644c<T extends SafeParcelable> extends DataBuffer<T> {
    private static final String[] jk = new String[]{"data"};
    private final Creator<T> jl;

    public C0644c(C0646d c0646d, Creator<T> creator) {
        super(c0646d);
        this.jl = creator;
    }

    public /* synthetic */ Object get(int x0) {
        return m802p(x0);
    }

    public T m802p(int i) {
        byte[] e = this.jf.m813e("data", i, 0);
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(e, 0, e.length);
        obtain.setDataPosition(0);
        SafeParcelable safeParcelable = (SafeParcelable) this.jl.createFromParcel(obtain);
        obtain.recycle();
        return safeParcelable;
    }
}
