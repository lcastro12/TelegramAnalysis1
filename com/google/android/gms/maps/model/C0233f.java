package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0233f {
    static void m750a(MarkerOptions markerOptions, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, markerOptions.getVersionCode());
        C0108b.m120a(parcel, 2, markerOptions.getPosition(), i, false);
        C0108b.m121a(parcel, 3, markerOptions.getTitle(), false);
        C0108b.m121a(parcel, 4, markerOptions.getSnippet(), false);
        C0108b.m118a(parcel, 5, markerOptions.cN(), false);
        C0108b.m115a(parcel, 6, markerOptions.getAnchorU());
        C0108b.m115a(parcel, 7, markerOptions.getAnchorV());
        C0108b.m124a(parcel, 8, markerOptions.isDraggable());
        C0108b.m124a(parcel, 9, markerOptions.isVisible());
        C0108b.m112C(parcel, k);
    }
}
