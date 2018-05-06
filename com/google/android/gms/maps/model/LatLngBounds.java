package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.C0227r;

public final class LatLngBounds implements SafeParcelable {
    public static final LatLngBoundsCreator CREATOR = new LatLngBoundsCreator();
    private final int iM;
    public final LatLng northeast;
    public final LatLng southwest;

    public static final class Builder {
        private double qv = Double.POSITIVE_INFINITY;
        private double qw = Double.NEGATIVE_INFINITY;
        private double qx = Double.NaN;
        private double qy = Double.NaN;

        private boolean m736b(double d) {
            boolean z = false;
            if (this.qx <= this.qy) {
                return this.qx <= d && d <= this.qy;
            } else {
                if (this.qx <= d || d <= this.qy) {
                    z = true;
                }
                return z;
            }
        }

        public LatLngBounds build() {
            dm.m389a(!Double.isNaN(this.qx), (Object) "no included points");
            return new LatLngBounds(new LatLng(this.qv, this.qx), new LatLng(this.qw, this.qy));
        }

        public Builder include(LatLng point) {
            this.qv = Math.min(this.qv, point.latitude);
            this.qw = Math.max(this.qw, point.latitude);
            double d = point.longitude;
            if (Double.isNaN(this.qx)) {
                this.qx = d;
                this.qy = d;
            } else if (!m736b(d)) {
                if (LatLngBounds.m1289b(this.qx, d) < LatLngBounds.m1291c(this.qy, d)) {
                    this.qx = d;
                } else {
                    this.qy = d;
                }
            }
            return this;
        }
    }

    LatLngBounds(int versionCode, LatLng southwest, LatLng northeast) {
        dm.m388a((Object) southwest, (Object) "null southwest");
        dm.m388a((Object) northeast, (Object) "null northeast");
        dm.m390a(northeast.latitude >= southwest.latitude, "southern latitude exceeds northern latitude (%s > %s)", Double.valueOf(southwest.latitude), Double.valueOf(northeast.latitude));
        this.iM = versionCode;
        this.southwest = southwest;
        this.northeast = northeast;
    }

    public LatLngBounds(LatLng southwest, LatLng northeast) {
        this(1, southwest, northeast);
    }

    private boolean m1288a(double d) {
        return this.southwest.latitude <= d && d <= this.northeast.latitude;
    }

    private static double m1289b(double d, double d2) {
        return ((d - d2) + 360.0d) % 360.0d;
    }

    private boolean m1290b(double d) {
        boolean z = false;
        if (this.southwest.longitude <= this.northeast.longitude) {
            return this.southwest.longitude <= d && d <= this.northeast.longitude;
        } else {
            if (this.southwest.longitude <= d || d <= this.northeast.longitude) {
                z = true;
            }
            return z;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private static double m1291c(double d, double d2) {
        return ((d2 - d) + 360.0d) % 360.0d;
    }

    public boolean contains(LatLng point) {
        return m1288a(point.latitude) && m1290b(point.longitude);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LatLngBounds)) {
            return false;
        }
        LatLngBounds latLngBounds = (LatLngBounds) o;
        return this.southwest.equals(latLngBounds.southwest) && this.northeast.equals(latLngBounds.northeast);
    }

    public LatLng getCenter() {
        double d = (this.southwest.latitude + this.northeast.latitude) / 2.0d;
        double d2 = this.northeast.longitude;
        double d3 = this.southwest.longitude;
        return new LatLng(d, d3 <= d2 ? (d2 + d3) / 2.0d : ((d2 + 360.0d) + d3) / 2.0d);
    }

    int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return dl.hashCode(this.southwest, this.northeast);
    }

    public LatLngBounds including(LatLng point) {
        double min = Math.min(this.southwest.latitude, point.latitude);
        double max = Math.max(this.northeast.latitude, point.latitude);
        double d = this.northeast.longitude;
        double d2 = this.southwest.longitude;
        double d3 = point.longitude;
        if (m1290b(d3)) {
            d3 = d2;
            d2 = d;
        } else if (m1289b(d2, d3) < m1291c(d, d3)) {
            d2 = d;
        } else {
            double d4 = d2;
            d2 = d3;
            d3 = d4;
        }
        return new LatLngBounds(new LatLng(min, d3), new LatLng(max, d2));
    }

    public String toString() {
        return dl.m387d(this).m386a("southwest", this.southwest).m386a("northeast", this.northeast).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        if (C0227r.cK()) {
            C0231d.m748a(this, out, flags);
        } else {
            LatLngBoundsCreator.m737a(this, out, flags);
        }
    }
}
