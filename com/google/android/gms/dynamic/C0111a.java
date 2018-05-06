package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class C0111a<T extends LifecycleDelegate> {
    private T lV;
    private Bundle lW;
    private LinkedList<C0110a> lX;
    private final C0113d<T> lY = new C06491(this);

    private interface C0110a {
        void mo707b(LifecycleDelegate lifecycleDelegate);

        int getState();
    }

    class C06491 implements C0113d<T> {
        final /* synthetic */ C0111a lZ;

        C06491(C0111a c0111a) {
            this.lZ = c0111a;
        }

        public void mo706a(T t) {
            this.lZ.lV = t;
            Iterator it = this.lZ.lX.iterator();
            while (it.hasNext()) {
                ((C0110a) it.next()).mo707b(this.lZ.lV);
            }
            this.lZ.lX.clear();
            this.lZ.lW = null;
        }
    }

    class C06536 implements C0110a {
        final /* synthetic */ C0111a lZ;

        C06536(C0111a c0111a) {
            this.lZ = c0111a;
        }

        public void mo707b(LifecycleDelegate lifecycleDelegate) {
            this.lZ.lV.onResume();
        }

        public int getState() {
            return 3;
        }
    }

    private void m135J(int i) {
        while (!this.lX.isEmpty() && ((C0110a) this.lX.getLast()).getState() >= i) {
            this.lX.removeLast();
        }
    }

    private void m139a(Bundle bundle, C0110a c0110a) {
        if (this.lV != null) {
            c0110a.mo707b(this.lV);
            return;
        }
        if (this.lX == null) {
            this.lX = new LinkedList();
        }
        this.lX.add(c0110a);
        if (bundle != null) {
            if (this.lW == null) {
                this.lW = (Bundle) bundle.clone();
            } else {
                this.lW.putAll(bundle);
            }
        }
        mo1075a(this.lY);
    }

    public void m141a(FrameLayout frameLayout) {
        final Context context = frameLayout.getContext();
        final int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        CharSequence b = GooglePlayServicesUtil.m30b(context, isGooglePlayServicesAvailable, -1);
        CharSequence b2 = GooglePlayServicesUtil.m29b(context, isGooglePlayServicesAvailable);
        View linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        View textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new LayoutParams(-2, -2));
        textView.setText(b);
        linearLayout.addView(textView);
        if (b2 != null) {
            View button = new Button(context);
            button.setLayoutParams(new LayoutParams(-2, -2));
            button.setText(b2);
            linearLayout.addView(button);
            button.setOnClickListener(new OnClickListener(this) {
                final /* synthetic */ C0111a lZ;

                public void onClick(View v) {
                    context.startActivity(GooglePlayServicesUtil.m25a(context, isGooglePlayServicesAvailable, -1));
                }
            });
        }
    }

    protected abstract void mo1075a(C0113d<T> c0113d);

    public T bP() {
        return this.lV;
    }

    public void onCreate(final Bundle savedInstanceState) {
        m139a(savedInstanceState, new C0110a(this) {
            final /* synthetic */ C0111a lZ;

            public void mo707b(LifecycleDelegate lifecycleDelegate) {
                this.lZ.lV.onCreate(savedInstanceState);
            }

            public int getState() {
                return 1;
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FrameLayout frameLayout = new FrameLayout(inflater.getContext());
        final LayoutInflater layoutInflater = inflater;
        final ViewGroup viewGroup = container;
        final Bundle bundle = savedInstanceState;
        m139a(savedInstanceState, new C0110a(this) {
            final /* synthetic */ C0111a lZ;

            public void mo707b(LifecycleDelegate lifecycleDelegate) {
                frameLayout.removeAllViews();
                frameLayout.addView(this.lZ.lV.onCreateView(layoutInflater, viewGroup, bundle));
            }

            public int getState() {
                return 2;
            }
        });
        if (this.lV == null) {
            m141a(frameLayout);
        }
        return frameLayout;
    }

    public void onDestroy() {
        if (this.lV != null) {
            this.lV.onDestroy();
        } else {
            m135J(1);
        }
    }

    public void onDestroyView() {
        if (this.lV != null) {
            this.lV.onDestroyView();
        } else {
            m135J(2);
        }
    }

    public void onInflate(final Activity activity, final Bundle attrs, final Bundle savedInstanceState) {
        m139a(savedInstanceState, new C0110a(this) {
            final /* synthetic */ C0111a lZ;

            public void mo707b(LifecycleDelegate lifecycleDelegate) {
                this.lZ.lV.onInflate(activity, attrs, savedInstanceState);
            }

            public int getState() {
                return 0;
            }
        });
    }

    public void onLowMemory() {
        if (this.lV != null) {
            this.lV.onLowMemory();
        }
    }

    public void onPause() {
        if (this.lV != null) {
            this.lV.onPause();
        } else {
            m135J(3);
        }
    }

    public void onResume() {
        m139a(null, new C06536(this));
    }

    public void onSaveInstanceState(Bundle outState) {
        if (this.lV != null) {
            this.lV.onSaveInstanceState(outState);
        } else if (this.lW != null) {
            outState.putAll(this.lW);
        }
    }
}
