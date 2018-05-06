package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class LineItem implements SafeParcelable {
    public static final Creator<LineItem> CREATOR = new C0258e();
    String description;
    private final int iM;
    String tD;
    String tE;
    String tQ;
    String tR;
    int tS;

    public final class Builder {
        final /* synthetic */ LineItem tT;

        private Builder(LineItem lineItem) {
            this.tT = lineItem;
        }

        public LineItem build() {
            return this.tT;
        }

        public Builder setCurrencyCode(String currencyCode) {
            this.tT.tE = currencyCode;
            return this;
        }

        public Builder setDescription(String description) {
            this.tT.description = description;
            return this;
        }

        public Builder setQuantity(String quantity) {
            this.tT.tQ = quantity;
            return this;
        }

        public Builder setRole(int role) {
            this.tT.tS = role;
            return this;
        }

        public Builder setTotalPrice(String totalPrice) {
            this.tT.tD = totalPrice;
            return this;
        }

        public Builder setUnitPrice(String unitPrice) {
            this.tT.tR = unitPrice;
            return this;
        }
    }

    public interface Role {
        public static final int REGULAR = 0;
        public static final int SHIPPING = 2;
        public static final int TAX = 1;
    }

    public LineItem() {
        this.tS = 0;
        this.iM = 1;
    }

    LineItem(int versionCode, String description, String quantity, String unitPrice, String totalPrice, int role, String currencyCode) {
        this.tS = 0;
        this.iM = versionCode;
        this.description = description;
        this.tQ = quantity;
        this.tR = unitPrice;
        this.tD = totalPrice;
        this.tS = role;
        this.tE = currencyCode;
    }

    public static Builder newBuilder() {
        LineItem lineItem = new LineItem();
        lineItem.getClass();
        return new Builder();
    }

    public int describeContents() {
        return 0;
    }

    public String getCurrencyCode() {
        return this.tE;
    }

    public String getDescription() {
        return this.description;
    }

    public String getQuantity() {
        return this.tQ;
    }

    public int getRole() {
        return this.tS;
    }

    public String getTotalPrice() {
        return this.tD;
    }

    public String getUnitPrice() {
        return this.tR;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public void writeToParcel(Parcel dest, int flags) {
        C0258e.m784a(this, dest, flags);
    }
}
