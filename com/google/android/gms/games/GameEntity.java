package com.google.android.gms.games;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.dd;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.eg;
import com.google.android.gms.internal.en;

public final class GameEntity extends en implements Game {
    public static final Creator<GameEntity> CREATOR = new C0656a();
    private final int iM;
    private final String mk;
    private final String ml;
    private final String mm;
    private final String mn;
    private final String mo;
    private final String mp;
    private final Uri mq;
    private final Uri mr;
    private final Uri ms;
    private final boolean mt;
    private final boolean mu;
    private final String mv;
    private final int mw;
    private final int mx;
    private final int my;

    static final class C0656a extends C0117a {
        C0656a() {
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return mo710t(x0);
        }

        public GameEntity mo710t(Parcel parcel) {
            if (en.m1439c(dd.aW()) || dd.m942y(GameEntity.class.getCanonicalName())) {
                return super.mo710t(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            String readString4 = parcel.readString();
            String readString5 = parcel.readString();
            String readString6 = parcel.readString();
            String readString7 = parcel.readString();
            Uri parse = readString7 == null ? null : Uri.parse(readString7);
            readString7 = parcel.readString();
            Uri parse2 = readString7 == null ? null : Uri.parse(readString7);
            readString7 = parcel.readString();
            return new GameEntity(1, readString, readString2, readString3, readString4, readString5, readString6, parse, parse2, readString7 == null ? null : Uri.parse(readString7), parcel.readInt() > 0, parcel.readInt() > 0, parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt());
        }
    }

    GameEntity(int versionCode, String applicationId, String displayName, String primaryCategory, String secondaryCategory, String description, String developerName, Uri iconImageUri, Uri hiResImageUri, Uri featuredImageUri, boolean playEnabledGame, boolean instanceInstalled, String instancePackageName, int gameplayAclStatus, int achievementTotalCount, int leaderboardCount) {
        this.iM = versionCode;
        this.mk = applicationId;
        this.ml = displayName;
        this.mm = primaryCategory;
        this.mn = secondaryCategory;
        this.mo = description;
        this.mp = developerName;
        this.mq = iconImageUri;
        this.mr = hiResImageUri;
        this.ms = featuredImageUri;
        this.mt = playEnabledGame;
        this.mu = instanceInstalled;
        this.mv = instancePackageName;
        this.mw = gameplayAclStatus;
        this.mx = achievementTotalCount;
        this.my = leaderboardCount;
    }

    public GameEntity(Game game) {
        this.iM = 1;
        this.mk = game.getApplicationId();
        this.mm = game.getPrimaryCategory();
        this.mn = game.getSecondaryCategory();
        this.mo = game.getDescription();
        this.mp = game.getDeveloperName();
        this.ml = game.getDisplayName();
        this.mq = game.getIconImageUri();
        this.mr = game.getHiResImageUri();
        this.ms = game.getFeaturedImageUri();
        this.mt = game.isPlayEnabledGame();
        this.mu = game.isInstanceInstalled();
        this.mv = game.getInstancePackageName();
        this.mw = game.getGameplayAclStatus();
        this.mx = game.getAchievementTotalCount();
        this.my = game.getLeaderboardCount();
    }

    static int m1575a(Game game) {
        return dl.hashCode(game.getApplicationId(), game.getDisplayName(), game.getPrimaryCategory(), game.getSecondaryCategory(), game.getDescription(), game.getDeveloperName(), game.getIconImageUri(), game.getHiResImageUri(), game.getFeaturedImageUri(), Boolean.valueOf(game.isPlayEnabledGame()), Boolean.valueOf(game.isInstanceInstalled()), game.getInstancePackageName(), Integer.valueOf(game.getGameplayAclStatus()), Integer.valueOf(game.getAchievementTotalCount()), Integer.valueOf(game.getLeaderboardCount()));
    }

    static boolean m1576a(Game game, Object obj) {
        if (!(obj instanceof Game)) {
            return false;
        }
        if (game == obj) {
            return true;
        }
        Game game2 = (Game) obj;
        return dl.equal(game2.getApplicationId(), game.getApplicationId()) && dl.equal(game2.getDisplayName(), game.getDisplayName()) && dl.equal(game2.getPrimaryCategory(), game.getPrimaryCategory()) && dl.equal(game2.getSecondaryCategory(), game.getSecondaryCategory()) && dl.equal(game2.getDescription(), game.getDescription()) && dl.equal(game2.getDeveloperName(), game.getDeveloperName()) && dl.equal(game2.getIconImageUri(), game.getIconImageUri()) && dl.equal(game2.getHiResImageUri(), game.getHiResImageUri()) && dl.equal(game2.getFeaturedImageUri(), game.getFeaturedImageUri()) && dl.equal(Boolean.valueOf(game2.isPlayEnabledGame()), Boolean.valueOf(game.isPlayEnabledGame())) && dl.equal(Boolean.valueOf(game2.isInstanceInstalled()), Boolean.valueOf(game.isInstanceInstalled())) && dl.equal(game2.getInstancePackageName(), game.getInstancePackageName()) && dl.equal(Integer.valueOf(game2.getGameplayAclStatus()), Integer.valueOf(game.getGameplayAclStatus())) && dl.equal(Integer.valueOf(game2.getAchievementTotalCount()), Integer.valueOf(game.getAchievementTotalCount())) && dl.equal(Integer.valueOf(game2.getLeaderboardCount()), Integer.valueOf(game.getLeaderboardCount()));
    }

    static String m1577b(Game game) {
        return dl.m387d(game).m386a("ApplicationId", game.getApplicationId()).m386a("DisplayName", game.getDisplayName()).m386a("PrimaryCategory", game.getPrimaryCategory()).m386a("SecondaryCategory", game.getSecondaryCategory()).m386a("Description", game.getDescription()).m386a("DeveloperName", game.getDeveloperName()).m386a("IconImageUri", game.getIconImageUri()).m386a("HiResImageUri", game.getHiResImageUri()).m386a("FeaturedImageUri", game.getFeaturedImageUri()).m386a("PlayEnabledGame", Boolean.valueOf(game.isPlayEnabledGame())).m386a("InstanceInstalled", Boolean.valueOf(game.isInstanceInstalled())).m386a("InstancePackageName", game.getInstancePackageName()).m386a("GameplayAclStatus", Integer.valueOf(game.getGameplayAclStatus())).m386a("AchievementTotalCount", Integer.valueOf(game.getAchievementTotalCount())).m386a("LeaderboardCount", Integer.valueOf(game.getLeaderboardCount())).toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return m1576a(this, obj);
    }

    public Game freeze() {
        return this;
    }

    public int getAchievementTotalCount() {
        return this.mx;
    }

    public String getApplicationId() {
        return this.mk;
    }

    public String getDescription() {
        return this.mo;
    }

    public void getDescription(CharArrayBuffer dataOut) {
        eg.m447b(this.mo, dataOut);
    }

    public String getDeveloperName() {
        return this.mp;
    }

    public void getDeveloperName(CharArrayBuffer dataOut) {
        eg.m447b(this.mp, dataOut);
    }

    public String getDisplayName() {
        return this.ml;
    }

    public void getDisplayName(CharArrayBuffer dataOut) {
        eg.m447b(this.ml, dataOut);
    }

    public Uri getFeaturedImageUri() {
        return this.ms;
    }

    public int getGameplayAclStatus() {
        return this.mw;
    }

    public Uri getHiResImageUri() {
        return this.mr;
    }

    public Uri getIconImageUri() {
        return this.mq;
    }

    public String getInstancePackageName() {
        return this.mv;
    }

    public int getLeaderboardCount() {
        return this.my;
    }

    public String getPrimaryCategory() {
        return this.mm;
    }

    public String getSecondaryCategory() {
        return this.mn;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return m1575a(this);
    }

    public boolean isDataValid() {
        return true;
    }

    public boolean isInstanceInstalled() {
        return this.mu;
    }

    public boolean isPlayEnabledGame() {
        return this.mt;
    }

    public String toString() {
        return m1577b((Game) this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        String str = null;
        if (aX()) {
            dest.writeString(this.mk);
            dest.writeString(this.ml);
            dest.writeString(this.mm);
            dest.writeString(this.mn);
            dest.writeString(this.mo);
            dest.writeString(this.mp);
            dest.writeString(this.mq == null ? null : this.mq.toString());
            dest.writeString(this.mr == null ? null : this.mr.toString());
            if (this.ms != null) {
                str = this.ms.toString();
            }
            dest.writeString(str);
            dest.writeInt(this.mt ? 1 : 0);
            if (!this.mu) {
                i = 0;
            }
            dest.writeInt(i);
            dest.writeString(this.mv);
            dest.writeInt(this.mw);
            dest.writeInt(this.mx);
            dest.writeInt(this.my);
            return;
        }
        C0117a.m146a(this, dest, flags);
    }
}
