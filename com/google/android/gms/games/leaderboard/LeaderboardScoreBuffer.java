package com.google.android.gms.games.leaderboard;

import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.common.data.DataBuffer;

public final class LeaderboardScoreBuffer extends DataBuffer<LeaderboardScore> {
    private final C0119b nv;

    public LeaderboardScoreBuffer(C0646d dataHolder) {
        super(dataHolder);
        this.nv = new C0119b(dataHolder.aM());
    }

    public C0119b cb() {
        return this.nv;
    }

    public LeaderboardScore get(int position) {
        return new C0902d(this.jf, position);
    }
}
