package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;

public final class bk extends FrameLayout implements OnClickListener {
    private final Activity fD;
    private final ImageButton gk;

    public bk(Activity activity, int i) {
        super(activity);
        this.fD = activity;
        setOnClickListener(this);
        this.gk = new ImageButton(activity);
        this.gk.setImageResource(17301527);
        this.gk.setBackgroundColor(0);
        this.gk.setOnClickListener(this);
        this.gk.setPadding(0, 0, 0, 0);
        int a = cm.m285a((Context) activity, i);
        addView(this.gk, new LayoutParams(a, a, 17));
    }

    public void m217d(boolean z) {
        this.gk.setVisibility(z ? 4 : 0);
    }

    public void onClick(View view) {
        this.fD.finish();
    }
}
