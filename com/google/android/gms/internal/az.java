package com.google.android.gms.internal;

import android.app.Activity;
import android.os.RemoteException;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;
import com.google.ads.mediation.admob.AdMobServerParameters;
import com.google.android.gms.dynamic.C0112b;
import com.google.android.gms.dynamic.C0898c;
import com.google.android.gms.internal.ax.C0683a;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public final class az<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> extends C0683a {
    private final MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> fr;
    private final NETWORK_EXTRAS fs;

    public az(MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> mediationAdapter, NETWORK_EXTRAS network_extras) {
        this.fr = mediationAdapter;
        this.fs = network_extras;
    }

    private SERVER_PARAMETERS m1332a(String str, int i) throws RemoteException {
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                Map hashMap = new HashMap(jSONObject.length());
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str2 = (String) keys.next();
                    hashMap.put(str2, jSONObject.getString(str2));
                }
                Map map = hashMap;
            } catch (Throwable th) {
                cn.m293b("Could not get MediationServerParameters.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            Object hashMap2 = new HashMap(0);
        }
        Class serverParametersType = this.fr.getServerParametersType();
        SERVER_PARAMETERS server_parameters = null;
        if (serverParametersType != null) {
            SERVER_PARAMETERS server_parameters2 = (MediationServerParameters) serverParametersType.newInstance();
            server_parameters2.load(map);
            server_parameters = server_parameters2;
        }
        if (server_parameters instanceof AdMobServerParameters) {
            ((AdMobServerParameters) server_parameters).tagForChildDirectedTreatment = i;
        }
        return server_parameters;
    }

    public void mo775a(C0112b c0112b, C0770v c0770v, String str, ay ayVar) throws RemoteException {
        if (this.fr instanceof MediationInterstitialAdapter) {
            cn.m295m("Requesting interstitial ad from adapter.");
            try {
                ((MediationInterstitialAdapter) this.fr).requestInterstitialAd(new ba(ayVar), (Activity) C0898c.m1317b(c0112b), m1332a(str, c0770v.tagForChildDirectedTreatment), bb.m197e(c0770v), this.fs);
            } catch (Throwable th) {
                cn.m293b("Could not request interstitial ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            cn.m299q("MediationAdapter is not a MediationInterstitialAdapter: " + this.fr.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void mo776a(C0112b c0112b, C0771x c0771x, C0770v c0770v, String str, ay ayVar) throws RemoteException {
        if (this.fr instanceof MediationBannerAdapter) {
            cn.m295m("Requesting banner ad from adapter.");
            try {
                ((MediationBannerAdapter) this.fr).requestBannerAd(new ba(ayVar), (Activity) C0898c.m1317b(c0112b), m1332a(str, c0770v.tagForChildDirectedTreatment), bb.m195a(c0771x), bb.m197e(c0770v), this.fs);
            } catch (Throwable th) {
                cn.m293b("Could not request banner ad from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            cn.m299q("MediationAdapter is not a MediationBannerAdapter: " + this.fr.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void destroy() throws RemoteException {
        try {
            this.fr.destroy();
        } catch (Throwable th) {
            cn.m293b("Could not destroy adapter.", th);
            RemoteException remoteException = new RemoteException();
        }
    }

    public C0112b getView() throws RemoteException {
        if (this.fr instanceof MediationBannerAdapter) {
            try {
                return C0898c.m1318g(((MediationBannerAdapter) this.fr).getBannerView());
            } catch (Throwable th) {
                cn.m293b("Could not get banner view from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            cn.m299q("MediationAdapter is not a MediationBannerAdapter: " + this.fr.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }

    public void showInterstitial() throws RemoteException {
        if (this.fr instanceof MediationInterstitialAdapter) {
            cn.m295m("Showing interstitial from adapter.");
            try {
                ((MediationInterstitialAdapter) this.fr).showInterstitial();
            } catch (Throwable th) {
                cn.m293b("Could not show interstitial from adapter.", th);
                RemoteException remoteException = new RemoteException();
            }
        } else {
            cn.m299q("MediationAdapter is not a MediationInterstitialAdapter: " + this.fr.getClass().getCanonicalName());
            throw new RemoteException();
        }
    }
}
