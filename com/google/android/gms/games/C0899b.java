package com.google.android.gms.games;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import com.google.android.gms.common.data.C0096b;
import com.google.android.gms.common.data.C0646d;

public final class C0899b extends C0096b implements Game {
    public C0899b(C0646d c0646d, int i) {
        super(c0646d, i);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return GameEntity.m1576a(this, obj);
    }

    public Game freeze() {
        return new GameEntity(this);
    }

    public int getAchievementTotalCount() {
        return getInteger("achievement_total_count");
    }

    public String getApplicationId() {
        return getString("external_game_id");
    }

    public String getDescription() {
        return getString("game_description");
    }

    public void getDescription(CharArrayBuffer dataOut) {
        m40a("game_description", dataOut);
    }

    public String getDeveloperName() {
        return getString("developer_name");
    }

    public void getDeveloperName(CharArrayBuffer dataOut) {
        m40a("developer_name", dataOut);
    }

    public String getDisplayName() {
        return getString("display_name");
    }

    public void getDisplayName(CharArrayBuffer dataOut) {
        m40a("display_name", dataOut);
    }

    public Uri getFeaturedImageUri() {
        return m41u("featured_image_uri");
    }

    public int getGameplayAclStatus() {
        return getInteger("gameplay_acl_status");
    }

    public Uri getHiResImageUri() {
        return m41u("game_hi_res_image_uri");
    }

    public Uri getIconImageUri() {
        return m41u("game_icon_image_uri");
    }

    public String getInstancePackageName() {
        return getString("package_name");
    }

    public int getLeaderboardCount() {
        return getInteger("leaderboard_count");
    }

    public String getPrimaryCategory() {
        return getString("primary_category");
    }

    public String getSecondaryCategory() {
        return getString("secondary_category");
    }

    public int hashCode() {
        return GameEntity.m1575a(this);
    }

    public boolean isInstanceInstalled() {
        return getInteger("installed") > 0;
    }

    public boolean isPlayEnabledGame() {
        return getBoolean("play_enabled_game");
    }

    public String toString() {
        return GameEntity.m1577b((Game) this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        ((GameEntity) freeze()).writeToParcel(dest, flags);
    }
}
