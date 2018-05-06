package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.util.TimeUtils;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.internal.fv.C0924a;
import com.google.android.gms.internal.fv.C0927b;
import com.google.android.gms.internal.fv.C0928c;
import com.google.android.gms.internal.fv.C0929d;
import com.google.android.gms.internal.fv.C0930f;
import com.google.android.gms.internal.fv.C0931g;
import com.google.android.gms.internal.fv.C0932h;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.telegram.messenger.MessagesController;

public class fw implements Creator<fv> {
    static void m621a(fv fvVar, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        Set di = fvVar.di();
        if (di.contains(Integer.valueOf(1))) {
            C0108b.m131c(parcel, 1, fvVar.getVersionCode());
        }
        if (di.contains(Integer.valueOf(2))) {
            C0108b.m121a(parcel, 2, fvVar.getAboutMe(), true);
        }
        if (di.contains(Integer.valueOf(3))) {
            C0108b.m120a(parcel, 3, fvVar.dD(), i, true);
        }
        if (di.contains(Integer.valueOf(4))) {
            C0108b.m121a(parcel, 4, fvVar.getBirthday(), true);
        }
        if (di.contains(Integer.valueOf(5))) {
            C0108b.m121a(parcel, 5, fvVar.getBraggingRights(), true);
        }
        if (di.contains(Integer.valueOf(6))) {
            C0108b.m131c(parcel, 6, fvVar.getCircledByCount());
        }
        if (di.contains(Integer.valueOf(7))) {
            C0108b.m120a(parcel, 7, fvVar.dE(), i, true);
        }
        if (di.contains(Integer.valueOf(8))) {
            C0108b.m121a(parcel, 8, fvVar.getCurrentLocation(), true);
        }
        if (di.contains(Integer.valueOf(9))) {
            C0108b.m121a(parcel, 9, fvVar.getDisplayName(), true);
        }
        if (di.contains(Integer.valueOf(12))) {
            C0108b.m131c(parcel, 12, fvVar.getGender());
        }
        if (di.contains(Integer.valueOf(14))) {
            C0108b.m121a(parcel, 14, fvVar.getId(), true);
        }
        if (di.contains(Integer.valueOf(15))) {
            C0108b.m120a(parcel, 15, fvVar.dF(), i, true);
        }
        if (di.contains(Integer.valueOf(16))) {
            C0108b.m124a(parcel, 16, fvVar.isPlusUser());
        }
        if (di.contains(Integer.valueOf(19))) {
            C0108b.m120a(parcel, 19, fvVar.dG(), i, true);
        }
        if (di.contains(Integer.valueOf(18))) {
            C0108b.m121a(parcel, 18, fvVar.getLanguage(), true);
        }
        if (di.contains(Integer.valueOf(21))) {
            C0108b.m131c(parcel, 21, fvVar.getObjectType());
        }
        if (di.contains(Integer.valueOf(20))) {
            C0108b.m121a(parcel, 20, fvVar.getNickname(), true);
        }
        if (di.contains(Integer.valueOf(23))) {
            C0108b.m130b(parcel, 23, fvVar.dI(), true);
        }
        if (di.contains(Integer.valueOf(22))) {
            C0108b.m130b(parcel, 22, fvVar.dH(), true);
        }
        if (di.contains(Integer.valueOf(25))) {
            C0108b.m131c(parcel, 25, fvVar.getRelationshipStatus());
        }
        if (di.contains(Integer.valueOf(24))) {
            C0108b.m131c(parcel, 24, fvVar.getPlusOneCount());
        }
        if (di.contains(Integer.valueOf(27))) {
            C0108b.m121a(parcel, 27, fvVar.getUrl(), true);
        }
        if (di.contains(Integer.valueOf(26))) {
            C0108b.m121a(parcel, 26, fvVar.getTagline(), true);
        }
        if (di.contains(Integer.valueOf(29))) {
            C0108b.m124a(parcel, 29, fvVar.isVerified());
        }
        if (di.contains(Integer.valueOf(28))) {
            C0108b.m130b(parcel, 28, fvVar.dJ(), true);
        }
        C0108b.m112C(parcel, k);
    }

