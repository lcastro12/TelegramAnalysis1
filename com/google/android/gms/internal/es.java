package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Display;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import java.lang.ref.WeakReference;

public class es {
    protected em mz;
    protected C0185a np;

    public static final class C0185a {
        public int bottom;
        public int gravity;
        public int left;
        public IBinder nq;
        public int nr;
        public int right;
        public int top;

        private C0185a(int i, IBinder iBinder) {
            this.nr = -1;
            this.left = 0;
            this.top = 0;
            this.right = 0;
            this.bottom = 0;
            this.gravity = i;
            this.nq = iBinder;
        }

        public Bundle ca() {
            Bundle bundle = new Bundle();
            bundle.putInt("popupLocationInfo.gravity", this.gravity);
            bundle.putInt("popupLocationInfo.displayId", this.nr);
            bundle.putInt("popupLocationInfo.left", this.left);
            bundle.putInt("popupLocationInfo.top", this.top);
            bundle.putInt("popupLocationInfo.right", this.right);
            bundle.putInt("popupLocationInfo.bottom", this.bottom);
            return bundle;
        }
    }

    private static final class C0737b extends es implements OnAttachStateChangeListener, OnGlobalLayoutListener {
        private boolean mK = false;
        private WeakReference<View> ns;

        protected C0737b(em emVar, int i) {
            super(emVar, i);
        }

        private void m1166f(View view) {
            int i = -1;
            if (ek.bO()) {
                Display display = view.getDisplay();
                if (display != null) {
                    i = display.getDisplayId();
                }
            }
            IBinder windowToken = view.getWindowToken();
            int[] iArr = new int[2];
            view.getLocationInWindow(iArr);
            int width = view.getWidth();
            int height = view.getHeight();
            this.np.nr = i;
            this.np.nq = windowToken;
            this.np.left = iArr[0];
            this.np.top = iArr[1];
            this.np.right = iArr[0] + width;
            this.np.bottom = iArr[1] + height;
            if (this.mK) {
                bX();
                this.mK = false;
            }
        }

        protected void mo986Q(int i) {
            this.np = new C0185a(i, null);
        }

        public void bX() {
            if (this.np.nq != null) {
                super.bX();
            } else {
                this.mK = this.ns != null;
            }
        }

        public void mo988e(View view) {
            View view2;
            Context context;
            this.mz.bT();
            if (this.ns != null) {
                view2 = (View) this.ns.get();
                context = this.mz.getContext();
                if (view2 == null && (context instanceof Activity)) {
                    view2 = ((Activity) context).getWindow().getDecorView();
                }
                if (view2 != null) {
                    view2.removeOnAttachStateChangeListener(this);
                    ViewTreeObserver viewTreeObserver = view2.getViewTreeObserver();
                    if (ek.bN()) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this);
                    } else {
                        viewTreeObserver.removeGlobalOnLayoutListener(this);
                    }
                }
            }
            this.ns = null;
            context = this.mz.getContext();
            if (view == null && (context instanceof Activity)) {
                view2 = ((Activity) context).findViewById(16908290);
                if (view2 == null) {
                    view2 = ((Activity) context).getWindow().getDecorView();
                }
                ep.m455c("PopupManager", "You have not specified a View to use as content view for popups. Falling back to the Activity content view which may not work properly in future versions of the API. Use setViewForPopups() to set your content view.");
                view = view2;
            }
            if (view != null) {
                m1166f(view);
                this.ns = new WeakReference(view);
                view.addOnAttachStateChangeListener(this);
                view.getViewTreeObserver().addOnGlobalLayoutListener(this);
                return;
            }
            ep.m456d("PopupManager", "No content view usable to display popups. Popups will not be displayed in response to this client's calls. Use setViewForPopups() to set your content view.");
        }

        public void onGlobalLayout() {
            if (this.ns != null) {
                View view = (View) this.ns.get();
                if (view != null) {
                    m1166f(view);
                }
            }
        }

        public void onViewAttachedToWindow(View v) {
            m1166f(v);
        }

        public void onViewDetachedFromWindow(View v) {
            this.mz.bT();
            v.removeOnAttachStateChangeListener(this);
        }
    }

    private es(em emVar, int i) {
        this.mz = emVar;
        mo986Q(i);
    }

    public static es m568a(em emVar, int i) {
        return ek.bK() ? new C0737b(emVar, i) : new es(emVar, i);
    }

    protected void mo986Q(int i) {
        this.np = new C0185a(i, new Binder());
    }

    public void bX() {
        this.mz.m1426a(this.np.nq, this.np.ca());
    }

    public Bundle bY() {
        return this.np.ca();
    }

    public IBinder bZ() {
        return this.np.nq;
    }

    public void mo988e(View view) {
    }

    public void setGravity(int gravity) {
        this.np.gravity = gravity;
    }
}
