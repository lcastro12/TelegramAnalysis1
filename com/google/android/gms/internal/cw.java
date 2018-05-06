package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.appstate.AppState;
import com.google.android.gms.appstate.AppStateBuffer;
import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.appstate.OnSignOutCompleteListener;
import com.google.android.gms.appstate.OnStateDeletedListener;
import com.google.android.gms.appstate.OnStateListLoadedListener;
import com.google.android.gms.appstate.OnStateLoadedListener;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.internal.cy.C0712a;
import com.google.android.gms.internal.de.C0176b;
import com.google.android.gms.internal.de.C0713c;
import com.google.android.gms.internal.de.C0911d;

public final class cw extends de<cy> {
    private final String it;

    final class C0707b extends C0176b<OnStateDeletedListener> {
        final /* synthetic */ cw iB;
        private final int iC;
        private final int iD;

        public C0707b(cw cwVar, OnStateDeletedListener onStateDeletedListener, int i, int i2) {
            this.iB = cwVar;
            super(cwVar, onStateDeletedListener);
            this.iC = i;
            this.iD = i2;
        }

        public void m925a(OnStateDeletedListener onStateDeletedListener) {
            onStateDeletedListener.onStateDeleted(this.iC, this.iD);
        }

        protected void aF() {
        }
    }

    final class C0708h extends C0176b<OnSignOutCompleteListener> {
        final /* synthetic */ cw iB;

        public C0708h(cw cwVar, OnSignOutCompleteListener onSignOutCompleteListener) {
            this.iB = cwVar;
            super(cwVar, onSignOutCompleteListener);
        }

        public void m927a(OnSignOutCompleteListener onSignOutCompleteListener) {
            onSignOutCompleteListener.onSignOutComplete();
        }

        protected void aF() {
        }
    }

    final class C0909d extends C0713c<OnStateListLoadedListener> {
        final /* synthetic */ cw iB;

        public C0909d(cw cwVar, OnStateListLoadedListener onStateListLoadedListener, C0646d c0646d) {
            this.iB = cwVar;
            super(cwVar, onStateListLoadedListener, c0646d);
        }

        public void m1358a(OnStateListLoadedListener onStateListLoadedListener, C0646d c0646d) {
            onStateListLoadedListener.onStateListLoaded(c0646d.getStatusCode(), new AppStateBuffer(c0646d));
        }
    }

    final class C0910f extends C0713c<OnStateLoadedListener> {
        final /* synthetic */ cw iB;
        private final int iD;

        public C0910f(cw cwVar, OnStateLoadedListener onStateLoadedListener, int i, C0646d c0646d) {
            this.iB = cwVar;
            super(cwVar, onStateLoadedListener, c0646d);
            this.iD = i;
        }

        public void m1360a(OnStateLoadedListener onStateLoadedListener, C0646d c0646d) {
            byte[] bArr = null;
            AppStateBuffer appStateBuffer = new AppStateBuffer(c0646d);
            try {
                String conflictVersion;
                byte[] localData;
                if (appStateBuffer.getCount() > 0) {
                    AppState appState = appStateBuffer.get(0);
                    conflictVersion = appState.getConflictVersion();
                    localData = appState.getLocalData();
                    bArr = appState.getConflictData();
                } else {
                    localData = null;
                    conflictVersion = null;
                }
                appStateBuffer.close();
                int statusCode = c0646d.getStatusCode();
                if (statusCode == AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION) {
                    onStateLoadedListener.onStateConflict(this.iD, conflictVersion, localData, bArr);
                } else {
                    onStateLoadedListener.onStateLoaded(statusCode, this.iD, localData);
                }
            } catch (Throwable th) {
                appStateBuffer.close();
            }
        }
    }

    final class C0951a extends cv {
        private final OnStateDeletedListener iA;
        final /* synthetic */ cw iB;

        public C0951a(cw cwVar, OnStateDeletedListener onStateDeletedListener) {
            this.iB = cwVar;
            this.iA = (OnStateDeletedListener) dm.m388a((Object) onStateDeletedListener, (Object) "Listener must not be null");
        }

        public void onStateDeleted(int statusCode, int stateKey) {
            this.iB.m957a(new C0707b(this.iB, this.iA, statusCode, stateKey));
        }
    }

