package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.dt.C0721a;

public class dv implements Creator<C0721a> {
    static void m410a(C0721a c0721a, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, c0721a.versionCode);
        C0108b.m121a(parcel, 2, c0721a.lx, false);
        C0108b.m131c(parcel, 3, c0721a.ly);
        C0108b.m112C(parcel, k);
    }

    public C0721a[] m411B(int i) {
        return new C0721a[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m412n(x0);
    }

    public C0721a m412n(Parcel parcel) {
        int i = 0;
        int j = C0107a.m92j(parcel);
        String str = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i3)) {
                case 1:
                    i2 = C0107a.m86f(parcel, i3);
                    break;
                case 2:
                    str = C0107a.m94l(parcel, i3);
                    break;
                case 3:
                    i = C0107a.m86f(parcel, i3);
                    break;
                default:
                    C0107a.m80b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0721a(i2, str, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m411B(x0);
    }
}
