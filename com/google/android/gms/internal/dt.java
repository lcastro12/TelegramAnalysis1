package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw.C0183b;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class dt implements SafeParcelable, C0183b<String, Integer> {
    public static final du CREATOR = new du();
    private final int iM;
    private final HashMap<String, Integer> lu;
    private final HashMap<Integer, String> lv;
    private final ArrayList<C0721a> lw;

    public static final class C0721a implements SafeParcelable {
        public static final dv CREATOR = new dv();
        final String lx;
        final int ly;
        final int versionCode;

        C0721a(int i, String str, int i2) {
            this.versionCode = i;
            this.lx = str;
            this.ly = i2;
        }

        C0721a(String str, int i) {
            this.versionCode = 1;
            this.lx = str;
            this.ly = i;
        }

        public int describeContents() {
            dv dvVar = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            dv dvVar = CREATOR;
            dv.m410a(this, out, flags);
        }
    }

    public dt() {
        this.iM = 1;
        this.lu = new HashMap();
        this.lv = new HashMap();
        this.lw = null;
    }

    dt(int i, ArrayList<C0721a> arrayList) {
        this.iM = i;
        this.lu = new HashMap();
        this.lv = new HashMap();
        this.lw = null;
        m987a((ArrayList) arrayList);
    }

    private void m987a(ArrayList<C0721a> arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            C0721a c0721a = (C0721a) it.next();
            m989c(c0721a.lx, c0721a.ly);
        }
    }

    public String m988a(Integer num) {
        String str = (String) this.lv.get(num);
        return (str == null && this.lu.containsKey("gms_unknown")) ? "gms_unknown" : str;
    }

    ArrayList<C0721a> bm() {
        ArrayList<C0721a> arrayList = new ArrayList();
        for (String str : this.lu.keySet()) {
            arrayList.add(new C0721a(str, ((Integer) this.lu.get(str)).intValue()));
        }
        return arrayList;
    }

    public int bn() {
        return 7;
    }

    public int bo() {
        return 0;
    }

    public dt m989c(String str, int i) {
        this.lu.put(str, Integer.valueOf(i));
        this.lv.put(Integer.valueOf(i), str);
        return this;
    }

    public int describeContents() {
        du duVar = CREATOR;
        return 0;
    }

    public /* synthetic */ Object mo851f(Object obj) {
        return m988a((Integer) obj);
    }

    int getVersionCode() {
        return this.iM;
    }

    public void writeToParcel(Parcel out, int flags) {
        du duVar = CREATOR;
        du.m407a(this, out, flags);
    }
}
