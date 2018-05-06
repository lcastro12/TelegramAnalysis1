package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import java.util.ArrayList;

public class C0255b implements Creator<Cart> {
    static void m778a(Cart cart, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, cart.getVersionCode());
        C0108b.m121a(parcel, 2, cart.tD, false);
        C0108b.m121a(parcel, 3, cart.tE, false);
        C0108b.m130b(parcel, 4, cart.tF, false);
        C0108b.m112C(parcel, k);
    }

    public Cart m779O(Parcel parcel) {
        String str = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        ArrayList arrayList = new ArrayList();
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
                    arrayList = C0107a.m82c(parcel, i2, LineItem.CREATOR);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new Cart(i, str2, str, arrayList);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public Cart[] at(int i) {
        return new Cart[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m779O(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return at(x0);
    }
}
