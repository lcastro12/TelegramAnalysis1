package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import java.util.List;

public class C0206w implements Creator<C0770v> {
    static void m697a(C0770v c0770v, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, c0770v.versionCode);
        C0108b.m116a(parcel, 2, c0770v.es);
        C0108b.m117a(parcel, 3, c0770v.extras, false);
        C0108b.m131c(parcel, 4, c0770v.et);
        C0108b.m122a(parcel, 5, c0770v.eu, false);
        C0108b.m124a(parcel, 6, c0770v.ev);
        C0108b.m131c(parcel, 7, c0770v.tagForChildDirectedTreatment);
        C0108b.m112C(parcel, k);
    }

    public C0770v m698a(Parcel parcel) {
        List list = null;
        int i = 0;
        int j = C0107a.m92j(parcel);
        long j2 = 0;
        boolean z = false;
        int i2 = 0;
        Bundle bundle = null;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i4)) {
                case 1:
                    i3 = C0107a.m86f(parcel, i4);
                    break;
                case 2:
                    j2 = C0107a.m87g(parcel, i4);
                    break;
                case 3:
                    bundle = C0107a.m96n(parcel, i4);
                    break;
                case 4:
                    i2 = C0107a.m86f(parcel, i4);
                    break;
                case 5:
                    list = C0107a.m106x(parcel, i4);
                    break;
                case 6:
                    z = C0107a.m83c(parcel, i4);
                    break;
                case 7:
                    i = C0107a.m86f(parcel, i4);
                    break;
                default:
                    C0107a.m80b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new C0770v(i3, j2, bundle, i2, list, z, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public C0770v[] m699b(int i) {
        return new C0770v[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m698a(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m699b(x0);
    }
}
