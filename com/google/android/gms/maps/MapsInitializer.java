package com.google.android.gms.maps;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.C0212c;
import com.google.android.gms.maps.internal.C0226q;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public final class MapsInitializer {
    private MapsInitializer() {
    }

    public static void initialize(Context context) throws GooglePlayServicesNotAvailableException {
        dm.m392e(context);
        C0212c u = C0226q.m729u(context);
        try {
            CameraUpdateFactory.m707a(u.cG());
            BitmapDescriptorFactory.m732a(u.cH());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
