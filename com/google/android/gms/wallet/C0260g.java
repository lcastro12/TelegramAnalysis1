package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0260g implements Creator<MaskedWallet> {
    static void m788a(MaskedWallet maskedWallet, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, maskedWallet.getVersionCode());
        C0108b.m121a(parcel, 2, maskedWallet.tH, false);
        C0108b.m121a(parcel, 3, maskedWallet.tI, false);
        C0108b.m127a(parcel, 4, maskedWallet.tN, false);
        C0108b.m121a(parcel, 5, maskedWallet.tK, false);
        C0108b.m120a(parcel, 6, maskedWallet.tL, i, false);
        C0108b.m120a(parcel, 7, maskedWallet.tM, i, false);
        C0108b.m126a(parcel, 8, maskedWallet.tZ, i, false);
        C0108b.m126a(parcel, 9, maskedWallet.ua, i, false);
        C0108b.m112C(parcel, k);
    }

    public MaskedWallet m789T(Parcel parcel) {
        OfferWalletObject[] offerWalletObjectArr = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        LoyaltyWalletObject[] loyaltyWalletObjectArr = null;
        Address address = null;
        Address address2 = null;
        String str = null;
        String[] strArr = null;
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
                    strArr = C0107a.m105w(parcel, i2);
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
                    loyaltyWalletObjectArr = (LoyaltyWalletObject[]) C0107a.m81b(parcel, i2, LoyaltyWalletObject.CREATOR);
                    break;
                case 9:
                    offerWalletObjectArr = (OfferWalletObject[]) C0107a.m81b(parcel, i2, OfferWalletObject.CREATOR);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new MaskedWallet(i, str3, str2, strArr, str, address2, address, loyaltyWalletObjectArr, offerWalletObjectArr);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public MaskedWallet[] ay(int i) {
        return new MaskedWallet[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m789T(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ay(x0);
    }
}
