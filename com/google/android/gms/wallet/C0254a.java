package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0254a implements Creator<Address> {
    static void m776a(Address address, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, address.getVersionCode());
        C0108b.m121a(parcel, 2, address.name, false);
        C0108b.m121a(parcel, 3, address.tu, false);
        C0108b.m121a(parcel, 4, address.tv, false);
        C0108b.m121a(parcel, 5, address.tw, false);
        C0108b.m121a(parcel, 6, address.hl, false);
        C0108b.m121a(parcel, 7, address.tx, false);
        C0108b.m121a(parcel, 8, address.ty, false);
        C0108b.m121a(parcel, 9, address.tz, false);
        C0108b.m121a(parcel, 10, address.tA, false);
        C0108b.m124a(parcel, 11, address.tB);
        C0108b.m121a(parcel, 12, address.tC, false);
        C0108b.m112C(parcel, k);
    }

    public Address m777N(Parcel parcel) {
        int j = C0107a.m92j(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        String str9 = null;
        boolean z = false;
        String str10 = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    str = C0107a.m94l(parcel, i2);
                    break;
                case 3:
                    str2 = C0107a.m94l(parcel, i2);
                    break;
                case 4:
                    str3 = C0107a.m94l(parcel, i2);
                    break;
                case 5:
                    str4 = C0107a.m94l(parcel, i2);
                    break;
                case 6:
                    str5 = C0107a.m94l(parcel, i2);
                    break;
                case 7:
                    str6 = C0107a.m94l(parcel, i2);
                    break;
                case 8:
                    str7 = C0107a.m94l(parcel, i2);
                    break;
                case 9:
                    str8 = C0107a.m94l(parcel, i2);
                    break;
                case 10:
                    str9 = C0107a.m94l(parcel, i2);
                    break;
                case 11:
                    z = C0107a.m83c(parcel, i2);
                    break;
                case 12:
                    str10 = C0107a.m94l(parcel, i2);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new Address(i, str, str2, str3, str4, str5, str6, str7, str8, str9, z, str10);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public Address[] as(int i) {
        return new Address[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m777N(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return as(x0);
    }
}
