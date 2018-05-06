package com.google.android.gms.internal;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;

public final class cz extends Drawable implements Callback {
    private boolean kb;
    private int kd;
    private long ke;
    private int kf;
    private int kg;
    private int kh;
    private int ki;
    private int kj;
    private boolean kk;
    private C0173b kl;
    private Drawable km;
    private Drawable kn;
    private boolean ko;
    private boolean kp;
    private boolean kq;
    private int kr;

    private static final class C0172a extends Drawable {
        private static final C0172a ks = new C0172a();
        private static final C0171a kt = new C0171a();

        private static final class C0171a extends ConstantState {
            private C0171a() {
            }

            public int getChangingConfigurations() {
                return 0;
            }

            public Drawable newDrawable() {
                return C0172a.ks;
            }
        }

        private C0172a() {
        }

        public void draw(Canvas canvas) {
        }

        public ConstantState getConstantState() {
            return kt;
        }

        public int getOpacity() {
            return -2;
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter cf) {
        }
    }

    static final class C0173b extends ConstantState {
        int ku;
        int kv;

        C0173b(C0173b c0173b) {
            if (c0173b != null) {
                this.ku = c0173b.ku;
                this.kv = c0173b.kv;
            }
        }

        public int getChangingConfigurations() {
            return this.ku;
        }

        public Drawable newDrawable() {
            return new cz(this);
        }
    }

    public cz(Drawable drawable, Drawable drawable2) {
        this(null);
        if (drawable == null) {
            drawable = C0172a.ks;
        }
        this.km = drawable;
        drawable.setCallback(this);
        C0173b c0173b = this.kl;
        c0173b.kv |= drawable.getChangingConfigurations();
        if (drawable2 == null) {
            drawable2 = C0172a.ks;
        }
        this.kn = drawable2;
        drawable2.setCallback(this);
        c0173b = this.kl;
        c0173b.kv |= drawable2.getChangingConfigurations();
    }

    cz(C0173b c0173b) {
        this.kd = 0;
        this.kh = MotionEventCompat.ACTION_MASK;
        this.kj = 0;
        this.kb = true;
        this.kl = new C0173b(c0173b);
    }

    public Drawable aS() {
        return this.kn;
    }

    public boolean canConstantState() {
        if (!this.ko) {
            boolean z = (this.km.getConstantState() == null || this.kn.getConstantState() == null) ? false : true;
            this.kp = z;
            this.ko = true;
        }
        return this.kp;
    }

    public void draw(Canvas canvas) {
        int i = 1;
        int i2 = 0;
        switch (this.kd) {
            case 1:
                this.ke = SystemClock.uptimeMillis();
                this.kd = 2;
                break;
            case 2:
                if (this.ke >= 0) {
                    float uptimeMillis = ((float) (SystemClock.uptimeMillis() - this.ke)) / ((float) this.ki);
                    if (uptimeMillis < 1.0f) {
                        i = 0;
                    }
                    if (i != 0) {
                        this.kd = 0;
                    }
                    float min = Math.min(uptimeMillis, 1.0f);
                    this.kj = (int) ((min * ((float) (this.kg - this.kf))) + ((float) this.kf));
                    break;
                }
                break;
        }
        i2 = i;
        i = this.kj;
        boolean z = this.kb;
        Drawable drawable = this.km;
        Drawable drawable2 = this.kn;
        if (i2 != 0) {
            if (!z || i == 0) {
                drawable.draw(canvas);
            }
            if (i == this.kh) {
                drawable2.setAlpha(this.kh);
                drawable2.draw(canvas);
                return;
            }
            return;
        }
        if (z) {
            drawable.setAlpha(this.kh - i);
        }
        drawable.draw(canvas);
        if (z) {
            drawable.setAlpha(this.kh);
        }
        if (i > 0) {
            drawable2.setAlpha(i);
            drawable2.draw(canvas);
            drawable2.setAlpha(this.kh);
        }
        invalidateSelf();
    }

    public int getChangingConfigurations() {
        return (super.getChangingConfigurations() | this.kl.ku) | this.kl.kv;
    }

    public ConstantState getConstantState() {
        if (!canConstantState()) {
            return null;
        }
        this.kl.ku = getChangingConfigurations();
        return this.kl;
    }

    public int getIntrinsicHeight() {
        return Math.max(this.km.getIntrinsicHeight(), this.kn.getIntrinsicHeight());
    }

    public int getIntrinsicWidth() {
        return Math.max(this.km.getIntrinsicWidth(), this.kn.getIntrinsicWidth());
    }

    public int getOpacity() {
        if (!this.kq) {
            this.kr = Drawable.resolveOpacity(this.km.getOpacity(), this.kn.getOpacity());
            this.kq = true;
        }
        return this.kr;
    }

    public void invalidateDrawable(Drawable who) {
        if (ek.bJ()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.invalidateDrawable(this);
            }
        }
    }

    public Drawable mutate() {
        if (!this.kk && super.mutate() == this) {
            if (canConstantState()) {
                this.km.mutate();
                this.kn.mutate();
                this.kk = true;
            } else {
                throw new IllegalStateException("One or more children of this LayerDrawable does not have constant state; this drawable cannot be mutated.");
            }
        }
        return this;
    }

    protected void onBoundsChange(Rect bounds) {
        this.km.setBounds(bounds);
        this.kn.setBounds(bounds);
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (ek.bJ()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.scheduleDrawable(this, what, when);
            }
        }
    }

    public void setAlpha(int alpha) {
        if (this.kj == this.kh) {
            this.kj = alpha;
        }
        this.kh = alpha;
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter cf) {
        this.km.setColorFilter(cf);
        this.kn.setColorFilter(cf);
    }

    public void startTransition(int durationMillis) {
        this.kf = 0;
        this.kg = this.kh;
        this.kj = 0;
        this.ki = durationMillis;
        this.kd = 1;
        invalidateSelf();
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (ek.bJ()) {
            Callback callback = getCallback();
            if (callback != null) {
                callback.unscheduleDrawable(this, what);
            }
        }
    }
}
