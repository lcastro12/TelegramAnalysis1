package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import java.util.List;

public class bx implements Creator<bw> {
    static void m229a(bw bwVar, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, bwVar.versionCode);
        C0108b.m121a(parcel, 2, bwVar.fW, false);
        C0108b.m121a(parcel, 3, bwVar.gG, false);
        C0108b.m122a(parcel, 4, bwVar.eW, false);
        C0108b.m131c(parcel, 5, bwVar.errorCode);
        C0108b.m122a(parcel, 6, bwVar.eX, false);
        C0108b.m116a(parcel, 7, bwVar.gH);
        C0108b.m124a(parcel, 8, bwVar.gI);
        C0108b.m116a(parcel, 9, bwVar.gJ);
        C0108b.m122a(parcel, 10, bwVar.gK, false);
        C0108b.m116a(parcel, 11, bwVar.fa);
        C0108b.m131c(parcel, 12, bwVar.orientation);
        C0108b.m112C(parcel, k);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m230f(x0);
    }

    public bw m230f(Parcel parcel) {
        int j = C0107a.m92j(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        List list = null;
        int i2 = 0;
        List list2 = null;
        long j2 = 0;
        boolean z = false;
        long j3 = 0;
        List list3 = null;
        long j4 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i4)) {
                case 1:
                    i = C0107a.m86f(parcel, i4);
                    break;
                case 2:
                    str = C0107a.m94l(parcel, i4);
                    break;
                case 3:
                    str2 = C0107a.m94l(parcel, i4);
                    break;
                case 4:
                    list = C0107a.m106x(parcel, i4);
                    break;
                case 5:
                    i2 = C0107a.m86f(parcel, i4);
                    break;
                case 6:
                    list2 = C0107a.m106x(parcel, i4);
                    break;
                case 7:
                    j2 = C0107a.m87g(parcel, i4);
                    break;
                case 8:
                    z = C0107a.m83c(parcel, i4);
                    break;
                case 9:
                    j3 = C0107a.m87g(parcel, i4);
                    break;
                case 10:
                    list3 = C0107a.m106x(parcel, i4);
                    break;
                case 11:
                    j4 = C0107a.m87g(parcel, i4);
                    break;
                case 12:
                    i3 = C0107a.m86f(parcel, i4);
                    break;
                default:
                    C0107a.m80b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new bw(i, str, str2, list, i2, list2, j2, z, j3, list3, j4, i3);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public bw[] m231j(int i) {
        return new bw[i];
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m231j(x0);
    }
}
