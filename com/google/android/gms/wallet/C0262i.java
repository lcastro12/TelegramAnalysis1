package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class C0262i implements Creator<NotifyTransactionStatusRequest> {
    static void m792a(NotifyTransactionStatusRequest notifyTransactionStatusRequest, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, notifyTransactionStatusRequest.iM);
        C0108b.m121a(parcel, 2, notifyTransactionStatusRequest.tH, false);
        C0108b.m131c(parcel, 3, notifyTransactionStatusRequest.status);
        C0108b.m121a(parcel, 4, notifyTransactionStatusRequest.uj, false);
        C0108b.m112C(parcel, k);
    }

    public NotifyTransactionStatusRequest m793V(Parcel parcel) {
        String str = null;
        int i = 0;
        int j = C0107a.m92j(parcel);
        String str2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i3)) {
                case 1:
                    i2 = C0107a.m86f(parcel, i3);
                    break;
                case 2:
                    str2 = C0107a.m94l(parcel, i3);
                    break;
                case 3:
                    i = C0107a.m86f(parcel, i3);
                    break;
                case 4:
                    str = C0107a.m94l(parcel, i3);
                    break;
                default:
                    C0107a.m80b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new NotifyTransactionStatusRequest(i2, str2, i, str);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public NotifyTransactionStatusRequest[] aA(int i) {
        return new NotifyTransactionStatusRequest[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m793V(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return aA(x0);
    }
}
