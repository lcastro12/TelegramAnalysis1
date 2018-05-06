package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.C0112b.C0655a;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.C0227r;

public final class GroundOverlayOptions implements SafeParcelable {
    public static final GroundOverlayOptionsCreator CREATOR = new GroundOverlayOptionsCreator();
    public static final float NO_DIMENSION = -1.0f;
    private final int iM;
    private float qd;
    private float qk;
    private boolean ql;
    private BitmapDescriptor qn;
    private LatLng qo;
    private float qp;
    private float qq;
    private LatLngBounds qr;
    private float qs;
    private float qt;
    private float qu;

    public GroundOverlayOptions() {
        this.ql = true;
        this.qs = 0.0f;
        this.qt = 0.5f;
        this.qu = 0.5f;
        this.iM = 1;
    }

    GroundOverlayOptions(int versionCode, IBinder wrappedImage, LatLng location, float width, float height, LatLngBounds bounds, float bearing, float zIndex, boolean visible, float transparency, float anchorU, float anchorV) {
        this.ql = true;
        this.qs = 0.0f;
        this.qt = 0.5f;
        this.qu = 0.5f;
        this.iM = versionCode;
        this.qn = new BitmapDescriptor(C0655a.m828z(wrappedImage));
        this.qo = location;
        this.qp = width;
        this.qq = height;
        this.qr = bounds;
        this.qd = bearing;
        this.qk = zIndex;
        this.ql = visible;
        this.qs = transparency;
        this.qt = anchorU;
        this.qu = anchorV;
    }

    private GroundOverlayOptions m1287a(LatLng latLng, float f, float f2) {
        this.qo = latLng;
        this.qp = f;
        this.qq = f2;
        return this;
    }

    public GroundOverlayOptions anchor(float u, float v) {
        this.qt = u;
        this.qu = v;
        return this;
    }

    public GroundOverlayOptions bearing(float bearing) {
        this.qd = ((bearing % 360.0f) + 360.0f) % 360.0f;
        return this;
    }

    IBinder cM() {
        return this.qn.cs().asBinder();
    }

    public int describeContents() {
        return 0;
    }

    public float getAnchorU() {
        return this.qt;
    }

    public float getAnchorV() {
        return this.qu;
    }

    public float getBearing() {
        return this.qd;
    }

    public LatLngBounds getBounds() {
        return this.qr;
    }

    public float getHeight() {
        return this.qq;
    }

    public BitmapDescriptor getImage() {
        return this.qn;
    }

    public LatLng getLocation() {
        return this.qo;
    }

    public float getTransparency() {
        return this.qs;
    }

    int getVersionCode() {
        return this.iM;
    }

    public float getWidth() {
        return this.qp;
    }

    public float getZIndex() {
        return this.qk;
    }

    public GroundOverlayOptions image(BitmapDescriptor image) {
        this.qn = image;
        return this;
    }

    public boolean isVisible() {
        return this.ql;
    }

    public GroundOverlayOptions position(LatLng location, float width) {
        boolean z = true;
        dm.m389a(this.qr == null, (Object) "Position has already been set using positionFromBounds");
        dm.m391b(location != null, "Location must be specified");
        if (width < 0.0f) {
            z = false;
        }
        dm.m391b(z, "Width must be non-negative");
        return m1287a(location, width, NO_DIMENSION);
    }

    public GroundOverlayOptions position(LatLng location, float width, float height) {
        boolean z = true;
        dm.m389a(this.qr == null, (Object) "Position has already been set using positionFromBounds");
        dm.m391b(location != null, "Location must be specified");
        dm.m391b(width >= 0.0f, "Width must be non-negative");
        if (height < 0.0f) {
            z = false;
        }
        dm.m391b(z, "Height must be non-negative");
        return m1287a(location, width, height);
    }

    public GroundOverlayOptions positionFromBounds(LatLngBounds bounds) {
        dm.m389a(this.qo == null, "Position has already been set using position: " + this.qo);
        this.qr = bounds;
        return this;
    }

    public GroundOverlayOptions transparency(float transparency) {
        boolean z = transparency >= 0.0f && transparency <= 1.0f;
        dm.m391b(z, "Transparency must be in the range [0..1]");
        this.qs = transparency;
        return this;
    }

    public GroundOverlayOptions visible(boolean visible) {
        this.ql = visible;
        return this;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (C0227r.cK()) {
            C0230c.m747a(this, out, flags);
        } else {
            GroundOverlayOptionsCreator.m735a(this, out, flags);
        }
    }

    public GroundOverlayOptions zIndex(float zIndex) {
        this.qk = zIndex;
        return this;
    }
}
