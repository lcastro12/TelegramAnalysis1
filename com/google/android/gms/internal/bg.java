package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class bg implements Creator<bh> {
    static void m203a(bh bhVar, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, bhVar.versionCode);
        C0108b.m120a(parcel, 2, bhVar.fR, i, false);
        C0108b.m118a(parcel, 3, bhVar.m895U(), false);
        C0108b.m118a(parcel, 4, bhVar.m896V(), false);
        C0108b.m118a(parcel, 5, bhVar.m897W(), false);
        C0108b.m118a(parcel, 6, bhVar.m898X(), false);
        C0108b.m121a(parcel, 7, bhVar.fW, false);
        C0108b.m124a(parcel, 8, bhVar.fX);
        C0108b.m121a(parcel, 9, bhVar.fY, false);
        C0108b.m118a(parcel, 10, bhVar.m899Y(), false);
        C0108b.m131c(parcel, 11, bhVar.orientation);
        C0108b.m131c(parcel, 12, bhVar.ga);
        C0108b.m121a(parcel, 13, bhVar.fz, false);
        C0108b.m120a(parcel, 14, bhVar.eg, i, false);
        C0108b.m112C(parcel, k);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m204d(x0);
    }

    public bh m204d(Parcel parcel) {
        int j = C0107a.m92j(parcel);
        int i = 0;
        be beVar = null;
        IBinder iBinder = null;
        IBinder iBinder2 = null;
        IBinder iBinder3 = null;
        IBinder iBinder4 = null;
        String str = null;
        boolean z = false;
        String str2 = null;
        IBinder iBinder5 = null;
        int i2 = 0;
        int i3 = 0;
        String str3 = null;
        co coVar = null;
        while (parcel.dataPosition() < j) {
            int i4 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i4)) {
                case 1:
                    i = C0107a.m86f(parcel, i4);
                    break;
                case 2:
                    beVar = (be) C0107a.m77a(parcel, i4, be.CREATOR);
                    break;
                case 3:
                    iBinder = C0107a.m95m(parcel, i4);
                    break;
                case 4:
                    iBinder2 = C0107a.m95m(parcel, i4);
                    break;
                case 5:
                    iBinder3 = C0107a.m95m(parcel, i4);
                    break;
                case 6:
                    iBinder4 = C0107a.m95m(parcel, i4);
                    break;
                case 7:
                    str = C0107a.m94l(parcel, i4);
                    break;
                case 8:
                    z = C0107a.m83c(parcel, i4);
                    break;
                case 9:
                    str2 = C0107a.m94l(parcel, i4);
                    break;
                case 10:
                    iBinder5 = C0107a.m95m(parcel, i4);
                    break;
                case 11:
                    i2 = C0107a.m86f(parcel, i4);
                    break;
                case 12:
                    i3 = C0107a.m86f(parcel, i4);
                    break;
                case 13:
                    str3 = C0107a.m94l(parcel, i4);
                    break;
                case 14:
                    coVar = (co) C0107a.m77a(parcel, i4, co.CREATOR);
                    break;
                default:
                    C0107a.m80b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new bh(i, beVar, iBinder, iBinder2, iBinder3, iBinder4, str, z, str2, iBinder5, i2, i3, str3, coVar);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public bh[] m205h(int i) {
        return new bh[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m205h(x0);
    }
}
