package com.google.android.gms.location;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class DetectedActivity implements SafeParcelable {
    public static final DetectedActivityCreator CREATOR = new DetectedActivityCreator();
    public static final int IN_VEHICLE = 0;
    public static final int ON_BICYCLE = 1;
    public static final int ON_FOOT = 2;
    public static final int STILL = 3;
    public static final int TILTING = 5;
    public static final int UNKNOWN = 4;
    private final int iM;
    int oy;
    int oz;

    public DetectedActivity(int activityType, int confidence) {
        this.iM = 1;
        this.oy = activityType;
        this.oz = confidence;
    }

    public DetectedActivity(int versionCode, int activityType, int confidence) {
        this.iM = versionCode;
        this.oy = activityType;
        this.oz = confidence;
    }

    private int m1243W(int i) {
        return i > 6 ? 4 : i;
    }

    public int describeContents() {
        return 0;
    }

    public int getConfidence() {
        return this.oz;
    }

    public int getType() {
        return m1243W(this.oy);
    }

    public int getVersionCode() {
        return this.iM;
    }

    public String toString() {
        return "DetectedActivity [type=" + getType() + ", confidence=" + this.oz + "]";
    }

    public void writeToParcel(Parcel out, int flags) {
        DetectedActivityCreator.m704a(this, out, flags);
    }
}
