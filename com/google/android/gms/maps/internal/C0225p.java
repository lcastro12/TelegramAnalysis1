package com.google.android.gms.maps.internal;

import android.os.Bundle;
import android.os.Parcelable;

public final class C0225p {
    private C0225p() {
    }

    public static void m726a(Bundle bundle, String str, Parcelable parcelable) {
        bundle.setClassLoader(C0225p.class.getClassLoader());
        Bundle bundle2 = bundle.getBundle("map_state");
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        bundle2.setClassLoader(C0225p.class.getClassLoader());
        bundle2.putParcelable(str, parcelable);
        bundle.putBundle("map_state", bundle2);
    }
}
