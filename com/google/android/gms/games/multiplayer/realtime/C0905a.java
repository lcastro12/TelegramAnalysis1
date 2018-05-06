package com.google.android.gms.games.multiplayer.realtime;

import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.common.data.C0647f;

public final class C0905a extends C0647f<Room> {
    public C0905a(C0646d c0646d) {
        super(c0646d);
    }

    protected /* synthetic */ Object mo1484a(int i, int i2) {
        return m1325b(i, i2);
    }

    protected Room m1325b(int i, int i2) {
        return new C0906c(this.jf, i, i2);
    }

    protected String getPrimaryDataMarkerColumn() {
        return "external_match_id";
    }
}
