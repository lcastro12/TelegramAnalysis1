package com.google.android.gms.internal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import java.util.HashMap;
import java.util.Map;

public final class ah {
    public static final ai eA = new C06742();
    public static final ai eB = new C06753();
    public static final ai eC = new C06764();
    public static final ai eD = new C06775();
    public static final ai eE = new C06786();
    public static final ai eF = new aj();
    public static final ai eG = new C06797();
    public static final ai eH = new ak();
    public static final ai ez = new C06731();

    static class C06731 implements ai {
        C06731() {
        }

        public void mo770a(cq cqVar, Map<String, String> map) {
            String str = (String) map.get("urls");
            if (str == null) {
                cn.m299q("URLs missing in canOpenURLs GMSG.");
                return;
            }
            String[] split = str.split(",");
            Map hashMap = new HashMap();
            PackageManager packageManager = cqVar.getContext().getPackageManager();
            for (String str2 : split) {
                String[] split2 = str2.split(";", 2);
                hashMap.put(str2, Boolean.valueOf(packageManager.resolveActivity(new Intent(split2.length > 1 ? split2[1].trim() : "android.intent.action.VIEW", Uri.parse(split2[0].trim())), 65536) != null));
            }
            cqVar.m306a("openableURLs", hashMap);
        }
    }

    static class C06742 implements ai {
        C06742() {
        }

        public void mo770a(cq cqVar, Map<String, String> map) {
            String str = (String) map.get("u");
            if (str == null) {
                cn.m299q("URL missing from click GMSG.");
                return;
            }
            Uri a;
            Uri parse = Uri.parse(str);
            try {
                C0193h ax = cqVar.ax();
                if (ax != null && ax.m665a(parse)) {
                    a = ax.m663a(parse, cqVar.getContext());
                    new cl(cqVar.getContext(), cqVar.ay().hP, a.toString()).start();
                }
            } catch (C0194i e) {
                cn.m299q("Unable to append parameter to URL: " + str);
            }
            a = parse;
            new cl(cqVar.getContext(), cqVar.ay().hP, a.toString()).start();
        }
    }

    static class C06753 implements ai {
        C06753() {
        }

        public void mo770a(cq cqVar, Map<String, String> map) {
            bf au = cqVar.au();
            if (au == null) {
                cn.m299q("A GMSG tried to close something that wasn't an overlay.");
            } else {
                au.close();
            }
        }
    }

    static class C06764 implements ai {
        C06764() {
        }

        public void mo770a(cq cqVar, Map<String, String> map) {
            bf au = cqVar.au();
            if (au == null) {
                cn.m299q("A GMSG tried to use a custom close button on something that wasn't an overlay.");
            } else {
                au.m1346d("1".equals(map.get("custom_close")));
            }
        }
    }

    static class C06775 implements ai {
        C06775() {
        }

        public void mo770a(cq cqVar, Map<String, String> map) {
            String str = (String) map.get("u");
            if (str == null) {
                cn.m299q("URL missing from httpTrack GMSG.");
            } else {
                new cl(cqVar.getContext(), cqVar.ay().hP, str).start();
            }
        }
    }

    static class C06786 implements ai {
        C06786() {
        }

        public void mo770a(cq cqVar, Map<String, String> map) {
            cn.m297o("Received log message: " + ((String) map.get("string")));
        }
    }

    static class C06797 implements ai {
        C06797() {
        }

        public void mo770a(cq cqVar, Map<String, String> map) {
            String str = (String) map.get("ty");
            String str2 = (String) map.get("td");
            try {
                int parseInt = Integer.parseInt((String) map.get("tx"));
                int parseInt2 = Integer.parseInt(str);
                int parseInt3 = Integer.parseInt(str2);
                C0193h ax = cqVar.ax();
                if (ax != null) {
                    ax.m666g().mo854a(parseInt, parseInt2, parseInt3);
                }
            } catch (NumberFormatException e) {
                cn.m299q("Could not parse touch parameters from gmsg.");
            }
        }
    }
}
