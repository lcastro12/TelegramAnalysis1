package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class C0770v implements SafeParcelable {
    public static final C0206w CREATOR = new C0206w();
    public final long es;
    public final int et;
    public final List<String> eu;
    public final boolean ev;
    public final Bundle extras;
    public final int tagForChildDirectedTreatment;
    public final int versionCode;

    C0770v(int i, long j, Bundle bundle, int i2, List<String> list, boolean z, int i3) {
        this.versionCode = i;
        this.es = j;
        this.extras = bundle;
        this.et = i2;
        this.eu = list;
        this.ev = z;
        this.tagForChildDirectedTreatment = i3;
    }

    public C0770v(Context context, AdRequest adRequest) {
        Bundle bundle = null;
        this.versionCode = 1;
        Date birthday = adRequest.getBirthday();
        this.es = birthday != null ? birthday.getTime() : -1;
        this.et = adRequest.getGender();
        Collection keywords = adRequest.getKeywords();
        this.eu = !keywords.isEmpty() ? Collections.unmodifiableList(new ArrayList(keywords)) : null;
        this.ev = adRequest.isTestDevice(context);
        this.tagForChildDirectedTreatment = adRequest.m8w();
        AdMobExtras adMobExtras = (AdMobExtras) adRequest.getNetworkExtras(AdMobExtras.class);
        if (adMobExtras != null) {
            bundle = adMobExtras.getExtras();
        }
        this.extras = bundle;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        C0206w.m697a(this, out, flags);
    }
}
