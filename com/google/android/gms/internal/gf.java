package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.fv.C0932h;
import java.util.HashSet;
import java.util.Set;

public class gf implements Creator<C0932h> {
    static void m639a(C0932h c0932h, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        Set di = c0932h.di();
        if (di.contains(Integer.valueOf(1))) {
            C0108b.m131c(parcel, 1, c0932h.getVersionCode());
        }
        if (di.contains(Integer.valueOf(3))) {
            C0108b.m131c(parcel, 3, c0932h.dV());
        }
        if (di.contains(Integer.valueOf(4))) {
            C0108b.m121a(parcel, 4, c0932h.getValue(), true);
        }
        if (di.contains(Integer.valueOf(5))) {
            C0108b.m121a(parcel, 5, c0932h.getLabel(), true);
        }
        if (di.contains(Integer.valueOf(6))) {
            C0108b.m131c(parcel, 6, c0932h.getType());
        }
        C0108b.m112C(parcel, k);
    }

    public C0932h m640M(Parcel parcel) {
        String str = null;
        int i = 0;
        int j = C0107a.m92j(parcel);
        Set hashSet = new HashSet();
        int i2 = 0;
        String str2 = null;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i4)) {
                case 1:
                    i3 = C0107a.m86f(parcel, i4);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 3:
                    i = C0107a.m86f(parcel, i4);
                    hashSet.add(Integer.valueOf(3));
                    break;
                case 4:
                    str = C0107a.m94l(parcel, i4);
                    hashSet.add(Integer.valueOf(4));
                    break;
                case 5:
                    str2 = C0107a.m94l(parcel, i4);
                    hashSet.add(Integer.valueOf(5));
                    break;
                case 6:
                    i2 = C0107a.m86f(parcel, i4);
                    hashSet.add(Integer.valueOf(6));
                    break;
                default:
                    C0107a.m80b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0932h(hashSet, i3, str2, i2, str, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public C0932h[] ar(int i) {
        return new C0932h[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m640M(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ar(x0);
    }
}
