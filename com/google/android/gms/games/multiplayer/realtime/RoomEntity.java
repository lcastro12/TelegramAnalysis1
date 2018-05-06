package com.google.android.gms.games.multiplayer.realtime;

import android.database.CharArrayBuffer;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import com.google.android.gms.internal.dd;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.eg;
import com.google.android.gms.internal.en;
import java.util.ArrayList;

public final class RoomEntity extends en implements Room {
    public static final Creator<RoomEntity> CREATOR = new C0663a();
    private final int iM;
    private final String mo;
    private final long nN;
    private final ArrayList<ParticipantEntity> nQ;
    private final int nR;
    private final String nb;
    private final Bundle oh;
    private final String ol;
    private final int om;
    private final int on;

    static final class C0663a extends C0124b {
        C0663a() {
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return mo750y(x0);
        }

        public RoomEntity mo750y(Parcel parcel) {
            if (en.m1439c(dd.aW()) || dd.m942y(RoomEntity.class.getCanonicalName())) {
                return super.mo750y(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            long readLong = parcel.readLong();
            int readInt = parcel.readInt();
            String readString3 = parcel.readString();
            int readInt2 = parcel.readInt();
            Bundle readBundle = parcel.readBundle();
            int readInt3 = parcel.readInt();
            ArrayList arrayList = new ArrayList(readInt3);
            for (int i = 0; i < readInt3; i++) {
                arrayList.add(ParticipantEntity.CREATOR.createFromParcel(parcel));
            }
            return new RoomEntity(2, readString, readString2, readLong, readInt, readString3, readInt2, readBundle, arrayList, -1);
        }
    }

    RoomEntity(int versionCode, String roomId, String creatorId, long creationTimestamp, int roomStatus, String description, int variant, Bundle autoMatchCriteria, ArrayList<ParticipantEntity> participants, int autoMatchWaitEstimateSeconds) {
        this.iM = versionCode;
        this.nb = roomId;
        this.ol = creatorId;
        this.nN = creationTimestamp;
        this.om = roomStatus;
        this.mo = description;
        this.nR = variant;
        this.oh = autoMatchCriteria;
        this.nQ = participants;
        this.on = autoMatchWaitEstimateSeconds;
    }

    public RoomEntity(Room room) {
        this.iM = 2;
        this.nb = room.getRoomId();
        this.ol = room.getCreatorId();
        this.nN = room.getCreationTimestamp();
        this.om = room.getStatus();
        this.mo = room.getDescription();
        this.nR = room.getVariant();
        this.oh = room.getAutoMatchCriteria();
        ArrayList participants = room.getParticipants();
        int size = participants.size();
        this.nQ = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            this.nQ.add((ParticipantEntity) ((Participant) participants.get(i)).freeze());
        }
        this.on = room.getAutoMatchWaitEstimateSeconds();
    }

    static int m1595a(Room room) {
        return dl.hashCode(room.getRoomId(), room.getCreatorId(), Long.valueOf(room.getCreationTimestamp()), Integer.valueOf(room.getStatus()), room.getDescription(), Integer.valueOf(room.getVariant()), room.getAutoMatchCriteria(), room.getParticipants(), Integer.valueOf(room.getAutoMatchWaitEstimateSeconds()));
    }

    static boolean m1596a(Room room, Object obj) {
        if (!(obj instanceof Room)) {
            return false;
        }
        if (room == obj) {
            return true;
        }
        Room room2 = (Room) obj;
        return dl.equal(room2.getRoomId(), room.getRoomId()) && dl.equal(room2.getCreatorId(), room.getCreatorId()) && dl.equal(Long.valueOf(room2.getCreationTimestamp()), Long.valueOf(room.getCreationTimestamp())) && dl.equal(Integer.valueOf(room2.getStatus()), Integer.valueOf(room.getStatus())) && dl.equal(room2.getDescription(), room.getDescription()) && dl.equal(Integer.valueOf(room2.getVariant()), Integer.valueOf(room.getVariant())) && dl.equal(room2.getAutoMatchCriteria(), room.getAutoMatchCriteria()) && dl.equal(room2.getParticipants(), room.getParticipants()) && dl.equal(Integer.valueOf(room2.getAutoMatchWaitEstimateSeconds()), Integer.valueOf(room.getAutoMatchWaitEstimateSeconds()));
    }

    static String m1597b(Room room) {
        return dl.m387d(room).m386a("RoomId", room.getRoomId()).m386a("CreatorId", room.getCreatorId()).m386a("CreationTimestamp", Long.valueOf(room.getCreationTimestamp())).m386a("RoomStatus", Integer.valueOf(room.getStatus())).m386a("Description", room.getDescription()).m386a("Variant", Integer.valueOf(room.getVariant())).m386a("AutoMatchCriteria", room.getAutoMatchCriteria()).m386a("Participants", room.getParticipants()).m386a("AutoMatchWaitEstimateSeconds", Integer.valueOf(room.getAutoMatchWaitEstimateSeconds())).toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return m1596a(this, obj);
    }

    public Room freeze() {
        return this;
    }

    public Bundle getAutoMatchCriteria() {
        return this.oh;
    }

    public int getAutoMatchWaitEstimateSeconds() {
        return this.on;
    }

    public long getCreationTimestamp() {
        return this.nN;
    }

    public String getCreatorId() {
        return this.ol;
    }

    public String getDescription() {
        return this.mo;
    }

    public void getDescription(CharArrayBuffer dataOut) {
        eg.m447b(this.mo, dataOut);
    }

    public String getParticipantId(String playerId) {
        int size = this.nQ.size();
        for (int i = 0; i < size; i++) {
            Participant participant = (Participant) this.nQ.get(i);
            Player player = participant.getPlayer();
            if (player != null && player.getPlayerId().equals(playerId)) {
                return participant.getParticipantId();
            }
        }
        return null;
    }

    public ArrayList<String> getParticipantIds() {
        int size = this.nQ.size();
        ArrayList<String> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(((ParticipantEntity) this.nQ.get(i)).getParticipantId());
        }
        return arrayList;
    }

    public int getParticipantStatus(String participantId) {
        int size = this.nQ.size();
        for (int i = 0; i < size; i++) {
            Participant participant = (Participant) this.nQ.get(i);
            if (participant.getParticipantId().equals(participantId)) {
                return participant.getStatus();
            }
        }
        throw new IllegalStateException("Participant " + participantId + " is not in room " + getRoomId());
    }

    public ArrayList<Participant> getParticipants() {
        return new ArrayList(this.nQ);
    }

    public String getRoomId() {
        return this.nb;
    }

    public int getStatus() {
        return this.om;
    }

    public int getVariant() {
        return this.nR;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return m1595a(this);
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return m1597b((Room) this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (aX()) {
            dest.writeString(this.nb);
            dest.writeString(this.ol);
            dest.writeLong(this.nN);
            dest.writeInt(this.om);
            dest.writeString(this.mo);
            dest.writeInt(this.nR);
            dest.writeBundle(this.oh);
            int size = this.nQ.size();
            dest.writeInt(size);
            for (int i = 0; i < size; i++) {
                ((ParticipantEntity) this.nQ.get(i)).writeToParcel(dest, flags);
            }
            return;
        }
        C0124b.m162a(this, dest, flags);
    }
}
