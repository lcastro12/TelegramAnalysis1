package com.google.android.gms.maps;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.dynamic.C0111a;
import com.google.android.gms.dynamic.C0113d;
import com.google.android.gms.dynamic.C0898c;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.C0225p;
import com.google.android.gms.maps.internal.C0226q;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.IMapFragmentDelegate;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public class SupportMapFragment extends Fragment {
    private GoogleMap pI;
    private final C0780b pT = new C0780b(this);

    static class C0779a implements LifecycleDelegate {
        private final IMapFragmentDelegate pK;
        private final Fragment pU;

        public C0779a(Fragment fragment, IMapFragmentDelegate iMapFragmentDelegate) {
            this.pK = (IMapFragmentDelegate) dm.m392e(iMapFragmentDelegate);
            this.pU = (Fragment) dm.m392e(fragment);
        }

        public IMapFragmentDelegate cD() {
            return this.pK;
        }

        public void onCreate(Bundle savedInstanceState) {
            if (savedInstanceState == null) {
                try {
                    savedInstanceState = new Bundle();
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            }
            Bundle arguments = this.pU.getArguments();
            if (arguments != null && arguments.containsKey("MapOptions")) {
                C0225p.m726a(savedInstanceState, "MapOptions", arguments.getParcelable("MapOptions"));
            }
            this.pK.onCreate(savedInstanceState);
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            try {
                return (View) C0898c.m1317b(this.pK.onCreateView(C0898c.m1318g(inflater), C0898c.m1318g(container), savedInstanceState));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroy() {
            try {
                this.pK.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroyView() {
            try {
                this.pK.onDestroyView();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
            try {
                this.pK.onInflate(C0898c.m1318g(activity), (GoogleMapOptions) attrs.getParcelable("MapOptions"), savedInstanceState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onLowMemory() {
            try {
                this.pK.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onPause() {
            try {
                this.pK.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onResume() {
            try {
                this.pK.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onSaveInstanceState(Bundle outState) {
            try {
                this.pK.onSaveInstanceState(outState);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    static class C0780b extends C0111a<C0779a> {
        private Activity fD;
        protected C0113d<C0779a> pL;
        private final Fragment pU;

        C0780b(Fragment fragment) {
            this.pU = fragment;
        }

        private void setActivity(Activity activity) {
            this.fD = activity;
            cE();
        }

        protected void mo1075a(C0113d<C0779a> c0113d) {
            this.pL = c0113d;
            cE();
        }

        public void cE() {
            if (this.fD != null && this.pL != null && bP() == null) {
                try {
                    MapsInitializer.initialize(this.fD);
                    this.pL.mo706a(new C0779a(this.pU, C0226q.m729u(this.fD).mo1181f(C0898c.m1318g(this.fD))));
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                }
            }
        }
    }

    public static SupportMapFragment newInstance() {
        return new SupportMapFragment();
    }

    public static SupportMapFragment newInstance(GoogleMapOptions options) {
        SupportMapFragment supportMapFragment = new SupportMapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("MapOptions", options);
        supportMapFragment.setArguments(bundle);
        return supportMapFragment;
    }

    protected IMapFragmentDelegate cD() {
        this.pT.cE();
        return this.pT.bP() == null ? null : ((C0779a) this.pT.bP()).cD();
    }

    public final GoogleMap getMap() {
        IMapFragmentDelegate cD = cD();
        if (cD == null) {
            return null;
        }
        try {
            IGoogleMapDelegate map = cD.getMap();
            if (map == null) {
                return null;
            }
            if (this.pI == null || this.pI.cu().asBinder() != map.asBinder()) {
                this.pI = new GoogleMap(map);
            }
            return this.pI;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.setClassLoader(SupportMapFragment.class.getClassLoader());
        }
        super.onActivityCreated(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.pT.setActivity(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pT.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.pT.onCreateView(inflater, container, savedInstanceState);
    }

    public void onDestroy() {
        this.pT.onDestroy();
        super.onDestroy();
    }

    public void onDestroyView() {
        this.pT.onDestroyView();
        super.onDestroyView();
    }

    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        this.pT.setActivity(activity);
        Parcelable createFromAttributes = GoogleMapOptions.createFromAttributes(activity, attrs);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MapOptions", createFromAttributes);
        this.pT.onInflate(activity, bundle, savedInstanceState);
    }

    public void onLowMemory() {
        this.pT.onLowMemory();
        super.onLowMemory();
    }

    public void onPause() {
        this.pT.onPause();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.pT.onResume();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.setClassLoader(SupportMapFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(outState);
        this.pT.onSaveInstanceState(outState);
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
    }
}
