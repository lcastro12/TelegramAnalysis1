package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0231d {
    static void m748a(LatLngBounds latLngBounds, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, latLngBounds.getVersionCode());
        C0108b.m120a(parcel, 2, latLngBounds.southwest, i, false);
        C0108b.m120a(parcel, 3, latLngBounds.northeast, i, false);
        C0108b.m112C(parcel, k);
    }
}
