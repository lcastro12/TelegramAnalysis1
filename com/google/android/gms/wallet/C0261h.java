package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0261h implements Creator<MaskedWalletRequest> {
    static void m790a(MaskedWalletRequest maskedWalletRequest, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, maskedWalletRequest.getVersionCode());
        C0108b.m121a(parcel, 2, maskedWalletRequest.tI, false);
        C0108b.m124a(parcel, 3, maskedWalletRequest.ub);
        C0108b.m124a(parcel, 4, maskedWalletRequest.uc);
        C0108b.m124a(parcel, 5, maskedWalletRequest.ud);
        C0108b.m121a(parcel, 6, maskedWalletRequest.ue, false);
        C0108b.m121a(parcel, 7, maskedWalletRequest.tE, false);
        C0108b.m121a(parcel, 8, maskedWalletRequest.uf, false);
        C0108b.m120a(parcel, 9, maskedWalletRequest.tO, i, false);
        C0108b.m124a(parcel, 10, maskedWalletRequest.ug);
        C0108b.m124a(parcel, 11, maskedWalletRequest.uh);
        C0108b.m112C(parcel, k);
    }

    public MaskedWalletRequest m791U(Parcel parcel) {
        Cart cart = null;
        boolean z = false;
        int j = C0107a.m92j(parcel);
        boolean z2 = false;
        String str = null;
        String str2 = null;
        String str3 = null;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        String str4 = null;
        int i = 0;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    str4 = C0107a.m94l(parcel, i2);
                    break;
                case 3:
                    z5 = C0107a.m83c(parcel, i2);
                    break;
                case 4:
                    z4 = C0107a.m83c(parcel, i2);
                    break;
                case 5:
                    z3 = C0107a.m83c(parcel, i2);
                    break;
                case 6:
                    str3 = C0107a.m94l(parcel, i2);
                    break;
                case 7:
                    str2 = C0107a.m94l(parcel, i2);
                    break;
                case 8:
                    str = C0107a.m94l(parcel, i2);
                    break;
                case 9:
                    cart = (Cart) C0107a.m77a(parcel, i2, Cart.CREATOR);
                    break;
                case 10:
                    z2 = C0107a.m83c(parcel, i2);
                    break;
                case 11:
                    z = C0107a.m83c(parcel, i2);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new MaskedWalletRequest(i, str4, z5, z4, z3, str3, str2, str, cart, z2, z);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public MaskedWalletRequest[] az(int i) {
        return new MaskedWalletRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m791U(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return az(x0);
    }
}
