package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.internal.dd;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.en;
import java.util.ArrayList;

public final class InvitationEntity extends en implements Invitation {
    public static final Creator<InvitationEntity> CREATOR = new C0661a();
    private final int iM;
    private final GameEntity nL;
    private final String nM;
    private final long nN;
    private final int nO;
    private final ParticipantEntity nP;
    private final ArrayList<ParticipantEntity> nQ;
    private final int nR;

    static final class C0661a extends C0120a {
        C0661a() {
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return mo746v(x0);
        }

        public InvitationEntity mo746v(Parcel parcel) {
            if (en.m1439c(dd.aW()) || dd.m942y(InvitationEntity.class.getCanonicalName())) {
                return super.mo746v(parcel);
            }
            GameEntity gameEntity = (GameEntity) GameEntity.CREATOR.createFromParcel(parcel);
            String readString = parcel.readString();
            long readLong = parcel.readLong();
            int readInt = parcel.readInt();
            ParticipantEntity participantEntity = (ParticipantEntity) ParticipantEntity.CREATOR.createFromParcel(parcel);
            int readInt2 = parcel.readInt();
            ArrayList arrayList = new ArrayList(readInt2);
            for (int i = 0; i < readInt2; i++) {
                arrayList.add(ParticipantEntity.CREATOR.createFromParcel(parcel));
            }
            return new InvitationEntity(1, gameEntity, readString, readLong, readInt, participantEntity, arrayList, -1);
        }
    }

    InvitationEntity(int versionCode, GameEntity game, String invitationId, long creationTimestamp, int invitationType, ParticipantEntity inviter, ArrayList<ParticipantEntity> participants, int variant) {
        this.iM = versionCode;
        this.nL = game;
        this.nM = invitationId;
        this.nN = creationTimestamp;
        this.nO = invitationType;
        this.nP = inviter;
        this.nQ = participants;
        this.nR = variant;
    }

    InvitationEntity(Invitation invitation) {
        this.iM = 1;
        this.nL = new GameEntity(invitation.getGame());
        this.nM = invitation.getInvitationId();
        this.nN = invitation.getCreationTimestamp();
        this.nO = invitation.ch();
        this.nR = invitation.getVariant();
        String participantId = invitation.getInviter().getParticipantId();
        Object obj = null;
        ArrayList participants = invitation.getParticipants();
        int size = participants.size();
        this.nQ = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            Participant participant = (Participant) participants.get(i);
            if (participant.getParticipantId().equals(participantId)) {
                obj = participant;
            }
            this.nQ.add((ParticipantEntity) participant.freeze());
        }
        dm.m388a(obj, (Object) "Must have a valid inviter!");
        this.nP = (ParticipantEntity) obj.freeze();
    }

    static int m1585a(Invitation invitation) {
        return dl.hashCode(invitation.getGame(), invitation.getInvitationId(), Long.valueOf(invitation.getCreationTimestamp()), Integer.valueOf(invitation.ch()), invitation.getInviter(), invitation.getParticipants(), Integer.valueOf(invitation.getVariant()));
    }

    static boolean m1586a(Invitation invitation, Object obj) {
        if (!(obj instanceof Invitation)) {
            return false;
        }
        if (invitation == obj) {
            return true;
        }
        Invitation invitation2 = (Invitation) obj;
        return dl.equal(invitation2.getGame(), invitation.getGame()) && dl.equal(invitation2.getInvitationId(), invitation.getInvitationId()) && dl.equal(Long.valueOf(invitation2.getCreationTimestamp()), Long.valueOf(invitation.getCreationTimestamp())) && dl.equal(Integer.valueOf(invitation2.ch()), Integer.valueOf(invitation.ch())) && dl.equal(invitation2.getInviter(), invitation.getInviter()) && dl.equal(invitation2.getParticipants(), invitation.getParticipants()) && dl.equal(Integer.valueOf(invitation2.getVariant()), Integer.valueOf(invitation.getVariant()));
    }

    static String m1587b(Invitation invitation) {
        return dl.m387d(invitation).m386a("Game", invitation.getGame()).m386a("InvitationId", invitation.getInvitationId()).m386a("CreationTimestamp", Long.valueOf(invitation.getCreationTimestamp())).m386a("InvitationType", Integer.valueOf(invitation.ch())).m386a("Inviter", invitation.getInviter()).m386a("Participants", invitation.getParticipants()).m386a("Variant", Integer.valueOf(invitation.getVariant())).toString();
    }

    public int ch() {
        return this.nO;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return m1586a(this, obj);
    }

    public Invitation freeze() {
        return this;
    }

    public long getCreationTimestamp() {
        return this.nN;
    }

    public Game getGame() {
        return this.nL;
    }

    public String getInvitationId() {
        return this.nM;
    }

    public Participant getInviter() {
        return this.nP;
    }

    public ArrayList<Participant> getParticipants() {
        return new ArrayList(this.nQ);
    }

    public int getVariant() {
        return this.nR;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return m1585a(this);
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return m1587b((Invitation) this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (aX()) {
            this.nL.writeToParcel(dest, flags);
            dest.writeString(this.nM);
            dest.writeLong(this.nN);
            dest.writeInt(this.nO);
            this.nP.writeToParcel(dest, flags);
            int size = this.nQ.size();
            dest.writeInt(size);
            for (int i = 0; i < size; i++) {
                ((ParticipantEntity) this.nQ.get(i)).writeToParcel(dest, flags);
            }
            return;
        }
        C0120a.m154a(this, dest, flags);
    }
}
