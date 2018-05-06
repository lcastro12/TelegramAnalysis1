package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.util.TimeUtils;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.telegram.messenger.MessagesController;

public class fr implements Creator<fq> {
    static void m617a(fq fqVar, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        Set di = fqVar.di();
        if (di.contains(Integer.valueOf(1))) {
            C0108b.m131c(parcel, 1, fqVar.getVersionCode());
        }
        if (di.contains(Integer.valueOf(2))) {
            C0108b.m120a(parcel, 2, fqVar.dj(), i, true);
        }
        if (di.contains(Integer.valueOf(3))) {
            C0108b.m122a(parcel, 3, fqVar.getAdditionalName(), true);
        }
        if (di.contains(Integer.valueOf(4))) {
            C0108b.m120a(parcel, 4, fqVar.dk(), i, true);
        }
        if (di.contains(Integer.valueOf(5))) {
            C0108b.m121a(parcel, 5, fqVar.getAddressCountry(), true);
        }
        if (di.contains(Integer.valueOf(6))) {
            C0108b.m121a(parcel, 6, fqVar.getAddressLocality(), true);
        }
        if (di.contains(Integer.valueOf(7))) {
            C0108b.m121a(parcel, 7, fqVar.getAddressRegion(), true);
        }
        if (di.contains(Integer.valueOf(8))) {
            C0108b.m130b(parcel, 8, fqVar.dl(), true);
        }
        if (di.contains(Integer.valueOf(9))) {
            C0108b.m131c(parcel, 9, fqVar.getAttendeeCount());
        }
        if (di.contains(Integer.valueOf(10))) {
            C0108b.m130b(parcel, 10, fqVar.dm(), true);
        }
        if (di.contains(Integer.valueOf(11))) {
            C0108b.m120a(parcel, 11, fqVar.dn(), i, true);
        }
        if (di.contains(Integer.valueOf(12))) {
            C0108b.m130b(parcel, 12, fqVar.m1484do(), true);
        }
        if (di.contains(Integer.valueOf(13))) {
            C0108b.m121a(parcel, 13, fqVar.getBestRating(), true);
        }
        if (di.contains(Integer.valueOf(14))) {
            C0108b.m121a(parcel, 14, fqVar.getBirthDate(), true);
        }
        if (di.contains(Integer.valueOf(15))) {
            C0108b.m120a(parcel, 15, fqVar.dp(), i, true);
        }
        if (di.contains(Integer.valueOf(17))) {
            C0108b.m121a(parcel, 17, fqVar.getContentSize(), true);
        }
        if (di.contains(Integer.valueOf(16))) {
            C0108b.m121a(parcel, 16, fqVar.getCaption(), true);
        }
        if (di.contains(Integer.valueOf(19))) {
            C0108b.m130b(parcel, 19, fqVar.dq(), true);
        }
        if (di.contains(Integer.valueOf(18))) {
            C0108b.m121a(parcel, 18, fqVar.getContentUrl(), true);
        }
        if (di.contains(Integer.valueOf(21))) {
            C0108b.m121a(parcel, 21, fqVar.getDateModified(), true);
        }
        if (di.contains(Integer.valueOf(20))) {
            C0108b.m121a(parcel, 20, fqVar.getDateCreated(), true);
        }
        if (di.contains(Integer.valueOf(23))) {
            C0108b.m121a(parcel, 23, fqVar.getDescription(), true);
        }
        if (di.contains(Integer.valueOf(22))) {
            C0108b.m121a(parcel, 22, fqVar.getDatePublished(), true);
        }
        if (di.contains(Integer.valueOf(25))) {
            C0108b.m121a(parcel, 25, fqVar.getEmbedUrl(), true);
        }
        if (di.contains(Integer.valueOf(24))) {
            C0108b.m121a(parcel, 24, fqVar.getDuration(), true);
        }
        if (di.contains(Integer.valueOf(27))) {
            C0108b.m121a(parcel, 27, fqVar.getFamilyName(), true);
        }
        if (di.contains(Integer.valueOf(26))) {
            C0108b.m121a(parcel, 26, fqVar.getEndDate(), true);
        }
        if (di.contains(Integer.valueOf(29))) {
            C0108b.m120a(parcel, 29, fqVar.dr(), i, true);
        }
        if (di.contains(Integer.valueOf(28))) {
            C0108b.m121a(parcel, 28, fqVar.getGender(), true);
        }
        if (di.contains(Integer.valueOf(31))) {
            C0108b.m121a(parcel, 31, fqVar.getHeight(), true);
        }
        if (di.contains(Integer.valueOf(30))) {
            C0108b.m121a(parcel, 30, fqVar.getGivenName(), true);
        }
        if (di.contains(Integer.valueOf(34))) {
            C0108b.m120a(parcel, 34, fqVar.ds(), i, true);
        }
        if (di.contains(Integer.valueOf(32))) {
            C0108b.m121a(parcel, 32, fqVar.getId(), true);
        }
        if (di.contains(Integer.valueOf(33))) {
            C0108b.m121a(parcel, 33, fqVar.getImage(), true);
        }
        if (di.contains(Integer.valueOf(38))) {
            C0108b.m114a(parcel, 38, fqVar.getLongitude());
        }
        if (di.contains(Integer.valueOf(39))) {
            C0108b.m121a(parcel, 39, fqVar.getName(), true);
        }
        if (di.contains(Integer.valueOf(36))) {
            C0108b.m114a(parcel, 36, fqVar.getLatitude());
        }
        if (di.contains(Integer.valueOf(37))) {
            C0108b.m120a(parcel, 37, fqVar.dt(), i, true);
        }
        if (di.contains(Integer.valueOf(42))) {
            C0108b.m121a(parcel, 42, fqVar.getPlayerType(), true);
        }
        if (di.contains(Integer.valueOf(43))) {
            C0108b.m121a(parcel, 43, fqVar.getPostOfficeBoxNumber(), true);
        }
        if (di.contains(Integer.valueOf(40))) {
            C0108b.m120a(parcel, 40, fqVar.du(), i, true);
        }
        if (di.contains(Integer.valueOf(41))) {
            C0108b.m130b(parcel, 41, fqVar.dv(), true);
        }
        if (di.contains(Integer.valueOf(46))) {
            C0108b.m120a(parcel, 46, fqVar.dw(), i, true);
        }
        if (di.contains(Integer.valueOf(47))) {
            C0108b.m121a(parcel, 47, fqVar.getStartDate(), true);
        }
        if (di.contains(Integer.valueOf(44))) {
            C0108b.m121a(parcel, 44, fqVar.getPostalCode(), true);
        }
        if (di.contains(Integer.valueOf(45))) {
            C0108b.m121a(parcel, 45, fqVar.getRatingValue(), true);
        }
        if (di.contains(Integer.valueOf(51))) {
            C0108b.m121a(parcel, 51, fqVar.getThumbnailUrl(), true);
        }
        if (di.contains(Integer.valueOf(50))) {
            C0108b.m120a(parcel, 50, fqVar.dx(), i, true);
        }
        if (di.contains(Integer.valueOf(49))) {
            C0108b.m121a(parcel, 49, fqVar.getText(), true);
        }
        if (di.contains(Integer.valueOf(48))) {
            C0108b.m121a(parcel, 48, fqVar.getStreetAddress(), true);
        }
        if (di.contains(Integer.valueOf(55))) {
            C0108b.m121a(parcel, 55, fqVar.getWidth(), true);
        }
        if (di.contains(Integer.valueOf(54))) {
            C0108b.m121a(parcel, 54, fqVar.getUrl(), true);
        }
        if (di.contains(Integer.valueOf(53))) {
            C0108b.m121a(parcel, 53, fqVar.getType(), true);
        }
        if (di.contains(Integer.valueOf(52))) {
            C0108b.m121a(parcel, 52, fqVar.getTickerSymbol(), true);
        }
        if (di.contains(Integer.valueOf(56))) {
            C0108b.m121a(parcel, 56, fqVar.getWorstRating(), true);
        }
        C0108b.m112C(parcel, k);
    }

