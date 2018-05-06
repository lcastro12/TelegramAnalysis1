package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ap {
    public final List<ao> eU;
    public final long eV;
    public final List<String> eW;
    public final List<String> eX;
    public final List<String> eY;
    public final String eZ;
    public final long fa;

    public ap(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject(str);
        if (cn.m294k(2)) {
            cn.m298p("Mediation Response JSON: " + jSONObject.toString(2));
        }
        JSONArray jSONArray = jSONObject.getJSONArray("ad_networks");
        List arrayList = new ArrayList(jSONArray.length());
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(new ao(jSONArray.getJSONObject(i)));
        }
        this.eU = Collections.unmodifiableList(arrayList);
        this.eZ = jSONObject.getString("qdata");
        JSONObject optJSONObject = jSONObject.optJSONObject("settings");
        if (optJSONObject != null) {
            this.eV = optJSONObject.optLong("ad_network_timeout_millis", -1);
            this.eW = au.m185a(optJSONObject, "click_urls");
            this.eX = au.m185a(optJSONObject, "imp_urls");
            this.eY = au.m185a(optJSONObject, "nofill_urls");
            long optLong = optJSONObject.optLong("refresh", -1);
            this.fa = optLong > 0 ? 1000 * optLong : -1;
            return;
        }
        this.eV = -1;
        this.eW = null;
        this.eX = null;
        this.eY = null;
        this.fa = -1;
    }
}
