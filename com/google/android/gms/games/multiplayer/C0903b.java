package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import com.google.android.gms.common.data.C0096b;
import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.games.C0899b;
import com.google.android.gms.games.Game;
import com.google.android.gms.internal.dm;
import java.util.ArrayList;

public final class C0903b extends C0096b implements Invitation {
    private final ArrayList<Participant> nQ;
    private final Game nS;
    private final C0904d nT;

    C0903b(C0646d c0646d, int i, int i2) {
        super(c0646d, i);
        this.nS = new C0899b(c0646d, i);
        this.nQ = new ArrayList(i2);
        String string = getString("external_inviter_id");
        Object obj = null;
        for (int i3 = 0; i3 < i2; i3++) {
            C0904d c0904d = new C0904d(this.jf, this.ji + i3);
            if (c0904d.getParticipantId().equals(string)) {
                obj = c0904d;
            }
            this.nQ.add(c0904d);
        }
        this.nT = (C0904d) dm.m388a(obj, (Object) "Must have a valid inviter!");
    }

    public int ch() {
        return getInteger("type");
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return InvitationEntity.m1586a(this, obj);
    }

    public Invitation freeze() {
        return new InvitationEntity(this);
    }

    public long getCreationTimestamp() {
        return getLong("creation_timestamp");
    }

    public Game getGame() {
        return this.nS;
    }

    public String getInvitationId() {
        return getString("external_invitation_id");
    }

    public Participant getInviter() {
        return this.nT;
    }

    public ArrayList<Participant> getParticipants() {
        return this.nQ;
    }

    public int getVariant() {
        return getInteger("variant");
    }

    public int hashCode() {
        return InvitationEntity.m1585a(this);
    }

    public String toString() {
        return InvitationEntity.m1587b((Invitation) this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        ((InvitationEntity) freeze()).writeToParcel(dest, flags);
    }
}
