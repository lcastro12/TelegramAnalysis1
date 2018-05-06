package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0258e implements Creator<LineItem> {
    static void m784a(LineItem lineItem, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, lineItem.getVersionCode());
        C0108b.m121a(parcel, 2, lineItem.description, false);
        C0108b.m121a(parcel, 3, lineItem.tQ, false);
        C0108b.m121a(parcel, 4, lineItem.tR, false);
        C0108b.m121a(parcel, 5, lineItem.tD, false);
        C0108b.m131c(parcel, 6, lineItem.tS);
        C0108b.m121a(parcel, 7, lineItem.tE, false);
        C0108b.m112C(parcel, k);
    }

    public LineItem m785R(Parcel parcel) {
        int i = 0;
        String str = null;
        int j = C0107a.m92j(parcel);
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i3)) {
                case 1:
                    i2 = C0107a.m86f(parcel, i3);
                    break;
                case 2:
                    str5 = C0107a.m94l(parcel, i3);
                    break;
                case 3:
                    str4 = C0107a.m94l(parcel, i3);
                    break;
                case 4:
                    str3 = C0107a.m94l(parcel, i3);
                    break;
                case 5:
                    str2 = C0107a.m94l(parcel, i3);
                    break;
                case 6:
                    i = C0107a.m86f(parcel, i3);
                    break;
                case 7:
                    str = C0107a.m94l(parcel, i3);
                    break;
                default:
                    C0107a.m80b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new LineItem(i2, str5, str4, str3, str2, i, str);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public LineItem[] aw(int i) {
        return new LineItem[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m785R(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aw(x0);
    }
}
