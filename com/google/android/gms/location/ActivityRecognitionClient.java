package com.google.android.gms.location;

import android.app.PendingIntent;
import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.internal.ez;

public class ActivityRecognitionClient implements GooglePlayServicesClient {
    private final ez ou;

    public ActivityRecognitionClient(Context context, ConnectionCallbacks connectedListener, OnConnectionFailedListener connectionFailedListener) {
        this.ou = new ez(context, connectedListener, connectionFailedListener, "activity_recognition");
    }

    public void connect() {
        this.ou.connect();
    }

    public void disconnect() {
        this.ou.disconnect();
    }

    public boolean isConnected() {
        return this.ou.isConnected();
    }

    public boolean isConnecting() {
        return this.ou.isConnecting();
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
        return this.ou.isConnectionCallbacksRegistered(listener);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener listener) {
        return this.ou.isConnectionFailedListenerRegistered(listener);
    }

    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        this.ou.registerConnectionCallbacks(listener);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener listener) {
        this.ou.registerConnectionFailedListener(listener);
    }

    public void removeActivityUpdates(PendingIntent callbackIntent) {
        this.ou.removeActivityUpdates(callbackIntent);
    }

    public void requestActivityUpdates(long detectionIntervalMillis, PendingIntent callbackIntent) {
        this.ou.requestActivityUpdates(detectionIntervalMillis, callbackIntent);
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
        this.ou.unregisterConnectionCallbacks(listener);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener listener) {
        this.ou.unregisterConnectionFailedListener(listener);
    }
}
