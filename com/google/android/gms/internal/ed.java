package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class ed implements Creator<ec> {
    static void m435a(ec ecVar, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, ecVar.getVersionCode());
        C0108b.m119a(parcel, 2, ecVar.bH(), false);
        C0108b.m120a(parcel, 3, ecVar.bI(), i, false);
        C0108b.m112C(parcel, k);
    }

    public ec[] m436G(int i) {
        return new ec[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m437s(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m436G(x0);
    }

    public ec m437s(Parcel parcel) {
        dz dzVar = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        Parcel parcel2 = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    parcel2 = C0107a.m108y(parcel, i2);
                    break;
                case 3:
                    dzVar = (dz) C0107a.m77a(parcel, i2, dz.CREATOR);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new ec(i, parcel2, dzVar);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }
}
