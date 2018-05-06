package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class GroundOverlayOptionsCreator implements Creator<GroundOverlayOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m735a(GroundOverlayOptions groundOverlayOptions, Parcel parcel, int i) {
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

    public GroundOverlayOptions createFromParcel(Parcel parcel) {
        int j = C0107a.m92j(parcel);
        int i = 0;
        IBinder iBinder = null;
        LatLng latLng = null;
        float f = 0.0f;
        float f2 = 0.0f;
        LatLngBounds latLngBounds = null;
        float f3 = 0.0f;
        float f4 = 0.0f;
        boolean z = false;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    iBinder = C0107a.m95m(parcel, i2);
                    break;
                case 3:
                    latLng = (LatLng) C0107a.m77a(parcel, i2, LatLng.CREATOR);
                    break;
                case 4:
                    f = C0107a.m89i(parcel, i2);
                    break;
                case 5:
                    f2 = C0107a.m89i(parcel, i2);
                    break;
                case 6:
                    latLngBounds = (LatLngBounds) C0107a.m77a(parcel, i2, LatLngBounds.CREATOR);
                    break;
                case 7:
                    f3 = C0107a.m89i(parcel, i2);
                    break;
                case 8:
                    f4 = C0107a.m89i(parcel, i2);
                    break;
                case 9:
                    z = C0107a.m83c(parcel, i2);
                    break;
                case 10:
                    f5 = C0107a.m89i(parcel, i2);
                    break;
                case 11:
                    f6 = C0107a.m89i(parcel, i2);
                    break;
                case 12:
                    f7 = C0107a.m89i(parcel, i2);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new GroundOverlayOptions(i, iBinder, latLng, f, f2, latLngBounds, f3, f4, z, f5, f6, f7);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public GroundOverlayOptions[] newArray(int size) {
        return new GroundOverlayOptions[size];
    }
}
