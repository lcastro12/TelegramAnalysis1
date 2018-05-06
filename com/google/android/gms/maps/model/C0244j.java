package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0244j {
    static void m767a(TileOverlayOptions tileOverlayOptions, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, tileOverlayOptions.getVersionCode());
        C0108b.m118a(parcel, 2, tileOverlayOptions.cP(), false);
        C0108b.m124a(parcel, 3, tileOverlayOptions.isVisible());
        C0108b.m115a(parcel, 4, tileOverlayOptions.getZIndex());
        C0108b.m112C(parcel, k);
    }
}
