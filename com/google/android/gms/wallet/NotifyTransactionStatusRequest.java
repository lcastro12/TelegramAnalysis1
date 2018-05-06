package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dm;

public class NotifyTransactionStatusRequest implements SafeParcelable {
    public static final Creator<NotifyTransactionStatusRequest> CREATOR = new C0262i();
    final int iM;
    int status;
    String tH;
    String uj;

    public final class Builder {
        final /* synthetic */ NotifyTransactionStatusRequest uk;

        private Builder(NotifyTransactionStatusRequest notifyTransactionStatusRequest) {
            this.uk = notifyTransactionStatusRequest;
        }

        public NotifyTransactionStatusRequest build() {
            boolean z = true;
            dm.m391b(!TextUtils.isEmpty(this.uk.tH), "googleTransactionId is required");
            if (this.uk.status < 1 || this.uk.status > 8) {
                z = false;
            }
            dm.m391b(z, "status is an unrecognized value");
            return this.uk;
        }

        public Builder setDetailedReason(String detailedReason) {
            this.uk.uj = detailedReason;
            return this;
        }

        public Builder setGoogleTransactionId(String googleTransactionId) {
            this.uk.tH = googleTransactionId;
            return this;
        }

        public Builder setStatus(int status) {
            this.uk.status = status;
            return this;
        }
    }

    public interface Status {
        public static final int SUCCESS = 1;

        public interface Error {
            public static final int AVS_DECLINE = 7;
            public static final int BAD_CARD = 4;
            public static final int BAD_CVC = 3;
            public static final int DECLINED = 5;
            public static final int FRAUD_DECLINE = 8;
            public static final int OTHER = 6;
            public static final int UNKNOWN = 2;
        }
    }

    public NotifyTransactionStatusRequest() {
        this.iM = 1;
    }

    NotifyTransactionStatusRequest(int versionCode, String googleTransactionId, int status, String detailedReason) {
        this.iM = versionCode;
        this.tH = googleTransactionId;
        this.status = status;
        this.uj = detailedReason;
    }

    public static Builder newBuilder() {
        NotifyTransactionStatusRequest notifyTransactionStatusRequest = new NotifyTransactionStatusRequest();
        notifyTransactionStatusRequest.getClass();
        return new Builder();
    }

    public int describeContents() {
        return 0;
    }

    public String getDetailedReason() {
        return this.uj;
    }

    public String getGoogleTransactionId() {
        return this.tH;
    }

    public int getStatus() {
        return this.status;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public void writeToParcel(Parcel out, int flags) {
        C0262i.m792a(this, out, flags);
    }
}
