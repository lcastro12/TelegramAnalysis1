package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.C0112b.C0655a;
import com.google.android.gms.maps.internal.C0227r;

public final class MarkerOptions implements SafeParcelable {
    public static final MarkerOptionsCreator CREATOR = new MarkerOptionsCreator();
    private final int iM;
    private float mAlpha;
    private LatLng qA;
    private String qB;
    private String qC;
    private BitmapDescriptor qD;
    private boolean qE;
    private boolean qF;
    private float qG;
    private float qH;
    private float qI;
    private boolean ql;
    private float qt;
    private float qu;

    public MarkerOptions() {
        this.qt = 0.5f;
        this.qu = 1.0f;
        this.ql = true;
        this.qF = false;
        this.qG = 0.0f;
        this.qH = 0.5f;
        this.qI = 0.0f;
        this.mAlpha = 1.0f;
        this.iM = 1;
    }

    MarkerOptions(int versionCode, LatLng position, String title, String snippet, IBinder wrappedIcon, float anchorU, float anchorV, boolean draggable, boolean visible, boolean flat, float rotation, float infoWindowAnchorU, float infoWindowAnchorV, float alpha) {
        this.qt = 0.5f;
        this.qu = 1.0f;
        this.ql = true;
        this.qF = false;
        this.qG = 0.0f;
        this.qH = 0.5f;
        this.qI = 0.0f;
        this.mAlpha = 1.0f;
        this.iM = versionCode;
        this.qA = position;
        this.qB = title;
        this.qC = snippet;
        this.qD = wrappedIcon == null ? null : new BitmapDescriptor(C0655a.m828z(wrappedIcon));
        this.qt = anchorU;
        this.qu = anchorV;
        this.qE = draggable;
        this.ql = visible;
        this.qF = flat;
        this.qG = rotation;
        this.qH = infoWindowAnchorU;
        this.qI = infoWindowAnchorV;
        this.mAlpha = alpha;
    }

    public MarkerOptions alpha(float alpha) {
        this.mAlpha = alpha;
        return this;
    }

    public MarkerOptions anchor(float u, float v) {
        this.qt = u;
        this.qu = v;
        return this;
    }

    IBinder cN() {
        return this.qD == null ? null : this.qD.cs().asBinder();
    }

    public int describeContents() {
        return 0;
    }

    public MarkerOptions draggable(boolean draggable) {
        this.qE = draggable;
        return this;
    }

    public MarkerOptions flat(boolean flat) {
        this.qF = flat;
        return this;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public float getAnchorU() {
        return this.qt;
    }

    public float getAnchorV() {
        return this.qu;
    }

    public BitmapDescriptor getIcon() {
        return this.qD;
    }

    public float getInfoWindowAnchorU() {
        return this.qH;
    }

    public float getInfoWindowAnchorV() {
        return this.qI;
    }

    public LatLng getPosition() {
        return this.qA;
    }

    public float getRotation() {
        return this.qG;
    }

    public String getSnippet() {
        return this.qC;
    }

    public String getTitle() {
        return this.qB;
    }

    int getVersionCode() {
        return this.iM;
    }

    public MarkerOptions icon(BitmapDescriptor icon) {
        this.qD = icon;
        return this;
    }

    public MarkerOptions infoWindowAnchor(float u, float v) {
        this.qH = u;
        this.qI = v;
        return this;
    }

    public boolean isDraggable() {
        return this.qE;
    }

    public boolean isFlat() {
        return this.qF;
    }

    public boolean isVisible() {
        return this.ql;
    }

    public MarkerOptions position(LatLng position) {
        this.qA = position;
        return this;
    }

    public MarkerOptions rotation(float rotation) {
        this.qG = rotation;
        return this;
    }

    public MarkerOptions snippet(String snippet) {
        this.qC = snippet;
        return this;
    }

    public MarkerOptions title(String title) {
        this.qB = title;
        return this;
    }

    public MarkerOptions visible(boolean visible) {
        this.ql = visible;
        return this;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (C0227r.cK()) {
            C0233f.m750a(this, out, flags);
        } else {
            MarkerOptionsCreator.m739a(this, out, flags);
        }
    }
}
