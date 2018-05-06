package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.ArrayList;

public class C0120a implements Creator<InvitationEntity> {
    static void m154a(InvitationEntity invitationEntity, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m120a(parcel, 1, invitationEntity.getGame(), i, false);
        C0108b.m131c(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, invitationEntity.getVersionCode());
        C0108b.m121a(parcel, 2, invitationEntity.getInvitationId(), false);
        C0108b.m116a(parcel, 3, invitationEntity.getCreationTimestamp());
        C0108b.m131c(parcel, 4, invitationEntity.ch());
        C0108b.m120a(parcel, 5, invitationEntity.getInviter(), i, false);
        C0108b.m130b(parcel, 6, invitationEntity.getParticipants(), false);
        C0108b.m131c(parcel, 7, invitationEntity.getVariant());
        C0108b.m112C(parcel, k);
    }

    public InvitationEntity[] m155S(int i) {
        return new InvitationEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return mo746v(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m155S(x0);
    }

    public InvitationEntity mo746v(Parcel parcel) {
        int i = 0;
        ArrayList arrayList = null;
        int j = C0107a.m92j(parcel);
        long j2 = 0;
        ParticipantEntity participantEntity = null;
        int i2 = 0;
        String str = null;
        GameEntity gameEntity = null;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i4)) {
                case 1:
                    gameEntity = (GameEntity) C0107a.m77a(parcel, i4, GameEntity.CREATOR);
                    break;
                case 2:
                    str = C0107a.m94l(parcel, i4);
                    break;
                case 3:
                    j2 = C0107a.m87g(parcel, i4);
                    break;
                case 4:
                    i2 = C0107a.m86f(parcel, i4);
                    break;
                case 5:
                    participantEntity = (ParticipantEntity) C0107a.m77a(parcel, i4, ParticipantEntity.CREATOR);
                    break;
                case 6:
                    arrayList = C0107a.m82c(parcel, i4, ParticipantEntity.CREATOR);
                    break;
                case 7:
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
            return new InvitationEntity(i3, gameEntity, str, j2, i2, participantEntity, arrayList, i);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }
}
