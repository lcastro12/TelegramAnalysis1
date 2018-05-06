package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.de.C0176b;
import com.google.android.gms.internal.de.C0911d;
import com.google.android.gms.internal.ew.C0739a;
import com.google.android.gms.internal.ex.C0741a;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.List;

public class ez extends de<ex> {
    private final fc<ex> oO = new C0743c();
    private final ey oU;
    private final String oV;

    private final class C0742a extends C0176b<OnAddGeofencesResultListener> {
        private final int iC;
        private final String[] oW;
        final /* synthetic */ ez oX;

        public C0742a(ez ezVar, OnAddGeofencesResultListener onAddGeofencesResultListener, int i, String[] strArr) {
            this.oX = ezVar;
            super(ezVar, onAddGeofencesResultListener);
            this.iC = LocationStatusCodes.m706Z(i);
            this.oW = strArr;
        }

        protected void m1181a(OnAddGeofencesResultListener onAddGeofencesResultListener) {
            if (onAddGeofencesResultListener != null) {
                onAddGeofencesResultListener.onAddGeofencesResult(this.iC, this.oW);
            }
        }

        protected void aF() {
        }
    }

    private final class C0743c implements fc<ex> {
        final /* synthetic */ ez oX;

        private C0743c(ez ezVar) {
            this.oX = ezVar;
        }

        public void bc() {
            this.oX.bc();
        }

        public /* synthetic */ IInterface bd() {
            return cn();
        }

        public ex cn() {
            return (ex) this.oX.bd();
        }
    }

    private final class C0744d extends C0176b<OnRemoveGeofencesResultListener> {
        private final int iC;
        private final PendingIntent mPendingIntent;
        private final String[] oW;
        final /* synthetic */ ez oX;
        private final int pb;

        public C0744d(ez ezVar, int i, OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, int i2, PendingIntent pendingIntent) {
            boolean z = true;
            this.oX = ezVar;
            super(ezVar, onRemoveGeofencesResultListener);
            if (i != 1) {
                z = false;
            }
            db.m343k(z);
            this.pb = i;
            this.iC = LocationStatusCodes.m706Z(i2);
            this.mPendingIntent = pendingIntent;
            this.oW = null;
        }

        public C0744d(ez ezVar, int i, OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, int i2, String[] strArr) {
            this.oX = ezVar;
            super(ezVar, onRemoveGeofencesResultListener);
            db.m343k(i == 2);
            this.pb = i;
            this.iC = LocationStatusCodes.m706Z(i2);
            this.oW = strArr;
            this.mPendingIntent = null;
        }

        protected void m1183a(OnRemoveGeofencesResultListener onRemoveGeofencesResultListener) {
            if (onRemoveGeofencesResultListener != null) {
                switch (this.pb) {
                    case 1:
                        onRemoveGeofencesResultListener.onRemoveGeofencesByPendingIntentResult(this.iC, this.mPendingIntent);
                        return;
                    case 2:
                        onRemoveGeofencesResultListener.onRemoveGeofencesByRequestIdsResult(this.iC, this.oW);
                        return;
                    default:
                        Log.wtf("LocationClientImpl", "Unsupported action: " + this.pb);
                        return;
                }
            }
        }

        protected void aF() {
        }
    }

    private static final class C0919b extends C0739a {
        private OnAddGeofencesResultListener oY;
        private OnRemoveGeofencesResultListener oZ;
        private ez pa;

        public C0919b(OnAddGeofencesResultListener onAddGeofencesResultListener, ez ezVar) {
            this.oY = onAddGeofencesResultListener;
            this.oZ = null;
            this.pa = ezVar;
        }

        public C0919b(OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, ez ezVar) {
            this.oZ = onRemoveGeofencesResultListener;
            this.oY = null;
            this.pa = ezVar;
        }

        public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) throws RemoteException {
            if (this.pa == null) {
                Log.wtf("LocationClientImpl", "onAddGeofenceResult called multiple times");
                return;
            }
            ez ezVar = this.pa;
            ez ezVar2 = this.pa;
            ezVar2.getClass();
            ezVar.m957a(new C0742a(ezVar2, this.oY, statusCode, geofenceRequestIds));
            this.pa = null;
            this.oY = null;
            this.oZ = null;
        }

        public void onRemoveGeofencesByPendingIntentResult(int statusCode, PendingIntent pendingIntent) {
            if (this.pa == null) {
                Log.wtf("LocationClientImpl", "onRemoveGeofencesByPendingIntentResult called multiple times");
                return;
            }
            ez ezVar = this.pa;
            ez ezVar2 = this.pa;
            ezVar2.getClass();
            ezVar.m957a(new C0744d(ezVar2, 1, this.oZ, statusCode, pendingIntent));
            this.pa = null;
            this.oY = null;
            this.oZ = null;
        }

