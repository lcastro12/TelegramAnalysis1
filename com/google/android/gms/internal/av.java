package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.internal.aw.C0681a;
import java.util.Map;

public final class av extends C0681a {
    private Map<Class<? extends NetworkExtras>, NetworkExtras> fq;

    private <NETWORK_EXTRAS extends com.google.ads.mediation.NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> ax m1329h(String str) throws RemoteException {
        try {
            MediationAdapter mediationAdapter = (MediationAdapter) Class.forName(str).newInstance();
            return new az(mediationAdapter, (com.google.ads.mediation.NetworkExtras) this.fq.get(mediationAdapter.getAdditionalParametersType()));
        } catch (Throwable th) {
            cn.m299q("Could not instantiate mediation adapter: " + str + ". " + th.getMessage());
            RemoteException remoteException = new RemoteException();
        }
    }

    public void m1330c(Map<Class<? extends NetworkExtras>, NetworkExtras> map) {
        this.fq = map;
    }

    public ax mo774g(String str) throws RemoteException {
        return m1329h(str);
    }
}
