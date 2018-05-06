package com.google.android.gms.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.C0090R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.C0210a;
import com.google.android.gms.maps.internal.C0227r;
import com.google.android.gms.maps.model.CameraPosition;

public final class GoogleMapOptions implements SafeParcelable {
    public static final GoogleMapOptionsCreator CREATOR = new GoogleMapOptionsCreator();
    private final int iM;
    private CameraPosition pA;
    private Boolean pB;
    private Boolean pC;
    private Boolean pD;
    private Boolean pE;
    private Boolean pF;
    private Boolean pG;
    private Boolean px;
    private Boolean py;
    private int pz;

    public GoogleMapOptions() {
        this.pz = -1;
        this.iM = 1;
    }

    GoogleMapOptions(int versionCode, byte zOrderOnTop, byte useViewLifecycleInFragment, int mapType, CameraPosition camera, byte zoomControlsEnabled, byte compassEnabled, byte scrollGesturesEnabled, byte zoomGesturesEnabled, byte tiltGesturesEnabled, byte rotateGesturesEnabled) {
        this.pz = -1;
        this.iM = versionCode;
        this.px = C0210a.m710a(zOrderOnTop);
        this.py = C0210a.m710a(useViewLifecycleInFragment);
        this.pz = mapType;
        this.pA = camera;
        this.pB = C0210a.m710a(zoomControlsEnabled);
        this.pC = C0210a.m710a(compassEnabled);
        this.pD = C0210a.m710a(scrollGesturesEnabled);
        this.pE = C0210a.m710a(zoomGesturesEnabled);
        this.pF = C0210a.m710a(tiltGesturesEnabled);
        this.pG = C0210a.m710a(rotateGesturesEnabled);
    }

    public static GoogleMapOptions createFromAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return null;
        }
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attrs, C0090R.styleable.MapAttrs);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        if (obtainAttributes.hasValue(0)) {
            googleMapOptions.mapType(obtainAttributes.getInt(0, -1));
        }
        if (obtainAttributes.hasValue(13)) {
            googleMapOptions.zOrderOnTop(obtainAttributes.getBoolean(13, false));
        }
        if (obtainAttributes.hasValue(12)) {
            googleMapOptions.useViewLifecycleInFragment(obtainAttributes.getBoolean(12, false));
        }
        if (obtainAttributes.hasValue(6)) {
            googleMapOptions.compassEnabled(obtainAttributes.getBoolean(6, true));
        }
        if (obtainAttributes.hasValue(7)) {
            googleMapOptions.rotateGesturesEnabled(obtainAttributes.getBoolean(7, true));
        }
        if (obtainAttributes.hasValue(8)) {
            googleMapOptions.scrollGesturesEnabled(obtainAttributes.getBoolean(8, true));
        }
        if (obtainAttributes.hasValue(9)) {
            googleMapOptions.tiltGesturesEnabled(obtainAttributes.getBoolean(9, true));
        }
        if (obtainAttributes.hasValue(11)) {
            googleMapOptions.zoomGesturesEnabled(obtainAttributes.getBoolean(11, true));
        }
        if (obtainAttributes.hasValue(10)) {
            googleMapOptions.zoomControlsEnabled(obtainAttributes.getBoolean(10, true));
        }
        googleMapOptions.camera(CameraPosition.createFromAttributes(context, attrs));
        obtainAttributes.recycle();
        return googleMapOptions;
    }

    byte cA() {
        return C0210a.m711b(this.pE);
    }

    byte cB() {
        return C0210a.m711b(this.pF);
    }

    byte cC() {
        return C0210a.m711b(this.pG);
    }

    public GoogleMapOptions camera(CameraPosition camera) {
        this.pA = camera;
        return this;
    }

    public GoogleMapOptions compassEnabled(boolean enabled) {
        this.pC = Boolean.valueOf(enabled);
        return this;
    }

    byte cv() {
        return C0210a.m711b(this.px);
    }

    byte cw() {
        return C0210a.m711b(this.py);
    }

    byte cx() {
        return C0210a.m711b(this.pB);
    }

    byte cy() {
        return C0210a.m711b(this.pC);
    }

    byte cz() {
        return C0210a.m711b(this.pD);
    }

    public int describeContents() {
        return 0;
    }

    public CameraPosition getCamera() {
        return this.pA;
    }

    public Boolean getCompassEnabled() {
        return this.pC;
    }

    public int getMapType() {
        return this.pz;
    }

    public Boolean getRotateGesturesEnabled() {
        return this.pG;
    }

    public Boolean getScrollGesturesEnabled() {
        return this.pD;
    }

    public Boolean getTiltGesturesEnabled() {
        return this.pF;
    }

    public Boolean getUseViewLifecycleInFragment() {
        return this.py;
    }

    int getVersionCode() {
        return this.iM;
    }

    public Boolean getZOrderOnTop() {
        return this.px;
    }

    public Boolean getZoomControlsEnabled() {
        return this.pB;
    }

    public Boolean getZoomGesturesEnabled() {
        return this.pE;
    }

    public GoogleMapOptions mapType(int mapType) {
        this.pz = mapType;
        return this;
    }

    public GoogleMapOptions rotateGesturesEnabled(boolean enabled) {
        this.pG = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions scrollGesturesEnabled(boolean enabled) {
        this.pD = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions tiltGesturesEnabled(boolean enabled) {
        this.pF = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions useViewLifecycleInFragment(boolean useViewLifecycleInFragment) {
        this.py = Boolean.valueOf(useViewLifecycleInFragment);
        return this;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (C0227r.cK()) {
            C0209a.m709a(this, out, flags);
        } else {
            GoogleMapOptionsCreator.m708a(this, out, flags);
        }
    }

    public GoogleMapOptions zOrderOnTop(boolean zOrderOnTop) {
        this.px = Boolean.valueOf(zOrderOnTop);
        return this;
    }

    public GoogleMapOptions zoomControlsEnabled(boolean enabled) {
        this.pB = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions zoomGesturesEnabled(boolean enabled) {
        this.pE = Boolean.valueOf(enabled);
        return this;
    }
}