        public void onRemoveGeofencesByRequestIdsResult(int statusCode, String[] geofenceRequestIds) {
            if (this.pa == null) {
                Log.wtf("LocationClientImpl", "onRemoveGeofencesByRequestIdsResult called multiple times");
                return;
            }
            ez ezVar = this.pa;
            ez ezVar2 = this.pa;
            ezVar2.getClass();
            ezVar.m957a(new C0744d(ezVar2, 2, this.oZ, statusCode, geofenceRequestIds));
            this.pa = null;
            this.oY = null;
            this.oZ = null;
        }
    }

    public ez(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str) {
        super(context, connectionCallbacks, onConnectionFailedListener, new String[0]);
        this.oU = new ey(context, this.oO);
        this.oV = str;
    }

    protected ex m1442G(IBinder iBinder) {
        return C0741a.m1180F(iBinder);
    }

    protected void mo1533a(dj djVar, C0911d c0911d) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putString("client_name", this.oV);
        djVar.mo842e(c0911d, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), bundle);
    }

    public void addGeofences(List<fa> geofences, PendingIntent pendingIntent, OnAddGeofencesResultListener listener) {
        bc();
        boolean z = geofences != null && geofences.size() > 0;
        dm.m391b(z, "At least one geofence must be specified.");
        dm.m388a((Object) pendingIntent, (Object) "PendingIntent must be specified.");
        dm.m388a((Object) listener, (Object) "OnAddGeofencesResultListener not provided.");
        if (listener == null) {
            ew ewVar = null;
        } else {
            Object c0919b = new C0919b(listener, this);
        }
        try {
            ((ex) bd()).mo1005a(geofences, pendingIntent, ewVar, getContext().getPackageName());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    protected String ag() {
        return "com.google.android.location.internal.GoogleLocationManagerService.START";
    }

    protected String ah() {
        return "com.google.android.gms.location.internal.IGoogleLocationManagerService";
    }

    public void disconnect() {
        synchronized (this.oU) {
            if (isConnected()) {
                this.oU.removeAllListeners();
                this.oU.cm();
            }
            super.disconnect();
        }
    }

    public Location getLastLocation() {
        return this.oU.getLastLocation();
    }

    protected /* synthetic */ IInterface mo1536p(IBinder iBinder) {
        return m1442G(iBinder);
    }

    public void removeActivityUpdates(PendingIntent callbackIntent) {
        bc();
        dm.m392e(callbackIntent);
        try {
            ((ex) bd()).removeActivityUpdates(callbackIntent);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeGeofences(PendingIntent pendingIntent, OnRemoveGeofencesResultListener listener) {
        bc();
        dm.m388a((Object) pendingIntent, (Object) "PendingIntent must be specified.");
        dm.m388a((Object) listener, (Object) "OnRemoveGeofencesResultListener not provided.");
        if (listener == null) {
            ew ewVar = null;
        } else {
            Object c0919b = new C0919b(listener, this);
        }
        try {
            ((ex) bd()).mo999a(pendingIntent, ewVar, getContext().getPackageName());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeGeofences(List<String> geofenceRequestIds, OnRemoveGeofencesResultListener listener) {
        bc();
        boolean z = geofenceRequestIds != null && geofenceRequestIds.size() > 0;
        dm.m391b(z, "geofenceRequestIds can't be null nor empty.");
        dm.m388a((Object) listener, (Object) "OnRemoveGeofencesResultListener not provided.");
        String[] strArr = (String[]) geofenceRequestIds.toArray(new String[0]);
        if (listener == null) {
            ew ewVar = null;
        } else {
            Object c0919b = new C0919b(listener, this);
        }
        try {
            ((ex) bd()).mo1006a(strArr, ewVar, getContext().getPackageName());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeLocationUpdates(PendingIntent callbackIntent) {
        this.oU.removeLocationUpdates(callbackIntent);
    }

    public void removeLocationUpdates(LocationListener listener) {
        this.oU.removeLocationUpdates(listener);
    }

    public void requestActivityUpdates(long detectionIntervalMillis, PendingIntent callbackIntent) {
        boolean z = true;
        bc();
        dm.m392e(callbackIntent);
        if (detectionIntervalMillis < 0) {
            z = false;
        }
        dm.m391b(z, "detectionIntervalMillis must be >= 0");
        try {
            ((ex) bd()).mo997a(detectionIntervalMillis, true, callbackIntent);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void requestLocationUpdates(LocationRequest request, PendingIntent callbackIntent) {
        this.oU.requestLocationUpdates(request, callbackIntent);
    }

    public void requestLocationUpdates(LocationRequest request, LocationListener listener) {
        requestLocationUpdates(request, listener, null);
    }

    public void requestLocationUpdates(LocationRequest request, LocationListener listener, Looper looper) {
        synchronized (this.oU) {
            this.oU.requestLocationUpdates(request, listener, looper);
        }
    }

    public void setMockLocation(Location mockLocation) {
        this.oU.setMockLocation(mockLocation);
    }

    public void setMockMode(boolean isMockMode) {
        this.oU.setMockMode(isMockMode);
    }
}
