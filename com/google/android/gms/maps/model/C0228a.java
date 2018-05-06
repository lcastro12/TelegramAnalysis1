package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0228a {
    static void m745a(CameraPosition cameraPosition, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, cameraPosition.getVersionCode());
        C0108b.m120a(parcel, 2, cameraPosition.target, i, false);
        C0108b.m115a(parcel, 3, cameraPosition.zoom);
        C0108b.m115a(parcel, 4, cameraPosition.tilt);
        C0108b.m115a(parcel, 5, cameraPosition.bearing);
        C0108b.m112C(parcel, k);
    }
}
