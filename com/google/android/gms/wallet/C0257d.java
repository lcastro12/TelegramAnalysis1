package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0257d implements Creator<FullWalletRequest> {
    static void m782a(FullWalletRequest fullWalletRequest, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, fullWalletRequest.getVersionCode());
        C0108b.m121a(parcel, 2, fullWalletRequest.tH, false);
        C0108b.m121a(parcel, 3, fullWalletRequest.tI, false);
        C0108b.m120a(parcel, 4, fullWalletRequest.tO, i, false);
        C0108b.m112C(parcel, k);
    }

    public FullWalletRequest m783Q(Parcel parcel) {
        Cart cart = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        String str = null;
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
                case 4:
                    cart = (Cart) C0107a.m77a(parcel, i2, Cart.CREATOR);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new FullWalletRequest(i, str2, str, cart);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public FullWalletRequest[] av(int i) {
        return new FullWalletRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m783Q(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return av(x0);
    }
}
