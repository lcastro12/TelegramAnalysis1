package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class CameraPositionCreator implements Creator<CameraPosition> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m733a(CameraPosition cameraPosition, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, cameraPosition.getVersionCode());
        C0108b.m120a(parcel, 2, cameraPosition.target, i, false);
        C0108b.m115a(parcel, 3, cameraPosition.zoom);
        C0108b.m115a(parcel, 4, cameraPosition.tilt);
        C0108b.m115a(parcel, 5, cameraPosition.bearing);
        C0108b.m112C(parcel, k);
    }

    public CameraPosition createFromParcel(Parcel parcel) {
        float f = 0.0f;
        int j = C0107a.m92j(parcel);
        int i = 0;
        LatLng latLng = null;
        float f2 = 0.0f;
        float f3 = 0.0f;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    latLng = (LatLng) C0107a.m77a(parcel, i2, LatLng.CREATOR);
                    break;
                case 3:
                    f3 = C0107a.m89i(parcel, i2);
                    break;
                case 4:
                    f2 = C0107a.m89i(parcel, i2);
                    break;
                case 5:
                    f = C0107a.m89i(parcel, i2);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new CameraPosition(i, latLng, f3, f2, f);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public CameraPosition[] newArray(int size) {
        return new CameraPosition[size];
    }
}
