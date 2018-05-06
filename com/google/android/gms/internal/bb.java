package com.google.android.gms.internal;

import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdRequest.Gender;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import java.util.Date;
import java.util.HashSet;

public final class bb {
    public static int m193a(ErrorCode errorCode) {
        switch (errorCode) {
            case INVALID_REQUEST:
                return 1;
            case NETWORK_ERROR:
                return 2;
            case NO_FILL:
                return 3;
            default:
                return 0;
        }
    }

    public static int m194a(Gender gender) {
        switch (gender) {
            case FEMALE:
                return 2;
            case MALE:
                return 1;
            default:
                return 0;
        }
    }

    public static AdSize m195a(C0771x c0771x) {
        return new AdSize(new com.google.android.gms.ads.AdSize(c0771x.width, c0771x.height, c0771x.ew));
    }

    public static Gender m196e(int i) {
        switch (i) {
            case 1:
                return Gender.MALE;
            case 2:
                return Gender.FEMALE;
            default:
                return Gender.UNKNOWN;
        }
    }

    public static MediationAdRequest m197e(C0770v c0770v) {
        return new MediationAdRequest(new Date(c0770v.es), m196e(c0770v.et), c0770v.eu != null ? new HashSet(c0770v.eu) : null, c0770v.ev);
    }

    public static final ErrorCode m198f(int i) {
        switch (i) {
            case 1:
                return ErrorCode.INVALID_REQUEST;
            case 2:
                return ErrorCode.NETWORK_ERROR;
            case 3:
                return ErrorCode.NO_FILL;
            default:
                return ErrorCode.INTERNAL_ERROR;
        }
    }
}
