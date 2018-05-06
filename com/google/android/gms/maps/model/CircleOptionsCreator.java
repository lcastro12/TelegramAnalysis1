package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class CircleOptionsCreator implements Creator<CircleOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m734a(CircleOptions circleOptions, Parcel parcel, int i) {
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

    public CircleOptions createFromParcel(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int j = C0107a.m92j(parcel);
        LatLng latLng = null;
        double d = 0.0d;
        int i = 0;
        int i2 = 0;
        float f2 = 0.0f;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i4)) {
                case 1:
                    i3 = C0107a.m86f(parcel, i4);
                    break;
                case 2:
                    latLng = (LatLng) C0107a.m77a(parcel, i4, LatLng.CREATOR);
                    break;
                case 3:
                    d = C0107a.m91j(parcel, i4);
                    break;
                case 4:
                    f2 = C0107a.m89i(parcel, i4);
                    break;
                case 5:
                    i2 = C0107a.m86f(parcel, i4);
                    break;
                case 6:
                    i = C0107a.m86f(parcel, i4);
                    break;
                case 7:
                    f = C0107a.m89i(parcel, i4);
                    break;
                case 8:
                    z = C0107a.m83c(parcel, i4);
                    break;
                default:
                    C0107a.m80b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new CircleOptions(i3, latLng, d, f2, i2, i, f, z);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public CircleOptions[] newArray(int size) {
        return new CircleOptions[size];
    }
}