    public fq m618B(Parcel parcel) {
        int j = C0107a.m92j(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        fq fqVar = null;
        List list = null;
        fq fqVar2 = null;
        String str = null;
        String str2 = null;
        String str3 = null;
        List list2 = null;
        int i2 = 0;
        List list3 = null;
        fq fqVar3 = null;
        List list4 = null;
        String str4 = null;
        String str5 = null;
        fq fqVar4 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        List list5 = null;
        String str9 = null;
        String str10 = null;
        String str11 = null;
        String str12 = null;
        String str13 = null;
        String str14 = null;
        String str15 = null;
        String str16 = null;
        String str17 = null;
        fq fqVar5 = null;
        String str18 = null;
        String str19 = null;
        String str20 = null;
        String str21 = null;
        fq fqVar6 = null;
        double d = 0.0d;
        fq fqVar7 = null;
        double d2 = 0.0d;
        String str22 = null;
        fq fqVar8 = null;
        List list6 = null;
        String str23 = null;
        String str24 = null;
        String str25 = null;
        String str26 = null;
        fq fqVar9 = null;
        String str27 = null;
        String str28 = null;
        String str29 = null;
        fq fqVar10 = null;
        String str30 = null;
        String str31 = null;
        String str32 = null;
        String str33 = null;
        String str34 = null;
        String str35 = null;
        while (parcel.dataPosition() < j) {
            int i3 = C0107a.m90i(parcel);
            fq fqVar11;
            switch (C0107a.m107y(i3)) {
                case 1:
                    i = C0107a.m86f(parcel, i3);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    fqVar11 = (fq) C0107a.m77a(parcel, i3, fq.CREATOR);
                    hashSet.add(Integer.valueOf(2));
                    fqVar = fqVar11;
                    break;
                case 3:
                    list = C0107a.m106x(parcel, i3);
                    hashSet.add(Integer.valueOf(3));
                    break;
                case 4:
                    fqVar11 = (fq) C0107a.m77a(parcel, i3, fq.CREATOR);
                    hashSet.add(Integer.valueOf(4));
                    fqVar2 = fqVar11;
                    break;
                case 5:
                    str = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(5));
                    break;
                case 6:
                    str2 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(6));
                    break;
                case 7:
                    str3 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(7));
                    break;
                case 8:
                    list2 = C0107a.m82c(parcel, i3, fq.CREATOR);
                    hashSet.add(Integer.valueOf(8));
                    break;
                case 9:
                    i2 = C0107a.m86f(parcel, i3);
                    hashSet.add(Integer.valueOf(9));
                    break;
                case 10:
                    list3 = C0107a.m82c(parcel, i3, fq.CREATOR);
                    hashSet.add(Integer.valueOf(10));
                    break;
                case 11:
                    fqVar11 = (fq) C0107a.m77a(parcel, i3, fq.CREATOR);
                    hashSet.add(Integer.valueOf(11));
                    fqVar3 = fqVar11;
                    break;
                case 12:
                    list4 = C0107a.m82c(parcel, i3, fq.CREATOR);
                    hashSet.add(Integer.valueOf(12));
                    break;
                case 13:
                    str4 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(13));
                    break;
                case 14:
                    str5 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(14));
                    break;
                case 15:
                    fqVar11 = (fq) C0107a.m77a(parcel, i3, (Creator) fq.CREATOR);
                    hashSet.add(Integer.valueOf(15));
                    fqVar4 = fqVar11;
                    break;
                case 16:
                    str6 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(16));
                    break;
                case 17:
                    str7 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(17));
                    break;
                case 18:
                    str8 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(18));
                    break;
                case TimeUtils.HUNDRED_DAY_FIELD_LEN /*19*/:
                    list5 = C0107a.m82c(parcel, i3, fq.CREATOR);
                    hashSet.add(Integer.valueOf(19));
                    break;
                case MessagesController.mediaCountDidLoaded /*20*/:
                    str9 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(20));
                    break;
                case MessagesController.encryptedChatUpdated /*21*/:
                    str10 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(21));
                    break;
                case MessagesController.messagesReadedEncrypted /*22*/:
                    str11 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(22));
                    break;
                case MessagesController.encryptedChatCreated /*23*/:
                    str12 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(23));
                    break;
                case MessagesController.userPhotosLoaded /*24*/:
                    str13 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(24));
                    break;
                case 25:
                    str14 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(25));
                    break;
                case 26:
                    str15 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(26));
                    break;
                case 27:
                    str16 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(27));
                    break;
                case 28:
                    str17 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(28));
                    break;
                case 29:
                    fqVar11 = (fq) C0107a.m77a(parcel, i3, (Creator) fq.CREATOR);
                    hashSet.add(Integer.valueOf(29));
                    fqVar5 = fqVar11;
                    break;
                case 30:
                    str18 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(30));
                    break;
                case 31:
                    str19 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(31));
                    break;
                case 32:
                    str20 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(32));
                    break;
                case 33:
                    str21 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(33));
                    break;
                case 34:
                    fqVar11 = (fq) C0107a.m77a(parcel, i3, (Creator) fq.CREATOR);
                    hashSet.add(Integer.valueOf(34));
                    fqVar6 = fqVar11;
                    break;
                case 36:
                    d = C0107a.m91j(parcel, i3);
                    hashSet.add(Integer.valueOf(36));
                    break;
                case 37:
                    fqVar11 = (fq) C0107a.m77a(parcel, i3, (Creator) fq.CREATOR);
                    hashSet.add(Integer.valueOf(37));
                    fqVar7 = fqVar11;
                    break;
                case 38:
                    d2 = C0107a.m91j(parcel, i3);
                    hashSet.add(Integer.valueOf(38));
                    break;
                case 39:
                    str22 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(39));
                    break;
                case 40:
                    fqVar11 = (fq) C0107a.m77a(parcel, i3, (Creator) fq.CREATOR);
                    hashSet.add(Integer.valueOf(40));
                    fqVar8 = fqVar11;
                    break;
                case 41:
                    list6 = C0107a.m82c(parcel, i3, fq.CREATOR);
                    hashSet.add(Integer.valueOf(41));
                    break;
                case 42:
                    str23 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(42));
                    break;
                case 43:
                    str24 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(43));
                    break;
                case 44:
                    str25 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(44));
                    break;
                case 45:
                    str26 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(45));
                    break;
                case 46:
                    fqVar11 = (fq) C0107a.m77a(parcel, i3, (Creator) fq.CREATOR);
                    hashSet.add(Integer.valueOf(46));
                    fqVar9 = fqVar11;
                    break;
                case 47:
                    str27 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(47));
                    break;
                case 48:
                    str28 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(48));
                    break;
                case 49:
                    str29 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(49));
                    break;
                case 50:
                    fqVar11 = (fq) C0107a.m77a(parcel, i3, (Creator) fq.CREATOR);
                    hashSet.add(Integer.valueOf(50));
                    fqVar10 = fqVar11;
                    break;
                case 51:
                    str30 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(51));
                    break;
                case 52:
                    str31 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(52));
                    break;
                case 53:
                    str32 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(53));
                    break;
                case 54:
                    str33 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(54));
                    break;
                case 55:
                    str34 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(55));
                    break;
                case 56:
                    str35 = C0107a.m94l(parcel, i3);
                    hashSet.add(Integer.valueOf(56));
                    break;
                default:
                    C0107a.m80b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new fq(hashSet, i, fqVar, list, fqVar2, str, str2, str3, list2, i2, list3, fqVar3, list4, str4, str5, fqVar4, str6, str7, str8, list5, str9, str10, str11, str12, str13, str14, str15, str16, str17, fqVar5, str18, str19, str20, str21, fqVar6, d, fqVar7, d2, str22, fqVar8, list6, str23, str24, str25, str26, fqVar9, str27, str28, str29, fqVar10, str30, str31, str32, str33, str34, str35);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public fq[] ag(int i) {
        return new fq[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel x0) {
        return m618B(x0);
    }

    public /* synthetic */ Object[] newArray(int x0) {
        return ag(x0);
    }
}
