package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class MaskedWalletRequest implements SafeParcelable {
    public static final Creator<MaskedWalletRequest> CREATOR = new C0261h();
    private final int iM;
    String tE;
    String tI;
    Cart tO;
    boolean ub;
    boolean uc;
    boolean ud;
    String ue;
    String uf;
    boolean ug;
    boolean uh;

    public final class Builder {
        final /* synthetic */ MaskedWalletRequest ui;

        private Builder(MaskedWalletRequest maskedWalletRequest) {
            this.ui = maskedWalletRequest;
        }

        public MaskedWalletRequest build() {
            return this.ui;
        }

        public Builder setCart(Cart cart) {
            this.ui.tO = cart;
            return this;
        }

        public Builder setCurrencyCode(String currencyCode) {
            this.ui.tE = currencyCode;
            return this;
        }

        public Builder setEstimatedTotalPrice(String estimatedTotalPrice) {
            this.ui.ue = estimatedTotalPrice;
            return this;
        }

        public Builder setIsBillingAgreement(boolean isBillingAgreement) {
            this.ui.uh = isBillingAgreement;
            return this;
        }

        public Builder setMerchantName(String merchantName) {
            this.ui.uf = merchantName;
            return this;
        }

        public Builder setMerchantTransactionId(String merchantTransactionId) {
            this.ui.tI = merchantTransactionId;
            return this;
        }

        public Builder setPhoneNumberRequired(boolean phoneNumberRequired) {
            this.ui.ub = phoneNumberRequired;
            return this;
        }

        public Builder setShippingAddressRequired(boolean shippingAddressRequired) {
            this.ui.uc = shippingAddressRequired;
            return this;
        }

        public Builder setShouldRetrieveWalletObjects(boolean shouldRetrieveWalletObjects) {
            this.ui.ug = shouldRetrieveWalletObjects;
            return this;
        }

        public Builder setUseMinimalBillingAddress(boolean useMinimalBillingAddress) {
            this.ui.ud = useMinimalBillingAddress;
            return this;
        }
    }

    public MaskedWalletRequest() {
        this.iM = 2;
    }

    MaskedWalletRequest(int versionCode, String merchantTransactionId, boolean phoneNumberRequired, boolean shippingAddressRequired, boolean useMinimalBillingAddress, String estimatedTotalPrice, String currencyCode, String merchantName, Cart cart, boolean shouldRetrieveWalletObjects, boolean isBillingAgreement) {
        this.iM = versionCode;
        this.tI = merchantTransactionId;
        this.ub = phoneNumberRequired;
        this.uc = shippingAddressRequired;
        this.ud = useMinimalBillingAddress;
        this.ue = estimatedTotalPrice;
        this.tE = currencyCode;
        this.uf = merchantName;
        this.tO = cart;
        this.ug = shouldRetrieveWalletObjects;
        this.uh = isBillingAgreement;
    }

    public static Builder newBuilder() {
        MaskedWalletRequest maskedWalletRequest = new MaskedWalletRequest();
        maskedWalletRequest.getClass();
        return new Builder();
    }

    public int describeContents() {
        return 0;
    }

    public Cart getCart() {
        return this.tO;
    }

    public String getCurrencyCode() {
        return this.tE;
    }

    public String getEstimatedTotalPrice() {
        return this.ue;
    }

    public String getMerchantName() {
        return this.uf;
    }

    public String getMerchantTransactionId() {
        return this.tI;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public boolean isBillingAgreement() {
        return this.uh;
    }

    public boolean isPhoneNumberRequired() {
        return this.ub;
    }

    public boolean isShippingAddressRequired() {
        return this.uc;
    }

    public boolean shouldRetrieveWalletObjects() {
        return this.ug;
    }

    public boolean useMinimalBillingAddress() {
        return this.ud;
    }

    public void writeToParcel(Parcel dest, int flags) {
        C0261h.m790a(this, dest, flags);
    }
}
