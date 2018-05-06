package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.support.v4.view.ViewCompat;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.C0227r;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PolygonOptions implements SafeParcelable {
    public static final PolygonOptionsCreator CREATOR = new PolygonOptionsCreator();
    private final int iM;
    private final List<LatLng> qK;
    private final List<List<LatLng>> qL;
    private boolean qM;
    private float qh;
    private int qi;
    private int qj;
    private float qk;
    private boolean ql;

    public PolygonOptions() {
        this.qh = 10.0f;
        this.qi = ViewCompat.MEASURED_STATE_MASK;
        this.qj = 0;
        this.qk = 0.0f;
        this.ql = true;
        this.qM = false;
        this.iM = 1;
        this.qK = new ArrayList();
        this.qL = new ArrayList();
    }

    PolygonOptions(int versionCode, List<LatLng> points, List holes, float strokeWidth, int strokeColor, int fillColor, float zIndex, boolean visible, boolean geodesic) {
        this.qh = 10.0f;
        this.qi = ViewCompat.MEASURED_STATE_MASK;
        this.qj = 0;
        this.qk = 0.0f;
        this.ql = true;
        this.qM = false;
        this.iM = versionCode;
        this.qK = points;
        this.qL = holes;
        this.qh = strokeWidth;
        this.qi = strokeColor;
        this.qj = fillColor;
        this.qk = zIndex;
        this.ql = visible;
        this.qM = geodesic;
    }

    public PolygonOptions add(LatLng point) {
        this.qK.add(point);
        return this;
    }

    public PolygonOptions add(LatLng... points) {
        this.qK.addAll(Arrays.asList(points));
        return this;
    }

    public PolygonOptions addAll(Iterable<LatLng> points) {
        for (LatLng add : points) {
            this.qK.add(add);
        }
        return this;
    }

    public PolygonOptions addHole(Iterable<LatLng> points) {
        ArrayList arrayList = new ArrayList();
        for (LatLng add : points) {
            arrayList.add(add);
        }
        this.qL.add(arrayList);
        return this;
    }

    List cO() {
        return this.qL;
    }

    public int describeContents() {
        return 0;
    }

    public PolygonOptions fillColor(int color) {
        this.qj = color;
        return this;
    }

    public PolygonOptions geodesic(boolean geodesic) {
        this.qM = geodesic;
        return this;
    }

    public int getFillColor() {
        return this.qj;
    }

    public List<List<LatLng>> getHoles() {
        return this.qL;
    }

    public List<LatLng> getPoints() {
        return this.qK;
    }

    public int getStrokeColor() {
        return this.qi;
    }

    public float getStrokeWidth() {
        return this.qh;
    }

    int getVersionCode() {
        return this.iM;
    }

    public float getZIndex() {
        return this.qk;
    }

    public boolean isGeodesic() {
        return this.qM;
    }

    public boolean isVisible() {
        return this.ql;
    }

    public PolygonOptions strokeColor(int color) {
        this.qi = color;
        return this;
    }

    public PolygonOptions strokeWidth(float width) {
        this.qh = width;
        return this;
    }

    public PolygonOptions visible(boolean visible) {
        this.ql = visible;
        return this;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (C0227r.cK()) {
            C0234g.m751a(this, out, flags);
        } else {
            PolygonOptionsCreator.m740a(this, out, flags);
        }
    }

    public PolygonOptions zIndex(float zIndex) {
        this.qk = zIndex;
        return this;
    }
}
