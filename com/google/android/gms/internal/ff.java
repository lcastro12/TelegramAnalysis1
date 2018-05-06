package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.de.C0176b;
import com.google.android.gms.internal.de.C0911d;
import com.google.android.gms.internal.fd.C0746a;
import com.google.android.gms.internal.fe.C0748a;
import com.google.android.gms.panorama.PanoramaClient.C0246a;
import com.google.android.gms.panorama.PanoramaClient.OnPanoramaInfoLoadedListener;

public class ff extends de<fe> {

    final class C0749a extends C0176b<C0246a> {
        public final ConnectionResult qV;
        public final Intent qW;
        final /* synthetic */ ff qX;
        public final int type;

        public C0749a(ff ffVar, C0246a c0246a, ConnectionResult connectionResult, int i, Intent intent) {
            this.qX = ffVar;
            super(ffVar, c0246a);
            this.qV = connectionResult;
            this.type = i;
            this.qW = intent;
        }

        protected void m1191a(C0246a c0246a) {
            if (c0246a != null) {
                c0246a.m769a(this.qV, this.type, this.qW);
            }
        }

        protected void aF() {
        }
    }

    final class C0750c extends C0176b<OnPanoramaInfoLoadedListener> {
        private final ConnectionResult qV;
        private final Intent qW;
        final /* synthetic */ ff qX;

        public C0750c(ff ffVar, OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener, ConnectionResult connectionResult, Intent intent) {
            this.qX = ffVar;
            super(ffVar, onPanoramaInfoLoadedListener);
            this.qV = connectionResult;
            this.qW = intent;
        }

        protected void m1193a(OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener) {
            if (onPanoramaInfoLoadedListener != null) {
                onPanoramaInfoLoadedListener.onPanoramaInfoLoaded(this.qV, this.qW);
            }
        }

        protected void aF() {
        }
    }

    final class C0921b extends C0746a {
        final /* synthetic */ ff qX;
        private final C0246a qY = null;
        private final OnPanoramaInfoLoadedListener qZ;
        private final Uri ra;

        public C0921b(ff ffVar, OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener, Uri uri) {
            this.qX = ffVar;
            this.qZ = onPanoramaInfoLoadedListener;
            this.ra = uri;
        }

        public void mo1014a(int i, Bundle bundle, int i2, Intent intent) {
            if (this.ra != null) {
                this.qX.getContext().revokeUriPermission(this.ra, 1);
            }
            PendingIntent pendingIntent = null;
            if (bundle != null) {
                pendingIntent = (PendingIntent) bundle.getParcelable("pendingIntent");
            }
            ConnectionResult connectionResult = new ConnectionResult(i, pendingIntent);
            if (this.qY != null) {
                this.qX.m957a(new C0749a(this.qX, this.qY, connectionResult, i2, intent));
            } else {
                this.qX.m957a(new C0750c(this.qX, this.qZ, connectionResult, intent));
            }
        }
    }

    public ff(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, connectionCallbacks, onConnectionFailedListener, (String[]) null);
    }

    protected void mo1533a(dj djVar, C0911d c0911d) throws RemoteException {
        djVar.mo835a(c0911d, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), new Bundle());
    }

    public void m1459a(C0921b c0921b, Uri uri, Bundle bundle, boolean z) {
        bc();
        if (z) {
            getContext().grantUriPermission(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, uri, 1);
        }
        try {
            ((fe) bd()).mo1015a(c0921b, uri, bundle, z);
        } catch (RemoteException e) {
            c0921b.mo1014a(8, null, 0, null);
        }
    }

    public void m1460a(OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener, Uri uri, boolean z) {
        m1459a(new C0921b(this, onPanoramaInfoLoadedListener, z ? uri : null), uri, null, z);
    }

    protected String ag() {
        return "com.google.android.gms.panorama.service.START";
    }

    protected String ah() {
        return "com.google.android.gms.panorama.internal.IPanoramaService";
    }

    public fe am(IBinder iBinder) {
        return C0748a.al(iBinder);
    }

    public /* synthetic */ IInterface mo1536p(IBinder iBinder) {
        return am(iBinder);
    }
}
