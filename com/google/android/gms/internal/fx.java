package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.fv.C0924a;
import java.util.HashSet;
import java.util.Set;

public class fx implements Creator<C0924a> {
    static void m623a(C0924a c0924a, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        Set di = c0924a.di();
        if (di.contains(Integer.valueOf(1))) {
            C0108b.m131c(parcel, 1, c0924a.getVersionCode());
        }
        if (di.contains(Integer.valueOf(2))) {
            C0108b.m131c(parcel, 2, c0924a.getMax());
        }
        if (di.contains(Integer.valueOf(3))) {
            C0108b.m131c(parcel, 3, c0924a.getMin());
        }
        C0108b.m112C(parcel, k);
    }

    public C0924a m624E(Parcel parcel) {
        int i = 0;
        int j = C0107a.m92j(parcel);
        Set hashSet = new HashSet();
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i4)) {
                case 1:
                    i3 = C0107a.m86f(parcel, i4);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    i2 = C0107a.m86f(parcel, i4);
                    hashSet.add(Integer.valueOf(2));
                    break;
                case 3:
                    i = C0107a.m86f(parcel, i4);
                    hashSet.add(Integer.valueOf(3));
                    break;
                default:
                    C0107a.m80b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0924a(hashSet, i3, i2, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public C0924a[] aj(int i) {
        return new C0924a[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m624E(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aj(x0);
    }
}