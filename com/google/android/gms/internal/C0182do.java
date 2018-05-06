package com.google.android.gms.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import com.google.android.gms.C0090R;

public final class C0182do extends Button {
    public C0182do(Context context) {
        this(context, null);
    }

    public C0182do(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 16842824);
    }

    private int m397b(int i, int i2, int i3) {
        switch (i) {
            case 0:
                return i2;
            case 1:
                return i3;
            default:
                throw new IllegalStateException("Unknown color scheme: " + i);
        }
    }

    private void m398b(Resources resources, int i, int i2) {
        int b;
        switch (i) {
            case 0:
            case 1:
                b = m397b(i2, C0090R.drawable.common_signin_btn_text_dark, C0090R.drawable.common_signin_btn_text_light);
                break;
            case 2:
                b = m397b(i2, C0090R.drawable.common_signin_btn_icon_dark, C0090R.drawable.common_signin_btn_icon_light);
                break;
            default:
                throw new IllegalStateException("Unknown button size: " + i);
        }
        if (b == -1) {
            throw new IllegalStateException("Could not find background resource!");
        }
        setBackgroundDrawable(resources.getDrawable(b));
    }

    private void m399c(Resources resources) {
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(14.0f);
        float f = resources.getDisplayMetrics().density;
        setMinHeight((int) ((f * 48.0f) + 0.5f));
        setMinWidth((int) ((f * 48.0f) + 0.5f));
    }

    private void m400c(Resources resources, int i, int i2) {
        setTextColor(resources.getColorStateList(m397b(i2, C0090R.color.common_signin_btn_text_dark, C0090R.color.common_signin_btn_text_light)));
        switch (i) {
            case 0:
                setText(resources.getString(C0090R.string.common_signin_button_text));
                return;
            case 1:
                setText(resources.getString(C0090R.string.common_signin_button_text_long));
                return;
            case 2:
                setText(null);
                return;
            default:
                throw new IllegalStateException("Unknown button size: " + i);
        }
    }

    public void m401a(Resources resources, int i, int i2) {
        boolean z = true;
        boolean z2 = i >= 0 && i < 3;
        dm.m389a(z2, "Unknown button size " + i);
        if (i2 < 0 || i2 >= 2) {
            z = false;
        }
        dm.m389a(z, "Unknown color scheme " + i2);
        m399c(resources);
        m398b(resources, i, i2);
        m400c(resources, i, i2);
    }
}
