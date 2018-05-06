package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.location.LocationStatusCodes;

public class fb implements Creator<fa> {
    static void m583a(fa faVar, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m121a(parcel, 1, faVar.getRequestId(), false);
        C0108b.m131c(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, faVar.getVersionCode());
        C0108b.m116a(parcel, 2, faVar.getExpirationTime());
        C0108b.m123a(parcel, 3, faVar.co());
        C0108b.m114a(parcel, 4, faVar.getLatitude());
        C0108b.m114a(parcel, 5, faVar.getLongitude());
        C0108b.m115a(parcel, 6, faVar.cp());
        C0108b.m131c(parcel, 7, faVar.cq());
        C0108b.m131c(parcel, 8, faVar.getNotificationResponsiveness());
        C0108b.m131c(parcel, 9, faVar.cr());
        C0108b.m112C(parcel, k);
    }

    public fa[] ac(int i) {
        return new fa[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m584z(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ac(x0);
    }

    public fa m584z(Parcel parcel) {
        int j = C0107a.m92j(parcel);
        int i = 0;
        String str = null;
        int i2 = 0;
        short s = (short) 0;
        double d = 0.0d;
        double d2 = 0.0d;
        float f = 0.0f;
        long j2 = 0;
        int i3 = 0;
        int i4 = -1;
        while (parcel.dataPosition() < j) {
            int i5 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i5)) {
                case 1:
                    str = C0107a.m94l(parcel, i5);
                    break;
                case 2:
                    j2 = C0107a.m87g(parcel, i5);
                    break;
                case 3:
                    s = C0107a.m85e(parcel, i5);
                    break;
                case 4:
                    d = C0107a.m91j(parcel, i5);
                    break;
                case 5:
                    d2 = C0107a.m91j(parcel, i5);
                    break;
                case 6:
                    f = C0107a.m89i(parcel, i5);
                    break;
                case 7:
                    i2 = C0107a.m86f(parcel, i5);
                    break;
                case 8:
                    i3 = C0107a.m86f(parcel, i5);
                    break;
                case 9:
                    i4 = C0107a.m86f(parcel, i5);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i = C0107a.m86f(parcel, i5);
                    break;
                default:
                    C0107a.m80b(parcel, i5);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new fa(i, str, i2, s, d, d2, f, j2, i3, i4);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }
}
