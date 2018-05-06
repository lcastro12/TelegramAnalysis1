package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0230c {
    static void m747a(GroundOverlayOptions groundOverlayOptions, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, groundOverlayOptions.getVersionCode());
        C0108b.m118a(parcel, 2, groundOverlayOptions.cM(), false);
        C0108b.m120a(parcel, 3, groundOverlayOptions.getLocation(), i, false);
        C0108b.m115a(parcel, 4, groundOverlayOptions.getWidth());
        C0108b.m115a(parcel, 5, groundOverlayOptions.getHeight());
        C0108b.m120a(parcel, 6, groundOverlayOptions.getBounds(), i, false);
        C0108b.m115a(parcel, 7, groundOverlayOptions.getBearing());
        C0108b.m115a(parcel, 8, groundOverlayOptions.getZIndex());
        C0108b.m124a(parcel, 9, groundOverlayOptions.isVisible());
        C0108b.m115a(parcel, 10, groundOverlayOptions.getTransparency());
        C0108b.m115a(parcel, 11, groundOverlayOptions.getAnchorU());
        C0108b.m115a(parcel, 12, groundOverlayOptions.getAnchorV());
        C0108b.m112C(parcel, k);
    }
}
