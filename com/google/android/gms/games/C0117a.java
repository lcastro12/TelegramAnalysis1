package com.google.android.gms.games;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.location.LocationStatusCodes;

public class C0117a implements Creator<GameEntity> {
    static void m146a(GameEntity gameEntity, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m121a(parcel, 1, gameEntity.getApplicationId(), false);
        C0108b.m121a(parcel, 2, gameEntity.getDisplayName(), false);
        C0108b.m121a(parcel, 3, gameEntity.getPrimaryCategory(), false);
        C0108b.m121a(parcel, 4, gameEntity.getSecondaryCategory(), false);
        C0108b.m121a(parcel, 5, gameEntity.getDescription(), false);
        C0108b.m121a(parcel, 6, gameEntity.getDeveloperName(), false);
        C0108b.m120a(parcel, 7, gameEntity.getIconImageUri(), i, false);
        C0108b.m120a(parcel, 8, gameEntity.getHiResImageUri(), i, false);
        C0108b.m120a(parcel, 9, gameEntity.getFeaturedImageUri(), i, false);
        C0108b.m124a(parcel, 10, gameEntity.isPlayEnabledGame());
        C0108b.m124a(parcel, 11, gameEntity.isInstanceInstalled());
        C0108b.m121a(parcel, 12, gameEntity.getInstancePackageName(), false);
        C0108b.m131c(parcel, 13, gameEntity.getGameplayAclStatus());
        C0108b.m131c(parcel, 14, gameEntity.getAchievementTotalCount());
        C0108b.m131c(parcel, 15, gameEntity.getLeaderboardCount());
        C0108b.m131c(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, gameEntity.getVersionCode());
        C0108b.m112C(parcel, k);
    }

    public GameEntity[] m147K(int i) {
        return new GameEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return mo710t(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return m147K(x0);
    }

    public GameEntity mo710t(Parcel parcel) {
        int j = C0107a.m92j(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        Uri uri = null;
        Uri uri2 = null;
        Uri uri3 = null;
        boolean z = false;
        boolean z2 = false;
        String str7 = null;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (parcel.dataPosition() < j) {
            int i5 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i5)) {
                case 1:
                    str = C0107a.m94l(parcel, i5);
                    break;
                case 2:
                    str2 = C0107a.m94l(parcel, i5);
                    break;
                case 3:
                    str3 = C0107a.m94l(parcel, i5);
                    break;
                case 4:
                    str4 = C0107a.m94l(parcel, i5);
                    break;
                case 5:
                    str5 = C0107a.m94l(parcel, i5);
                    break;
                case 6:
                    str6 = C0107a.m94l(parcel, i5);
                    break;
                case 7:
                    uri = (Uri) C0107a.m77a(parcel, i5, Uri.CREATOR);
                    break;
                case 8:
                    uri2 = (Uri) C0107a.m77a(parcel, i5, Uri.CREATOR);
                    break;
                case 9:
                    uri3 = (Uri) C0107a.m77a(parcel, i5, Uri.CREATOR);
                    break;
                case 10:
                    z = C0107a.m83c(parcel, i5);
                    break;
                case 11:
                    z2 = C0107a.m83c(parcel, i5);
                    break;
                case 12:
                    str7 = C0107a.m94l(parcel, i5);
                    break;
                case 13:
                    i2 = C0107a.m86f(parcel, i5);
                    break;
                case 14:
                    i3 = C0107a.m86f(parcel, i5);
                    break;
                case 15:
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
            return new GameEntity(i, str, str2, str3, str4, str5, str6, uri, uri2, uri3, z, z2, str7, i2, i3, i4);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }
}
