package com.google.android.gms.internal;

import android.os.Parcel;
import android.support.v4.util.TimeUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw.C0722a;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.AgeRange;
import com.google.android.gms.plus.model.people.Person.Cover;
import com.google.android.gms.plus.model.people.Person.Cover.CoverInfo;
import com.google.android.gms.plus.model.people.Person.Cover.CoverPhoto;
import com.google.android.gms.plus.model.people.Person.Image;
import com.google.android.gms.plus.model.people.Person.Name;
import com.google.android.gms.plus.model.people.Person.Organizations;
import com.google.android.gms.plus.model.people.Person.PlacesLived;
import com.google.android.gms.plus.model.people.Person.Urls;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.telegram.messenger.MessagesController;

public final class fv extends dw implements SafeParcelable, Person {
    public static final fw CREATOR = new fw();
    private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
    private int dI;
    private String hN;
    private final int iM;
    private String ml;
    private final Set<Integer> rI;
    private String sJ;
    private C0924a sK;
    private String sL;
    private String sM;
    private int sN;
    private C0927b sO;
    private String sP;
    private C0928c sQ;
    private boolean sR;
    private String sS;
    private C0929d sT;
    private String sU;
    private int sV;
    private List<C0930f> sW;
    private List<C0931g> sX;
    private int sY;
    private int sZ;
    private String sm;
    private String ta;
    private List<C0932h> tb;
    private boolean tc;

    public static class C0190e {
        public static int aa(String str) {
            if (str.equals("person")) {
                return 0;
            }
            if (str.equals("page")) {
                return 1;
            }
            throw new IllegalArgumentException("Unknown objectType string: " + str);
        }
    }

    public static final class C0924a extends dw implements SafeParcelable, AgeRange {
        public static final fx CREATOR = new fx();
        private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
        private final int iM;
        private final Set<Integer> rI;
        private int td;
        private int te;

        static {
            rH.put("max", C0722a.m995d("max", 2));
            rH.put("min", C0722a.m995d("min", 3));
        }

        public C0924a() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        C0924a(Set<Integer> set, int i, int i2, int i3) {
            this.rI = set;
            this.iM = i;
            this.td = i2;
            this.te = i3;
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
                    return Integer.valueOf(this.td);
                case 3:
                    return Integer.valueOf(this.te);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
            }
        }

        public HashMap<String, C0722a<?, ?>> bp() {
            return rH;
        }

        public C0924a dL() {
            return this;
        }

