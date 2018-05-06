package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.location.LocationStatusCodes;

public class C0098e implements Creator<C0646d> {
    static void m45a(C0646d c0646d, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m127a(parcel, 1, c0646d.aK(), false);
        C0108b.m131c(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, c0646d.getVersionCode());
        C0108b.m126a(parcel, 2, c0646d.aL(), i, false);
        C0108b.m131c(parcel, 3, c0646d.getStatusCode());
        C0108b.m117a(parcel, 4, c0646d.aM(), false);
        C0108b.m112C(parcel, k);
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m46h(x0);
    }

    public C0646d m46h(Parcel parcel) {
        int i = 0;
        Bundle bundle = null;
        int j = C0107a.m92j(parcel);
        CursorWindow[] cursorWindowArr = null;
        String[] strArr = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i3)) {
                case 1:
                    strArr = C0107a.m105w(parcel, i3);
                    break;
                case 2:
                    cursorWindowArr = (CursorWindow[]) C0107a.m81b(parcel, i3, CursorWindow.CREATOR);
                    break;
                case 3:
                    i = C0107a.m86f(parcel, i3);
                    break;
                case 4:
                    bundle = C0107a.m96n(parcel, i3);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i2 = C0107a.m86f(parcel, i3);
                    break;
                default:
                    C0107a.m80b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new C0106a("Overread allowed size end=" + j, parcel);
        }
        C0646d c0646d = new C0646d(i2, strArr, cursorWindowArr, i, bundle);
        c0646d.aJ();
        return c0646d;
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m47s(x0);
    }

    public C0646d[] m47s(int i) {
        return new C0646d[i];
    }
}
