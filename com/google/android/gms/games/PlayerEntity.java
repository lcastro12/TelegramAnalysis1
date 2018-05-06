package com.google.android.gms.games;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.db;
import com.google.android.gms.internal.dd;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.eg;
import com.google.android.gms.internal.en;

public final class PlayerEntity extends en implements Player {
    public static final Creator<PlayerEntity> CREATOR = new C0657a();
    private final int iM;
    private final String mD;
    private final long mE;
    private final String ml;
    private final Uri mq;
    private final Uri mr;

    static final class C0657a extends C0118c {
        C0657a() {
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return mo712u(x0);
        }

        public PlayerEntity mo712u(Parcel parcel) {
            Uri uri = null;
            if (en.m1439c(dd.aW()) || dd.m942y(PlayerEntity.class.getCanonicalName())) {
                return super.mo712u(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            String readString4 = parcel.readString();
            Uri parse = readString3 == null ? null : Uri.parse(readString3);
            if (readString4 != null) {
                uri = Uri.parse(readString4);
            }
            return new PlayerEntity(1, readString, readString2, parse, uri, parcel.readLong());
        }
    }

    PlayerEntity(int versionCode, String playerId, String displayName, Uri iconImageUri, Uri hiResImageUri, long retrievedTimestamp) {
        this.iM = versionCode;
        this.mD = playerId;
        this.ml = displayName;
        this.mq = iconImageUri;
        this.mr = hiResImageUri;
        this.mE = retrievedTimestamp;
    }

    public PlayerEntity(Player player) {
        boolean z = true;
        this.iM = 1;
        this.mD = player.getPlayerId();
        this.ml = player.getDisplayName();
        this.mq = player.getIconImageUri();
        this.mr = player.getHiResImageUri();
        this.mE = player.getRetrievedTimestamp();
        db.m342c(this.mD);
        db.m342c(this.ml);
        if (this.mE <= 0) {
            z = false;
        }
        db.m343k(z);
    }

    static int m1580a(Player player) {
        return dl.hashCode(player.getPlayerId(), player.getDisplayName(), player.getIconImageUri(), player.getHiResImageUri(), Long.valueOf(player.getRetrievedTimestamp()));
    }

    static boolean m1581a(Player player, Object obj) {
        if (!(obj instanceof Player)) {
            return false;
        }
        if (player == obj) {
            return true;
        }
        Player player2 = (Player) obj;
        return dl.equal(player2.getPlayerId(), player.getPlayerId()) && dl.equal(player2.getDisplayName(), player.getDisplayName()) && dl.equal(player2.getIconImageUri(), player.getIconImageUri()) && dl.equal(player2.getHiResImageUri(), player.getHiResImageUri()) && dl.equal(Long.valueOf(player2.getRetrievedTimestamp()), Long.valueOf(player.getRetrievedTimestamp()));
    }

    static String m1582b(Player player) {
        return dl.m387d(player).m386a("PlayerId", player.getPlayerId()).m386a("DisplayName", player.getDisplayName()).m386a("IconImageUri", player.getIconImageUri()).m386a("HiResImageUri", player.getHiResImageUri()).m386a("RetrievedTimestamp", Long.valueOf(player.getRetrievedTimestamp())).toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return m1581a(this, obj);
    }

    public Player freeze() {
        return this;
    }

    public String getDisplayName() {
        return this.ml;
    }

    public void getDisplayName(CharArrayBuffer dataOut) {
        eg.m447b(this.ml, dataOut);
    }

    public Uri getHiResImageUri() {
        return this.mr;
    }

    public Uri getIconImageUri() {
        return this.mq;
    }

    public String getPlayerId() {
        return this.mD;
    }

    public long getRetrievedTimestamp() {
        return this.mE;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public boolean hasHiResImage() {
        return getHiResImageUri() != null;
    }

    public boolean hasIconImage() {
        return getIconImageUri() != null;
    }

    public int hashCode() {
        return m1580a(this);
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return m1582b((Player) this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        String str = null;
        if (aX()) {
            dest.writeString(this.mD);
            dest.writeString(this.ml);
            dest.writeString(this.mq == null ? null : this.mq.toString());
            if (this.mr != null) {
                str = this.mr.toString();
            }
            dest.writeString(str);
            dest.writeLong(this.mE);
            return;
        }
        C0118c.m149a(this, dest, flags);
    }
}
