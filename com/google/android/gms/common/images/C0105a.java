package com.google.android.gms.common.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.images.ImageManager.OnImageLoadedListener;
import com.google.android.gms.internal.cz;
import com.google.android.gms.internal.da;
import com.google.android.gms.internal.db;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.ek;
import java.lang.ref.WeakReference;

public final class C0105a {
    final C0104a jS;
    private int jT;
    private int jU;
    int jV;
    private int jW;
    private WeakReference<OnImageLoadedListener> jX;
    private WeakReference<ImageView> jY;
    private WeakReference<TextView> jZ;
    private int ka;
    private boolean kb;
    private boolean kc;

    public static final class C0104a {
        public final Uri uri;

        public C0104a(Uri uri) {
            this.uri = uri;
        }

        public boolean equals(Object obj) {
            if (obj instanceof C0104a) {
                return this == obj || ((C0104a) obj).hashCode() == hashCode();
            } else {
                return false;
            }
        }

        public int hashCode() {
            return dl.hashCode(this.uri);
        }
    }

    public C0105a(int i) {
        this.jT = 0;
        this.jU = 0;
        this.ka = -1;
        this.kb = true;
        this.kc = false;
        this.jS = new C0104a(null);
        this.jU = i;
    }

    public C0105a(Uri uri) {
        this.jT = 0;
        this.jU = 0;
        this.ka = -1;
        this.kb = true;
        this.kc = false;
        this.jS = new C0104a(uri);
        this.jU = 0;
    }

    private cz m65a(Drawable drawable, Drawable drawable2) {
        if (drawable == null) {
            drawable = null;
        } else if (drawable instanceof cz) {
            drawable = ((cz) drawable).aS();
        }
        return new cz(drawable, drawable2);
    }

    private void m66a(Drawable drawable, boolean z, boolean z2, boolean z3) {
        switch (this.jV) {
            case 1:
                if (!z2) {
                    OnImageLoadedListener onImageLoadedListener = (OnImageLoadedListener) this.jX.get();
                    if (onImageLoadedListener != null) {
                        onImageLoadedListener.onImageLoaded(this.jS.uri, drawable);
                        return;
                    }
                    return;
                }
                return;
            case 2:
                ImageView imageView = (ImageView) this.jY.get();
                if (imageView != null) {
                    m67a(imageView, drawable, z, z2, z3);
                    return;
                }
                return;
            case 3:
                TextView textView = (TextView) this.jZ.get();
                if (textView != null) {
                    m68a(textView, this.ka, drawable, z, z2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void m67a(ImageView imageView, Drawable drawable, boolean z, boolean z2, boolean z3) {
        Object obj = (z2 || z3) ? null : 1;
        if (obj != null && (imageView instanceof da)) {
            int aU = ((da) imageView).aU();
            if (this.jU != 0 && aU == this.jU) {
                return;
            }
        }
        boolean a = m69a(z, z2);
        Drawable a2 = a ? m65a(imageView.getDrawable(), drawable) : drawable;
        imageView.setImageDrawable(a2);
        if (imageView instanceof da) {
            da daVar = (da) imageView;
            daVar.m339d(z3 ? this.jS.uri : null);
            daVar.m340w(obj != null ? this.jU : 0);
        }
        if (a) {
            ((cz) a2).startTransition(250);
        }
    }

    private void m68a(TextView textView, int i, Drawable drawable, boolean z, boolean z2) {
        boolean a = m69a(z, z2);
        Drawable[] compoundDrawablesRelative = ek.bO() ? textView.getCompoundDrawablesRelative() : textView.getCompoundDrawables();
        Drawable a2 = a ? m65a(compoundDrawablesRelative[i], drawable) : drawable;
        Drawable drawable2 = i == 0 ? a2 : compoundDrawablesRelative[0];
        Drawable drawable3 = i == 1 ? a2 : compoundDrawablesRelative[1];
        Drawable drawable4 = i == 2 ? a2 : compoundDrawablesRelative[2];
        Drawable drawable5 = i == 3 ? a2 : compoundDrawablesRelative[3];
        if (ek.bO()) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        }
        if (a) {
            ((cz) a2).startTransition(250);
        }
    }

    private boolean m69a(boolean z, boolean z2) {
        return this.kb && !z2 && (!z || this.kc);
    }

    void m70a(Context context, Bitmap bitmap, boolean z) {
        db.m342c(bitmap);
        m66a(new BitmapDrawable(context.getResources(), bitmap), z, false, true);
    }

    public void m71a(ImageView imageView) {
        db.m342c(imageView);
        this.jX = null;
        this.jY = new WeakReference(imageView);
        this.jZ = null;
        this.ka = -1;
        this.jV = 2;
        this.jW = imageView.hashCode();
    }

    public void m72a(OnImageLoadedListener onImageLoadedListener) {
        db.m342c(onImageLoadedListener);
        this.jX = new WeakReference(onImageLoadedListener);
        this.jY = null;
        this.jZ = null;
        this.ka = -1;
        this.jV = 1;
        this.jW = dl.hashCode(onImageLoadedListener, this.jS);
    }

    void m73b(Context context, boolean z) {
        Drawable drawable = null;
        if (this.jU != 0) {
            drawable = context.getResources().getDrawable(this.jU);
        }
        m66a(drawable, z, false, false);
    }

    public boolean equals(Object obj) {
        if (obj instanceof C0105a) {
            return this == obj || ((C0105a) obj).hashCode() == hashCode();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.jW;
    }

    void m74r(Context context) {
        Drawable drawable = null;
        if (this.jT != 0) {
            drawable = context.getResources().getDrawable(this.jT);
        }
        m66a(drawable, false, true, false);
    }

    public void m75v(int i) {
        this.jU = i;
    }
}
