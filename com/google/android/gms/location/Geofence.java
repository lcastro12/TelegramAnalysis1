package com.google.android.gms.location;

import android.os.SystemClock;
import com.google.android.gms.internal.fa;

public interface Geofence {
    public static final int GEOFENCE_TRANSITION_DWELL = 4;
    public static final int GEOFENCE_TRANSITION_ENTER = 1;
    public static final int GEOFENCE_TRANSITION_EXIT = 2;
    public static final long NEVER_EXPIRE = -1;

    public static final class Builder {
        private String oA = null;
        private int oB = 0;
        private long oC = Long.MIN_VALUE;
        private short oD = (short) -1;
        private double oE;
        private double oF;
        private float oG;
        private int oH = 0;
        private int oI = -1;

        public Geofence build() {
            if (this.oA == null) {
                throw new IllegalArgumentException("Request ID not set.");
            } else if (this.oB == 0) {
                throw new IllegalArgumentException("Transitions types not set.");
            } else if ((this.oB & 4) != 0 && this.oI < 0) {
                throw new IllegalArgumentException("Non-negative loitering delay needs to be set when transition types include GEOFENCE_TRANSITION_DWELLING.");
            } else if (this.oC == Long.MIN_VALUE) {
                throw new IllegalArgumentException("Expiration not set.");
            } else if (this.oD == (short) -1) {
                throw new IllegalArgumentException("Geofence region not set.");
            } else if (this.oH >= 0) {
                return new fa(this.oA, this.oB, (short) 1, this.oE, this.oF, this.oG, this.oC, this.oH, this.oI);
            } else {
                throw new IllegalArgumentException("Notification responsiveness should be nonnegative.");
            }
        }

        public Builder setCircularRegion(double latitude, double longitude, float radius) {
            this.oD = (short) 1;
            this.oE = latitude;
            this.oF = longitude;
            this.oG = radius;
            return this;
        }

        public Builder setExpirationDuration(long durationMillis) {
            if (durationMillis < 0) {
                this.oC = -1;
            } else {
                this.oC = SystemClock.elapsedRealtime() + durationMillis;
            }
            return this;
        }

        public Builder setLoiteringDelay(int loiteringDelayMs) {
            this.oI = loiteringDelayMs;
            return this;
        }

        public Builder setNotificationResponsiveness(int notificationResponsivenessMs) {
            this.oH = notificationResponsivenessMs;
            return this;
        }

        public Builder setRequestId(String requestId) {
            this.oA = requestId;
            return this;
        }

        public Builder setTransitionTypes(int transitionTypes) {
            this.oB = transitionTypes;
            return this;
        }
    }

    String getRequestId();
}
