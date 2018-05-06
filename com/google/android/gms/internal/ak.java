package com.google.android.gms.internal;

import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import java.util.Map;

public final class ak implements ai {
    private static int m857a(DisplayMetrics displayMetrics, Map<String, String> map, String str, int i) {
        String str2 = (String) map.get(str);
        if (str2 != null) {
            try {
                i = cm.m286a(displayMetrics, Integer.parseInt(str2));
            } catch (NumberFormatException e) {
                cn.m299q("Could not parse " + str + " in a video GMSG: " + str2);
            }
        }
        return i;
    }

    public void mo770a(cq cqVar, Map<String, String> map) {
        String str = (String) map.get("action");
        if (str == null) {
            cn.m299q("Action missing from video GMSG.");
            return;
        }
        bf au = cqVar.au();
        if (au == null) {
            cn.m299q("Could not get ad overlay for a video GMSG.");
            return;
        }
        boolean equalsIgnoreCase = "new".equalsIgnoreCase(str);
        boolean equalsIgnoreCase2 = "position".equalsIgnoreCase(str);
        int a;
        if (equalsIgnoreCase || equalsIgnoreCase2) {
            DisplayMetrics displayMetrics = cqVar.getContext().getResources().getDisplayMetrics();
            a = m857a(displayMetrics, map, "x", 0);
            int a2 = m857a(displayMetrics, map, "y", 0);
            int a3 = m857a(displayMetrics, map, "w", -1);
            int a4 = m857a(displayMetrics, map, "h", -1);
            if (equalsIgnoreCase && au.m1339Q() == null) {
                au.m1344c(a, a2, a3, a4);
                return;
            } else {
                au.m1343b(a, a2, a3, a4);
                return;
            }
        }
        bj Q = au.m1339Q();
        if (Q == null) {
            bj.m210a(cqVar, "no_video_view", null);
        } else if ("click".equalsIgnoreCase(str)) {
            displayMetrics = cqVar.getContext().getResources().getDisplayMetrics();
            int a5 = m857a(displayMetrics, map, "x", 0);
            a = m857a(displayMetrics, map, "y", 0);
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, (float) a5, (float) a, 0);
            Q.m214b(obtain);
            obtain.recycle();
        } else if ("controls".equalsIgnoreCase(str)) {
            str = (String) map.get("enabled");
            if (str == null) {
                cn.m299q("Enabled parameter missing from controls video GMSG.");
            } else {
                Q.m215f(Boolean.parseBoolean(str));
            }
        } else if ("currentTime".equalsIgnoreCase(str)) {
            str = (String) map.get("time");
            if (str == null) {
                cn.m299q("Time parameter missing from currentTime video GMSG.");
                return;
            }
            try {
                Q.seekTo((int) (Float.parseFloat(str) * 1000.0f));
            } catch (NumberFormatException e) {
                cn.m299q("Could not parse time parameter from currentTime video GMSG: " + str);
            }
        } else if ("hide".equalsIgnoreCase(str)) {
            Q.setVisibility(4);
        } else if ("load".equalsIgnoreCase(str)) {
            Q.m213Z();
        } else if ("pause".equalsIgnoreCase(str)) {
            Q.pause();
        } else if ("play".equalsIgnoreCase(str)) {
            Q.play();
        } else if ("show".equalsIgnoreCase(str)) {
            Q.setVisibility(0);
        } else if ("src".equalsIgnoreCase(str)) {
            Q.m216i((String) map.get("src"));
        } else {
            cn.m299q("Unknown video action: " + str);
        }
    }
}
