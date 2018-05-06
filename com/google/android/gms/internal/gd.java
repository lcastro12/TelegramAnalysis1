package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.fv.C0930f;
import java.util.HashSet;
import java.util.Set;

public class gd implements Creator<C0930f> {
    static void m635a(C0930f c0930f, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        Set di = c0930f.di();
        if (di.contains(Integer.valueOf(1))) {
            C0108b.m131c(parcel, 1, c0930f.getVersionCode());
        }
        if (di.contains(Integer.valueOf(2))) {
            C0108b.m121a(parcel, 2, c0930f.getDepartment(), true);
        }
        if (di.contains(Integer.valueOf(3))) {
            C0108b.m121a(parcel, 3, c0930f.getDescription(), true);
        }
        if (di.contains(Integer.valueOf(4))) {
            C0108b.m121a(parcel, 4, c0930f.getEndDate(), true);
        }
        if (di.contains(Integer.valueOf(5))) {
            C0108b.m121a(parcel, 5, c0930f.getLocation(), true);
        }
        if (di.contains(Integer.valueOf(6))) {
            C0108b.m121a(parcel, 6, c0930f.getName(), true);
        }
        if (di.contains(Integer.valueOf(7))) {
            C0108b.m124a(parcel, 7, c0930f.isPrimary());
        }
        if (di.contains(Integer.valueOf(8))) {
            C0108b.m121a(parcel, 8, c0930f.getStartDate(), true);
        }
        if (di.contains(Integer.valueOf(9))) {
            C0108b.m121a(parcel, 9, c0930f.getTitle(), true);
        }
        if (di.contains(Integer.valueOf(10))) {
            C0108b.m131c(parcel, 10, c0930f.getType());
        }
        C0108b.m112C(parcel, k);
    }

    public C0930f m636K(Parcel parcel) {
        int i = 0;
        String str = null;
        int j = C0107a.m92j(parcel);
        Set hashSet = new HashSet();
        String str2 = null;
        boolean z = false;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i3)) {
                case 1:
                    i2 = C0107a.m86f(parcel, i3);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    str7 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(2));
                    break;
                case 3:
                    str6 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(3));
                    break;
                case 4:
                    str5 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(4));
                    break;
                case 5:
                    str4 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(5));
                    break;
                case 6:
                    str3 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(6));
                    break;
                case 7:
                    z = C0107a.m83c(parcel, i3);
                    hashSet.add(Integer.valueOf(7));
                    break;
                case 8:
                    str2 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(8));
                    break;
                case 9:
                    str = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(9));
                    break;
                case 10:
                    i = C0107a.m86f(parcel, i3);
                    hashSet.add(Integer.valueOf(10));
                    break;
                default:
                    C0107a.m80b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0930f(hashSet, i2, str7, str6, str5, str4, str3, z, str2, str, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public C0930f[] ap(int i) {
        return new C0930f[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m636K(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ap(x0);
    }
}
