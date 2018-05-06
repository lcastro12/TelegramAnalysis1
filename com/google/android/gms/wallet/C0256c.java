package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0256c implements Creator<FullWallet> {
    static void m780a(FullWallet fullWallet, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, fullWallet.getVersionCode());
        C0108b.m121a(parcel, 2, fullWallet.tH, false);
        C0108b.m121a(parcel, 3, fullWallet.tI, false);
        C0108b.m120a(parcel, 4, fullWallet.tJ, i, false);
        C0108b.m121a(parcel, 5, fullWallet.tK, false);
        C0108b.m120a(parcel, 6, fullWallet.tL, i, false);
        C0108b.m120a(parcel, 7, fullWallet.tM, i, false);
        C0108b.m127a(parcel, 8, fullWallet.tN, false);
        C0108b.m112C(parcel, k);
    }

    public FullWallet m781P(Parcel parcel) {
        String[] strArr = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        Address address = null;
        Address address2 = null;
        String str = null;
        ProxyCard proxyCard = null;
        String str2 = null;
        String str3 = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    str3 = C0107a.m94l(parcel, i2);
                    break;
                case 3:
                    str2 = C0107a.m94l(parcel, i2);
                    break;
                case 4:
                    proxyCard = (ProxyCard) C0107a.m77a(parcel, i2, ProxyCard.CREATOR);
                    break;
                case 5:
                    str = C0107a.m94l(parcel, i2);
                    break;
                case 6:
                    address2 = (Address) C0107a.m77a(parcel, i2, Address.CREATOR);
                    break;
                case 7:
                    address = (Address) C0107a.m77a(parcel, i2, Address.CREATOR);
                    break;
                case 8:
                    strArr = C0107a.m105w(parcel, i2);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new FullWallet(i, str3, str2, proxyCard, str, address2, address, strArr);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public FullWallet[] au(int i) {
        return new FullWallet[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m781P(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return au(x0);
    }
}
