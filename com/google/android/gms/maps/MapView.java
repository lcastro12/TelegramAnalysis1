package com.google.android.gms.maps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.dynamic.C0111a;
import com.google.android.gms.dynamic.C0113d;
import com.google.android.gms.dynamic.C0898c;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.C0226q;
import com.google.android.gms.maps.internal.IMapViewDelegate;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public class MapView extends FrameLayout {
    private GoogleMap pI;
    private final C0778b pM;

    static class C0777a implements LifecycleDelegate {
        private final ViewGroup pN;
        private final IMapViewDelegate pO;
        private View pP;

        public C0777a(ViewGroup viewGroup, IMapViewDelegate iMapViewDelegate) {
            this.pO = (IMapViewDelegate) dm.m392e(iMapViewDelegate);
            this.pN = (ViewGroup) dm.m392e(viewGroup);
        }

        public IMapViewDelegate cF() {
            return this.pO;
        }

        public void onCreate(Bundle savedInstanceState) {
            try {
                this.pO.onCreate(savedInstanceState);
                this.pP = (View) C0898c.m1317b(this.pO.getView());
                this.pN.removeAllViews();
                this.pN.addView(this.pP);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            throw new UnsupportedOperationException("onCreateView not allowed on MapViewDelegate");
        }

        public void onDestroy() {
            try {
                this.pO.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroyView() {
            throw new UnsupportedOperationException("onDestroyView not allowed on MapViewDelegate");
        }

        public void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
            throw new UnsupportedOperationException("onInflate not allowed on MapViewDelegate");
        }

        public void onLowMemory() {
            try {
                this.pO.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onPause() {
            try {
                this.pO.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onResume() {
            try {
                this.pO.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onSaveInstanceState(Bundle outState) {
            try {
                this.pO.onSaveInstanceState(outState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    static class C0778b extends C0111a<C0777a> {
        private final Context mContext;
        protected C0113d<C0777a> pL;
        private final ViewGroup pQ;
        private final GoogleMapOptions pR;

        C0778b(ViewGroup viewGroup, Context context, GoogleMapOptions googleMapOptions) {
            this.pQ = viewGroup;
            this.mContext = context;
            this.pR = googleMapOptions;
        }

        protected void mo1075a(C0113d<C0777a> c0113d) {
            this.pL = c0113d;
            cE();
        }

        public void cE() {
            if (this.pL != null && bP() == null) {
                try {
                    this.pL.mo706a(new C0777a(this.pQ, C0226q.m729u(this.mContext).mo1176a(C0898c.m1318g(this.mContext), this.pR)));
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                }
            }
        }
    }

    public MapView(Context context) {
        super(context);
        this.pM = new C0778b(this, context, null);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.pM = new C0778b(this, context, GoogleMapOptions.createFromAttributes(context, attrs));
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.pM = new C0778b(this, context, GoogleMapOptions.createFromAttributes(context, attrs));
    }

    public MapView(Context context, GoogleMapOptions options) {
        super(context);
        this.pM = new C0778b(this, context, options);
    }

    public final GoogleMap getMap() {
        if (this.pI != null) {
            return this.pI;
        }
        this.pM.cE();
        if (this.pM.bP() == null) {
            return null;
        }
        try {
            this.pI = new GoogleMap(((C0777a) this.pM.bP()).cF().getMap());
            return this.pI;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void onCreate(Bundle savedInstanceState) {
        this.pM.onCreate(savedInstanceState);
        if (this.pM.bP() == null) {
            this.pM.m141a((FrameLayout) this);
        }
    }

    public final void onDestroy() {
        this.pM.onDestroy();
    }

    public final void onLowMemory() {
        this.pM.onLowMemory();
    }

    public final void onPause() {
        this.pM.onPause();
    }

    public final void onResume() {
        this.pM.onResume();
    }

    public final void onSaveInstanceState(Bundle outState) {
        this.pM.onSaveInstanceState(outState);
    }
}
