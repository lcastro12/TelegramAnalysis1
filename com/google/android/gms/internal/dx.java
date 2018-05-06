package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.dw.C0722a;

public class dx implements Creator<C0722a> {
    static void m423a(C0722a c0722a, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, c0722a.getVersionCode());
        C0108b.m131c(parcel, 2, c0722a.bn());
        C0108b.m124a(parcel, 3, c0722a.bt());
        C0108b.m131c(parcel, 4, c0722a.bo());
        C0108b.m124a(parcel, 5, c0722a.bu());
        C0108b.m121a(parcel, 6, c0722a.bv(), false);
        C0108b.m131c(parcel, 7, c0722a.bw());
        C0108b.m121a(parcel, 8, c0722a.by(), false);
        C0108b.m120a(parcel, 9, c0722a.bA(), i, false);
        C0108b.m112C(parcel, k);
    }

    public C0722a[] m424C(int i) {
        return new C0722a[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m425o(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m424C(x0);
    }

    public C0722a m425o(Parcel parcel) {
        dr drVar = null;
        int i = 0;
        int j = C0107a.m92j(parcel);
        String str = null;
        String str2 = null;
        boolean z = false;
        int i2 = 0;
        boolean z2 = false;
        int i3 = 0;
        int i4 = 0;
        while (parcel.dataPosition() < j) {
            int i5 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i5)) {
                case 1:
                    i4 = C0107a.m86f(parcel, i5);
                    break;
                case 2:
                    i3 = C0107a.m86f(parcel, i5);
                    break;
                case 3:
                    z2 = C0107a.m83c(parcel, i5);
                    break;
                case 4:
                    i2 = C0107a.m86f(parcel, i5);
                    break;
                case 5:
                    z = C0107a.m83c(parcel, i5);
                    break;
                case 6:
                    str2 = C0107a.m94l(parcel, i5);
                    break;
                case 7:
                    i = C0107a.m86f(parcel, i5);
                    break;
                case 8:
                    str = C0107a.m94l(parcel, i5);
                    break;
                case 9:
                    drVar = (dr) C0107a.m77a(parcel, i5, dr.CREATOR);
                    break;
                default:
                    C0107a.m80b(parcel, i5);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0722a(i4, i3, z2, i2, z, str2, i, str, drVar);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }
}
