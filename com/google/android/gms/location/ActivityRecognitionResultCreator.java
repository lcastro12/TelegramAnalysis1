package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import java.util.List;

public class ActivityRecognitionResultCreator implements Creator<ActivityRecognitionResult> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m703a(ActivityRecognitionResult activityRecognitionResult, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m130b(parcel, 1, activityRecognitionResult.ov, false);
        C0108b.m131c(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, activityRecognitionResult.getVersionCode());
        C0108b.m116a(parcel, 2, activityRecognitionResult.ow);
        C0108b.m116a(parcel, 3, activityRecognitionResult.ox);
        C0108b.m112C(parcel, k);
    }

    public ActivityRecognitionResult createFromParcel(Parcel parcel) {
        long j = 0;
        int j2 = C0107a.m92j(parcel);
        int i = 0;
        List list = null;
        long j3 = 0;
        while (parcel.dataPosition() < j2) {
            int i2 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i2)) {
                case 1:
                    list = C0107a.m82c(parcel, i2, DetectedActivity.CREATOR);
                    break;
                case 2:
                    j3 = C0107a.m87g(parcel, i2);
                    break;
                case 3:
                    j = C0107a.m87g(parcel, i2);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i = C0107a.m86f(parcel, i2);
                    break;
                default:
                    C0107a.m80b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() == j2) {
            return new ActivityRecognitionResult(i, list, j3, j);
        }
        throw new C0106a("Overread allowed size end=" + j2, parcel);
    }

    public ActivityRecognitionResult[] newArray(int size) {
        return new ActivityRecognitionResult[size];
    }
}
