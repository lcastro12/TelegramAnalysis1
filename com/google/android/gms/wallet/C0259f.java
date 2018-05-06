package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0259f implements Creator<LoyaltyWalletObject> {
    static void m786a(LoyaltyWalletObject loyaltyWalletObject, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, loyaltyWalletObject.getVersionCode());
        C0108b.m121a(parcel, 2, loyaltyWalletObject.tU, false);
        C0108b.m121a(parcel, 3, loyaltyWalletObject.tV, false);
        C0108b.m121a(parcel, 4, loyaltyWalletObject.tW, false);
        C0108b.m121a(parcel, 5, loyaltyWalletObject.tX, false);
        C0108b.m121a(parcel, 6, loyaltyWalletObject.tY, false);
        C0108b.m112C(parcel, k);
    }

    public LoyaltyWalletObject m787S(Parcel parcel) {
        String str = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    str5 = C0107a.m94l(parcel, i2);
                    break;
                case 3:
                    str4 = C0107a.m94l(parcel, i2);
                    break;
                case 4:
                    str3 = C0107a.m94l(parcel, i2);
                    break;
                case 5:
                    str2 = C0107a.m94l(parcel, i2);
                    break;
                case 6:
                    str = C0107a.m94l(parcel, i2);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new LoyaltyWalletObject(i, str5, str4, str3, str2, str);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public LoyaltyWalletObject[] ax(int i) {
        return new LoyaltyWalletObject[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m787S(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ax(x0);
    }
}
