package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.data.C0096b;
import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.internal.dl;
import java.util.ArrayList;

public final class C0659a extends C0096b implements Leaderboard {
    private final int nu;

    C0659a(C0646d c0646d, int i, int i2) {
        super(c0646d, i);
        this.nu = i2;
    }

    public String getDisplayName() {
        return getString("name");
    }

    public void getDisplayName(CharArrayBuffer dataOut) {
        m40a("name", dataOut);
    }

    public Uri getIconImageUri() {
        return m41u("board_icon_image_uri");
    }

    public String getLeaderboardId() {
        return getString("external_leaderboard_id");
    }

    public int getScoreOrder() {
        return getInteger("score_order");
    }

    public ArrayList<LeaderboardVariant> getVariants() {
        ArrayList<LeaderboardVariant> arrayList = new ArrayList(this.nu);
        for (int i = 0; i < this.nu; i++) {
            arrayList.add(new C0660e(this.jf, this.ji + i));
        }
        return arrayList;
    }

    public String toString() {
        return dl.m387d(this).m386a("ID", getLeaderboardId()).m386a("DisplayName", getDisplayName()).m386a("IconImageURI", getIconImageUri()).m386a("ScoreOrder", Integer.valueOf(getScoreOrder())).toString();
    }
}
