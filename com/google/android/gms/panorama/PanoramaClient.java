package com.google.android.gms.panorama;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.internal.ff;

public class PanoramaClient implements GooglePlayServicesClient {
    private final ff qU;

    public interface OnPanoramaInfoLoadedListener {
        void onPanoramaInfoLoaded(ConnectionResult connectionResult, Intent intent);
    }

    public interface C0246a {
        void m769a(ConnectionResult connectionResult, int i, Intent intent);
    }

    public PanoramaClient(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener connectionFailedListener) {
        this.qU = new ff(context, connectionCallbacks, connectionFailedListener);
    }

    public void connect() {
        this.qU.connect();
    }

    public void disconnect() {
        this.qU.disconnect();
    }

    public boolean isConnected() {
        return this.qU.isConnected();
    }

    public boolean isConnecting() {
        return this.qU.isConnecting();
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
        return this.qU.isConnectionCallbacksRegistered(listener);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener listener) {
        return this.qU.isConnectionFailedListenerRegistered(listener);
    }

    public void loadPanoramaInfo(OnPanoramaInfoLoadedListener listener, Uri uri) {
        this.qU.m1460a(listener, uri, false);
    }

    public void loadPanoramaInfoAndGrantAccess(OnPanoramaInfoLoadedListener listener, Uri uri) {
        this.qU.m1460a(listener, uri, true);
    }

    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        this.qU.registerConnectionCallbacks(listener);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener listener) {
        this.qU.registerConnectionFailedListener(listener);
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
        this.qU.unregisterConnectionCallbacks(listener);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener listener) {
        this.qU.unregisterConnectionFailedListener(listener);
    }
}
