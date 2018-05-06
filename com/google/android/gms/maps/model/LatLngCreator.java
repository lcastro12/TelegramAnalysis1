package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class LatLngCreator implements Creator<LatLng> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m738a(LatLng latLng, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, latLng.getVersionCode());
        C0108b.m114a(parcel, 2, latLng.latitude);
        C0108b.m114a(parcel, 3, latLng.longitude);
        C0108b.m112C(parcel, k);
    }

    public LatLng createFromParcel(Parcel parcel) {
        double d = 0.0d;
        int j = C0107a.m92j(parcel);
        int i = 0;
        double d2 = 0.0d;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    d2 = C0107a.m91j(parcel, i2);
                    break;
                case 3:
                    d = C0107a.m91j(parcel, i2);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new LatLng(i, d2, d);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public LatLng[] newArray(int size) {
        return new LatLng[size];
    }
}