    public fv m622D(Parcel parcel) {
        int j = C0107a.m92j(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        String str = null;
        C0924a c0924a = null;
        String str2 = null;
        String str3 = null;
        int i2 = 0;
        C0927b c0927b = null;
        String str4 = null;
        String str5 = null;
        int i3 = 0;
        String str6 = null;
        C0928c c0928c = null;
        boolean z = false;
        String str7 = null;
        C0929d c0929d = null;
        String str8 = null;
        int i4 = 0;
        List list = null;
        List list2 = null;
        int i5 = 0;
        int i6 = 0;
        String str9 = null;
        String str10 = null;
        List list3 = null;
        boolean z2 = false;
        while (parcel.dataPosition() < j) {
            int i7 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i7)) {
                case 1:
                    i = C0107a.m86f(parcel, i7);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    str = C0107a.m94l(parcel, i7);
                    hashSet.add(Integer.valueOf(2));
                    break;
                case 3:
                    C0924a c0924a2 = (C0924a) C0107a.m77a(parcel, i7, C0924a.CREATOR);
                    hashSet.add(Integer.valueOf(3));
                    c0924a = c0924a2;
                    break;
                case 4:
                    str2 = C0107a.m94l(parcel, i7);
                    hashSet.add(Integer.valueOf(4));
                    break;
                case 5:
                    str3 = C0107a.m94l(parcel, i7);
                    hashSet.add(Integer.valueOf(5));
                    break;
                case 6:
                    i2 = C0107a.m86f(parcel, i7);
                    hashSet.add(Integer.valueOf(6));
                    break;
                case 7:
                    C0927b c0927b2 = (C0927b) C0107a.m77a(parcel, i7, C0927b.CREATOR);
                    hashSet.add(Integer.valueOf(7));
                    c0927b = c0927b2;
                    break;
                case 8:
                    str4 = C0107a.m94l(parcel, i7);
                    hashSet.add(Integer.valueOf(8));
                    break;
                case 9:
                    str5 = C0107a.m94l(parcel, i7);
                    hashSet.add(Integer.valueOf(9));
                    break;
                case 12:
                    i3 = C0107a.m86f(parcel, i7);
                    hashSet.add(Integer.valueOf(12));
                    break;
                case 14:
                    str6 = C0107a.m94l(parcel, i7);
                    hashSet.add(Integer.valueOf(14));
                    break;
                case 15:
                    C0928c c0928c2 = (C0928c) C0107a.m77a(parcel, i7, C0928c.CREATOR);
                    hashSet.add(Integer.valueOf(15));
                    c0928c = c0928c2;
                    break;
                case 16:
                    z = C0107a.m83c(parcel, i7);
                    hashSet.add(Integer.valueOf(16));
                    break;
                case 18:
                    str7 = C0107a.m94l(parcel, i7);
                    hashSet.add(Integer.valueOf(18));
                    break;
                case TimeUtils.HUNDRED_DAY_FIELD_LEN /*19*/:
                    C0929d c0929d2 = (C0929d) C0107a.m77a(parcel, i7, (Creator) C0929d.CREATOR);
                    hashSet.add(Integer.valueOf(19));
                    c0929d = c0929d2;
                    break;
                case MessagesController.mediaCountDidLoaded /*20*/:
                    str8 = C0107a.m94l(parcel, i7);
                    hashSet.add(Integer.valueOf(20));
                    break;
                case MessagesController.encryptedChatUpdated /*21*/:
                    i4 = C0107a.m86f(parcel, i7);
                    hashSet.add(Integer.valueOf(21));
                    break;
                case MessagesController.messagesReadedEncrypted /*22*/:
                    list = C0107a.m82c(parcel, i7, C0930f.CREATOR);
                    hashSet.add(Integer.valueOf(22));
                    break;
                case MessagesController.encryptedChatCreated /*23*/:
                    list2 = C0107a.m82c(parcel, i7, C0931g.CREATOR);
                    hashSet.add(Integer.valueOf(23));
                    break;
                case MessagesController.userPhotosLoaded /*24*/:
                    i5 = C0107a.m86f(parcel, i7);
                    hashSet.add(Integer.valueOf(24));
                    break;
                case 25:
                    i6 = C0107a.m86f(parcel, i7);
                    hashSet.add(Integer.valueOf(25));
                    break;
                case 26:
                    str9 = C0107a.m94l(parcel, i7);
                    hashSet.add(Integer.valueOf(26));
                    break;
                case 27:
                    str10 = C0107a.m94l(parcel, i7);
                    hashSet.add(Integer.valueOf(27));
                    break;
                case 28:
                    list3 = C0107a.m82c(parcel, i7, C0932h.CREATOR);
                    hashSet.add(Integer.valueOf(28));
                    break;
                case 29:
                    z2 = C0107a.m83c(parcel, i7);
                    hashSet.add(Integer.valueOf(29));
                    break;
                default:
                    C0107a.m80b(parcel, i7);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new fv(hashSet, i, str, c0924a, str2, str3, i2, c0927b, str4, str5, i3, str6, c0928c, z, str7, c0929d, str8, i4, list, list2, i5, i6, str9, str10, list3, z2);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public fv[] ai(int i) {
        return new fv[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m622D(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ai(x0);
    }
}
