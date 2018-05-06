package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import java.util.HashSet;
import java.util.Set;

public class ft implements Creator<fs> {
    static void m619a(fs fsVar, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        Set di = fsVar.di();
        if (di.contains(Integer.valueOf(1))) {
            C0108b.m131c(parcel, 1, fsVar.getVersionCode());
        }
        if (di.contains(Integer.valueOf(2))) {
            C0108b.m121a(parcel, 2, fsVar.getId(), true);
        }
        if (di.contains(Integer.valueOf(4))) {
            C0108b.m120a(parcel, 4, fsVar.dz(), i, true);
        }
        if (di.contains(Integer.valueOf(5))) {
            C0108b.m121a(parcel, 5, fsVar.getStartDate(), true);
        }
        if (di.contains(Integer.valueOf(6))) {
            C0108b.m120a(parcel, 6, fsVar.dA(), i, true);
        }
        if (di.contains(Integer.valueOf(7))) {
            C0108b.m121a(parcel, 7, fsVar.getType(), true);
        }
        C0108b.m112C(parcel, k);
    }

    public fs m620C(Parcel parcel) {
        String str = null;
        int j = C0107a.m92j(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        fq fqVar = null;
        String str2 = null;
        fq fqVar2 = null;
        String str3 = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            fq fqVar3;
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    str3 = C0107a.m94l(parcel, i2);
                    hashSet.add(Integer.valueOf(2));
                    break;
                case 4:
                    fqVar3 = (fq) C0107a.m77a(parcel, i2, fq.CREATOR);
                    hashSet.add(Integer.valueOf(4));
                    fqVar2 = fqVar3;
                    break;
                case 5:
                    str2 = C0107a.m94l(parcel, i2);
                    hashSet.add(Integer.valueOf(5));
                    break;
                case 6:
                    fqVar3 = (fq) C0107a.m77a(parcel, i2, fq.CREATOR);
                    hashSet.add(Integer.valueOf(6));
                    fqVar = fqVar3;
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
            return new fs(hashSet, i, str3, fqVar2, str2, fqVar, str);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public fs[] ah(int i) {
        return new fs[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m620C(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ah(x0);
    }
}
