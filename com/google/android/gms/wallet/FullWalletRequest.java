package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class FullWalletRequest implements SafeParcelable {
    public static final Creator<FullWalletRequest> CREATOR = new C0257d();
    private final int iM;
    String tH;
    String tI;
    Cart tO;

    public final class Builder {
        final /* synthetic */ FullWalletRequest tP;

        private Builder(FullWalletRequest fullWalletRequest) {
            this.tP = fullWalletRequest;
        }

        public FullWalletRequest build() {
            return this.tP;
        }

        public Builder setCart(Cart cart) {
            this.tP.tO = cart;
            return this;
        }

        public Builder setGoogleTransactionId(String googleTransactionId) {
            this.tP.tH = googleTransactionId;
            return this;
        }

        public Builder setMerchantTransactionId(String merchantTransactionId) {
            this.tP.tI = merchantTransactionId;
            return this;
        }
    }

    public FullWalletRequest() {
        this.iM = 1;
    }

    FullWalletRequest(int versionCode, String googleTransactionId, String merchantTransactionId, Cart cart) {
        this.iM = versionCode;
        this.tH = googleTransactionId;
        this.tI = merchantTransactionId;
        this.tO = cart;
    }

    public static Builder newBuilder() {
        FullWalletRequest fullWalletRequest = new FullWalletRequest();
        fullWalletRequest.getClass();
        return new Builder();
    }

    public int describeContents() {
        return 0;
    }

    public Cart getCart() {
        return this.tO;
    }

    public String getGoogleTransactionId() {
        return this.tH;
    }

    public String getMerchantTransactionId() {
        return this.tI;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public void writeToParcel(Parcel dest, int flags) {
        C0257d.m782a(this, dest, flags);
    }
}
