package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.eg;

public final class C0901c implements LeaderboardScore {
    private final long nA;
    private final long nB;
    private final String nC;
    private final Uri nD;
    private final Uri nE;
    private final PlayerEntity nF;
    private final String nG;
    private final long nx;
    private final String ny;
    private final String nz;

    public C0901c(LeaderboardScore leaderboardScore) {
        this.nx = leaderboardScore.getRank();
        this.ny = (String) dm.m392e(leaderboardScore.getDisplayRank());
        this.nz = (String) dm.m392e(leaderboardScore.getDisplayScore());
        this.nA = leaderboardScore.getRawScore();
        this.nB = leaderboardScore.getTimestampMillis();
        this.nC = leaderboardScore.getScoreHolderDisplayName();
        this.nD = leaderboardScore.getScoreHolderIconImageUri();
        this.nE = leaderboardScore.getScoreHolderHiResImageUri();
        Player scoreHolder = leaderboardScore.getScoreHolder();
        this.nF = scoreHolder == null ? null : (PlayerEntity) scoreHolder.freeze();
        this.nG = leaderboardScore.getScoreTag();
    }

    static int m1320a(LeaderboardScore leaderboardScore) {
        return dl.hashCode(Long.valueOf(leaderboardScore.getRank()), leaderboardScore.getDisplayRank(), Long.valueOf(leaderboardScore.getRawScore()), leaderboardScore.getDisplayScore(), Long.valueOf(leaderboardScore.getTimestampMillis()), leaderboardScore.getScoreHolderDisplayName(), leaderboardScore.getScoreHolderIconImageUri(), leaderboardScore.getScoreHolderHiResImageUri(), leaderboardScore.getScoreHolder());
    }

    static boolean m1321a(LeaderboardScore leaderboardScore, Object obj) {
        if (!(obj instanceof LeaderboardScore)) {
            return false;
        }
        if (leaderboardScore == obj) {
            return true;
        }
        LeaderboardScore leaderboardScore2 = (LeaderboardScore) obj;
        return dl.equal(Long.valueOf(leaderboardScore2.getRank()), Long.valueOf(leaderboardScore.getRank())) && dl.equal(leaderboardScore2.getDisplayRank(), leaderboardScore.getDisplayRank()) && dl.equal(Long.valueOf(leaderboardScore2.getRawScore()), Long.valueOf(leaderboardScore.getRawScore())) && dl.equal(leaderboardScore2.getDisplayScore(), leaderboardScore.getDisplayScore()) && dl.equal(Long.valueOf(leaderboardScore2.getTimestampMillis()), Long.valueOf(leaderboardScore.getTimestampMillis())) && dl.equal(leaderboardScore2.getScoreHolderDisplayName(), leaderboardScore.getScoreHolderDisplayName()) && dl.equal(leaderboardScore2.getScoreHolderIconImageUri(), leaderboardScore.getScoreHolderIconImageUri()) && dl.equal(leaderboardScore2.getScoreHolderHiResImageUri(), leaderboardScore.getScoreHolderHiResImageUri()) && dl.equal(leaderboardScore2.getScoreHolder(), leaderboardScore.getScoreHolder()) && dl.equal(leaderboardScore2.getScoreTag(), leaderboardScore.getScoreTag());
    }

    static String m1322b(LeaderboardScore leaderboardScore) {
        return dl.m387d(leaderboardScore).m386a("Rank", Long.valueOf(leaderboardScore.getRank())).m386a("DisplayRank", leaderboardScore.getDisplayRank()).m386a("Score", Long.valueOf(leaderboardScore.getRawScore())).m386a("DisplayScore", leaderboardScore.getDisplayScore()).m386a("Timestamp", Long.valueOf(leaderboardScore.getTimestampMillis())).m386a("DisplayName", leaderboardScore.getScoreHolderDisplayName()).m386a("IconImageUri", leaderboardScore.getScoreHolderIconImageUri()).m386a("HiResImageUri", leaderboardScore.getScoreHolderHiResImageUri()).m386a("Player", leaderboardScore.getScoreHolder() == null ? null : leaderboardScore.getScoreHolder()).m386a("ScoreTag", leaderboardScore.getScoreTag()).toString();
    }

    public LeaderboardScore cd() {
        return this;
    }

    public boolean equals(Object obj) {
        return C0901c.m1321a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return cd();
    }

    public String getDisplayRank() {
        return this.ny;
    }

    public void getDisplayRank(CharArrayBuffer dataOut) {
        eg.m447b(this.ny, dataOut);
    }

    public String getDisplayScore() {
        return this.nz;
    }

    public void getDisplayScore(CharArrayBuffer dataOut) {
        eg.m447b(this.nz, dataOut);
    }

    public long getRank() {
        return this.nx;
    }

    public long getRawScore() {
        return this.nA;
    }

    public Player getScoreHolder() {
        return this.nF;
    }

    public String getScoreHolderDisplayName() {
        return this.nF == null ? this.nC : this.nF.getDisplayName();
    }

    public void getScoreHolderDisplayName(CharArrayBuffer dataOut) {
        if (this.nF == null) {
            eg.m447b(this.nC, dataOut);
        } else {
            this.nF.getDisplayName(dataOut);
        }
    }

    public Uri getScoreHolderHiResImageUri() {
        return this.nF == null ? this.nE : this.nF.getHiResImageUri();
    }

    public Uri getScoreHolderIconImageUri() {
        return this.nF == null ? this.nD : this.nF.getIconImageUri();
    }

    public String getScoreTag() {
        return this.nG;
    }

    public long getTimestampMillis() {
        return this.nB;
    }

    public int hashCode() {
        return C0901c.m1320a(this);
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return C0901c.m1322b(this);
    }
}
