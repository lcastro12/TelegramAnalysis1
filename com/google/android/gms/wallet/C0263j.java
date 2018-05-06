package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0263j implements Creator<OfferWalletObject> {
    static void m794a(OfferWalletObject offerWalletObject, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, offerWalletObject.getVersionCode());
        C0108b.m121a(parcel, 2, offerWalletObject.tU, false);
        C0108b.m121a(parcel, 3, offerWalletObject.ul, false);
        C0108b.m112C(parcel, k);
    }

    public OfferWalletObject m795W(Parcel parcel) {
        String str = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        String str2 = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    str2 = C0107a.m94l(parcel, i2);
                    break;
                case 3:
                    str = C0107a.m94l(parcel, i2);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new OfferWalletObject(i, str2, str);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public OfferWalletObject[] aB(int i) {
        return new OfferWalletObject[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m795W(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aB(x0);
    }
}
