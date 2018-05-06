package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.internal.by.C0704a;
import com.google.android.gms.internal.de.C0911d;

public class bt extends de<by> {
    private final int gz;

    public bt(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, int i) {
        super(context, connectionCallbacks, onConnectionFailedListener, new String[0]);
        this.gz = i;
    }

    protected void mo1533a(dj djVar, C0911d c0911d) throws RemoteException {
        djVar.mo844g(c0911d, this.gz, getContext().getPackageName(), new Bundle());
    }

    protected String ag() {
        return "com.google.android.gms.ads.service.START";
    }

    protected String ah() {
        return "com.google.android.gms.ads.internal.request.IAdRequestService";
    }

    public by ai() {
        return (by) super.bd();
    }

    protected by m1348o(IBinder iBinder) {
        return C0704a.m921q(iBinder);
    }

    protected /* synthetic */ IInterface mo1536p(IBinder iBinder) {
        return m1348o(iBinder);
    }
}
