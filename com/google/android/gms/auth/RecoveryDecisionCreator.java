package com.google.android.gms.auth;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class RecoveryDecisionCreator implements Creator<RecoveryDecision> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m21a(RecoveryDecision recoveryDecision, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, recoveryDecision.iM);
        C0108b.m120a(parcel, 2, recoveryDecision.recoveryIntent, i, false);
        C0108b.m124a(parcel, 3, recoveryDecision.showRecoveryInterstitial);
        C0108b.m124a(parcel, 4, recoveryDecision.isRecoveryInfoNeeded);
        C0108b.m124a(parcel, 5, recoveryDecision.isRecoveryInterstitialAllowed);
        C0108b.m120a(parcel, 6, recoveryDecision.recoveryIntentWithoutIntro, i, false);
        C0108b.m112C(parcel, k);
    }

    public RecoveryDecision createFromParcel(Parcel parcel) {
        PendingIntent pendingIntent = null;
        boolean z = false;
        int j = C0107a.m92j(parcel);
        boolean z2 = false;
        boolean z3 = false;
        PendingIntent pendingIntent2 = null;
        int i = 0;
        while (parcel.dataPosition() < j) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    i = C0107a.m86f(parcel, i2);
                    break;
                case 2:
                    pendingIntent2 = (PendingIntent) C0107a.m77a(parcel, i2, PendingIntent.CREATOR);
                    break;
                case 3:
                    z3 = C0107a.m83c(parcel, i2);
                    break;
                case 4:
                    z2 = C0107a.m83c(parcel, i2);
                    break;
                case 5:
                    z = C0107a.m83c(parcel, i2);
                    break;
                case 6:
                    pendingIntent = (PendingIntent) C0107a.m77a(parcel, i2, PendingIntent.CREATOR);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new RecoveryDecision(i, pendingIntent2, z3, z2, z, pendingIntent);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public RecoveryDecision[] newArray(int size) {
        return new RecoveryDecision[size];
    }
}