    final class C0952c extends cv {
        final /* synthetic */ cw iB;
        private final OnStateListLoadedListener iE;

        public C0952c(cw cwVar, OnStateListLoadedListener onStateListLoadedListener) {
            this.iB = cwVar;
            this.iE = (OnStateListLoadedListener) dm.m388a((Object) onStateListLoadedListener, (Object) "Listener must not be null");
        }

        public void mo816a(C0646d c0646d) {
            this.iB.m957a(new C0909d(this.iB, this.iE, c0646d));
        }
    }

    final class C0953e extends cv {
        final /* synthetic */ cw iB;
        private final OnStateLoadedListener iF;

        public C0953e(cw cwVar, OnStateLoadedListener onStateLoadedListener) {
            this.iB = cwVar;
            this.iF = (OnStateLoadedListener) dm.m388a((Object) onStateLoadedListener, (Object) "Listener must not be null");
        }

        public void mo815a(int i, C0646d c0646d) {
            this.iB.m957a(new C0910f(this.iB, this.iF, i, c0646d));
        }
    }

    final class C0954g extends cv {
        final /* synthetic */ cw iB;
        private final OnSignOutCompleteListener iG;

        public C0954g(cw cwVar, OnSignOutCompleteListener onSignOutCompleteListener) {
            this.iB = cwVar;
            this.iG = (OnSignOutCompleteListener) dm.m388a((Object) onSignOutCompleteListener, (Object) "Listener must not be null");
        }

        public void onSignOutComplete() {
            this.iB.m957a(new C0708h(this.iB, this.iG));
        }
    }

    public cw(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str, String[] strArr) {
        super(context, connectionCallbacks, onConnectionFailedListener, strArr);
        this.it = (String) dm.m392e(str);
    }

    public void m1362a(OnStateLoadedListener onStateLoadedListener, int i, byte[] bArr) {
        if (onStateLoadedListener == null) {
            cx cxVar = null;
        } else {
            Object c0953e = new C0953e(this, onStateLoadedListener);
        }
        try {
            ((cy) bd()).mo823a(cxVar, i, bArr);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    protected void mo1533a(dj djVar, C0911d c0911d) throws RemoteException {
        djVar.mo836a(c0911d, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), this.it, aY());
    }

    protected void mo1538a(String... strArr) {
        boolean z = false;
        for (String equals : strArr) {
            if (equals.equals(Scopes.APP_STATE)) {
                z = true;
            }
        }
        dm.m389a(z, String.format("AppStateClient requires %s to function.", new Object[]{Scopes.APP_STATE}));
    }

    protected String ag() {
        return "com.google.android.gms.appstate.service.START";
    }

    protected String ah() {
        return "com.google.android.gms.appstate.internal.IAppStateService";
    }

    public void deleteState(OnStateDeletedListener listener, int stateKey) {
        try {
            ((cy) bd()).mo825b(new C0951a(this, listener), stateKey);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    public int getMaxNumKeys() {
        try {
            return ((cy) bd()).getMaxNumKeys();
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
            return 2;
        }
    }

    public int getMaxStateSize() {
        try {
            return ((cy) bd()).getMaxStateSize();
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
            return 2;
        }
    }

    public void listStates(OnStateListLoadedListener listener) {
        try {
            ((cy) bd()).mo820a(new C0952c(this, listener));
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    public void loadState(OnStateLoadedListener listener, int stateKey) {
        try {
            ((cy) bd()).mo821a(new C0953e(this, listener), stateKey);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    protected /* synthetic */ IInterface mo1536p(IBinder iBinder) {
        return m1366r(iBinder);
    }

    protected cy m1366r(IBinder iBinder) {
        return C0712a.m940t(iBinder);
    }

    public void resolveState(OnStateLoadedListener listener, int stateKey, String resolvedVersion, byte[] resolvedData) {
        try {
            ((cy) bd()).mo822a(new C0953e(this, listener), stateKey, resolvedVersion, resolvedData);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    public void signOut(OnSignOutCompleteListener listener) {
        if (listener == null) {
            cx cxVar = null;
        } else {
            Object c0954g = new C0954g(this, listener);
        }
        try {
            ((cy) bd()).mo824b(cxVar);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }
}
