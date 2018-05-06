package com.google.android.gms.games.multiplayer;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.location.LocationStatusCodes;

public class C0121c implements Creator<ParticipantEntity> {
    static void m157a(ParticipantEntity participantEntity, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m121a(parcel, 1, participantEntity.getParticipantId(), false);
        C0108b.m131c(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, participantEntity.getVersionCode());
        C0108b.m121a(parcel, 2, participantEntity.getDisplayName(), false);
        C0108b.m120a(parcel, 3, participantEntity.getIconImageUri(), i, false);
        C0108b.m120a(parcel, 4, participantEntity.getHiResImageUri(), i, false);
        C0108b.m131c(parcel, 5, participantEntity.getStatus());
        C0108b.m121a(parcel, 6, participantEntity.ci(), false);
        C0108b.m124a(parcel, 7, participantEntity.isConnectedToRoom());
        C0108b.m120a(parcel, 8, participantEntity.getPlayer(), i, false);
        C0108b.m131c(parcel, 9, participantEntity.getCapabilities());
        C0108b.m112C(parcel, k);
    }

    public ParticipantEntity[] m158T(int i) {
        return new ParticipantEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return mo748w(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m158T(x0);
    }

    public ParticipantEntity mo748w(Parcel parcel) {
        int i = 0;
        PlayerEntity playerEntity = null;
        int j = C0107a.m92j(parcel);
        boolean z = false;
        String str = null;
        int i2 = 0;
        Uri uri = null;
        Uri uri2 = null;
        String str2 = null;
        String str3 = null;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i4)) {
                case 1:
                    str3 = C0107a.m94l(parcel, i4);
                    break;
                case 2:
                    str2 = C0107a.m94l(parcel, i4);
                    break;
                case 3:
                    uri2 = (Uri) C0107a.m77a(parcel, i4, Uri.CREATOR);
                    break;
                case 4:
                    uri = (Uri) C0107a.m77a(parcel, i4, Uri.CREATOR);
                    break;
                case 5:
                    i2 = C0107a.m86f(parcel, i4);
                    break;
                case 6:
                    str = C0107a.m94l(parcel, i4);
                    break;
                case 7:
                    z = C0107a.m83c(parcel, i4);
                    break;
                case 8:
                    playerEntity = (PlayerEntity) C0107a.m77a(parcel, i4, PlayerEntity.CREATOR);
                    break;
                case 9:
                    i = C0107a.m86f(parcel, i4);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i3 = C0107a.m86f(parcel, i4);
                    break;
                default:
                    C0107a.m80b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new ParticipantEntity(i3, str3, str2, uri2, uri, i2, str, z, playerEntity, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }
}
