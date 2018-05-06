package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class TileCreator implements Creator<Tile> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m742a(Tile tile, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, tile.getVersionCode());
        C0108b.m131c(parcel, 2, tile.width);
        C0108b.m131c(parcel, 3, tile.height);
        C0108b.m125a(parcel, 4, tile.data, false);
        C0108b.m112C(parcel, k);
    }

    public Tile createFromParcel(Parcel parcel) {
        int i = 0;
        int j = C0107a.m92j(parcel);
        byte[] bArr = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i4)) {
                case 1:
                    i3 = C0107a.m86f(parcel, i4);
                    break;
                case 2:
                    i2 = C0107a.m86f(parcel, i4);
                    break;
                case 3:
                    i = C0107a.m86f(parcel, i4);
                    break;
                case 4:
                    bArr = C0107a.m97o(parcel, i4);
                    break;
                default:
                    C0107a.m80b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new Tile(i3, i2, i, bArr);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public Tile[] newArray(int size) {
        return new Tile[size];
    }
}
