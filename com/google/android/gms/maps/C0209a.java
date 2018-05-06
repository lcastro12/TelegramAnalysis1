package com.google.android.gms.maps;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0209a {
    static void m709a(GoogleMapOptions googleMapOptions, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, googleMapOptions.getVersionCode());
        C0108b.m113a(parcel, 2, googleMapOptions.cv());
        C0108b.m113a(parcel, 3, googleMapOptions.cw());
        C0108b.m131c(parcel, 4, googleMapOptions.getMapType());
        C0108b.m120a(parcel, 5, googleMapOptions.getCamera(), i, false);
        C0108b.m113a(parcel, 6, googleMapOptions.cx());
        C0108b.m113a(parcel, 7, googleMapOptions.cy());
        C0108b.m113a(parcel, 8, googleMapOptions.cz());
        C0108b.m113a(parcel, 9, googleMapOptions.cA());
        C0108b.m113a(parcel, 10, googleMapOptions.cB());
        C0108b.m113a(parcel, 11, googleMapOptions.cC());
        C0108b.m112C(parcel, k);
    }
}
