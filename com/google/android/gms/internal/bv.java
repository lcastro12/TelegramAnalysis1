package com.google.android.gms.internal;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class bv implements Creator<bu> {
    static void m226a(bu buVar, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, buVar.versionCode);
        C0108b.m117a(parcel, 2, buVar.gA, false);
        C0108b.m120a(parcel, 3, buVar.gB, i, false);
        C0108b.m120a(parcel, 4, buVar.ed, i, false);
        C0108b.m121a(parcel, 5, buVar.adUnitId, false);
        C0108b.m120a(parcel, 6, buVar.applicationInfo, i, false);
        C0108b.m120a(parcel, 7, buVar.gC, i, false);
        C0108b.m121a(parcel, 8, buVar.gD, false);
        C0108b.m121a(parcel, 9, buVar.gE, false);
        C0108b.m121a(parcel, 10, buVar.gF, false);
        C0108b.m120a(parcel, 11, buVar.eg, i, false);
        C0108b.m112C(parcel, k);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m227e(x0);
    }

    public bu m227e(Parcel parcel) {
        co coVar = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        PackageInfo packageInfo = null;
        ApplicationInfo applicationInfo = null;
        String str4 = null;
        C0771x c0771x = null;
        C0770v c0770v = null;
        Bundle bundle = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    bundle = C0107a.m96n(parcel, i2);
                    break;
                case 3:
                    c0770v = (C0770v) C0107a.m77a(parcel, i2, C0770v.CREATOR);
                    break;
                case 4:
                    c0771x = (C0771x) C0107a.m77a(parcel, i2, C0771x.CREATOR);
                    break;
                case 5:
                    str4 = C0107a.m94l(parcel, i2);
                    break;
                case 6:
                    applicationInfo = (ApplicationInfo) C0107a.m77a(parcel, i2, ApplicationInfo.CREATOR);
                    break;
                case 7:
                    packageInfo = (PackageInfo) C0107a.m77a(parcel, i2, PackageInfo.CREATOR);
                    break;
                case 8:
                    str3 = C0107a.m94l(parcel, i2);
                    break;
                case 9:
                    str2 = C0107a.m94l(parcel, i2);
                    break;
                case 10:
                    str = C0107a.m94l(parcel, i2);
                    break;
                case 11:
                    coVar = (co) C0107a.m77a(parcel, i2, co.CREATOR);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new bu(i, bundle, c0770v, c0771x, str4, applicationInfo, packageInfo, str3, str2, str, coVar);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public bu[] m228i(int i) {
        return new bu[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m228i(x0);
    }
}
