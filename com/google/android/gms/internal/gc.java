package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.fv.C0929d;
import java.util.HashSet;
import java.util.Set;

public class gc implements Creator<C0929d> {
    static void m633a(C0929d c0929d, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        Set di = c0929d.di();
        if (di.contains(Integer.valueOf(1))) {
            C0108b.m131c(parcel, 1, c0929d.getVersionCode());
        }
        if (di.contains(Integer.valueOf(2))) {
            C0108b.m121a(parcel, 2, c0929d.getFamilyName(), true);
        }
        if (di.contains(Integer.valueOf(3))) {
            C0108b.m121a(parcel, 3, c0929d.getFormatted(), true);
        }
        if (di.contains(Integer.valueOf(4))) {
            C0108b.m121a(parcel, 4, c0929d.getGivenName(), true);
        }
        if (di.contains(Integer.valueOf(5))) {
            C0108b.m121a(parcel, 5, c0929d.getHonorificPrefix(), true);
        }
        if (di.contains(Integer.valueOf(6))) {
            C0108b.m121a(parcel, 6, c0929d.getHonorificSuffix(), true);
        }
        if (di.contains(Integer.valueOf(7))) {
            C0108b.m121a(parcel, 7, c0929d.getMiddleName(), true);
        }
        C0108b.m112C(parcel, k);
    }

    public C0929d m634J(Parcel parcel) {
        String str = null;
        int j = C0107a.m92j(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    str6 = C0107a.m94l(parcel, i2);
                    hashSet.add(Integer.valueOf(2));
                    break;
                case 3:
                    str5 = C0107a.m94l(parcel, i2);
                    hashSet.add(Integer.valueOf(3));
                    break;
                case 4:
                    str4 = C0107a.m94l(parcel, i2);
                    hashSet.add(Integer.valueOf(4));
                    break;
                case 5:
                    str3 = C0107a.m94l(parcel, i2);
                    hashSet.add(Integer.valueOf(5));
                    break;
                case 6:
                    str2 = C0107a.m94l(parcel, i2);
                    hashSet.add(Integer.valueOf(6));
                    break;
                case 7:
                    str = C0107a.m94l(parcel, i2);
                    hashSet.add(Integer.valueOf(7));
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0929d(hashSet, i, str6, str5, str4, str3, str2, str);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public C0929d[] ao(int i) {
        return new C0929d[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m634J(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ao(x0);
    }
}
