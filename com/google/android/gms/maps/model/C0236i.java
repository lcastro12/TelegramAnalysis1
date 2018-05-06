package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0236i {
    static void m753a(Tile tile, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, tile.getVersionCode());
        C0108b.m131c(parcel, 2, tile.width);
        C0108b.m131c(parcel, 3, tile.height);
        C0108b.m125a(parcel, 4, tile.data, false);
        C0108b.m112C(parcel, k);
    }
}
