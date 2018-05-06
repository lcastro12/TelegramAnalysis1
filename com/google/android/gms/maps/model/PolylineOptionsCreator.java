package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import java.util.List;

public class PolylineOptionsCreator implements Creator<PolylineOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m741a(PolylineOptions polylineOptions, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, polylineOptions.getVersionCode());
        C0108b.m130b(parcel, 2, polylineOptions.getPoints(), false);
        C0108b.m115a(parcel, 3, polylineOptions.getWidth());
        C0108b.m131c(parcel, 4, polylineOptions.getColor());
        C0108b.m115a(parcel, 5, polylineOptions.getZIndex());
        C0108b.m124a(parcel, 6, polylineOptions.isVisible());
        C0108b.m124a(parcel, 7, polylineOptions.isGeodesic());
        C0108b.m112C(parcel, k);
    }

    public PolylineOptions createFromParcel(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int j = C0107a.m92j(parcel);
        List list = null;
        boolean z2 = false;
        int i = 0;
        float f2 = 0.0f;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i3)) {
                case 1:
                    i2 = C0107a.m86f(parcel, i3);
                    break;
                case 2:
                    list = C0107a.m82c(parcel, i3, LatLng.CREATOR);
                    break;
                case 3:
                    f2 = C0107a.m89i(parcel, i3);
                    break;
                case 4:
                    i = C0107a.m86f(parcel, i3);
                    break;
                case 5:
                    f = C0107a.m89i(parcel, i3);
                    break;
                case 6:
                    z2 = C0107a.m83c(parcel, i3);
                    break;
                case 7:
                    z = C0107a.m83c(parcel, i3);
                    break;
                default:
                    C0107a.m80b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new PolylineOptions(i2, list, f2, i, f, z2, z);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public PolylineOptions[] newArray(int size) {
        return new PolylineOptions[size];
    }
}
