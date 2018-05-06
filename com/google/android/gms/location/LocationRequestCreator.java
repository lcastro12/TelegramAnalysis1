package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;

public class LocationRequestCreator implements Creator<LocationRequest> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m705a(LocationRequest locationRequest, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, locationRequest.mPriority);
        C0108b.m131c(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, locationRequest.getVersionCode());
        C0108b.m116a(parcel, 2, locationRequest.oJ);
        C0108b.m116a(parcel, 3, locationRequest.oK);
        C0108b.m124a(parcel, 4, locationRequest.oL);
        C0108b.m116a(parcel, 5, locationRequest.oC);
        C0108b.m131c(parcel, 6, locationRequest.oM);
        C0108b.m115a(parcel, 7, locationRequest.oN);
        C0108b.m112C(parcel, k);
    }

    public LocationRequest createFromParcel(Parcel parcel) {
        boolean z = false;
        int j = C0107a.m92j(parcel);
        int i = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
        long j2 = 3600000;
        long j3 = 600000;
        long j4 = Long.MAX_VALUE;
        int i2 = Integer.MAX_VALUE;
        float f = 0.0f;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i4)) {
                case 1:
                    i = C0107a.m86f(parcel, i4);
                    break;
                case 2:
                    j2 = C0107a.m87g(parcel, i4);
                    break;
                case 3:
                    j3 = C0107a.m87g(parcel, i4);
                    break;
                case 4:
                    z = C0107a.m83c(parcel, i4);
                    break;
                case 5:
                    j4 = C0107a.m87g(parcel, i4);
                    break;
                case 6:
                    i2 = C0107a.m86f(parcel, i4);
                    break;
                case 7:
                    f = C0107a.m89i(parcel, i4);
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
            return new LocationRequest(i3, i, j2, j3, z, j4, i2, f);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public LocationRequest[] newArray(int size) {
        return new LocationRequest[size];
    }
}
