package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements SafeParcelable {
    public static final Creator<Cart> CREATOR = new C0255b();
    private final int iM;
    String tD;
    String tE;
    ArrayList<LineItem> tF;

    public final class Builder {
        final /* synthetic */ Cart tG;

        private Builder(Cart cart) {
            this.tG = cart;
        }

        public Builder addLineItem(LineItem lineItem) {
            this.tG.tF.add(lineItem);
            return this;
        }

        public Cart build() {
            return this.tG;
        }

        public Builder setCurrencyCode(String currencyCode) {
            this.tG.tE = currencyCode;
            return this;
        }

        public Builder setLineItems(List<LineItem> lineItems) {
            this.tG.tF.clear();
            this.tG.tF.addAll(lineItems);
            return this;
        }

        public Builder setTotalPrice(String totalPrice) {
            this.tG.tD = totalPrice;
            return this;
        }
    }

    public Cart() {
        this.iM = 1;
        this.tF = new ArrayList();
    }

    Cart(int versionCode, String totalPrice, String currencyCode, ArrayList<LineItem> lineItems) {
        this.iM = versionCode;
        this.tD = totalPrice;
        this.tE = currencyCode;
        this.tF = lineItems;
    }

    public static Builder newBuilder() {
        Cart cart = new Cart();
        cart.getClass();
        return new Builder();
    }

    public int describeContents() {
        return 0;
    }

    public String getCurrencyCode() {
        return this.tE;
    }

    public ArrayList<LineItem> getLineItems() {
        return this.tF;
    }

    public String getTotalPrice() {
        return this.tD;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public void writeToParcel(Parcel dest, int flags) {
        C0255b.m778a(this, dest, flags);
    }
}
