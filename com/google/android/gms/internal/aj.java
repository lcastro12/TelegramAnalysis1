package com.google.android.gms.internal;

import java.util.Map;

public final class aj implements ai {
    private static boolean m854a(Map<String, String> map) {
        return "1".equals(map.get("custom_close"));
    }

    private static int m855b(Map<String, String> map) {
        String str = (String) map.get("o");
        if (str != null) {
            if ("p".equalsIgnoreCase(str)) {
                return ci.ao();
            }
            if ("l".equalsIgnoreCase(str)) {
                return ci.an();
            }
        }
        return -1;
    }

    public void mo770a(cq cqVar, Map<String, String> map) {
        String str = (String) map.get("a");
        if (str == null) {
            cn.m299q("Action missing from an open GMSG.");
            return;
        }
        cr aw = cqVar.aw();
        if ("expand".equalsIgnoreCase(str)) {
            if (cqVar.az()) {
                cn.m299q("Cannot expand WebView that is already expanded.");
            } else {
                aw.m317a(m854a(map), m855b(map));
            }
        } else if ("webapp".equalsIgnoreCase(str)) {
            str = (String) map.get("u");
            if (str != null) {
                aw.m318a(m854a(map), m855b(map), str);
            } else {
                aw.m319a(m854a(map), m855b(map), (String) map.get("html"), (String) map.get("baseurl"));
            }
        } else {
            aw.m313a(new be((String) map.get("i"), (String) map.get("u"), (String) map.get("m"), (String) map.get("p"), (String) map.get("c"), (String) map.get("f"), (String) map.get("e")));
        }
    }
}
