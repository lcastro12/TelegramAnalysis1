package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class dw {

    public interface C0183b<I, O> {
        int bn();

        int bo();

        I mo851f(O o);
    }

    public static class C0722a<I, O> implements SafeParcelable {
        public static final dx CREATOR = new dx();
        private final int iM;
        protected final boolean lA;
        protected final int lB;
        protected final boolean lC;
        protected final String lD;
        protected final int lE;
        protected final Class<? extends dw> lF;
        protected final String lG;
        private dz lH;
        private C0183b<I, O> lI;
        protected final int lz;

        C0722a(int i, int i2, boolean z, int i3, boolean z2, String str, int i4, String str2, dr drVar) {
            this.iM = i;
            this.lz = i2;
            this.lA = z;
            this.lB = i3;
            this.lC = z2;
            this.lD = str;
            this.lE = i4;
            if (str2 == null) {
                this.lF = null;
                this.lG = null;
            } else {
                this.lF = ec.class;
                this.lG = str2;
            }
            if (drVar == null) {
                this.lI = null;
            } else {
                this.lI = drVar.bl();
            }
        }

        protected C0722a(int i, boolean z, int i2, boolean z2, String str, int i3, Class<? extends dw> cls, C0183b<I, O> c0183b) {
            this.iM = 1;
            this.lz = i;
            this.lA = z;
            this.lB = i2;
            this.lC = z2;
            this.lD = str;
            this.lE = i3;
            this.lF = cls;
            if (cls == null) {
                this.lG = null;
            } else {
                this.lG = cls.getCanonicalName();
            }
            this.lI = c0183b;
        }

        public static C0722a m991a(String str, int i, C0183b<?, ?> c0183b, boolean z) {
            return new C0722a(c0183b.bn(), z, c0183b.bo(), false, str, i, null, c0183b);
        }

        public static <T extends dw> C0722a<T, T> m992a(String str, int i, Class<T> cls) {
            return new C0722a(11, false, 11, false, str, i, cls, null);
        }

        public static <T extends dw> C0722a<ArrayList<T>, ArrayList<T>> m993b(String str, int i, Class<T> cls) {
            return new C0722a(11, true, 11, true, str, i, cls, null);
        }

        public static C0722a<Integer, Integer> m995d(String str, int i) {
            return new C0722a(0, false, 0, false, str, i, null, null);
        }

        public static C0722a<Double, Double> m996e(String str, int i) {
            return new C0722a(4, false, 4, false, str, i, null, null);
        }

        public static C0722a<Boolean, Boolean> m997f(String str, int i) {
            return new C0722a(6, false, 6, false, str, i, null, null);
        }

        public static C0722a<String, String> m998g(String str, int i) {
            return new C0722a(7, false, 7, false, str, i, null, null);
        }

        public static C0722a<ArrayList<String>, ArrayList<String>> m999h(String str, int i) {
            return new C0722a(7, true, 7, true, str, i, null, null);
        }

        public void m1000a(dz dzVar) {
            this.lH = dzVar;
        }

        dr bA() {
            return this.lI == null ? null : dr.m986a(this.lI);
        }

        public HashMap<String, C0722a<?, ?>> bB() {
            dm.m392e(this.lG);
            dm.m392e(this.lH);
            return this.lH.m1004H(this.lG);
        }

        public int bn() {
            return this.lz;
        }

        public int bo() {
            return this.lB;
        }

        public C0722a<I, O> bs() {
            return new C0722a(this.iM, this.lz, this.lA, this.lB, this.lC, this.lD, this.lE, this.lG, bA());
        }

        public boolean bt() {
            return this.lA;
        }

        public boolean bu() {
            return this.lC;
        }

        public String bv() {
            return this.lD;
        }

        public int bw() {
            return this.lE;
        }

        public Class<? extends dw> bx() {
            return this.lF;
        }

        String by() {
            return this.lG == null ? null : this.lG;
        }

        public boolean bz() {
            return this.lI != null;
        }

        public int describeContents() {
            dx dxVar = CREATOR;
            return 0;
        }

        public I m1001f(O o) {
            return this.lI.mo851f(o);
        }

        public int getVersionCode() {
            return this.iM;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Field\n");
            stringBuilder.append("            versionCode=").append(this.iM).append('\n');
            stringBuilder.append("                 typeIn=").append(this.lz).append('\n');
            stringBuilder.append("            typeInArray=").append(this.lA).append('\n');
            stringBuilder.append("                typeOut=").append(this.lB).append('\n');
            stringBuilder.append("           typeOutArray=").append(this.lC).append('\n');
            stringBuilder.append("        outputFieldName=").append(this.lD).append('\n');
            stringBuilder.append("      safeParcelFieldId=").append(this.lE).append('\n');
            stringBuilder.append("       concreteTypeName=").append(by()).append('\n');
            if (bx() != null) {
                stringBuilder.append("     concreteType.class=").append(bx().getCanonicalName()).append('\n');
            }
            stringBuilder.append("          converterName=").append(this.lI == null ? "null" : this.lI.getClass().getCanonicalName()).append('\n');
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel out, int flags) {
            dx dxVar = CREATOR;
            dx.m423a(this, out, flags);
        }
    }

    private void m414a(StringBuilder stringBuilder, C0722a c0722a, Object obj) {
        if (c0722a.bn() == 11) {
            stringBuilder.append(((dw) c0722a.bx().cast(obj)).toString());
        } else if (c0722a.bn() == 7) {
            stringBuilder.append("\"");
            stringBuilder.append(ei.m449I((String) obj));
            stringBuilder.append("\"");
        } else {
            stringBuilder.append(obj);
        }
    }

    private void m415a(StringBuilder stringBuilder, C0722a c0722a, ArrayList<Object> arrayList) {
        stringBuilder.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                m414a(stringBuilder, c0722a, obj);
            }
        }
        stringBuilder.append("]");
    }

    protected abstract Object mo856D(String str);

    protected abstract boolean mo857E(String str);

    protected boolean m418F(String str) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    protected boolean m419G(String str) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }

    protected <O, I> I m420a(C0722a<I, O> c0722a, Object obj) {
        return c0722a.lI != null ? c0722a.m1001f(obj) : obj;
    }

    protected boolean mo1545a(C0722a c0722a) {
        return c0722a.bo() == 11 ? c0722a.bu() ? m419G(c0722a.bv()) : m418F(c0722a.bv()) : mo857E(c0722a.bv());
    }

    protected Object mo1546b(C0722a c0722a) {
        boolean z = true;
        String bv = c0722a.bv();
        if (c0722a.bx() == null) {
            return mo856D(c0722a.bv());
        }
        if (mo856D(c0722a.bv()) != null) {
            z = false;
        }
        dm.m389a(z, "Concrete field shouldn't be value object: " + c0722a.bv());
        Map br = c0722a.bu() ? br() : bq();
        if (br != null) {
            return br.get(bv);
        }
        try {
            return getClass().getMethod("get" + Character.toUpperCase(bv.charAt(0)) + bv.substring(1), new Class[0]).invoke(this, new Object[0]);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public abstract HashMap<String, C0722a<?, ?>> bp();

    public HashMap<String, Object> bq() {
        return null;
    }

    public HashMap<String, Object> br() {
        return null;
    }

    public String toString() {
        HashMap bp = bp();
        StringBuilder stringBuilder = new StringBuilder(100);
        for (String str : bp.keySet()) {
            C0722a c0722a = (C0722a) bp.get(str);
            if (mo1545a(c0722a)) {
                Object a = m420a(c0722a, mo1546b(c0722a));
                if (stringBuilder.length() == 0) {
                    stringBuilder.append("{");
                } else {
                    stringBuilder.append(",");
                }
                stringBuilder.append("\"").append(str).append("\":");
                if (a != null) {
                    switch (c0722a.bo()) {
                        case 8:
                            stringBuilder.append("\"").append(ef.m445b((byte[]) a)).append("\"");
                            break;
                        case 9:
                            stringBuilder.append("\"").append(ef.m446c((byte[]) a)).append("\"");
                            break;
                        case 10:
                            ej.m450a(stringBuilder, (HashMap) a);
                            break;
                        default:
                            if (!c0722a.bt()) {
                                m414a(stringBuilder, c0722a, a);
                                break;
                            }
                            m415a(stringBuilder, c0722a, (ArrayList) a);
                            break;
                    }
                }
                stringBuilder.append("null");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.append("}");
        } else {
            stringBuilder.append("{}");
        }
        return stringBuilder.toString();
    }
}
