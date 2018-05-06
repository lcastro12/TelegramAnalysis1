package com.google.android.gms.appstate;

import com.google.android.gms.common.data.C0096b;
import com.google.android.gms.common.data.C0646d;

public final class C0897b extends C0096b implements AppState {
    C0897b(C0646d c0646d, int i) {
        super(c0646d, i);
    }

    public AppState aE() {
        return new C0896a(this);
    }

    public boolean equals(Object obj) {
        return C0896a.m1315a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return aE();
    }

    public byte[] getConflictData() {
        return getByteArray("conflict_data");
    }

    public String getConflictVersion() {
        return getString("conflict_version");
    }

    public int getKey() {
        return getInteger("key");
    }

    public byte[] getLocalData() {
        return getByteArray("local_data");
    }

    public String getLocalVersion() {
        return getString("local_version");
    }

    public boolean hasConflict() {
        return !m42v("conflict_version");
    }

    public int hashCode() {
        return C0896a.m1314a(this);
    }

    public String toString() {
        return C0896a.m1316b(this);
    }
}
