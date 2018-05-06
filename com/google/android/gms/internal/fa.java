package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.Locale;

public class fa implements SafeParcelable, Geofence {
    public static final fb CREATOR = new fb();
    private final int iM;
    private final String oA;
    private final int oB;
    private final short oD;
    private final double oE;
    private final double oF;
    private final float oG;
    private final int oH;
    private final int oI;
    private final long pc;

    public fa(int i, String str, int i2, short s, double d, double d2, float f, long j, int i3, int i4) {
        m1185R(str);
        m1187b(f);
        m1186a(d, d2);
        int aa = aa(i2);
        this.iM = i;
        this.oD = s;
        this.oA = str;
        this.oE = d;
        this.oF = d2;
        this.oG = f;
        this.pc = j;
        this.oB = aa;
        this.oH = i3;
        this.oI = i4;
    }

    public fa(String str, int i, short s, double d, double d2, float f, long j, int i2, int i3) {
        this(1, str, i, s, d, d2, f, j, i2, i3);
    }

    private static void m1185R(String str) {
        if (str == null || str.length() > 100) {
            throw new IllegalArgumentException("requestId is null or too long: " + str);
        }
    }

    private static void m1186a(double d, double d2) {
        if (d > 90.0d || d < -90.0d) {
            throw new IllegalArgumentException("invalid latitude: " + d);
        } else if (d2 > 180.0d || d2 < -180.0d) {
            throw new IllegalArgumentException("invalid longitude: " + d2);
        }
    }

    private static int aa(int i) {
        int i2 = i & 7;
        if (i2 != 0) {
            return i2;
        }
        throw new IllegalArgumentException("No supported transition specified: " + i);
    }

    private static String ab(int i) {
        switch (i) {
            case 1:
                return "CIRCLE";
            default:
                return null;
        }
    }

    private static void m1187b(float f) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("invalid radius: " + f);
        }
    }

    public static fa m1188d(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        fa z = CREATOR.m584z(obtain);
        obtain.recycle();
        return z;
    }

    public short co() {
        return this.oD;
    }

    public float cp() {
        return this.oG;
    }

    public int cq() {
        return this.oB;
    }

    public int cr() {
        return this.oI;
    }

    public int describeContents() {
        fb fbVar = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof fa)) {
            return false;
        }
        fa faVar = (fa) obj;
        if (this.oG != faVar.oG) {
            return false;
        }
        if (this.oE != faVar.oE) {
            return false;
        }
        if (this.oF != faVar.oF) {
            return false;
        }
        return this.oD == faVar.oD;
    }

    public long getExpirationTime() {
        return this.pc;
    }

    public double getLatitude() {
        return this.oE;
    }

    public double getLongitude() {
        return this.oF;
    }

    public int getNotificationResponsiveness() {
        return this.oH;
    }

    public String getRequestId() {
        return this.oA;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.oE);
        int i = ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 31;
        long doubleToLongBits2 = Double.doubleToLongBits(this.oF);
        return (((((((i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)))) * 31) + Float.floatToIntBits(this.oG)) * 31) + this.oD) * 31) + this.oB;
    }

    public String toString() {
        return String.format(Locale.US, "Geofence[%s id:%s transitions:%d %.6f, %.6f %.0fm, resp=%ds, dwell=%dms, @%d]", new Object[]{ab(this.oD), this.oA, Integer.valueOf(this.oB), Double.valueOf(this.oE), Double.valueOf(this.oF), Float.valueOf(this.oG), Integer.valueOf(this.oH / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE), Integer.valueOf(this.oI), Long.valueOf(this.pc)});
    }

    public void writeToParcel(Parcel parcel, int flags) {
        fb fbVar = CREATOR;
        fb.m583a(this, parcel, flags);
    }
}
