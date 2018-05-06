package com.google.android.gms.internal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class cc {
    private String gX;
    private String gY;
    private List<String> gZ;
    private List<String> ha;
    private long hb = -1;
    private boolean hc = false;
    private final long hd = -1;
    private List<String> he;
    private long hf = -1;
    private int hg = -1;

    private static long m242a(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        if (!(list == null || list.isEmpty())) {
            String str2 = (String) list.get(0);
            try {
                return (long) (Float.parseFloat(str2) * 1000.0f);
            } catch (NumberFormatException e) {
                cn.m299q("Could not parse float from " + str + " header: " + str2);
            }
        }
        return -1;
    }

    private static List<String> m243b(Map<String, List<String>> map, String str) {
        List list = (List) map.get(str);
        if (!(list == null || list.isEmpty())) {
            String str2 = (String) list.get(0);
            if (str2 != null) {
                return Arrays.asList(str2.trim().split("\\s+"));
            }
        }
        return null;
    }

    private void m244e(Map<String, List<String>> map) {
        List b = m243b(map, "X-Afma-Click-Tracking-Urls");
        if (b != null) {
            this.gZ = b;
        }
    }

    private void m245f(Map<String, List<String>> map) {
        List b = m243b(map, "X-Afma-Tracking-Urls");
        if (b != null) {
            this.ha = b;
        }
    }

    private void m246g(Map<String, List<String>> map) {
        long a = m242a(map, "X-Afma-Interstitial-Timeout");
        if (a != -1) {
            this.hb = a;
        }
    }

    private void m247h(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Mediation");
        if (list != null && !list.isEmpty()) {
            this.hc = Boolean.valueOf((String) list.get(0)).booleanValue();
        }
    }

    private void m248i(Map<String, List<String>> map) {
        List b = m243b(map, "X-Afma-Manual-Tracking-Urls");
        if (b != null) {
            this.he = b;
        }
    }

    private void m249j(Map<String, List<String>> map) {
        long a = m242a(map, "X-Afma-Refresh-Rate");
        if (a != -1) {
            this.hf = a;
        }
    }

    private void m250k(Map<String, List<String>> map) {
        List list = (List) map.get("X-Afma-Orientation");
        if (list != null && !list.isEmpty()) {
            String str = (String) list.get(0);
            if ("portrait".equalsIgnoreCase(str)) {
                this.hg = ci.ao();
            } else if ("landscape".equalsIgnoreCase(str)) {
                this.hg = ci.an();
            }
        }
    }

    public void m251a(String str, Map<String, List<String>> map, String str2) {
        this.gX = str;
        this.gY = str2;
        m252d(map);
    }

    public bw ak() {
        return new bw(this.gX, this.gY, this.gZ, this.ha, this.hb, this.hc, -1, this.he, this.hf, this.hg);
    }

    public void m252d(Map<String, List<String>> map) {
        m244e(map);
        m245f(map);
        m246g(map);
        m247h(map);
        m248i(map);
        m249j(map);
        m250k(map);
    }
}
