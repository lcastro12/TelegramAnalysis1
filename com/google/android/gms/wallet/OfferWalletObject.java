package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class OfferWalletObject implements SafeParcelable {
    public static final Creator<OfferWalletObject> CREATOR = new C0263j();
    private final int iM;
    String tU;
    String ul;

    public OfferWalletObject() {
        this.iM = 2;
    }

    OfferWalletObject(int versionCode, String id, String redemptionCode) {
        this.iM = versionCode;
        this.tU = id;
        this.ul = redemptionCode;
    }

    public int describeContents() {
        return 0;
    }

    public String getId() {
        return this.tU;
    }

    public String getRedemptionCode() {
        return this.ul;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public void writeToParcel(Parcel dest, int flags) {
        C0263j.m794a(this, dest, flags);
    }
}
