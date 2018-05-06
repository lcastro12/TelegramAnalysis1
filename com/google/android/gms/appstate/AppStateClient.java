package com.google.android.gms.appstate;

import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.internal.cw;
import com.google.android.gms.internal.dm;

public final class AppStateClient implements GooglePlayServicesClient {
    public static final int STATUS_CLIENT_RECONNECT_REQUIRED = 2;
    public static final int STATUS_DEVELOPER_ERROR = 7;
    public static final int STATUS_INTERNAL_ERROR = 1;
    public static final int STATUS_NETWORK_ERROR_NO_DATA = 4;
    public static final int STATUS_NETWORK_ERROR_OPERATION_DEFERRED = 5;
    public static final int STATUS_NETWORK_ERROR_OPERATION_FAILED = 6;
    public static final int STATUS_NETWORK_ERROR_STALE_DATA = 3;
    public static final int STATUS_OK = 0;
    public static final int STATUS_STATE_KEY_LIMIT_EXCEEDED = 2003;
    public static final int STATUS_STATE_KEY_NOT_FOUND = 2002;
    public static final int STATUS_WRITE_OUT_OF_DATE_VERSION = 2000;
    public static final int STATUS_WRITE_SIZE_EXCEEDED = 2001;
    private final cw io;

    public static final class Builder {
        private static final String[] ip = new String[]{Scopes.APP_STATE};
        private ConnectionCallbacks iq;
        private OnConnectionFailedListener ir;
        private String[] is = ip;
        private String it = "<<default account>>";
        private Context mContext;

        public Builder(Context context, ConnectionCallbacks connectedListener, OnConnectionFailedListener connectionFailedListener) {
            this.mContext = context;
            this.iq = connectedListener;
            this.ir = connectionFailedListener;
        }

        public AppStateClient create() {
            return new AppStateClient(this.mContext, this.iq, this.ir, this.it, this.is);
        }

        public Builder setAccountName(String accountName) {
            this.it = (String) dm.m392e(accountName);
            return this;
        }

        public Builder setScopes(String... scopes) {
            this.is = scopes;
            return this;
        }
    }

    private AppStateClient(Context context, ConnectionCallbacks connectedListener, OnConnectionFailedListener connectionFailedListener, String accountName, String[] scopes) {
        this.io = new cw(context, connectedListener, connectionFailedListener, accountName, scopes);
    }

    public void connect() {
        this.io.connect();
    }

    public void deleteState(OnStateDeletedListener listener, int stateKey) {
        this.io.deleteState(listener, stateKey);
    }

    public void disconnect() {
        this.io.disconnect();
    }

    public int getMaxNumKeys() {
        return this.io.getMaxNumKeys();
    }

    public int getMaxStateSize() {
        return this.io.getMaxStateSize();
    }

    public boolean isConnected() {
        return this.io.isConnected();
    }

    public boolean isConnecting() {
        return this.io.isConnecting();
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
        return this.io.isConnectionCallbacksRegistered(listener);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener listener) {
        return this.io.isConnectionFailedListenerRegistered(listener);
    }

    public void listStates(OnStateListLoadedListener listener) {
        this.io.listStates(listener);
    }

    public void loadState(OnStateLoadedListener listener, int stateKey) {
        this.io.loadState(listener, stateKey);
    }

    public void reconnect() {
        this.io.disconnect();
        this.io.connect();
    }

    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        this.io.registerConnectionCallbacks(listener);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener listener) {
        this.io.registerConnectionFailedListener(listener);
    }

    public void resolveState(OnStateLoadedListener listener, int stateKey, String resolvedVersion, byte[] resolvedData) {
        this.io.resolveState(listener, stateKey, resolvedVersion, resolvedData);
    }

    public void signOut() {
        this.io.signOut(null);
    }

    public void signOut(OnSignOutCompleteListener listener) {
        dm.m388a((Object) listener, (Object) "Must provide a valid listener");
        this.io.signOut(listener);
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
        this.io.unregisterConnectionCallbacks(listener);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener listener) {
        this.io.unregisterConnectionFailedListener(listener);
    }

    public void updateState(int stateKey, byte[] data) {
        this.io.m1362a(null, stateKey, data);
    }

    public void updateStateImmediate(OnStateLoadedListener listener, int stateKey, byte[] data) {
        dm.m388a((Object) listener, (Object) "Must provide a valid listener");
        this.io.m1362a(listener, stateKey, data);
    }
}
