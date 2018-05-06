package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.dw.C0722a;
import com.google.android.gms.internal.dz.C0724b;

public class dy implements Creator<C0724b> {
    static void m426a(C0724b c0724b, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, c0724b.versionCode);
        C0108b.m121a(parcel, 2, c0724b.lN, false);
        C0108b.m120a(parcel, 3, c0724b.lO, i, false);
        C0108b.m112C(parcel, k);
    }

    public C0724b[] m427D(int i) {
        return new C0724b[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m428p(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m427D(x0);
    }

    public C0724b m428p(Parcel parcel) {
        C0722a c0722a = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    str = C0107a.m94l(parcel, i2);
                    break;
                case 3:
                    c0722a = (C0722a) C0107a.m77a(parcel, i2, C0722a.CREATOR);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0724b(i, str, c0722a);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }
}
