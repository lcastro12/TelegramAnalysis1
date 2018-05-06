package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.C0227r;
import com.google.android.gms.maps.model.internal.C0243g;
import com.google.android.gms.maps.model.internal.C0243g.C0839a;

public final class TileOverlayOptions implements SafeParcelable {
    public static final TileOverlayOptionsCreator CREATOR = new TileOverlayOptionsCreator();
    private final int iM;
    private C0243g qP;
    private TileProvider qQ;
    private float qk;
    private boolean ql;

    class C08231 implements TileProvider {
        private final C0243g qR = this.qS.qP;
        final /* synthetic */ TileOverlayOptions qS;

        C08231(TileOverlayOptions tileOverlayOptions) {
            this.qS = tileOverlayOptions;
        }

        public Tile getTile(int x, int y, int zoom) {
            try {
                return this.qR.getTile(x, y, zoom);
            } catch (RemoteException e) {
                return null;
            }
        }
    }

    public TileOverlayOptions() {
        this.ql = true;
        this.iM = 1;
    }

    TileOverlayOptions(int versionCode, IBinder delegate, boolean visible, float zIndex) {
        this.ql = true;
        this.iM = versionCode;
        this.qP = C0839a.aj(delegate);
        this.qQ = this.qP == null ? null : new C08231(this);
        this.ql = visible;
        this.qk = zIndex;
    }

    IBinder cP() {
        return this.qP.asBinder();
    }

    public int describeContents() {
        return 0;
    }

    public TileProvider getTileProvider() {
        return this.qQ;
    }

    int getVersionCode() {
        return this.iM;
    }

    public float getZIndex() {
        return this.qk;
    }

    public boolean isVisible() {
        return this.ql;
    }

    public TileOverlayOptions tileProvider(final TileProvider tileProvider) {
        this.qQ = tileProvider;
        this.qP = this.qQ == null ? null : new C0839a(this) {
            final /* synthetic */ TileOverlayOptions qS;

            public Tile getTile(int x, int y, int zoom) {
                return tileProvider.getTile(x, y, zoom);
            }
        };
        return this;
    }

    public TileOverlayOptions visible(boolean visible) {
        this.ql = visible;
        return this;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (C0227r.cK()) {
            C0244j.m767a(this, out, flags);
        } else {
            TileOverlayOptionsCreator.m743a(this, out, flags);
        }
    }

    public TileOverlayOptions zIndex(float zIndex) {
        this.qk = zIndex;
        return this;
    }
}
