package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.data.C0096b;
import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.games.C0900d;
import com.google.android.gms.games.Player;

public final class C0902d extends C0096b implements LeaderboardScore {
    private final C0900d nH;

    C0902d(C0646d c0646d, int i) {
        super(c0646d, i);
        this.nH = new C0900d(c0646d, i);
    }

    public LeaderboardScore cd() {
        return new C0901c(this);
    }

    public boolean equals(Object obj) {
        return C0901c.m1321a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return cd();
    }

    public String getDisplayRank() {
        return getString("display_rank");
    }

    public void getDisplayRank(CharArrayBuffer dataOut) {
        m40a("display_rank", dataOut);
    }

    public String getDisplayScore() {
        return getString("display_score");
    }

    public void getDisplayScore(CharArrayBuffer dataOut) {
        m40a("display_score", dataOut);
    }

    public long getRank() {
        return getLong("rank");
    }

    public long getRawScore() {
        return getLong("raw_score");
    }

    public Player getScoreHolder() {
        return m42v("external_player_id") ? null : this.nH;
    }

    public String getScoreHolderDisplayName() {
        return m42v("external_player_id") ? getString("default_display_name") : this.nH.getDisplayName();
    }

    public void getScoreHolderDisplayName(CharArrayBuffer dataOut) {
        if (m42v("external_player_id")) {
            m40a("default_display_name", dataOut);
        } else {
            this.nH.getDisplayName(dataOut);
        }
    }

    public Uri getScoreHolderHiResImageUri() {
        return m42v("external_player_id") ? null : this.nH.getHiResImageUri();
    }

    public Uri getScoreHolderIconImageUri() {
        return m42v("external_player_id") ? m41u("default_display_image_uri") : this.nH.getIconImageUri();
    }

    public String getScoreTag() {
        return getString("score_tag");
    }

    public long getTimestampMillis() {
        return getLong("achieved_timestamp");
    }

    public int hashCode() {
        return C0901c.m1320a(this);
    }

    public String toString() {
        return C0901c.m1322b(this);
    }
}
