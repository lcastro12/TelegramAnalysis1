package com.google.android.gms.games.multiplayer;

import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.common.data.C0647f;

public final class InvitationBuffer extends C0647f<Invitation> {
    public InvitationBuffer(C0646d dataHolder) {
        super(dataHolder);
    }

    protected /* synthetic */ Object mo1484a(int i, int i2) {
        return getEntry(i, i2);
    }

    protected Invitation getEntry(int rowIndex, int numChildren) {
        return new C0903b(this.jf, rowIndex, numChildren);
    }

    protected String getPrimaryDataMarkerColumn() {
        return "external_invitation_id";
    }
}
