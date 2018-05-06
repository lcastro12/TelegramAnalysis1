package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class TileOverlayOptionsCreator implements Creator<TileOverlayOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m743a(TileOverlayOptions tileOverlayOptions, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, tileOverlayOptions.getVersionCode());
        C0108b.m118a(parcel, 2, tileOverlayOptions.cP(), false);
        C0108b.m124a(parcel, 3, tileOverlayOptions.isVisible());
        C0108b.m115a(parcel, 4, tileOverlayOptions.getZIndex());
        C0108b.m112C(parcel, k);
    }

    public TileOverlayOptions createFromParcel(Parcel parcel) {
        boolean z = false;
        int j = C0107a.m92j(parcel);
        IBinder iBinder = null;
        float f = 0.0f;
        int i = 0;
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
                    z = C0107a.m83c(parcel, i2);
                    break;
                case 4:
                    f = C0107a.m89i(parcel, i2);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new TileOverlayOptions(i, iBinder, z, f);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public TileOverlayOptions[] newArray(int size) {
        return new TileOverlayOptions[size];
    }
}
