package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0264k implements Creator<ProxyCard> {
    static void m796a(ProxyCard proxyCard, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, proxyCard.getVersionCode());
        C0108b.m121a(parcel, 2, proxyCard.um, false);
        C0108b.m121a(parcel, 3, proxyCard.un, false);
        C0108b.m131c(parcel, 4, proxyCard.uo);
        C0108b.m131c(parcel, 5, proxyCard.up);
        C0108b.m112C(parcel, k);
    }

    public ProxyCard m797X(Parcel parcel) {
        String str = null;
        int i = 0;
        int j = C0107a.m92j(parcel);
        int i2 = 0;
        String str2 = null;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i4)) {
                case 1:
                    i3 = C0107a.m86f(parcel, i4);
                    break;
                case 2:
                    str2 = C0107a.m94l(parcel, i4);
                    break;
                case 3:
                    str = C0107a.m94l(parcel, i4);
                    break;
                case 4:
                    i2 = C0107a.m86f(parcel, i4);
                    break;
                case 5:
                    i = C0107a.m86f(parcel, i4);
                    break;
                default:
                    C0107a.m80b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new ProxyCard(i3, str2, str, i2, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public ProxyCard[] aC(int i) {
        return new ProxyCard[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m797X(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aC(x0);
    }
}
