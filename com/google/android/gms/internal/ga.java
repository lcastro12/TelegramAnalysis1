package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.fv.C0927b.C0926b;
import java.util.HashSet;
import java.util.Set;

public class ga implements Creator<C0926b> {
    static void m629a(C0926b c0926b, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        Set di = c0926b.di();
        if (di.contains(Integer.valueOf(1))) {
            C0108b.m131c(parcel, 1, c0926b.getVersionCode());
        }
        if (di.contains(Integer.valueOf(2))) {
            C0108b.m131c(parcel, 2, c0926b.getHeight());
        }
        if (di.contains(Integer.valueOf(3))) {
            C0108b.m121a(parcel, 3, c0926b.getUrl(), true);
        }
        if (di.contains(Integer.valueOf(4))) {
            C0108b.m131c(parcel, 4, c0926b.getWidth());
        }
        C0108b.m112C(parcel, k);
    }

    public C0926b m630H(Parcel parcel) {
        int i = 0;
        int j = C0107a.m92j(parcel);
        Set hashSet = new HashSet();
        String str = null;
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
                    str = C0107a.m94l(parcel, i4);
                    hashSet.add(Integer.valueOf(3));
                    break;
                case 4:
                    i = C0107a.m86f(parcel, i4);
                    hashSet.add(Integer.valueOf(4));
                    break;
                default:
                    C0107a.m80b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0926b(hashSet, i3, i2, str, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public C0926b[] am(int i) {
        return new C0926b[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m630H(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return am(x0);
    }
}
