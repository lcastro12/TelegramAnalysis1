package com.google.android.gms.internal;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public final class cm {
    public static final Handler hO = new Handler(Looper.getMainLooper());

    public static int m285a(Context context, int i) {
        return m286a(context.getResources().getDisplayMetrics(), i);
    }

    public static int m286a(DisplayMetrics displayMetrics, int i) {
        return (int) TypedValue.applyDimension(1, (float) i, displayMetrics);
    }

    public static void m287a(ViewGroup viewGroup, C0771x c0771x, String str) {
        cn.m299q(str);
        m288a(viewGroup, c0771x, str, SupportMenu.CATEGORY_MASK, ViewCompat.MEASURED_STATE_MASK);
    }

    private static void m288a(ViewGroup viewGroup, C0771x c0771x, String str, int i, int i2) {
        if (viewGroup.getChildCount() == 0) {
            Context context = viewGroup.getContext();
            View textView = new TextView(context);
            textView.setGravity(17);
            textView.setText(str);
            textView.setTextColor(i);
            textView.setBackgroundColor(i2);
            View frameLayout = new FrameLayout(context);
            frameLayout.setBackgroundColor(i);
            int a = m285a(context, 3);
            frameLayout.addView(textView, new LayoutParams(c0771x.widthPixels - a, c0771x.heightPixels - a, 17));
            viewGroup.addView(frameLayout, c0771x.widthPixels, c0771x.heightPixels);
        }
    }

    public static boolean aq() {
        return Build.DEVICE.startsWith("generic");
    }

    public static boolean ar() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void m289b(ViewGroup viewGroup, C0771x c0771x, String str) {
        m288a(viewGroup, c0771x, str, ViewCompat.MEASURED_STATE_MASK, -1);
    }

    public static String m290l(Context context) {
        String string = Secure.getString(context.getContentResolver(), "android_id");
        if (string == null || aq()) {
            string = "emulator";
        }
        return m291l(string);
    }

    public static String m291l(String str) {
        int i = 0;
        while (i < 2) {
            try {
                MessageDigest.getInstance("MD5").update(str.getBytes());
                return String.format(Locale.US, "%032X", new Object[]{new BigInteger(1, r1.digest())});
            } catch (NoSuchAlgorithmException e) {
                i++;
            }
        }
        return null;
    }
}
