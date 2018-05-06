package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.maps.model.CameraPosition;

public class GoogleMapOptionsCreator implements Creator<GoogleMapOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m708a(GoogleMapOptions googleMapOptions, Parcel parcel, int i) {
        int k = C0108b.m133k(parcel);
        C0108b.m131c(parcel, 1, googleMapOptions.getVersionCode());
        C0108b.m113a(parcel, 2, googleMapOptions.cv());
        C0108b.m113a(parcel, 3, googleMapOptions.cw());
        C0108b.m131c(parcel, 4, googleMapOptions.getMapType());
        C0108b.m120a(parcel, 5, googleMapOptions.getCamera(), i, false);
        C0108b.m113a(parcel, 6, googleMapOptions.cx());
        C0108b.m113a(parcel, 7, googleMapOptions.cy());
        C0108b.m113a(parcel, 8, googleMapOptions.cz());
        C0108b.m113a(parcel, 9, googleMapOptions.cA());
        C0108b.m113a(parcel, 10, googleMapOptions.cB());
        C0108b.m113a(parcel, 11, googleMapOptions.cC());
        C0108b.m112C(parcel, k);
    }

    public GoogleMapOptions createFromParcel(Parcel parcel) {
        byte b = (byte) 0;
        int j = C0107a.m92j(parcel);
        CameraPosition cameraPosition = null;
        byte b2 = (byte) 0;
        byte b3 = (byte) 0;
        byte b4 = (byte) 0;
        byte b5 = (byte) 0;
        byte b6 = (byte) 0;
        int i = 0;
        byte b7 = (byte) 0;
        byte b8 = (byte) 0;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = C0107a.m90i(parcel);
            switch (C0107a.m107y(i3)) {
                case 1:
                    i2 = C0107a.m86f(parcel, i3);
                    break;
                case 2:
                    b8 = C0107a.m84d(parcel, i3);
                    break;
                case 3:
                    b7 = C0107a.m84d(parcel, i3);
                    break;
                case 4:
                    i = C0107a.m86f(parcel, i3);
                    break;
                case 5:
                    cameraPosition = (CameraPosition) C0107a.m77a(parcel, i3, CameraPosition.CREATOR);
                    break;
                case 6:
                    b6 = C0107a.m84d(parcel, i3);
                    break;
                case 7:
                    b5 = C0107a.m84d(parcel, i3);
                    break;
                case 8:
                    b4 = C0107a.m84d(parcel, i3);
                    break;
                case 9:
                    b3 = C0107a.m84d(parcel, i3);
                    break;
                case 10:
                    b2 = C0107a.m84d(parcel, i3);
                    break;
                case 11:
                    b = C0107a.m84d(parcel, i3);
                    break;
                default:
                    C0107a.m80b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() == j) {
            return new GoogleMapOptions(i2, b8, b7, i, cameraPosition, b6, b5, b4, b3, b2, b);
        }
        throw new C0106a("Overread allowed size end=" + j, parcel);
    }

    public GoogleMapOptions[] newArray(int size) {
        return new GoogleMapOptions[size];
    }
}
