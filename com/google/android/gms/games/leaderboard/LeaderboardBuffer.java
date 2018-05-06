package com.google.android.gms.games.leaderboard;

import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.common.data.C0647f;

public final class LeaderboardBuffer extends C0647f<Leaderboard> {
    public LeaderboardBuffer(C0646d dataHolder) {
        super(dataHolder);
    }

    protected /* synthetic */ Object mo1484a(int i, int i2) {
        return getEntry(i, i2);
    }

    protected Leaderboard getEntry(int rowIndex, int numChildren) {
        return new C0659a(this.jf, rowIndex, numChildren);
    }

    protected String getPrimaryDataMarkerColumn() {
        return "external_leaderboard_id";
    }
}
