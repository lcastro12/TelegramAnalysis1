package com.google.android.gms.internal;

import android.os.Build.VERSION;

public final class ek {
    private static boolean m451I(int i) {
        return VERSION.SDK_INT >= i;
    }

    public static boolean bJ() {
        return m451I(11);
    }

    public static boolean bK() {
        return m451I(12);
    }

    public static boolean bL() {
        return m451I(13);
    }

    public static boolean bM() {
        return m451I(14);
    }

    public static boolean bN() {
        return m451I(16);
    }

    public static boolean bO() {
        return m451I(17);
    }
}
