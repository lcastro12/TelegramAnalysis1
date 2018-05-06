package com.google.android.gms.games.multiplayer.realtime;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.ArrayList;

public class C0124b implements Creator<RoomEntity> {
    static void m162a(RoomEntity roomEntity, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m121a(parcel, 1, roomEntity.getRoomId(), false);
        C0108b.m131c(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, roomEntity.getVersionCode());
        C0108b.m121a(parcel, 2, roomEntity.getCreatorId(), false);
        C0108b.m116a(parcel, 3, roomEntity.getCreationTimestamp());
        C0108b.m131c(parcel, 4, roomEntity.getStatus());
        C0108b.m121a(parcel, 5, roomEntity.getDescription(), false);
        C0108b.m131c(parcel, 6, roomEntity.getVariant());
        C0108b.m117a(parcel, 7, roomEntity.getAutoMatchCriteria(), false);
        C0108b.m130b(parcel, 8, roomEntity.getParticipants(), false);
        C0108b.m131c(parcel, 9, roomEntity.getAutoMatchWaitEstimateSeconds());
        C0108b.m112C(parcel, k);
    }

    public RoomEntity[] m163V(int i) {
        return new RoomEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return mo750y(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m163V(x0);
    }

    public RoomEntity mo750y(Parcel parcel) {
        int i = 0;
        ArrayList arrayList = null;
        int j = C0107a.m92j(parcel);
        long j2 = 0;
        Bundle bundle = null;
        int i2 = 0;
        String str = null;
        int i3 = 0;
        String str2 = null;
        String str3 = null;
        int i4 = 0;
        while (parcel.dataPosition() < j) {
            int i5 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i5)) {
                case 1:
                    str3 = C0107a.m94l(parcel, i5);
                    break;
                case 2:
                    str2 = C0107a.m94l(parcel, i5);
                    break;
                case 3:
                    j2 = C0107a.m87g(parcel, i5);
                    break;
                case 4:
                    i3 = C0107a.m86f(parcel, i5);
                    break;
                case 5:
                    str = C0107a.m94l(parcel, i5);
                    break;
                case 6:
                    i2 = C0107a.m86f(parcel, i5);
                    break;
                case 7:
                    bundle = C0107a.m96n(parcel, i5);
                    break;
                case 8:
                    arrayList = C0107a.m82c(parcel, i5, ParticipantEntity.CREATOR);
                    break;
                case 9:
                    i = C0107a.m86f(parcel, i5);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i4 = C0107a.m86f(parcel, i5);
                    break;
                default:
                    C0107a.m80b(parcel, i5);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new RoomEntity(i4, str3, str2, j2, i3, str, i2, bundle, arrayList, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }
}
