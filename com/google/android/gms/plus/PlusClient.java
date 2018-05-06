package com.google.android.gms.plus;

import android.content.Context;
import android.net.Uri;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.internal.fl;
import com.google.android.gms.internal.fo;
import com.google.android.gms.plus.model.moments.Moment;
import com.google.android.gms.plus.model.moments.MomentBuffer;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import java.util.Collection;

public class PlusClient implements GooglePlayServicesClient {
    final fl rb;

    public static class Builder {
        private final OnConnectionFailedListener ir;
        private final Context mContext;
        private final ConnectionCallbacks rc;
        private final fo rd = new fo(this.mContext);

        public Builder(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener connectionFailedListener) {
            this.mContext = context;
            this.rc = connectionCallbacks;
            this.ir = connectionFailedListener;
        }

        public PlusClient build() {
            return new PlusClient(new fl(this.mContext, this.rd.dh(), this.rc, this.ir));
        }

        public Builder clearScopes() {
            this.rd.dg();
            return this;
        }

        public Builder setAccountName(String accountName) {
            this.rd.m612Z(accountName);
            return this;
        }

        public Builder setActions(String... actions) {
            this.rd.m614e(actions);
            return this;
        }

        public Builder setScopes(String... scopes) {
            this.rd.m613d(scopes);
            return this;
        }
    }

    public interface OnAccessRevokedListener {
        void onAccessRevoked(ConnectionResult connectionResult);
    }

    public interface OnMomentsLoadedListener {
        void onMomentsLoaded(ConnectionResult connectionResult, MomentBuffer momentBuffer, String str, String str2);
    }

    public interface OnPeopleLoadedListener {
        void onPeopleLoaded(ConnectionResult connectionResult, PersonBuffer personBuffer, String str);
    }

    public interface OrderBy {
        public static final int ALPHABETICAL = 0;
        public static final int BEST = 1;
    }

    PlusClient(fl plusClientImpl) {
        this.rb = plusClientImpl;
    }

    fl cR() {
        return this.rb;
    }

    public void clearDefaultAccount() {
        this.rb.clearDefaultAccount();
    }

    public void connect() {
        this.rb.connect();
    }

    public void disconnect() {
        this.rb.disconnect();
    }

    public String getAccountName() {
        return this.rb.getAccountName();
    }

    public Person getCurrentPerson() {
        return this.rb.getCurrentPerson();
    }

    public boolean isConnected() {
        return this.rb.isConnected();
    }

    public boolean isConnecting() {
        return this.rb.isConnecting();
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
        return this.rb.isConnectionCallbacksRegistered(listener);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener listener) {
        return this.rb.isConnectionFailedListenerRegistered(listener);
    }

    public void loadMoments(OnMomentsLoadedListener listener) {
        this.rb.loadMoments(listener);
    }

    public void loadMoments(OnMomentsLoadedListener listener, int maxResults, String pageToken, Uri targetUrl, String type, String userId) {
        this.rb.loadMoments(listener, maxResults, pageToken, targetUrl, type, userId);
    }

    public void loadPeople(OnPeopleLoadedListener listener, Collection<String> personIds) {
        this.rb.m1477a(listener, (Collection) personIds);
    }

    public void loadPeople(OnPeopleLoadedListener listener, String... personIds) {
        this.rb.m1478a(listener, personIds);
    }

    public void loadVisiblePeople(OnPeopleLoadedListener listener, int orderBy, String pageToken) {
        this.rb.loadVisiblePeople(listener, orderBy, pageToken);
    }

    public void loadVisiblePeople(OnPeopleLoadedListener listener, String pageToken) {
        this.rb.loadVisiblePeople(listener, pageToken);
    }

    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        this.rb.registerConnectionCallbacks(listener);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener listener) {
        this.rb.registerConnectionFailedListener(listener);
    }

    public void removeMoment(String momentId) {
        this.rb.removeMoment(momentId);
    }

    public void revokeAccessAndDisconnect(OnAccessRevokedListener listener) {
        this.rb.revokeAccessAndDisconnect(listener);
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
        this.rb.unregisterConnectionCallbacks(listener);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener listener) {
        this.rb.unregisterConnectionFailedListener(listener);
    }

    public void writeMoment(Moment moment) {
        this.rb.writeMoment(moment);
    }
}
