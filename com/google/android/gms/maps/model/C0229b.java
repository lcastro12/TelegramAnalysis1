package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0229b {
    static void m746a(CircleOptions circleOptions, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, circleOptions.getVersionCode());
        C0108b.m120a(parcel, 2, circleOptions.getCenter(), i, false);
        C0108b.m114a(parcel, 3, circleOptions.getRadius());
        C0108b.m115a(parcel, 4, circleOptions.getStrokeWidth());
        C0108b.m131c(parcel, 5, circleOptions.getStrokeColor());
        C0108b.m131c(parcel, 6, circleOptions.getFillColor());
        C0108b.m115a(parcel, 7, circleOptions.getZIndex());
        C0108b.m124a(parcel, 8, circleOptions.isVisible());
        C0108b.m112C(parcel, k);
    }
}
