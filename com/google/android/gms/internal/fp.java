package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.location.LocationStatusCodes;

public class fp implements Creator<fn> {
    static void m615a(fn fnVar, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m121a(parcel, 1, fnVar.getAccountName(), false);
        C0108b.m131c(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, fnVar.getVersionCode());
        C0108b.m127a(parcel, 2, fnVar.cZ(), false);
        C0108b.m127a(parcel, 3, fnVar.da(), false);
        C0108b.m127a(parcel, 4, fnVar.db(), false);
        C0108b.m121a(parcel, 5, fnVar.dc(), false);
        C0108b.m121a(parcel, 6, fnVar.dd(), false);
        C0108b.m121a(parcel, 7, fnVar.de(), false);
        C0108b.m121a(parcel, 8, fnVar.df(), false);
        C0108b.m112C(parcel, k);
    }

    public fn m616A(Parcel parcel) {
        String str = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String[] strArr = null;
        String[] strArr2 = null;
        String[] strArr3 = null;
        String str5 = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    str5 = C0107a.m94l(parcel, i2);
                    break;
                case 2:
                    strArr3 = C0107a.m105w(parcel, i2);
                    break;
                case 3:
                    strArr2 = C0107a.m105w(parcel, i2);
                    break;
                case 4:
                    strArr = C0107a.m105w(parcel, i2);
                    break;
                case 5:
                    str4 = C0107a.m94l(parcel, i2);
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
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i = C0107a.m86f(parcel, i2);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new fn(i, str5, strArr3, strArr2, strArr, str4, str3, str2, str);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public fn[] af(int i) {
        return new fn[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m616A(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return af(x0);
    }
}
