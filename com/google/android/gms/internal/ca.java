package com.google.android.gms.internal;

import android.location.Location;
import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONException;

public final class ca {
    private static final SimpleDateFormat gS = new SimpleDateFormat("yyyyMMdd");

    public static String m233a(bu buVar, cd cdVar, Location location) {
        try {
            HashMap hashMap = new HashMap();
            if (buVar.gA != null) {
                hashMap.put("ad_pos", buVar.gA);
            }
            m236a(hashMap, buVar.gB);
            hashMap.put("format", buVar.ed.ew);
            if (buVar.ed.width == -1) {
                hashMap.put("smart_w", "full");
            }
            if (buVar.ed.height == -2) {
                hashMap.put("smart_h", "auto");
            }
            hashMap.put("slotname", buVar.adUnitId);
            hashMap.put("pn", buVar.applicationInfo.packageName);
            if (buVar.gC != null) {
                hashMap.put("vc", Integer.valueOf(buVar.gC.versionCode));
            }
            hashMap.put("ms", buVar.gD);
            hashMap.put("seq_num", buVar.gE);
            hashMap.put("session_id", buVar.gF);
            hashMap.put("js", buVar.eg.hP);
            m235a(hashMap, cdVar);
            m234a(hashMap, location);
            if (cn.m294k(2)) {
                cn.m298p("Ad Request JSON: " + ci.m277l(hashMap).toString(2));
            }
            return ci.m277l(hashMap).toString();
        } catch (JSONException e) {
            cn.m299q("Problem serializing ad request to JSON: " + e.getMessage());
            return null;
        }
    }

    private static void m234a(HashMap<String, Object> hashMap, Location location) {
        if (location != null) {
            HashMap hashMap2 = new HashMap();
            Float valueOf = Float.valueOf(location.getAccuracy() * 1000.0f);
            Long valueOf2 = Long.valueOf(location.getTime() * 1000);
            Long valueOf3 = Long.valueOf((long) (location.getLatitude() * 1.0E7d));
            Long valueOf4 = Long.valueOf((long) (location.getLongitude() * 1.0E7d));
            hashMap2.put("radius", valueOf);
            hashMap2.put("lat", valueOf3);
            hashMap2.put("long", valueOf4);
            hashMap2.put("time", valueOf2);
            hashMap.put("loc", hashMap2);
        }
    }

    private static void m235a(HashMap<String, Object> hashMap, cd cdVar) {
        hashMap.put("am", Integer.valueOf(cdVar.hh));
        hashMap.put("cog", m237g(cdVar.hi));
        hashMap.put("coh", m237g(cdVar.hj));
        if (!TextUtils.isEmpty(cdVar.hk)) {
            hashMap.put("carrier", cdVar.hk);
        }
        hashMap.put("gl", cdVar.hl);
        if (cdVar.hm) {
            hashMap.put("simulator", Integer.valueOf(1));
        }
        hashMap.put("ma", m237g(cdVar.hn));
        hashMap.put("sp", m237g(cdVar.ho));
        hashMap.put("hl", cdVar.hp);
        if (!TextUtils.isEmpty(cdVar.hq)) {
            hashMap.put("mv", cdVar.hq);
        }
        hashMap.put("muv", Integer.valueOf(cdVar.hr));
        if (cdVar.hs != -2) {
            hashMap.put("cnt", Integer.valueOf(cdVar.hs));
        }
        hashMap.put("gnt", Integer.valueOf(cdVar.ht));
        hashMap.put("pt", Integer.valueOf(cdVar.hu));
        hashMap.put("rm", Integer.valueOf(cdVar.hv));
        hashMap.put("riv", Integer.valueOf(cdVar.hw));
        hashMap.put("u_sd", Float.valueOf(cdVar.hx));
        hashMap.put("sh", Integer.valueOf(cdVar.hz));
        hashMap.put("sw", Integer.valueOf(cdVar.hy));
    }

    private static void m236a(HashMap<String, Object> hashMap, C0770v c0770v) {
        if (c0770v.es != -1) {
            hashMap.put("cust_age", gS.format(new Date(c0770v.es)));
        }
        if (c0770v.extras != null) {
            hashMap.put("extras", c0770v.extras);
        }
        if (c0770v.et != -1) {
            hashMap.put("cust_gender", Integer.valueOf(c0770v.et));
        }
        if (c0770v.eu != null) {
            hashMap.put("kw", c0770v.eu);
        }
        if (c0770v.tagForChildDirectedTreatment != -1) {
            hashMap.put("tag_for_child_directed_treatment", Integer.valueOf(c0770v.tagForChildDirectedTreatment));
        }
        if (c0770v.ev) {
            hashMap.put("adtest", "on");
        }
    }

    private static Integer m237g(boolean z) {
        return Integer.valueOf(z ? 1 : 0);
    }
}
