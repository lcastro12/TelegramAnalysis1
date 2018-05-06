package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.internal.de.C0176b;
import com.google.android.gms.internal.de.C0713c;
import com.google.android.gms.internal.de.C0911d;
import com.google.android.gms.internal.fk.C0758a;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.PlusClient.OnMomentsLoadedListener;
import com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener;
import com.google.android.gms.plus.model.moments.Moment;
import com.google.android.gms.plus.model.moments.MomentBuffer;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class fl extends de<fk> implements GooglePlayServicesClient {
    private Person ro;
    private fn rp;

    final class C0759f extends C0176b<OnAccessRevokedListener> {
        final /* synthetic */ fl rr;
        private final ConnectionResult rs;

        public C0759f(fl flVar, OnAccessRevokedListener onAccessRevokedListener, ConnectionResult connectionResult) {
            this.rr = flVar;
            super(flVar, onAccessRevokedListener);
            this.rs = connectionResult;
        }

        protected void m1217a(OnAccessRevokedListener onAccessRevokedListener) {
            this.rr.disconnect();
            if (onAccessRevokedListener != null) {
                onAccessRevokedListener.onAccessRevoked(this.rs);
            }
        }

        protected void aF() {
        }
    }

    final class C0922b extends C0713c<OnMomentsLoadedListener> {
        final /* synthetic */ fl rr;
        private final ConnectionResult rs;
        private final String rt;
        private final String ru;

        public C0922b(fl flVar, OnMomentsLoadedListener onMomentsLoadedListener, ConnectionResult connectionResult, C0646d c0646d, String str, String str2) {
            this.rr = flVar;
            super(flVar, onMomentsLoadedListener, c0646d);
            this.rs = connectionResult;
            this.rt = str;
            this.ru = str2;
        }

        protected void m1470a(OnMomentsLoadedListener onMomentsLoadedListener, C0646d c0646d) {
            onMomentsLoadedListener.onMomentsLoaded(this.rs, c0646d != null ? new MomentBuffer(c0646d) : null, this.rt, this.ru);
        }
    }

    final class C0923d extends C0713c<OnPeopleLoadedListener> {
        final /* synthetic */ fl rr;
        private final ConnectionResult rs;
        private final String rt;

        public C0923d(fl flVar, OnPeopleLoadedListener onPeopleLoadedListener, ConnectionResult connectionResult, C0646d c0646d, String str) {
            this.rr = flVar;
            super(flVar, onPeopleLoadedListener, c0646d);
            this.rs = connectionResult;
            this.rt = str;
        }

        protected void m1472a(OnPeopleLoadedListener onPeopleLoadedListener, C0646d c0646d) {
            onPeopleLoadedListener.onPeopleLoaded(this.rs, c0646d != null ? new PersonBuffer(c0646d) : null, this.rt);
        }
    }

    final class C0966a extends fg {
        private final OnMomentsLoadedListener rq;
        final /* synthetic */ fl rr;

        public C0966a(fl flVar, OnMomentsLoadedListener onMomentsLoadedListener) {
            this.rr = flVar;
            this.rq = onMomentsLoadedListener;
        }

        public void mo1022a(C0646d c0646d, String str, String str2) {
            C0646d c0646d2;
            ConnectionResult connectionResult = new ConnectionResult(c0646d.getStatusCode(), c0646d.aM() != null ? (PendingIntent) c0646d.aM().getParcelable("pendingIntent") : null);
            if (connectionResult.isSuccess() || c0646d == null) {
                c0646d2 = c0646d;
            } else {
                if (!c0646d.isClosed()) {
                    c0646d.close();
                }
                c0646d2 = null;
            }
            this.rr.m957a(new C0922b(this.rr, this.rq, connectionResult, c0646d2, str, str2));
        }
    }

    final class C0967c extends fg {
        final /* synthetic */ fl rr;
        private final OnPeopleLoadedListener rv;

        public C0967c(fl flVar, OnPeopleLoadedListener onPeopleLoadedListener) {
            this.rr = flVar;
            this.rv = onPeopleLoadedListener;
        }

        public void mo1021a(C0646d c0646d, String str) {
            C0646d c0646d2;
            ConnectionResult connectionResult = new ConnectionResult(c0646d.getStatusCode(), c0646d.aM() != null ? (PendingIntent) c0646d.aM().getParcelable("pendingIntent") : null);
            if (connectionResult.isSuccess() || c0646d == null) {
                c0646d2 = c0646d;
            } else {
                if (!c0646d.isClosed()) {
                    c0646d.close();
                }
                c0646d2 = null;
            }
            this.rr.m957a(new C0923d(this.rr, this.rv, connectionResult, c0646d2, str));
        }
    }

    final class C0968e extends fg {
        final /* synthetic */ fl rr;
        private final OnAccessRevokedListener rw;

        public C0968e(fl flVar, OnAccessRevokedListener onAccessRevokedListener) {
            this.rr = flVar;
            this.rw = onAccessRevokedListener;
        }

        public void mo1023b(int i, Bundle bundle) {
            PendingIntent pendingIntent = null;
            if (bundle != null) {
                pendingIntent = (PendingIntent) bundle.getParcelable("pendingIntent");
            }
            this.rr.m957a(new C0759f(this.rr, this.rw, new ConnectionResult(i, pendingIntent)));
        }
    }

    public fl(Context context, fn fnVar, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, connectionCallbacks, onConnectionFailedListener, fnVar.cZ());
        this.rp = fnVar;
    }

    public boolean m1474Y(String str) {
        return Arrays.asList(aY()).contains(str);
    }

    protected void mo1539a(int i, IBinder iBinder, Bundle bundle) {
        if (i == 0 && bundle != null && bundle.containsKey("loaded_person")) {
            this.ro = fv.m1525e(bundle.getByteArray("loaded_person"));
        }
        super.mo1539a(i, iBinder, bundle);
    }

    protected void mo1533a(dj djVar, C0911d c0911d) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putString("client_id", this.rp.df());
        bundle.putStringArray("request_visible_actions", this.rp.da());
        djVar.mo837a(c0911d, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, this.rp.dd(), this.rp.dc(), aY(), this.rp.getAccountName(), bundle);
    }

    public void m1477a(OnPeopleLoadedListener onPeopleLoadedListener, Collection<String> collection) {
        bc();
        fh c0967c = new C0967c(this, onPeopleLoadedListener);
        try {
            ((fk) bd()).mo1036a(c0967c, new ArrayList(collection));
        } catch (RemoteException e) {
            c0967c.mo1021a(C0646d.m806r(8), null);
        }
    }

    public void m1478a(OnPeopleLoadedListener onPeopleLoadedListener, String[] strArr) {
        m1477a(onPeopleLoadedListener, Arrays.asList(strArr));
    }

    protected String ag() {
        return "com.google.android.gms.plus.service.START";
    }

    protected String ah() {
        return "com.google.android.gms.plus.internal.IPlusService";
    }

    protected fk ar(IBinder iBinder) {
        return C0758a.aq(iBinder);
    }

    public void clearDefaultAccount() {
        bc();
        try {
            this.ro = null;
            ((fk) bd()).clearDefaultAccount();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public String getAccountName() {
        bc();
        try {
            return ((fk) bd()).getAccountName();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public Person getCurrentPerson() {
        bc();
        return this.ro;
    }

    public void loadMoments(OnMomentsLoadedListener listener) {
        loadMoments(listener, 20, null, null, null, "me");
    }

    public void loadMoments(OnMomentsLoadedListener listener, int maxResults, String pageToken, Uri targetUrl, String type, String userId) {
        bc();
        Object c0966a = listener != null ? new C0966a(this, listener) : null;
        try {
            ((fk) bd()).mo1032a(c0966a, maxResults, pageToken, targetUrl, type, userId);
        } catch (RemoteException e) {
            c0966a.mo1022a(C0646d.m806r(8), null, null);
        }
    }

    public void loadVisiblePeople(OnPeopleLoadedListener listener, int orderBy, String pageToken) {
        bc();
        Object c0967c = new C0967c(this, listener);
        try {
            ((fk) bd()).mo1031a(c0967c, 1, orderBy, -1, pageToken);
        } catch (RemoteException e) {
            c0967c.mo1021a(C0646d.m806r(8), null);
        }
    }

    public void loadVisiblePeople(OnPeopleLoadedListener listener, String pageToken) {
        loadVisiblePeople(listener, 0, pageToken);
    }

    protected /* synthetic */ IInterface mo1536p(IBinder iBinder) {
        return ar(iBinder);
    }

    public void removeMoment(String momentId) {
        bc();
        try {
            ((fk) bd()).removeMoment(momentId);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void revokeAccessAndDisconnect(OnAccessRevokedListener listener) {
        bc();
        clearDefaultAccount();
        Object c0968e = new C0968e(this, listener);
        try {
            ((fk) bd()).mo1037b(c0968e);
        } catch (RemoteException e) {
            c0968e.mo1023b(8, null);
        }
    }

    public void writeMoment(Moment moment) {
        bc();
        try {
            ((fk) bd()).mo1029a(ec.m1020a((fs) moment));
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }
}
