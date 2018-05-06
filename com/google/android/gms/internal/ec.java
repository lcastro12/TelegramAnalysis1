package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0107a;
import com.google.android.gms.common.internal.safeparcel.C0107a.C0106a;
import com.google.android.gms.common.internal.safeparcel.C0108b;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw.C0722a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class ec extends dw implements SafeParcelable {
    public static final ed CREATOR = new ed();
    private final int iM;
    private final dz lH;
    private final Parcel lP;
    private final int lQ;
    private int lR;
    private int lS;
    private final String mClassName;

    ec(int i, Parcel parcel, dz dzVar) {
        this.iM = i;
        this.lP = (Parcel) dm.m392e(parcel);
        this.lQ = 2;
        this.lH = dzVar;
        if (this.lH == null) {
            this.mClassName = null;
        } else {
            this.mClassName = this.lH.bF();
        }
        this.lR = 2;
    }

    private ec(SafeParcelable safeParcelable, dz dzVar, String str) {
        this.iM = 1;
        this.lP = Parcel.obtain();
        safeParcelable.writeToParcel(this.lP, 0);
        this.lQ = 1;
        this.lH = (dz) dm.m392e(dzVar);
        this.mClassName = (String) dm.m392e(str);
        this.lR = 2;
    }

    public static <T extends dw & SafeParcelable> ec m1020a(T t) {
        String canonicalName = t.getClass().getCanonicalName();
        return new ec((SafeParcelable) t, m1026b((dw) t), canonicalName);
    }

    private static void m1021a(dz dzVar, dw dwVar) {
        Class cls = dwVar.getClass();
        if (!dzVar.m1006b(cls)) {
            HashMap bp = dwVar.bp();
            dzVar.m1005a(cls, dwVar.bp());
            for (String str : bp.keySet()) {
                C0722a c0722a = (C0722a) bp.get(str);
                Class bx = c0722a.bx();
                if (bx != null) {
                    try {
                        m1021a(dzVar, (dw) bx.newInstance());
                    } catch (Throwable e) {
                        throw new IllegalStateException("Could not instantiate an object of type " + c0722a.bx().getCanonicalName(), e);
                    } catch (Throwable e2) {
                        throw new IllegalStateException("Could not access object of type " + c0722a.bx().getCanonicalName(), e2);
                    }
                }
            }
        }
    }

    private void m1022a(StringBuilder stringBuilder, int i, Object obj) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                stringBuilder.append(obj);
                return;
            case 7:
                stringBuilder.append("\"").append(ei.m449I(obj.toString())).append("\"");
                return;
            case 8:
                stringBuilder.append("\"").append(ef.m445b((byte[]) obj)).append("\"");
                return;
            case 9:
                stringBuilder.append("\"").append(ef.m446c((byte[]) obj));
                stringBuilder.append("\"");
                return;
            case 10:
                ej.m450a(stringBuilder, (HashMap) obj);
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown type = " + i);
        }
    }

    private void m1023a(StringBuilder stringBuilder, C0722a<?, ?> c0722a, Parcel parcel, int i) {
        switch (c0722a.bo()) {
            case 0:
                m1030b(stringBuilder, (C0722a) c0722a, m420a(c0722a, Integer.valueOf(C0107a.m86f(parcel, i))));
                return;
            case 1:
                m1030b(stringBuilder, (C0722a) c0722a, m420a(c0722a, C0107a.m88h(parcel, i)));
                return;
            case 2:
                m1030b(stringBuilder, (C0722a) c0722a, m420a(c0722a, Long.valueOf(C0107a.m87g(parcel, i))));
                return;
            case 3:
                m1030b(stringBuilder, (C0722a) c0722a, m420a(c0722a, Float.valueOf(C0107a.m89i(parcel, i))));
                return;
            case 4:
                m1030b(stringBuilder, (C0722a) c0722a, m420a(c0722a, Double.valueOf(C0107a.m91j(parcel, i))));
                return;
            case 5:
                m1030b(stringBuilder, (C0722a) c0722a, m420a(c0722a, C0107a.m93k(parcel, i)));
                return;
            case 6:
                m1030b(stringBuilder, (C0722a) c0722a, m420a(c0722a, Boolean.valueOf(C0107a.m83c(parcel, i))));
                return;
            case 7:
                m1030b(stringBuilder, (C0722a) c0722a, m420a(c0722a, C0107a.m94l(parcel, i)));
                return;
            case 8:
            case 9:
                m1030b(stringBuilder, (C0722a) c0722a, m420a(c0722a, C0107a.m97o(parcel, i)));
                return;
            case 10:
                m1030b(stringBuilder, (C0722a) c0722a, m420a(c0722a, m1027b(C0107a.m96n(parcel, i))));
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown field out type = " + c0722a.bo());
        }
    }

    private void m1024a(StringBuilder stringBuilder, String str, C0722a<?, ?> c0722a, Parcel parcel, int i) {
        stringBuilder.append("\"").append(str).append("\":");
        if (c0722a.bz()) {
            m1023a(stringBuilder, c0722a, parcel, i);
        } else {
            m1029b(stringBuilder, c0722a, parcel, i);
        }
    }

    private void m1025a(StringBuilder stringBuilder, HashMap<String, C0722a<?, ?>> hashMap, Parcel parcel) {
        HashMap b = m1028b((HashMap) hashMap);
        stringBuilder.append('{');
        int j = C0107a.m92j(parcel);
        Object obj = null;
        while (parcel.dataPosition() < j) {
            int i = C0107a.m90i(parcel);
            Entry entry = (Entry) b.get(Integer.valueOf(C0107a.m107y(i)));
            if (entry != null) {
                if (obj != null) {
                    stringBuilder.append(",");
                }
                m1024a(stringBuilder, (String) entry.getKey(), (C0722a) entry.getValue(), parcel, i);
                obj = 1;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new C0106a("Overread allowed size end=" + j, parcel);
        }
        stringBuilder.append('}');
    }

    private static dz m1026b(dw dwVar) {
        dz dzVar = new dz(dwVar.getClass());
        m1021a(dzVar, dwVar);
        dzVar.bD();
        dzVar.bC();
        return dzVar;
    }

    public static HashMap<String, String> m1027b(Bundle bundle) {
        HashMap<String, String> hashMap = new HashMap();
        for (String str : bundle.keySet()) {
            hashMap.put(str, bundle.getString(str));
        }
        return hashMap;
    }

    private static HashMap<Integer, Entry<String, C0722a<?, ?>>> m1028b(HashMap<String, C0722a<?, ?>> hashMap) {
        HashMap<Integer, Entry<String, C0722a<?, ?>>> hashMap2 = new HashMap();
        for (Entry entry : hashMap.entrySet()) {
            hashMap2.put(Integer.valueOf(((C0722a) entry.getValue()).bw()), entry);
        }
        return hashMap2;
    }

    private void m1029b(StringBuilder stringBuilder, C0722a<?, ?> c0722a, Parcel parcel, int i) {
        if (c0722a.bu()) {
            stringBuilder.append("[");
            switch (c0722a.bo()) {
                case 0:
                    ee.m440a(stringBuilder, C0107a.m99q(parcel, i));
                    break;
                case 1:
                    ee.m442a(stringBuilder, C0107a.m101s(parcel, i));
                    break;
                case 2:
                    ee.m441a(stringBuilder, C0107a.m100r(parcel, i));
                    break;
                case 3:
                    ee.m439a(stringBuilder, C0107a.m102t(parcel, i));
                    break;
                case 4:
                    ee.m438a(stringBuilder, C0107a.m103u(parcel, i));
                    break;
                case 5:
                    ee.m442a(stringBuilder, C0107a.m104v(parcel, i));
                    break;
                case 6:
                    ee.m444a(stringBuilder, C0107a.m98p(parcel, i));
                    break;
                case 7:
                    ee.m443a(stringBuilder, C0107a.m105w(parcel, i));
                    break;
                case 8:
                case 9:
                case 10:
                    throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                case 11:
                    Parcel[] z = C0107a.m109z(parcel, i);
                    int length = z.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        if (i2 > 0) {
                            stringBuilder.append(",");
                        }
                        z[i2].setDataPosition(0);
                        m1025a(stringBuilder, c0722a.bB(), z[i2]);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown field type out.");
            }
            stringBuilder.append("]");
            return;
        }
        switch (c0722a.bo()) {
            case 0:
                stringBuilder.append(C0107a.m86f(parcel, i));
                return;
            case 1:
                stringBuilder.append(C0107a.m88h(parcel, i));
                return;
            case 2:
                stringBuilder.append(C0107a.m87g(parcel, i));
                return;
            case 3:
                stringBuilder.append(C0107a.m89i(parcel, i));
                return;
            case 4:
                stringBuilder.append(C0107a.m91j(parcel, i));
                return;
            case 5:
                stringBuilder.append(C0107a.m93k(parcel, i));
                return;
            case 6:
                stringBuilder.append(C0107a.m83c(parcel, i));
                return;
            case 7:
                stringBuilder.append("\"").append(ei.m449I(C0107a.m94l(parcel, i))).append("\"");
                return;
            case 8:
                stringBuilder.append("\"").append(ef.m445b(C0107a.m97o(parcel, i))).append("\"");
                return;
            case 9:
                stringBuilder.append("\"").append(ef.m446c(C0107a.m97o(parcel, i)));
                stringBuilder.append("\"");
                return;
            case 10:
                Bundle n = C0107a.m96n(parcel, i);
                Set<String> keySet = n.keySet();
                keySet.size();
                stringBuilder.append("{");
                int i3 = 1;
                for (String str : keySet) {
                    if (i3 == 0) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append("\"").append(str).append("\"");
                    stringBuilder.append(":");
                    stringBuilder.append("\"").append(ei.m449I(n.getString(str))).append("\"");
                    i3 = 0;
                }
                stringBuilder.append("}");
                return;
            case 11:
                Parcel y = C0107a.m108y(parcel, i);
                y.setDataPosition(0);
                m1025a(stringBuilder, c0722a.bB(), y);
                return;
            default:
                throw new IllegalStateException("Unknown field type out");
        }
    }

    private void m1030b(StringBuilder stringBuilder, C0722a<?, ?> c0722a, Object obj) {
        if (c0722a.bt()) {
            m1031b(stringBuilder, (C0722a) c0722a, (ArrayList) obj);
        } else {
            m1022a(stringBuilder, c0722a.bn(), obj);
        }
    }

    private void m1031b(StringBuilder stringBuilder, C0722a<?, ?> c0722a, ArrayList<?> arrayList) {
        stringBuilder.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            m1022a(stringBuilder, c0722a.bn(), arrayList.get(i));
        }
        stringBuilder.append("]");
    }

    protected Object mo856D(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    protected boolean mo857E(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    public Parcel bH() {
        switch (this.lR) {
            case 0:
                this.lS = C0108b.m133k(this.lP);
                C0108b.m112C(this.lP, this.lS);
                this.lR = 2;
                break;
            case 1:
                C0108b.m112C(this.lP, this.lS);
                this.lR = 2;
                break;
        }
        return this.lP;
    }

    dz bI() {
        switch (this.lQ) {
            case 0:
                return null;
            case 1:
                return this.lH;
            case 2:
                return this.lH;
            default:
                throw new IllegalStateException("Invalid creation type: " + this.lQ);
        }
    }

    public HashMap<String, C0722a<?, ?>> bp() {
        return this.lH == null ? null : this.lH.m1004H(this.mClassName);
    }

    public int describeContents() {
        ed edVar = CREATOR;
        return 0;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public String toString() {
        dm.m388a(this.lH, (Object) "Cannot convert to JSON on client side.");
        Parcel bH = bH();
        bH.setDataPosition(0);
        StringBuilder stringBuilder = new StringBuilder(100);
        m1025a(stringBuilder, this.lH.m1004H(this.mClassName), bH);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        ed edVar = CREATOR;
        ed.m435a(this, out, flags);
    }
}
