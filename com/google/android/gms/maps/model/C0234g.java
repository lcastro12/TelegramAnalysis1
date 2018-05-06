package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0234g {
    static void m751a(PolygonOptions polygonOptions, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, polygonOptions.getVersionCode());
        C0108b.m130b(parcel, 2, polygonOptions.getPoints(), false);
        C0108b.m132c(parcel, 3, polygonOptions.cO(), false);
        C0108b.m115a(parcel, 4, polygonOptions.getStrokeWidth());
        C0108b.m131c(parcel, 5, polygonOptions.getStrokeColor());
        C0108b.m131c(parcel, 6, polygonOptions.getFillColor());
        C0108b.m115a(parcel, 7, polygonOptions.getZIndex());
        C0108b.m124a(parcel, 8, polygonOptions.isVisible());
        C0108b.m124a(parcel, 9, polygonOptions.isGeodesic());
        C0108b.m112C(parcel, k);
    }
}
