package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.fv.C0931g;
import java.util.HashSet;
import java.util.Set;

public class ge implements Creator<C0931g> {
    static void m637a(C0931g c0931g, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        Set di = c0931g.di();
        if (di.contains(Integer.valueOf(1))) {
            C0108b.m131c(parcel, 1, c0931g.getVersionCode());
        }
        if (di.contains(Integer.valueOf(2))) {
            C0108b.m124a(parcel, 2, c0931g.isPrimary());
        }
        if (di.contains(Integer.valueOf(3))) {
            C0108b.m121a(parcel, 3, c0931g.getValue(), true);
        }
        C0108b.m112C(parcel, k);
    }

    public C0931g m638L(Parcel parcel) {
        boolean z = false;
        int j = C0107a.m92j(parcel);
        Set hashSet = new HashSet();
        String str = null;
        int i = 0;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    z = C0107a.m83c(parcel, i2);
                    hashSet.add(Integer.valueOf(2));
                    break;
                case 3:
                    str = C0107a.m94l(parcel, i2);
                    hashSet.add(Integer.valueOf(3));
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0931g(hashSet, i, z, str);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public C0931g[] aq(int i) {
        return new C0931g[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m638L(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aq(x0);
    }
}
