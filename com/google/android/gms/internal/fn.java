package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class fn implements SafeParcelable {
    public static final fp CREATOR = new fp();
    private final int iM;
    private final String it;
    private final String[] rA;
    private final String rB;
    private final String rC;
    private final String rD;
    private final String rE;
    private final String[] ry;
    private final String[] rz;

    fn(int i, String str, String[] strArr, String[] strArr2, String[] strArr3, String str2, String str3, String str4, String str5) {
        this.iM = i;
        this.it = str;
        this.ry = strArr;
        this.rz = strArr2;
        this.rA = strArr3;
        this.rB = str2;
        this.rC = str3;
        this.rD = str4;
        this.rE = str5;
    }

    fn(String str, String[] strArr, String[] strArr2, String[] strArr3, String str2, String str3, String str4, String str5) {
        this.iM = 1;
        this.it = str;
        this.ry = strArr;
        this.rz = strArr2;
        this.rA = strArr3;
        this.rB = str2;
        this.rC = str3;
        this.rD = str4;
        this.rE = str5;
    }

    public String[] cZ() {
        return this.ry;
    }

    public String[] da() {
        return this.rz;
    }

    public String[] db() {
        return this.rA;
    }

    public String dc() {
        return this.rB;
    }

    public String dd() {
        return this.rC;
    }

    public String de() {
        return this.rD;
    }

    public int describeContents() {
        return 0;
    }

    public String df() {
        return this.rE;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof fn)) {
            return false;
        }
        fn fnVar = (fn) obj;
        return this.iM == fnVar.iM && dl.equal(this.it, fnVar.it) && dl.equal(this.ry, fnVar.ry) && dl.equal(this.rz, fnVar.rz) && dl.equal(this.rA, fnVar.rA) && dl.equal(this.rB, fnVar.rB) && dl.equal(this.rC, fnVar.rC) && dl.equal(this.rD, fnVar.rD) && dl.equal(this.rE, fnVar.rE);
    }

    public String getAccountName() {
        return this.it;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return dl.hashCode(Integer.valueOf(this.iM), this.it, this.ry, this.rz, this.rA, this.rB, this.rC, this.rD, this.rE);
    }

    public String toString() {
        return dl.m387d(this).m386a("versionCode", Integer.valueOf(this.iM)).m386a("accountName", this.it).m386a("requestedScopes", this.ry).m386a("visibleActivities", this.rz).m386a("requiredFeatures", this.rA).m386a("packageNameForAuth", this.rB).m386a("callingPackageName", this.rC).m386a("applicationName", this.rD).m386a("clientId", this.rE).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        fp.m615a(this, out, flags);
    }
}
