package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw.C0722a;
import com.google.android.gms.plus.model.moments.ItemScope;
import com.google.android.gms.plus.model.moments.Moment;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class fs extends dw implements SafeParcelable, Moment {
    public static final ft CREATOR = new ft();
    private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
    private final int iM;
    private final Set<Integer> rI;
    private String sD;
    private fq sG;
    private fq sH;
    private String sm;
    private String sx;

    static {
        rH.put("id", C0722a.m998g("id", 2));
        rH.put("result", C0722a.m992a("result", 4, fq.class));
        rH.put("startDate", C0722a.m998g("startDate", 5));
        rH.put("target", C0722a.m992a("target", 6, fq.class));
        rH.put("type", C0722a.m998g("type", 7));
    }

    public fs() {
        this.iM = 1;
        this.rI = new HashSet();
    }

    fs(Set<Integer> set, int i, String str, fq fqVar, String str2, fq fqVar2, String str3) {
        this.rI = set;
        this.iM = i;
        this.sm = str;
        this.sG = fqVar;
        this.sx = str2;
        this.sH = fqVar2;
        this.sD = str3;
    }

    public fs(Set<Integer> set, String str, fq fqVar, String str2, fq fqVar2, String str3) {
        this.rI = set;
        this.iM = 1;
        this.sm = str;
        this.sG = fqVar;
        this.sx = str2;
        this.sH = fqVar2;
        this.sD = str3;
    }

    protected Object mo856D(String str) {
        return null;
    }

    protected boolean mo857E(String str) {
        return false;
    }

    protected boolean mo1545a(C0722a c0722a) {
        return this.rI.contains(Integer.valueOf(c0722a.bw()));
    }

    protected Object mo1546b(C0722a c0722a) {
        switch (c0722a.bw()) {
            case 2:
                return this.sm;
            case 4:
                return this.sG;
            case 5:
                return this.sx;
            case 6:
                return this.sH;
            case 7:
                return this.sD;
            default:
                throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
        }
    }

    public HashMap<String, C0722a<?, ?>> bp() {
        return rH;
    }

    fq dA() {
        return this.sH;
    }

    public fs dB() {
        return this;
    }

    public int describeContents() {
        ft ftVar = CREATOR;
        return 0;
    }

    Set<Integer> di() {
        return this.rI;
    }

    fq dz() {
        return this.sG;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof fs)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        fs fsVar = (fs) obj;
        for (C0722a c0722a : rH.values()) {
            if (mo1545a(c0722a)) {
                if (!fsVar.mo1545a(c0722a)) {
                    return false;
                }
                if (!mo1546b(c0722a).equals(fsVar.mo1546b(c0722a))) {
                    return false;
                }
            } else if (fsVar.mo1545a(c0722a)) {
                return false;
            }
        }
        return true;
    }

    public /* synthetic */ Object freeze() {
        return dB();
    }

    public String getId() {
        return this.sm;
    }

    public ItemScope getResult() {
        return this.sG;
    }

    public String getStartDate() {
        return this.sx;
    }

    public ItemScope getTarget() {
        return this.sH;
    }

    public String getType() {
        return this.sD;
    }

    int getVersionCode() {
        return this.iM;
    }

    public boolean hasId() {
        return this.rI.contains(Integer.valueOf(2));
    }

    public boolean hasResult() {
        return this.rI.contains(Integer.valueOf(4));
    }

    public boolean hasStartDate() {
        return this.rI.contains(Integer.valueOf(5));
    }

    public boolean hasTarget() {
        return this.rI.contains(Integer.valueOf(6));
    }

    public boolean hasType() {
        return this.rI.contains(Integer.valueOf(7));
    }

    public int hashCode() {
        int i = 0;
        for (C0722a c0722a : rH.values()) {
            int hashCode;
            if (mo1545a(c0722a)) {
                hashCode = mo1546b(c0722a).hashCode() + (i + c0722a.bw());
            } else {
                hashCode = i;
            }
            i = hashCode;
        }
        return i;
    }

    public boolean isDataValid() {
        return true;
    }

    public void writeToParcel(Parcel out, int flags) {
        ft ftVar = CREATOR;
        ft.m619a(this, out, flags);
    }
}
