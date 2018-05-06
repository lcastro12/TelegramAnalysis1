package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.fv.C0928c;
import java.util.HashSet;
import java.util.Set;

public class gb implements Creator<C0928c> {
    static void m631a(C0928c c0928c, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        Set di = c0928c.di();
        if (di.contains(Integer.valueOf(1))) {
            C0108b.m131c(parcel, 1, c0928c.getVersionCode());
        }
        if (di.contains(Integer.valueOf(2))) {
            C0108b.m121a(parcel, 2, c0928c.getUrl(), true);
        }
        C0108b.m112C(parcel, k);
    }

    public C0928c m632I(Parcel parcel) {
        int j = C0107a.m92j(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    str = C0107a.m94l(parcel, i2);
                    hashSet.add(Integer.valueOf(2));
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0928c(hashSet, i, str);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public C0928c[] an(int i) {
        return new C0928c[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m632I(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return an(x0);
    }
}
