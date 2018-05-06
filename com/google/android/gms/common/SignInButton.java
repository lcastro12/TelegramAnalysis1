package com.google.android.gms.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import com.google.android.gms.dynamic.C0115e.C0114a;
import com.google.android.gms.internal.C0182do;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.dn;

public final class SignInButton extends FrameLayout implements OnClickListener {
    public static final int COLOR_DARK = 0;
    public static final int COLOR_LIGHT = 1;
    public static final int SIZE_ICON_ONLY = 2;
    public static final int SIZE_STANDARD = 0;
    public static final int SIZE_WIDE = 1;
    private int jc;
    private View jd;
    private OnClickListener je;
    private int mSize;

    public SignInButton(Context context) {
        this(context, null);
    }

    public SignInButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignInButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.je = null;
        setStyle(0, 0);
    }

    private static Button m38c(Context context, int i, int i2) {
        Button c0182do = new C0182do(context);
        c0182do.m401a(context.getResources(), i, i2);
        return c0182do;
    }

    private void m39p(Context context) {
        if (this.jd != null) {
            removeView(this.jd);
        }
        try {
            this.jd = dn.m982d(context, this.mSize, this.jc);
        } catch (C0114a e) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            this.jd = m38c(context, this.mSize, this.jc);
        }
        addView(this.jd);
        this.jd.setEnabled(isEnabled());
        this.jd.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (this.je != null && view == this.jd) {
            this.je.onClick(this);
        }
    }

    public void setColorScheme(int colorScheme) {
        setStyle(this.mSize, colorScheme);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.jd.setEnabled(enabled);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.je = listener;
        if (this.jd != null) {
            this.jd.setOnClickListener(this);
        }
    }

    public void setSize(int buttonSize) {
        setStyle(buttonSize, this.jc);
    }

    public void setStyle(int buttonSize, int colorScheme) {
        boolean z = true;
        boolean z2 = buttonSize >= 0 && buttonSize < 3;
        dm.m389a(z2, "Unknown button size " + buttonSize);
        if (colorScheme < 0 || colorScheme >= 2) {
            z = false;
        }
        dm.m389a(z, "Unknown color scheme " + colorScheme);
        this.mSize = buttonSize;
        this.jc = colorScheme;
        m39p(getContext());
    }
}
