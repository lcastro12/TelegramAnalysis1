package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0232e {
    static void m749a(LatLng latLng, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, latLng.getVersionCode());
        C0108b.m114a(parcel, 2, latLng.latitude);
        C0108b.m114a(parcel, 3, latLng.longitude);
        C0108b.m112C(parcel, k);
    }
}
