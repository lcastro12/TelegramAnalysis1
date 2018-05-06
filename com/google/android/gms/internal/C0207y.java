package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0207y implements Creator<C0771x> {
    static void m700a(C0771x c0771x, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, c0771x.versionCode);
        C0108b.m121a(parcel, 2, c0771x.ew, false);
        C0108b.m131c(parcel, 3, c0771x.height);
        C0108b.m131c(parcel, 4, c0771x.heightPixels);
        C0108b.m124a(parcel, 5, c0771x.ex);
        C0108b.m131c(parcel, 6, c0771x.width);
        C0108b.m131c(parcel, 7, c0771x.widthPixels);
        C0108b.m112C(parcel, k);
    }

    public C0771x m701b(Parcel parcel) {
        int i = 0;
        int j = C0107a.m92j(parcel);
        String str = null;
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (parcel.dataPosition() < j) {
            int i6 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i6)) {
                case 1:
                    i5 = C0107a.m86f(parcel, i6);
                    break;
                case 2:
                    str = C0107a.m94l(parcel, i6);
                    break;
                case 3:
                    i4 = C0107a.m86f(parcel, i6);
                    break;
                case 4:
                    i3 = C0107a.m86f(parcel, i6);
                    break;
                case 5:
                    z = C0107a.m83c(parcel, i6);
                    break;
                case 6:
                    i2 = C0107a.m86f(parcel, i6);
                    break;
                case 7:
                    i = C0107a.m86f(parcel, i6);
                    break;
                default:
                    C0107a.m80b(parcel, i6);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0771x(i5, str, i4, i3, z, i2, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public C0771x[] m702c(int i) {
        return new C0771x[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m701b(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m702c(x0);
    }
}
