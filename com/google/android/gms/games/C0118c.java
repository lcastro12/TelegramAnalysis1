package com.google.android.gms.games;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.location.LocationStatusCodes;

public class C0118c implements Creator<PlayerEntity> {
    static void m149a(PlayerEntity playerEntity, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m121a(parcel, 1, playerEntity.getPlayerId(), false);
        C0108b.m131c(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, playerEntity.getVersionCode());
        C0108b.m121a(parcel, 2, playerEntity.getDisplayName(), false);
        C0108b.m120a(parcel, 3, playerEntity.getIconImageUri(), i, false);
        C0108b.m120a(parcel, 4, playerEntity.getHiResImageUri(), i, false);
        C0108b.m116a(parcel, 5, playerEntity.getRetrievedTimestamp());
        C0108b.m112C(parcel, k);
    }

    public PlayerEntity[] m150L(int i) {
        return new PlayerEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return mo712u(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m150L(x0);
    }

    public PlayerEntity mo712u(Parcel parcel) {
        Uri uri = null;
        int j = C0107a.m92j(parcel);
        int i = 0;
        long j2 = 0;
        Uri uri2 = null;
        String str = null;
        String str2 = null;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    str2 = C0107a.m94l(parcel, i2);
                    break;
                case 2:
                    str = C0107a.m94l(parcel, i2);
                    break;
                case 3:
                    uri2 = (Uri) C0107a.m77a(parcel, i2, Uri.CREATOR);
                    break;
                case 4:
                    uri = (Uri) C0107a.m77a(parcel, i2, Uri.CREATOR);
                    break;
                case 5:
                    j2 = C0107a.m87g(parcel, i2);
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
            return new PlayerEntity(i, str2, str, uri2, uri, j2);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }
}
