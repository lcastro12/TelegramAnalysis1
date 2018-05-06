package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw.C0722a;
import java.util.ArrayList;
import java.util.HashMap;

public class dz implements SafeParcelable {
    public static final ea CREATOR = new ea();
    private final int iM;
    private final HashMap<String, HashMap<String, C0722a<?, ?>>> lJ;
    private final ArrayList<C0723a> lK;
    private final String lL;

    public static class C0723a implements SafeParcelable {
        public static final eb CREATOR = new eb();
        final String className;
        final ArrayList<C0724b> lM;
        final int versionCode;

        C0723a(int i, String str, ArrayList<C0724b> arrayList) {
            this.versionCode = i;
            this.className = str;
            this.lM = arrayList;
        }

        C0723a(String str, HashMap<String, C0722a<?, ?>> hashMap) {
            this.versionCode = 1;
            this.className = str;
            this.lM = C0723a.m1002a(hashMap);
        }

        private static ArrayList<C0724b> m1002a(HashMap<String, C0722a<?, ?>> hashMap) {
            if (hashMap == null) {
                return null;
            }
            ArrayList<C0724b> arrayList = new ArrayList();
            for (String str : hashMap.keySet()) {
                arrayList.add(new C0724b(str, (C0722a) hashMap.get(str)));
            }
            return arrayList;
        }

        HashMap<String, C0722a<?, ?>> bG() {
            HashMap<String, C0722a<?, ?>> hashMap = new HashMap();
            int size = this.lM.size();
            for (int i = 0; i < size; i++) {
                C0724b c0724b = (C0724b) this.lM.get(i);
                hashMap.put(c0724b.lN, c0724b.lO);
            }
            return hashMap;
        }

        public int describeContents() {
            eb ebVar = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            eb ebVar = CREATOR;
            eb.m432a(this, out, flags);
        }
    }

    public static class C0724b implements SafeParcelable {
        public static final dy CREATOR = new dy();
        final String lN;
        final C0722a<?, ?> lO;
        final int versionCode;

        C0724b(int i, String str, C0722a<?, ?> c0722a) {
            this.versionCode = i;
            this.lN = str;
            this.lO = c0722a;
        }

        C0724b(String str, C0722a<?, ?> c0722a) {
            this.versionCode = 1;
            this.lN = str;
            this.lO = c0722a;
        }

        public int describeContents() {
            dy dyVar = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            dy dyVar = CREATOR;
            dy.m426a(this, out, flags);
        }
    }

    dz(int i, ArrayList<C0723a> arrayList, String str) {
        this.iM = i;
        this.lK = null;
        this.lJ = m1003b((ArrayList) arrayList);
        this.lL = (String) dm.m392e(str);
        bC();
    }

    public dz(Class<? extends dw> cls) {
        this.iM = 1;
        this.lK = null;
        this.lJ = new HashMap();
        this.lL = cls.getCanonicalName();
    }

    private static HashMap<String, HashMap<String, C0722a<?, ?>>> m1003b(ArrayList<C0723a> arrayList) {
        HashMap<String, HashMap<String, C0722a<?, ?>>> hashMap = new HashMap();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            C0723a c0723a = (C0723a) arrayList.get(i);
            hashMap.put(c0723a.className, c0723a.bG());
        }
        return hashMap;
    }

    public HashMap<String, C0722a<?, ?>> m1004H(String str) {
        return (HashMap) this.lJ.get(str);
    }

    public void m1005a(Class<? extends dw> cls, HashMap<String, C0722a<?, ?>> hashMap) {
        this.lJ.put(cls.getCanonicalName(), hashMap);
    }

    public boolean m1006b(Class<? extends dw> cls) {
        return this.lJ.containsKey(cls.getCanonicalName());
    }

    public void bC() {
        for (String str : this.lJ.keySet()) {
            HashMap hashMap = (HashMap) this.lJ.get(str);
            for (String str2 : hashMap.keySet()) {
                ((C0722a) hashMap.get(str2)).m1000a(this);
            }
        }
    }

    public void bD() {
        for (String str : this.lJ.keySet()) {
            HashMap hashMap = (HashMap) this.lJ.get(str);
            HashMap hashMap2 = new HashMap();
            for (String str2 : hashMap.keySet()) {
                hashMap2.put(str2, ((C0722a) hashMap.get(str2)).bs());
            }
            this.lJ.put(str, hashMap2);
        }
    }

    ArrayList<C0723a> bE() {
        ArrayList<C0723a> arrayList = new ArrayList();
        for (String str : this.lJ.keySet()) {
            arrayList.add(new C0723a(str, (HashMap) this.lJ.get(str)));
        }
        return arrayList;
    }

    public String bF() {
        return this.lL;
    }

    public int describeContents() {
        ea eaVar = CREATOR;
        return 0;
    }

    int getVersionCode() {
        return this.iM;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : this.lJ.keySet()) {
            stringBuilder.append(str).append(":\n");
            HashMap hashMap = (HashMap) this.lJ.get(str);
            for (String str2 : hashMap.keySet()) {
                stringBuilder.append("  ").append(str2).append(": ");
                stringBuilder.append(hashMap.get(str2));
            }
        }
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        ea eaVar = CREATOR;
        ea.m429a(this, out, flags);
    }
}
