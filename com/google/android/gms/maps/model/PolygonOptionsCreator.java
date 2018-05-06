package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import java.util.ArrayList;
import java.util.List;

public class PolygonOptionsCreator implements Creator<PolygonOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m740a(PolygonOptions polygonOptions, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, polygonOptions.getVersionCode());
        C0108b.m130b(parcel, 2, polygonOptions.getPoints(), false);
        C0108b.m132c(parcel, 3, polygonOptions.cO(), false);
        C0108b.m115a(parcel, 4, polygonOptions.getStrokeWidth());
        C0108b.m131c(parcel, 5, polygonOptions.getStrokeColor());
        C0108b.m131c(parcel, 6, polygonOptions.getFillColor());
        C0108b.m115a(parcel, 7, polygonOptions.getZIndex());
        C0108b.m124a(parcel, 8, polygonOptions.isVisible());
        C0108b.m124a(parcel, 9, polygonOptions.isGeodesic());
        C0108b.m112C(parcel, k);
    }

    public PolygonOptions createFromParcel(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int j = C0107a.m92j(parcel);
        List list = null;
        List arrayList = new ArrayList();
        boolean z2 = false;
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
                    list = C0107a.m82c(parcel, i4, LatLng.CREATOR);
                    break;
                case 3:
                    C0107a.m79a(parcel, i4, arrayList, getClass().getClassLoader());
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
                    z2 = C0107a.m83c(parcel, i4);
                    break;
                case 9:
                    z = C0107a.m83c(parcel, i4);
                    break;
                default:
                    C0107a.m80b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new PolygonOptions(i3, list, arrayList, f2, i2, i, f, z2, z);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public PolygonOptions[] newArray(int size) {
        return new PolygonOptions[size];
    }
}
