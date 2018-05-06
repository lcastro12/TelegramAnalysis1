package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0235h {
    static void m752a(PolylineOptions polylineOptions, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, polylineOptions.getVersionCode());
        C0108b.m130b(parcel, 2, polylineOptions.getPoints(), false);
        C0108b.m115a(parcel, 3, polylineOptions.getWidth());
        C0108b.m131c(parcel, 4, polylineOptions.getColor());
        C0108b.m115a(parcel, 5, polylineOptions.getZIndex());
        C0108b.m124a(parcel, 6, polylineOptions.isVisible());
        C0108b.m124a(parcel, 7, polylineOptions.isGeodesic());
        C0108b.m112C(parcel, k);
    }
}