        public int describeContents() {
            fx fxVar = CREATOR;
            return 0;
        }

        Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0924a)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0924a c0924a = (C0924a) obj;
            for (C0722a c0722a : rH.values()) {
                if (mo1545a(c0722a)) {
                    if (!c0924a.mo1545a(c0722a)) {
                        return false;
                    }
                    if (!mo1546b(c0722a).equals(c0924a.mo1546b(c0722a))) {
                        return false;
                    }
                } else if (c0924a.mo1545a(c0722a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return dL();
        }

        public int getMax() {
            return this.td;
        }

        public int getMin() {
            return this.te;
        }

        int getVersionCode() {
            return this.iM;
        }

        public boolean hasMax() {
            return this.rI.contains(Integer.valueOf(2));
        }

        public boolean hasMin() {
            return this.rI.contains(Integer.valueOf(3));
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
            fx fxVar = CREATOR;
            fx.m623a(this, out, flags);
        }
    }

    public static final class C0927b extends dw implements SafeParcelable, Cover {
        public static final fy CREATOR = new fy();
        private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
        private final int iM;
        private final Set<Integer> rI;
        private C0925a tf;
        private C0926b tg;
        private int th;

        public static final class C0925a extends dw implements SafeParcelable, CoverInfo {
            public static final fz CREATOR = new fz();
            private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
            private final int iM;
            private final Set<Integer> rI;
            private int ti;
            private int tj;

            static {
                rH.put("leftImageOffset", C0722a.m995d("leftImageOffset", 2));
                rH.put("topImageOffset", C0722a.m995d("topImageOffset", 3));
            }

            public C0925a() {
                this.iM = 1;
                this.rI = new HashSet();
            }

            C0925a(Set<Integer> set, int i, int i2, int i3) {
                this.rI = set;
                this.iM = i;
                this.ti = i2;
                this.tj = i3;
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
                        return Integer.valueOf(this.ti);
                    case 3:
                        return Integer.valueOf(this.tj);
                    default:
                        throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
                }
            }

            public HashMap<String, C0722a<?, ?>> bp() {
                return rH;
            }

            public C0925a dP() {
                return this;
            }

            public int describeContents() {
                fz fzVar = CREATOR;
                return 0;
            }

            Set<Integer> di() {
                return this.rI;
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof C0925a)) {
                    return false;
                }
                if (this == obj) {
                    return true;
                }
                C0925a c0925a = (C0925a) obj;
                for (C0722a c0722a : rH.values()) {
                    if (mo1545a(c0722a)) {
                        if (!c0925a.mo1545a(c0722a)) {
                            return false;
                        }
                        if (!mo1546b(c0722a).equals(c0925a.mo1546b(c0722a))) {
                            return false;
                        }
                    } else if (c0925a.mo1545a(c0722a)) {
                        return false;
                    }
                }
                return true;
            }

            public /* synthetic */ Object freeze() {
                return dP();
            }

            public int getLeftImageOffset() {
                return this.ti;
            }

            public int getTopImageOffset() {
                return this.tj;
            }

            int getVersionCode() {
                return this.iM;
            }

            public boolean hasLeftImageOffset() {
                return this.rI.contains(Integer.valueOf(2));
            }

            public boolean hasTopImageOffset() {
                return this.rI.contains(Integer.valueOf(3));
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
                fz fzVar = CREATOR;
                fz.m627a(this, out, flags);
            }
        }

        public static final class C0926b extends dw implements SafeParcelable, CoverPhoto {
            public static final ga CREATOR = new ga();
            private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
            private int dP;
            private int dQ;
            private String hN;
            private final int iM;
            private final Set<Integer> rI;

            static {
                rH.put("height", C0722a.m995d("height", 2));
                rH.put(PlusShare.KEY_CALL_TO_ACTION_URL, C0722a.m998g(PlusShare.KEY_CALL_TO_ACTION_URL, 3));
                rH.put("width", C0722a.m995d("width", 4));
            }

            public C0926b() {
                this.iM = 1;
                this.rI = new HashSet();
            }

            C0926b(Set<Integer> set, int i, int i2, String str, int i3) {
                this.rI = set;
                this.iM = i;
                this.dQ = i2;
                this.hN = str;
                this.dP = i3;
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
                        return Integer.valueOf(this.dQ);
                    case 3:
                        return this.hN;
                    case 4:
                        return Integer.valueOf(this.dP);
                    default:
                        throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
                }
            }

            public HashMap<String, C0722a<?, ?>> bp() {
                return rH;
            }

            public C0926b dQ() {
                return this;
            }

            public int describeContents() {
                ga gaVar = CREATOR;
                return 0;
            }

            Set<Integer> di() {
                return this.rI;
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof C0926b)) {
                    return false;
                }
                if (this == obj) {
                    return true;
                }
                C0926b c0926b = (C0926b) obj;
                for (C0722a c0722a : rH.values()) {
                    if (mo1545a(c0722a)) {
                        if (!c0926b.mo1545a(c0722a)) {
                            return false;
                        }
                        if (!mo1546b(c0722a).equals(c0926b.mo1546b(c0722a))) {
                            return false;
                        }
                    } else if (c0926b.mo1545a(c0722a)) {
                        return false;
                    }
                }
                return true;
            }

            public /* synthetic */ Object freeze() {
                return dQ();
            }

            public int getHeight() {
                return this.dQ;
            }

            public String getUrl() {
                return this.hN;
            }

            int getVersionCode() {
                return this.iM;
            }

            public int getWidth() {
                return this.dP;
            }

            public boolean hasHeight() {
                return this.rI.contains(Integer.valueOf(2));
            }

            public boolean hasUrl() {
                return this.rI.contains(Integer.valueOf(3));
            }

            public boolean hasWidth() {
                return this.rI.contains(Integer.valueOf(4));
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
                ga gaVar = CREATOR;
                ga.m629a(this, out, flags);
            }
        }

        static {
            rH.put("coverInfo", C0722a.m992a("coverInfo", 2, C0925a.class));
            rH.put("coverPhoto", C0722a.m992a("coverPhoto", 3, C0926b.class));
            rH.put("layout", C0722a.m991a("layout", 4, new dt().m989c("banner", 0), false));
        }

        public C0927b() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        C0927b(Set<Integer> set, int i, C0925a c0925a, C0926b c0926b, int i2) {
            this.rI = set;
            this.iM = i;
            this.tf = c0925a;
            this.tg = c0926b;
            this.th = i2;
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
                    return this.tf;
                case 3:
                    return this.tg;
                case 4:
                    return Integer.valueOf(this.th);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
            }
        }

        public HashMap<String, C0722a<?, ?>> bp() {
            return rH;
        }

        C0925a dM() {
            return this.tf;
        }

        C0926b dN() {
            return this.tg;
        }

        public C0927b dO() {
            return this;
        }

        public int describeContents() {
            fy fyVar = CREATOR;
            return 0;
        }

        Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0927b)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0927b c0927b = (C0927b) obj;
            for (C0722a c0722a : rH.values()) {
                if (mo1545a(c0722a)) {
                    if (!c0927b.mo1545a(c0722a)) {
                        return false;
                    }
                    if (!mo1546b(c0722a).equals(c0927b.mo1546b(c0722a))) {
                        return false;
                    }
                } else if (c0927b.mo1545a(c0722a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return dO();
        }

        public CoverInfo getCoverInfo() {
            return this.tf;
        }

        public CoverPhoto getCoverPhoto() {
            return this.tg;
        }

        public int getLayout() {
            return this.th;
        }

        int getVersionCode() {
            return this.iM;
        }

        public boolean hasCoverInfo() {
            return this.rI.contains(Integer.valueOf(2));
        }

        public boolean hasCoverPhoto() {
            return this.rI.contains(Integer.valueOf(3));
        }

        public boolean hasLayout() {
            return this.rI.contains(Integer.valueOf(4));
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
            fy fyVar = CREATOR;
            fy.m625a(this, out, flags);
        }
    }

    public static final class C0928c extends dw implements SafeParcelable, Image {
        public static final gb CREATOR = new gb();
        private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
        private String hN;
        private final int iM;
        private final Set<Integer> rI;

        static {
            rH.put(PlusShare.KEY_CALL_TO_ACTION_URL, C0722a.m998g(PlusShare.KEY_CALL_TO_ACTION_URL, 2));
        }

        public C0928c() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        public C0928c(String str) {
            this.rI = new HashSet();
            this.iM = 1;
            this.hN = str;
            this.rI.add(Integer.valueOf(2));
        }

        C0928c(Set<Integer> set, int i, String str) {
            this.rI = set;
            this.iM = i;
            this.hN = str;
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
                    return this.hN;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
            }
        }

        public HashMap<String, C0722a<?, ?>> bp() {
            return rH;
        }

        public C0928c dR() {
            return this;
        }

        public int describeContents() {
            gb gbVar = CREATOR;
            return 0;
        }

        Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0928c)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0928c c0928c = (C0928c) obj;
            for (C0722a c0722a : rH.values()) {
                if (mo1545a(c0722a)) {
                    if (!c0928c.mo1545a(c0722a)) {
                        return false;
                    }
                    if (!mo1546b(c0722a).equals(c0928c.mo1546b(c0722a))) {
                        return false;
                    }
                } else if (c0928c.mo1545a(c0722a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return dR();
        }

        public String getUrl() {
            return this.hN;
        }

        int getVersionCode() {
            return this.iM;
        }

        public boolean hasUrl() {
            return this.rI.contains(Integer.valueOf(2));
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
            gb gbVar = CREATOR;
            gb.m631a(this, out, flags);
        }
    }

    public static final class C0929d extends dw implements SafeParcelable, Name {
        public static final gc CREATOR = new gc();
        private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
        private final int iM;
        private final Set<Integer> rI;
        private String sh;
        private String sk;
        private String tk;
        private String tl;
        private String tm;
        private String tn;

        static {
            rH.put("familyName", C0722a.m998g("familyName", 2));
            rH.put("formatted", C0722a.m998g("formatted", 3));
            rH.put("givenName", C0722a.m998g("givenName", 4));
            rH.put("honorificPrefix", C0722a.m998g("honorificPrefix", 5));
            rH.put("honorificSuffix", C0722a.m998g("honorificSuffix", 6));
            rH.put("middleName", C0722a.m998g("middleName", 7));
        }

        public C0929d() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        C0929d(Set<Integer> set, int i, String str, String str2, String str3, String str4, String str5, String str6) {
            this.rI = set;
            this.iM = i;
            this.sh = str;
            this.tk = str2;
            this.sk = str3;
            this.tl = str4;
            this.tm = str5;
            this.tn = str6;
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
                    return this.sh;
                case 3:
                    return this.tk;
                case 4:
                    return this.sk;
                case 5:
                    return this.tl;
                case 6:
                    return this.tm;
                case 7:
                    return this.tn;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
            }
        }

        public HashMap<String, C0722a<?, ?>> bp() {
            return rH;
        }

        public C0929d dS() {
            return this;
        }

        public int describeContents() {
            gc gcVar = CREATOR;
            return 0;
        }

        Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0929d)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0929d c0929d = (C0929d) obj;
            for (C0722a c0722a : rH.values()) {
                if (mo1545a(c0722a)) {
                    if (!c0929d.mo1545a(c0722a)) {
                        return false;
                    }
                    if (!mo1546b(c0722a).equals(c0929d.mo1546b(c0722a))) {
                        return false;
                    }
                } else if (c0929d.mo1545a(c0722a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return dS();
        }

        public String getFamilyName() {
            return this.sh;
        }

        public String getFormatted() {
            return this.tk;
        }

        public String getGivenName() {
            return this.sk;
        }

        public String getHonorificPrefix() {
            return this.tl;
        }

        public String getHonorificSuffix() {
            return this.tm;
        }

        public String getMiddleName() {
            return this.tn;
        }

        int getVersionCode() {
            return this.iM;
        }

        public boolean hasFamilyName() {
            return this.rI.contains(Integer.valueOf(2));
        }

        public boolean hasFormatted() {
            return this.rI.contains(Integer.valueOf(3));
        }

        public boolean hasGivenName() {
            return this.rI.contains(Integer.valueOf(4));
        }

        public boolean hasHonorificPrefix() {
            return this.rI.contains(Integer.valueOf(5));
        }

        public boolean hasHonorificSuffix() {
            return this.rI.contains(Integer.valueOf(6));
        }

        public boolean hasMiddleName() {
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
            gc gcVar = CREATOR;
            gc.m633a(this, out, flags);
        }
    }

    public static final class C0930f extends dw implements SafeParcelable, Organizations {
        public static final gd CREATOR = new gd();
        private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
        private final int iM;
        private int jV;
        private String mName;
        private String mo;
        private String qB;
        private final Set<Integer> rI;
        private String sg;
        private String sx;
        private String to;
        private String tp;
        private boolean tq;

        static {
            rH.put("department", C0722a.m998g("department", 2));
            rH.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, C0722a.m998g(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, 3));
            rH.put("endDate", C0722a.m998g("endDate", 4));
            rH.put("location", C0722a.m998g("location", 5));
            rH.put("name", C0722a.m998g("name", 6));
            rH.put("primary", C0722a.m997f("primary", 7));
            rH.put("startDate", C0722a.m998g("startDate", 8));
            rH.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, C0722a.m998g(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, 9));
            rH.put("type", C0722a.m991a("type", 10, new dt().m989c("work", 0).m989c("school", 1), false));
        }

        public C0930f() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        C0930f(Set<Integer> set, int i, String str, String str2, String str3, String str4, String str5, boolean z, String str6, String str7, int i2) {
            this.rI = set;
            this.iM = i;
            this.to = str;
            this.mo = str2;
            this.sg = str3;
            this.tp = str4;
            this.mName = str5;
            this.tq = z;
            this.sx = str6;
            this.qB = str7;
            this.jV = i2;
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
                    return this.to;
                case 3:
                    return this.mo;
                case 4:
                    return this.sg;
                case 5:
                    return this.tp;
                case 6:
                    return this.mName;
                case 7:
                    return Boolean.valueOf(this.tq);
                case 8:
                    return this.sx;
                case 9:
                    return this.qB;
                case 10:
                    return Integer.valueOf(this.jV);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
            }
        }

        public HashMap<String, C0722a<?, ?>> bp() {
            return rH;
        }

        public C0930f dT() {
            return this;
        }

        public int describeContents() {
            gd gdVar = CREATOR;
            return 0;
        }

        Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0930f)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0930f c0930f = (C0930f) obj;
            for (C0722a c0722a : rH.values()) {
                if (mo1545a(c0722a)) {
                    if (!c0930f.mo1545a(c0722a)) {
                        return false;
                    }
                    if (!mo1546b(c0722a).equals(c0930f.mo1546b(c0722a))) {
                        return false;
                    }
                } else if (c0930f.mo1545a(c0722a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return dT();
        }

        public String getDepartment() {
            return this.to;
        }

        public String getDescription() {
            return this.mo;
        }

        public String getEndDate() {
            return this.sg;
        }

        public String getLocation() {
            return this.tp;
        }

        public String getName() {
            return this.mName;
        }

        public String getStartDate() {
            return this.sx;
        }

        public String getTitle() {
            return this.qB;
        }

        public int getType() {
            return this.jV;
        }

        int getVersionCode() {
            return this.iM;
        }

        public boolean hasDepartment() {
            return this.rI.contains(Integer.valueOf(2));
        }

        public boolean hasDescription() {
            return this.rI.contains(Integer.valueOf(3));
        }

        public boolean hasEndDate() {
            return this.rI.contains(Integer.valueOf(4));
        }

        public boolean hasLocation() {
            return this.rI.contains(Integer.valueOf(5));
        }

        public boolean hasName() {
            return this.rI.contains(Integer.valueOf(6));
        }

        public boolean hasPrimary() {
            return this.rI.contains(Integer.valueOf(7));
        }

        public boolean hasStartDate() {
            return this.rI.contains(Integer.valueOf(8));
        }

        public boolean hasTitle() {
            return this.rI.contains(Integer.valueOf(9));
        }

        public boolean hasType() {
            return this.rI.contains(Integer.valueOf(10));
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

        public boolean isPrimary() {
            return this.tq;
        }

        public void writeToParcel(Parcel out, int flags) {
            gd gdVar = CREATOR;
            gd.m635a(this, out, flags);
        }
    }

    public static final class C0931g extends dw implements SafeParcelable, PlacesLived {
        public static final ge CREATOR = new ge();
        private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
        private final int iM;
        private String mValue;
        private final Set<Integer> rI;
        private boolean tq;

        static {
            rH.put("primary", C0722a.m997f("primary", 2));
            rH.put("value", C0722a.m998g("value", 3));
        }

        public C0931g() {
            this.iM = 1;
            this.rI = new HashSet();
        }

        C0931g(Set<Integer> set, int i, boolean z, String str) {
            this.rI = set;
            this.iM = i;
            this.tq = z;
            this.mValue = str;
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
                    return Boolean.valueOf(this.tq);
                case 3:
                    return this.mValue;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
            }
        }

        public HashMap<String, C0722a<?, ?>> bp() {
            return rH;
        }

        public C0931g dU() {
            return this;
        }

        public int describeContents() {
            ge geVar = CREATOR;
            return 0;
        }

        Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0931g)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0931g c0931g = (C0931g) obj;
            for (C0722a c0722a : rH.values()) {
                if (mo1545a(c0722a)) {
                    if (!c0931g.mo1545a(c0722a)) {
                        return false;
                    }
                    if (!mo1546b(c0722a).equals(c0931g.mo1546b(c0722a))) {
                        return false;
                    }
                } else if (c0931g.mo1545a(c0722a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return dU();
        }

        public String getValue() {
            return this.mValue;
        }

        int getVersionCode() {
            return this.iM;
        }

        public boolean hasPrimary() {
            return this.rI.contains(Integer.valueOf(2));
        }

        public boolean hasValue() {
            return this.rI.contains(Integer.valueOf(3));
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

        public boolean isPrimary() {
            return this.tq;
        }

        public void writeToParcel(Parcel out, int flags) {
            ge geVar = CREATOR;
            ge.m637a(this, out, flags);
        }
    }

    public static final class C0932h extends dw implements SafeParcelable, Urls {
        public static final gf CREATOR = new gf();
        private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
        private final int iM;
        private int jV;
        private String mValue;
        private final Set<Integer> rI;
        private String tr;
        private final int ts;

        static {
            rH.put(PlusShare.KEY_CALL_TO_ACTION_LABEL, C0722a.m998g(PlusShare.KEY_CALL_TO_ACTION_LABEL, 5));
            rH.put("type", C0722a.m991a("type", 6, new dt().m989c("home", 0).m989c("work", 1).m989c("blog", 2).m989c("profile", 3).m989c("other", 4).m989c("otherProfile", 5).m989c("contributor", 6).m989c("website", 7), false));
            rH.put("value", C0722a.m998g("value", 4));
        }

        public C0932h() {
            this.ts = 4;
            this.iM = 2;
            this.rI = new HashSet();
        }

        C0932h(Set<Integer> set, int i, String str, int i2, String str2, int i3) {
            this.ts = 4;
            this.rI = set;
            this.iM = i;
            this.tr = str;
            this.jV = i2;
            this.mValue = str2;
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
                case 4:
                    return this.mValue;
                case 5:
                    return this.tr;
                case 6:
                    return Integer.valueOf(this.jV);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
            }
        }

        public HashMap<String, C0722a<?, ?>> bp() {
            return rH;
        }

        @Deprecated
        public int dV() {
            return 4;
        }

        public C0932h dW() {
            return this;
        }

        public int describeContents() {
            gf gfVar = CREATOR;
            return 0;
        }

        Set<Integer> di() {
            return this.rI;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof C0932h)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            C0932h c0932h = (C0932h) obj;
            for (C0722a c0722a : rH.values()) {
                if (mo1545a(c0722a)) {
                    if (!c0932h.mo1545a(c0722a)) {
                        return false;
                    }
                    if (!mo1546b(c0722a).equals(c0932h.mo1546b(c0722a))) {
                        return false;
                    }
                } else if (c0932h.mo1545a(c0722a)) {
                    return false;
                }
            }
            return true;
        }

        public /* synthetic */ Object freeze() {
            return dW();
        }

        public String getLabel() {
            return this.tr;
        }

        public int getType() {
            return this.jV;
        }

        public String getValue() {
            return this.mValue;
        }

        int getVersionCode() {
            return this.iM;
        }

        public boolean hasLabel() {
            return this.rI.contains(Integer.valueOf(5));
        }

        public boolean hasType() {
            return this.rI.contains(Integer.valueOf(6));
        }

        public boolean hasValue() {
            return this.rI.contains(Integer.valueOf(4));
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
            gf gfVar = CREATOR;
            gf.m639a(this, out, flags);
        }
    }

    static {
        rH.put("aboutMe", C0722a.m998g("aboutMe", 2));
        rH.put("ageRange", C0722a.m992a("ageRange", 3, C0924a.class));
        rH.put("birthday", C0722a.m998g("birthday", 4));
        rH.put("braggingRights", C0722a.m998g("braggingRights", 5));
        rH.put("circledByCount", C0722a.m995d("circledByCount", 6));
        rH.put("cover", C0722a.m992a("cover", 7, C0927b.class));
        rH.put("currentLocation", C0722a.m998g("currentLocation", 8));
        rH.put("displayName", C0722a.m998g("displayName", 9));
        rH.put("gender", C0722a.m991a("gender", 12, new dt().m989c("male", 0).m989c("female", 1).m989c("other", 2), false));
        rH.put("id", C0722a.m998g("id", 14));
        rH.put("image", C0722a.m992a("image", 15, C0928c.class));
        rH.put("isPlusUser", C0722a.m997f("isPlusUser", 16));
        rH.put("language", C0722a.m998g("language", 18));
        rH.put("name", C0722a.m992a("name", 19, C0929d.class));
        rH.put("nickname", C0722a.m998g("nickname", 20));
        rH.put("objectType", C0722a.m991a("objectType", 21, new dt().m989c("person", 0).m989c("page", 1), false));
        rH.put("organizations", C0722a.m993b("organizations", 22, C0930f.class));
        rH.put("placesLived", C0722a.m993b("placesLived", 23, C0931g.class));
        rH.put("plusOneCount", C0722a.m995d("plusOneCount", 24));
        rH.put("relationshipStatus", C0722a.m991a("relationshipStatus", 25, new dt().m989c("single", 0).m989c("in_a_relationship", 1).m989c("engaged", 2).m989c("married", 3).m989c("its_complicated", 4).m989c("open_relationship", 5).m989c("widowed", 6).m989c("in_domestic_partnership", 7).m989c("in_civil_union", 8), false));
        rH.put("tagline", C0722a.m998g("tagline", 26));
        rH.put(PlusShare.KEY_CALL_TO_ACTION_URL, C0722a.m998g(PlusShare.KEY_CALL_TO_ACTION_URL, 27));
        rH.put("urls", C0722a.m993b("urls", 28, C0932h.class));
        rH.put("verified", C0722a.m997f("verified", 29));
    }

    public fv() {
        this.iM = 2;
        this.rI = new HashSet();
    }

    public fv(String str, String str2, C0928c c0928c, int i, String str3) {
        this.iM = 2;
        this.rI = new HashSet();
        this.ml = str;
        this.rI.add(Integer.valueOf(9));
        this.sm = str2;
        this.rI.add(Integer.valueOf(14));
        this.sQ = c0928c;
        this.rI.add(Integer.valueOf(15));
        this.sV = i;
        this.rI.add(Integer.valueOf(21));
        this.hN = str3;
        this.rI.add(Integer.valueOf(27));
    }

    fv(Set<Integer> set, int i, String str, C0924a c0924a, String str2, String str3, int i2, C0927b c0927b, String str4, String str5, int i3, String str6, C0928c c0928c, boolean z, String str7, C0929d c0929d, String str8, int i4, List<C0930f> list, List<C0931g> list2, int i5, int i6, String str9, String str10, List<C0932h> list3, boolean z2) {
        this.rI = set;
        this.iM = i;
        this.sJ = str;
        this.sK = c0924a;
        this.sL = str2;
        this.sM = str3;
        this.sN = i2;
        this.sO = c0927b;
        this.sP = str4;
        this.ml = str5;
        this.dI = i3;
        this.sm = str6;
        this.sQ = c0928c;
        this.sR = z;
        this.sS = str7;
        this.sT = c0929d;
        this.sU = str8;
        this.sV = i4;
        this.sW = list;
        this.sX = list2;
        this.sY = i5;
        this.sZ = i6;
        this.ta = str9;
        this.hN = str10;
        this.tb = list3;
        this.tc = z2;
    }

    public static fv m1525e(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        fv D = CREATOR.m622D(obtain);
        obtain.recycle();
        return D;
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
                return this.sJ;
            case 3:
                return this.sK;
            case 4:
                return this.sL;
            case 5:
                return this.sM;
            case 6:
                return Integer.valueOf(this.sN);
            case 7:
                return this.sO;
            case 8:
                return this.sP;
            case 9:
                return this.ml;
            case 12:
                return Integer.valueOf(this.dI);
            case 14:
                return this.sm;
            case 15:
                return this.sQ;
            case 16:
                return Boolean.valueOf(this.sR);
            case 18:
                return this.sS;
            case TimeUtils.HUNDRED_DAY_FIELD_LEN /*19*/:
                return this.sT;
            case MessagesController.mediaCountDidLoaded /*20*/:
                return this.sU;
            case MessagesController.encryptedChatUpdated /*21*/:
                return Integer.valueOf(this.sV);
            case MessagesController.messagesReadedEncrypted /*22*/:
                return this.sW;
            case MessagesController.encryptedChatCreated /*23*/:
                return this.sX;
            case MessagesController.userPhotosLoaded /*24*/:
                return Integer.valueOf(this.sY);
            case 25:
                return Integer.valueOf(this.sZ);
            case 26:
                return this.ta;
            case 27:
                return this.hN;
            case 28:
                return this.tb;
            case 29:
                return Boolean.valueOf(this.tc);
            default:
                throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
        }
    }

    public HashMap<String, C0722a<?, ?>> bp() {
        return rH;
    }

    C0924a dD() {
        return this.sK;
    }

    C0927b dE() {
        return this.sO;
    }

    C0928c dF() {
        return this.sQ;
    }

    C0929d dG() {
        return this.sT;
    }

    List<C0930f> dH() {
        return this.sW;
    }

    List<C0931g> dI() {
        return this.sX;
    }

    List<C0932h> dJ() {
        return this.tb;
    }

    public fv dK() {
        return this;
    }

    public int describeContents() {
        fw fwVar = CREATOR;
        return 0;
    }

    Set<Integer> di() {
        return this.rI;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof fv)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        fv fvVar = (fv) obj;
        for (C0722a c0722a : rH.values()) {
            if (mo1545a(c0722a)) {
                if (!fvVar.mo1545a(c0722a)) {
                    return false;
                }
                if (!mo1546b(c0722a).equals(fvVar.mo1546b(c0722a))) {
                    return false;
                }
            } else if (fvVar.mo1545a(c0722a)) {
                return false;
            }
        }
        return true;
    }

    public /* synthetic */ Object freeze() {
        return dK();
    }

    public String getAboutMe() {
        return this.sJ;
    }

    public AgeRange getAgeRange() {
        return this.sK;
    }

    public String getBirthday() {
        return this.sL;
    }

    public String getBraggingRights() {
        return this.sM;
    }

    public int getCircledByCount() {
        return this.sN;
    }

    public Cover getCover() {
        return this.sO;
    }

    public String getCurrentLocation() {
        return this.sP;
    }

    public String getDisplayName() {
        return this.ml;
    }

    public int getGender() {
        return this.dI;
    }

    public String getId() {
        return this.sm;
    }

    public Image getImage() {
        return this.sQ;
    }

    public String getLanguage() {
        return this.sS;
    }

    public Name getName() {
        return this.sT;
    }

    public String getNickname() {
        return this.sU;
    }

    public int getObjectType() {
        return this.sV;
    }

    public List<Organizations> getOrganizations() {
        return (ArrayList) this.sW;
    }

    public List<PlacesLived> getPlacesLived() {
        return (ArrayList) this.sX;
    }

    public int getPlusOneCount() {
        return this.sY;
    }

    public int getRelationshipStatus() {
        return this.sZ;
    }

    public String getTagline() {
        return this.ta;
    }

    public String getUrl() {
        return this.hN;
    }

    public List<Urls> getUrls() {
        return (ArrayList) this.tb;
    }

    int getVersionCode() {
        return this.iM;
    }

    public boolean hasAboutMe() {
        return this.rI.contains(Integer.valueOf(2));
    }

    public boolean hasAgeRange() {
        return this.rI.contains(Integer.valueOf(3));
    }

    public boolean hasBirthday() {
        return this.rI.contains(Integer.valueOf(4));
    }

    public boolean hasBraggingRights() {
        return this.rI.contains(Integer.valueOf(5));
    }

    public boolean hasCircledByCount() {
        return this.rI.contains(Integer.valueOf(6));
    }

    public boolean hasCover() {
        return this.rI.contains(Integer.valueOf(7));
    }

    public boolean hasCurrentLocation() {
        return this.rI.contains(Integer.valueOf(8));
    }

    public boolean hasDisplayName() {
        return this.rI.contains(Integer.valueOf(9));
    }

    public boolean hasGender() {
        return this.rI.contains(Integer.valueOf(12));
    }

    public boolean hasId() {
        return this.rI.contains(Integer.valueOf(14));
    }

    public boolean hasImage() {
        return this.rI.contains(Integer.valueOf(15));
    }

    public boolean hasIsPlusUser() {
        return this.rI.contains(Integer.valueOf(16));
    }

    public boolean hasLanguage() {
        return this.rI.contains(Integer.valueOf(18));
    }

    public boolean hasName() {
        return this.rI.contains(Integer.valueOf(19));
    }

    public boolean hasNickname() {
        return this.rI.contains(Integer.valueOf(20));
    }

    public boolean hasObjectType() {
        return this.rI.contains(Integer.valueOf(21));
    }

    public boolean hasOrganizations() {
        return this.rI.contains(Integer.valueOf(22));
    }

    public boolean hasPlacesLived() {
        return this.rI.contains(Integer.valueOf(23));
    }

    public boolean hasPlusOneCount() {
        return this.rI.contains(Integer.valueOf(24));
    }

    public boolean hasRelationshipStatus() {
        return this.rI.contains(Integer.valueOf(25));
    }

    public boolean hasTagline() {
        return this.rI.contains(Integer.valueOf(26));
    }

    public boolean hasUrl() {
        return this.rI.contains(Integer.valueOf(27));
    }

    public boolean hasUrls() {
        return this.rI.contains(Integer.valueOf(28));
    }

    public boolean hasVerified() {
        return this.rI.contains(Integer.valueOf(29));
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

    public boolean isPlusUser() {
        return this.sR;
    }

    public boolean isVerified() {
        return this.tc;
    }

    public void writeToParcel(Parcel out, int flags) {
        fw fwVar = CREATOR;
        fw.m621a(this, out, flags);
    }
}
