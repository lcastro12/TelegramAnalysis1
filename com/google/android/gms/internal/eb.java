package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.dz.C0723a;
import com.google.android.gms.internal.dz.C0724b;
import java.util.ArrayList;

public class eb implements Creator<C0723a> {
    static void m432a(C0723a c0723a, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, c0723a.versionCode);
        C0108b.m121a(parcel, 2, c0723a.className, false);
        C0108b.m130b(parcel, 3, c0723a.lM, false);
        C0108b.m112C(parcel, k);
    }

    public C0723a[] m433F(int i) {
        return new C0723a[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m434r(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m433F(x0);
    }

    public C0723a m434r(Parcel parcel) {
        ArrayList arrayList = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    str = C0107a.m94l(parcel, i2);
                    break;
                case 3:
                    arrayList = C0107a.m82c(parcel, i2, C0724b.CREATOR);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0723a(i, str, arrayList);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }
}
