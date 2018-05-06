package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class ds implements Creator<dr> {
    static void m404a(dr drVar, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, drVar.getVersionCode());
        C0108b.m120a(parcel, 2, drVar.bk(), i, false);
        C0108b.m112C(parcel, k);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m405l(x0);
    }

    public dr m405l(Parcel parcel) {
        int j = C0107a.m92j(parcel);
        int i = 0;
        dt dtVar = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    dtVar = (dt) C0107a.m77a(parcel, i2, dt.CREATOR);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new dr(i, dtVar);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m406z(x0);
    }

    public dr[] m406z(int i) {
        return new dr[i];
    }
}
