package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.fv.C0927b;
import com.google.android.gms.internal.fv.C0927b.C0925a;
import com.google.android.gms.internal.fv.C0927b.C0926b;
import java.util.HashSet;
import java.util.Set;

public class fy implements Creator<C0927b> {
    static void m625a(C0927b c0927b, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        Set di = c0927b.di();
        if (di.contains(Integer.valueOf(1))) {
            C0108b.m131c(parcel, 1, c0927b.getVersionCode());
        }
        if (di.contains(Integer.valueOf(2))) {
            C0108b.m120a(parcel, 2, c0927b.dM(), i, true);
        }
        if (di.contains(Integer.valueOf(3))) {
            C0108b.m120a(parcel, 3, c0927b.dN(), i, true);
        }
        if (di.contains(Integer.valueOf(4))) {
            C0108b.m131c(parcel, 4, c0927b.getLayout());
        }
        C0108b.m112C(parcel, k);
    }

    public C0927b m626F(Parcel parcel) {
        C0926b c0926b = null;
        int i = 0;
        int j = C0107a.m92j(parcel);
        Set hashSet = new HashSet();
        C0925a c0925a = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i3)) {
                case 1:
                    i2 = C0107a.m86f(parcel, i3);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    C0925a c0925a2 = (C0925a) C0107a.m77a(parcel, i3, C0925a.CREATOR);
                    hashSet.add(Integer.valueOf(2));
                    c0925a = c0925a2;
                    break;
                case 3:
                    C0926b c0926b2 = (C0926b) C0107a.m77a(parcel, i3, C0926b.CREATOR);
                    hashSet.add(Integer.valueOf(3));
                    c0926b = c0926b2;
                    break;
                case 4:
                    i = C0107a.m86f(parcel, i3);
                    hashSet.add(Integer.valueOf(4));
                    break;
                default:
                    C0107a.m80b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0927b(hashSet, i2, c0925a, c0926b, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public C0927b[] ak(int i) {
        return new C0927b[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m626F(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ak(x0);
    }
}
