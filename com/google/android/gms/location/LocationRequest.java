package com.google.android.gms.location;

import android.os.Parcel;
import android.os.SystemClock;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dl;

public final class LocationRequest implements SafeParcelable {
    public static final LocationRequestCreator CREATOR = new LocationRequestCreator();
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    public static final int PRIORITY_HIGH_ACCURACY = 100;
    public static final int PRIORITY_LOW_POWER = 104;
    public static final int PRIORITY_NO_POWER = 105;
    private final int iM;
    int mPriority;
    long oC;
    long oJ;
    long oK;
    boolean oL;
    int oM;
    float oN;

    public LocationRequest() {
        this.iM = 1;
        this.mPriority = PRIORITY_BALANCED_POWER_ACCURACY;
        this.oJ = 3600000;
        this.oK = 600000;
        this.oL = false;
        this.oC = Long.MAX_VALUE;
        this.oM = Integer.MAX_VALUE;
        this.oN = 0.0f;
    }

    LocationRequest(int versionCode, int priority, long interval, long fastestInterval, boolean explicitFastestInterval, long expireAt, int numUpdates, float smallestDisplacement) {
        this.iM = versionCode;
        this.mPriority = priority;
        this.oJ = interval;
        this.oK = fastestInterval;
        this.oL = explicitFastestInterval;
        this.oC = expireAt;
        this.oM = numUpdates;
        this.oN = smallestDisplacement;
    }

    private static void m1244X(int i) {
        switch (i) {
            case PRIORITY_HIGH_ACCURACY /*100*/:
            case PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
            case PRIORITY_LOW_POWER /*104*/:
            case PRIORITY_NO_POWER /*105*/:
                return;
            default:
                throw new IllegalArgumentException("invalid quality: " + i);
        }
    }

    public static String m1245Y(int i) {
        switch (i) {
            case PRIORITY_HIGH_ACCURACY /*100*/:
                return "PRIORITY_HIGH_ACCURACY";
            case PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                return "PRIORITY_BALANCED_POWER_ACCURACY";
            case PRIORITY_LOW_POWER /*104*/:
                return "PRIORITY_LOW_POWER";
            case PRIORITY_NO_POWER /*105*/:
                return "PRIORITY_NO_POWER";
            default:
                return "???";
        }
    }

    private static void m1246a(float f) {
        if (f < 0.0f) {
            throw new IllegalArgumentException("invalid displacement: " + f);
        }
    }

    public static LocationRequest create() {
        return new LocationRequest();
    }

    private static void m1247h(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("invalid interval: " + j);
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof LocationRequest)) {
            return false;
        }
        LocationRequest locationRequest = (LocationRequest) object;
        return this.mPriority == locationRequest.mPriority && this.oJ == locationRequest.oJ && this.oK == locationRequest.oK && this.oL == locationRequest.oL && this.oC == locationRequest.oC && this.oM == locationRequest.oM && this.oN == locationRequest.oN;
    }

    public long getExpirationTime() {
        return this.oC;
    }

    public long getFastestInterval() {
        return this.oK;
    }

    public long getInterval() {
        return this.oJ;
    }

    public int getNumUpdates() {
        return this.oM;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public float getSmallestDisplacement() {
        return this.oN;
    }

    int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return dl.hashCode(Integer.valueOf(this.mPriority), Long.valueOf(this.oJ), Long.valueOf(this.oK), Boolean.valueOf(this.oL), Long.valueOf(this.oC), Integer.valueOf(this.oM), Float.valueOf(this.oN));
    }

    public LocationRequest setExpirationDuration(long millis) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (millis > Long.MAX_VALUE - elapsedRealtime) {
            this.oC = Long.MAX_VALUE;
        } else {
            this.oC = elapsedRealtime + millis;
        }
        if (this.oC < 0) {
            this.oC = 0;
        }
        return this;
    }

    public LocationRequest setExpirationTime(long millis) {
        this.oC = millis;
        if (this.oC < 0) {
            this.oC = 0;
        }
        return this;
    }

    public LocationRequest setFastestInterval(long millis) {
        m1247h(millis);
        this.oL = true;
        this.oK = millis;
        return this;
    }

    public LocationRequest setInterval(long millis) {
        m1247h(millis);
        this.oJ = millis;
        if (!this.oL) {
            this.oK = (long) (((double) this.oJ) / 6.0d);
        }
        return this;
    }

    public LocationRequest setNumUpdates(int numUpdates) {
        if (numUpdates <= 0) {
            throw new IllegalArgumentException("invalid numUpdates: " + numUpdates);
        }
        this.oM = numUpdates;
        return this;
    }

    public LocationRequest setPriority(int priority) {
        m1244X(priority);
        this.mPriority = priority;
        return this;
    }

    public LocationRequest setSmallestDisplacement(float smallestDisplacementMeters) {
        m1246a(smallestDisplacementMeters);
        this.oN = smallestDisplacementMeters;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Request[").append(m1245Y(this.mPriority));
        if (this.mPriority != PRIORITY_NO_POWER) {
            stringBuilder.append(" requested=");
            stringBuilder.append(this.oJ + "ms");
        }
        stringBuilder.append(" fastest=");
        stringBuilder.append(this.oK + "ms");
        if (this.oC != Long.MAX_VALUE) {
            long elapsedRealtime = this.oC - SystemClock.elapsedRealtime();
            stringBuilder.append(" expireIn=");
            stringBuilder.append(elapsedRealtime + "ms");
        }
        if (this.oM != Integer.MAX_VALUE) {
            stringBuilder.append(" num=").append(this.oM);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        LocationRequestCreator.m705a(this, parcel, flags);
    }
}
