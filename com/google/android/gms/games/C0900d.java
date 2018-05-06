package com.google.android.gms.games;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import com.google.android.gms.common.data.C0096b;
import com.google.android.gms.common.data.C0646d;

public final class C0900d extends C0096b implements Player {
    public C0900d(C0646d c0646d, int i) {
        super(c0646d, i);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return PlayerEntity.m1581a(this, obj);
    }

    public Player freeze() {
        return new PlayerEntity(this);
    }

    public String getDisplayName() {
        return getString("profile_name");
    }

    public void getDisplayName(CharArrayBuffer dataOut) {
        m40a("profile_name", dataOut);
    }

    public Uri getHiResImageUri() {
        return m41u("profile_hi_res_image_uri");
    }

    public Uri getIconImageUri() {
        return m41u("profile_icon_image_uri");
    }

    public String getPlayerId() {
        return getString("external_player_id");
    }

    public long getRetrievedTimestamp() {
        return getLong("last_updated");
    }

    public boolean hasHiResImage() {
        return getHiResImageUri() != null;
    }

    public boolean hasIconImage() {
        return getIconImageUri() != null;
    }

    public int hashCode() {
        return PlayerEntity.m1580a(this);
    }

    public String toString() {
        return PlayerEntity.m1582b((Player) this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        ((PlayerEntity) freeze()).writeToParcel(dest, flags);
    }
}
