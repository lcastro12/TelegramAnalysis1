package com.google.android.gms.games.multiplayer;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.internal.dd;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.eg;
import com.google.android.gms.internal.en;

public final class ParticipantEntity extends en implements Participant {
    public static final Creator<ParticipantEntity> CREATOR = new C0662a();
    private final int iM;
    private final String ml;
    private final Uri mq;
    private final Uri mr;
    private final int nU;
    private final String nV;
    private final boolean nW;
    private final PlayerEntity nX;
    private final int nY;
    private final String nd;

    static final class C0662a extends C0121c {
        C0662a() {
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return mo748w(x0);
        }

        public ParticipantEntity mo748w(Parcel parcel) {
            int i = 0;
            if (en.m1439c(dd.aW()) || dd.m942y(ParticipantEntity.class.getCanonicalName())) {
                return super.mo748w(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            Uri parse = readString3 == null ? null : Uri.parse(readString3);
            String readString4 = parcel.readString();
            Uri parse2 = readString4 == null ? null : Uri.parse(readString4);
            int readInt = parcel.readInt();
            String readString5 = parcel.readString();
            boolean z = parcel.readInt() > 0;
            if (parcel.readInt() > 0) {
                i = 1;
            }
            return new ParticipantEntity(1, readString, readString2, parse, parse2, readInt, readString5, z, i != 0 ? (PlayerEntity) PlayerEntity.CREATOR.createFromParcel(parcel) : null, 7);
        }
    }

    ParticipantEntity(int versionCode, String participantId, String displayName, Uri iconImageUri, Uri hiResImageUri, int status, String clientAddress, boolean connectedToRoom, PlayerEntity player, int capabilities) {
        this.iM = versionCode;
        this.nd = participantId;
        this.ml = displayName;
        this.mq = iconImageUri;
        this.mr = hiResImageUri;
        this.nU = status;
        this.nV = clientAddress;
        this.nW = connectedToRoom;
        this.nX = player;
        this.nY = capabilities;
    }

    public ParticipantEntity(Participant participant) {
        this.iM = 1;
        this.nd = participant.getParticipantId();
        this.ml = participant.getDisplayName();
        this.mq = participant.getIconImageUri();
        this.mr = participant.getHiResImageUri();
        this.nU = participant.getStatus();
        this.nV = participant.ci();
        this.nW = participant.isConnectedToRoom();
        Player player = participant.getPlayer();
        this.nX = player == null ? null : new PlayerEntity(player);
        this.nY = participant.getCapabilities();
    }

    static int m1590a(Participant participant) {
        return dl.hashCode(participant.getPlayer(), Integer.valueOf(participant.getStatus()), participant.ci(), Boolean.valueOf(participant.isConnectedToRoom()), participant.getDisplayName(), participant.getIconImageUri(), participant.getHiResImageUri(), Integer.valueOf(participant.getCapabilities()));
    }

    static boolean m1591a(Participant participant, Object obj) {
        if (!(obj instanceof Participant)) {
            return false;
        }
        if (participant == obj) {
            return true;
        }
        Participant participant2 = (Participant) obj;
        return dl.equal(participant2.getPlayer(), participant.getPlayer()) && dl.equal(Integer.valueOf(participant2.getStatus()), Integer.valueOf(participant.getStatus())) && dl.equal(participant2.ci(), participant.ci()) && dl.equal(Boolean.valueOf(participant2.isConnectedToRoom()), Boolean.valueOf(participant.isConnectedToRoom())) && dl.equal(participant2.getDisplayName(), participant.getDisplayName()) && dl.equal(participant2.getIconImageUri(), participant.getIconImageUri()) && dl.equal(participant2.getHiResImageUri(), participant.getHiResImageUri()) && dl.equal(Integer.valueOf(participant2.getCapabilities()), Integer.valueOf(participant.getCapabilities()));
    }

    static String m1592b(Participant participant) {
        return dl.m387d(participant).m386a("Player", participant.getPlayer()).m386a("Status", Integer.valueOf(participant.getStatus())).m386a("ClientAddress", participant.ci()).m386a("ConnectedToRoom", Boolean.valueOf(participant.isConnectedToRoom())).m386a("DisplayName", participant.getDisplayName()).m386a("IconImage", participant.getIconImageUri()).m386a("HiResImage", participant.getHiResImageUri()).m386a("Capabilities", Integer.valueOf(participant.getCapabilities())).toString();
    }

    public String ci() {
        return this.nV;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return m1591a(this, obj);
    }

    public Participant freeze() {
        return this;
    }

    public int getCapabilities() {
        return this.nY;
    }

    public String getDisplayName() {
        return this.nX == null ? this.ml : this.nX.getDisplayName();
    }

    public void getDisplayName(CharArrayBuffer dataOut) {
        if (this.nX == null) {
            eg.m447b(this.ml, dataOut);
        } else {
            this.nX.getDisplayName(dataOut);
        }
    }

    public Uri getHiResImageUri() {
        return this.nX == null ? this.mr : this.nX.getHiResImageUri();
    }

    public Uri getIconImageUri() {
        return this.nX == null ? this.mq : this.nX.getIconImageUri();
    }

    public String getParticipantId() {
        return this.nd;
    }

    public Player getPlayer() {
        return this.nX;
    }

    public int getStatus() {
        return this.nU;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return m1590a(this);
    }

    public boolean isConnectedToRoom() {
        return this.nW;
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return m1592b((Participant) this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        String str = null;
        int i = 0;
        if (aX()) {
            dest.writeString(this.nd);
            dest.writeString(this.ml);
            dest.writeString(this.mq == null ? null : this.mq.toString());
            if (this.mr != null) {
                str = this.mr.toString();
            }
            dest.writeString(str);
            dest.writeInt(this.nU);
            dest.writeString(this.nV);
            dest.writeInt(this.nW ? 1 : 0);
            if (this.nX != null) {
                i = 1;
            }
            dest.writeInt(i);
            if (this.nX != null) {
                this.nX.writeToParcel(dest, flags);
                return;
            }
            return;
        }
        C0121c.m157a(this, dest, flags);
    }
}
