package com.google.android.gms.ads;

import android.content.Context;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.internal.cm;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class AdRequest {
    public static final String DEVICE_ID_EMULATOR = cm.m291l("emulator");
    public static final int ERROR_CODE_INTERNAL_ERROR = 0;
    public static final int ERROR_CODE_INVALID_REQUEST = 1;
    public static final int ERROR_CODE_NETWORK_ERROR = 2;
    public static final int ERROR_CODE_NO_FILL = 3;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_UNKNOWN = 0;
    private final Date f9d;
    private final int dI;
    private final Map<Class<? extends NetworkExtras>, NetworkExtras> dJ;
    private final int dK;
    private final Set<String> dL;
    private final Set<String> f10f;

    public static final class Builder {
        private Date f8d;
        private int dI = -1;
        private int dK = -1;
        private final HashSet<String> dM = new HashSet();
        private final HashMap<Class<? extends NetworkExtras>, NetworkExtras> dN = new HashMap();
        private final HashSet<String> dO = new HashSet();

        public Builder addKeyword(String keyword) {
            this.dM.add(keyword);
            return this;
        }

        public Builder addNetworkExtras(NetworkExtras networkExtras) {
            this.dN.put(networkExtras.getClass(), networkExtras);
            return this;
        }

        public Builder addTestDevice(String deviceId) {
            this.dO.add(deviceId);
            return this;
        }

        public AdRequest build() {
            return new AdRequest();
        }

        public Builder setBirthday(Date birthday) {
            this.f8d = birthday;
            return this;
        }

        public Builder setGender(int gender) {
            this.dI = gender;
            return this;
        }

        public Builder tagForChildDirectedTreatment(boolean tagForChildDirectedTreatment) {
            this.dK = tagForChildDirectedTreatment ? 1 : 0;
            return this;
        }
    }

    private AdRequest(Builder builder) {
        this.f9d = builder.f8d;
        this.dI = builder.dI;
        this.f10f = Collections.unmodifiableSet(builder.dM);
        this.dJ = Collections.unmodifiableMap(builder.dN);
        this.dK = builder.dK;
        this.dL = Collections.unmodifiableSet(builder.dO);
    }

    public Date getBirthday() {
        return this.f9d;
    }

    public int getGender() {
        return this.dI;
    }

    public Set<String> getKeywords() {
        return this.f10f;
    }

    public <T extends NetworkExtras> T getNetworkExtras(Class<T> networkExtrasClass) {
        return (NetworkExtras) this.dJ.get(networkExtrasClass);
    }

    public boolean isTestDevice(Context context) {
        return this.dL.contains(cm.m290l(context));
    }

    Map<Class<? extends NetworkExtras>, NetworkExtras> m7v() {
        return this.dJ;
    }

    public int m8w() {
        return this.dK;
    }
}
