package com.google.android.gms.internal;

import android.os.Parcel;
import android.support.v4.util.TimeUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw.C0722a;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.moments.ItemScope;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.telegram.messenger.MessagesController;

public final class fq extends dw implements SafeParcelable, ItemScope {
    public static final fr CREATOR = new fr();
    private static final HashMap<String, C0722a<?, ?>> rH = new HashMap();
    private String hN;
    private final int iM;
    private String mName;
    private String mo;
    private double oE;
    private double oF;
    private final Set<Integer> rI;
    private fq rJ;
    private List<String> rK;
    private fq rL;
    private String rM;
    private String rN;
    private String rO;
    private List<fq> rP;
    private int rQ;
    private List<fq> rR;
    private fq rS;
    private List<fq> rT;
    private String rU;
    private String rV;
    private fq rW;
    private String rX;
    private String rY;
    private String rZ;
    private fq sA;
    private String sB;
    private String sC;
    private String sD;
    private String sE;
    private String sF;
    private List<fq> sa;
    private String sb;
    private String sc;
    private String sd;
    private String se;
    private String sf;
    private String sg;
    private String sh;
    private String si;
    private fq sj;
    private String sk;
    private String sl;
    private String sm;
    private String sn;
    private fq so;
    private fq sp;
    private fq sq;
    private List<fq> sr;
    private String ss;
    private String st;
    private String su;
    private String sv;
    private fq sw;
    private String sx;
    private String sy;
    private String sz;

    static {
        rH.put("about", C0722a.m992a("about", 2, fq.class));
        rH.put("additionalName", C0722a.m999h("additionalName", 3));
        rH.put("address", C0722a.m992a("address", 4, fq.class));
        rH.put("addressCountry", C0722a.m998g("addressCountry", 5));
        rH.put("addressLocality", C0722a.m998g("addressLocality", 6));
        rH.put("addressRegion", C0722a.m998g("addressRegion", 7));
        rH.put("associated_media", C0722a.m993b("associated_media", 8, fq.class));
        rH.put("attendeeCount", C0722a.m995d("attendeeCount", 9));
        rH.put("attendees", C0722a.m993b("attendees", 10, fq.class));
        rH.put("audio", C0722a.m992a("audio", 11, fq.class));
        rH.put("author", C0722a.m993b("author", 12, fq.class));
        rH.put("bestRating", C0722a.m998g("bestRating", 13));
        rH.put("birthDate", C0722a.m998g("birthDate", 14));
        rH.put("byArtist", C0722a.m992a("byArtist", 15, fq.class));
        rH.put("caption", C0722a.m998g("caption", 16));
        rH.put("contentSize", C0722a.m998g("contentSize", 17));
        rH.put("contentUrl", C0722a.m998g("contentUrl", 18));
        rH.put("contributor", C0722a.m993b("contributor", 19, fq.class));
        rH.put("dateCreated", C0722a.m998g("dateCreated", 20));
        rH.put("dateModified", C0722a.m998g("dateModified", 21));
        rH.put("datePublished", C0722a.m998g("datePublished", 22));
        rH.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, C0722a.m998g(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, 23));
        rH.put("duration", C0722a.m998g("duration", 24));
        rH.put("embedUrl", C0722a.m998g("embedUrl", 25));
        rH.put("endDate", C0722a.m998g("endDate", 26));
        rH.put("familyName", C0722a.m998g("familyName", 27));
        rH.put("gender", C0722a.m998g("gender", 28));
        rH.put("geo", C0722a.m992a("geo", 29, fq.class));
        rH.put("givenName", C0722a.m998g("givenName", 30));
        rH.put("height", C0722a.m998g("height", 31));
        rH.put("id", C0722a.m998g("id", 32));
        rH.put("image", C0722a.m998g("image", 33));
        rH.put("inAlbum", C0722a.m992a("inAlbum", 34, fq.class));
        rH.put("latitude", C0722a.m996e("latitude", 36));
        rH.put("location", C0722a.m992a("location", 37, fq.class));
        rH.put("longitude", C0722a.m996e("longitude", 38));
        rH.put("name", C0722a.m998g("name", 39));
        rH.put("partOfTVSeries", C0722a.m992a("partOfTVSeries", 40, fq.class));
        rH.put("performers", C0722a.m993b("performers", 41, fq.class));
        rH.put("playerType", C0722a.m998g("playerType", 42));
        rH.put("postOfficeBoxNumber", C0722a.m998g("postOfficeBoxNumber", 43));
        rH.put("postalCode", C0722a.m998g("postalCode", 44));
        rH.put("ratingValue", C0722a.m998g("ratingValue", 45));
        rH.put("reviewRating", C0722a.m992a("reviewRating", 46, fq.class));
        rH.put("startDate", C0722a.m998g("startDate", 47));
        rH.put("streetAddress", C0722a.m998g("streetAddress", 48));
        rH.put("text", C0722a.m998g("text", 49));
        rH.put("thumbnail", C0722a.m992a("thumbnail", 50, fq.class));
        rH.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_THUMBNAIL_URL, C0722a.m998g(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_THUMBNAIL_URL, 51));
        rH.put("tickerSymbol", C0722a.m998g("tickerSymbol", 52));
        rH.put("type", C0722a.m998g("type", 53));
        rH.put(PlusShare.KEY_CALL_TO_ACTION_URL, C0722a.m998g(PlusShare.KEY_CALL_TO_ACTION_URL, 54));
        rH.put("width", C0722a.m998g("width", 55));
        rH.put("worstRating", C0722a.m998g("worstRating", 56));
    }

    public fq() {
        this.iM = 1;
        this.rI = new HashSet();
    }

    fq(Set<Integer> set, int i, fq fqVar, List<String> list, fq fqVar2, String str, String str2, String str3, List<fq> list2, int i2, List<fq> list3, fq fqVar3, List<fq> list4, String str4, String str5, fq fqVar4, String str6, String str7, String str8, List<fq> list5, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, fq fqVar5, String str18, String str19, String str20, String str21, fq fqVar6, double d, fq fqVar7, double d2, String str22, fq fqVar8, List<fq> list6, String str23, String str24, String str25, String str26, fq fqVar9, String str27, String str28, String str29, fq fqVar10, String str30, String str31, String str32, String str33, String str34, String str35) {
        this.rI = set;
        this.iM = i;
        this.rJ = fqVar;
        this.rK = list;
        this.rL = fqVar2;
        this.rM = str;
        this.rN = str2;
        this.rO = str3;
        this.rP = list2;
        this.rQ = i2;
        this.rR = list3;
        this.rS = fqVar3;
        this.rT = list4;
        this.rU = str4;
        this.rV = str5;
        this.rW = fqVar4;
        this.rX = str6;
        this.rY = str7;
        this.rZ = str8;
        this.sa = list5;
        this.sb = str9;
        this.sc = str10;
        this.sd = str11;
        this.mo = str12;
        this.se = str13;
        this.sf = str14;
        this.sg = str15;
        this.sh = str16;
        this.si = str17;
        this.sj = fqVar5;
        this.sk = str18;
        this.sl = str19;
        this.sm = str20;
        this.sn = str21;
        this.so = fqVar6;
        this.oE = d;
        this.sp = fqVar7;
        this.oF = d2;
        this.mName = str22;
        this.sq = fqVar8;
        this.sr = list6;
        this.ss = str23;
        this.st = str24;
        this.su = str25;
        this.sv = str26;
        this.sw = fqVar9;
        this.sx = str27;
        this.sy = str28;
        this.sz = str29;
        this.sA = fqVar10;
        this.sB = str30;
        this.sC = str31;
        this.sD = str32;
        this.hN = str33;
        this.sE = str34;
        this.sF = str35;
    }

    public fq(Set<Integer> set, fq fqVar, List<String> list, fq fqVar2, String str, String str2, String str3, List<fq> list2, int i, List<fq> list3, fq fqVar3, List<fq> list4, String str4, String str5, fq fqVar4, String str6, String str7, String str8, List<fq> list5, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, fq fqVar5, String str18, String str19, String str20, String str21, fq fqVar6, double d, fq fqVar7, double d2, String str22, fq fqVar8, List<fq> list6, String str23, String str24, String str25, String str26, fq fqVar9, String str27, String str28, String str29, fq fqVar10, String str30, String str31, String str32, String str33, String str34, String str35) {
        this.rI = set;
        this.iM = 1;
        this.rJ = fqVar;
        this.rK = list;
        this.rL = fqVar2;
        this.rM = str;
        this.rN = str2;
        this.rO = str3;
        this.rP = list2;
        this.rQ = i;
        this.rR = list3;
        this.rS = fqVar3;
        this.rT = list4;
        this.rU = str4;
        this.rV = str5;
        this.rW = fqVar4;
        this.rX = str6;
        this.rY = str7;
        this.rZ = str8;
        this.sa = list5;
        this.sb = str9;
        this.sc = str10;
        this.sd = str11;
        this.mo = str12;
        this.se = str13;
        this.sf = str14;
        this.sg = str15;
        this.sh = str16;
        this.si = str17;
        this.sj = fqVar5;
        this.sk = str18;
        this.sl = str19;
        this.sm = str20;
        this.sn = str21;
        this.so = fqVar6;
        this.oE = d;
        this.sp = fqVar7;
        this.oF = d2;
        this.mName = str22;
        this.sq = fqVar8;
        this.sr = list6;
        this.ss = str23;
        this.st = str24;
        this.su = str25;
        this.sv = str26;
        this.sw = fqVar9;
        this.sx = str27;
        this.sy = str28;
        this.sz = str29;
        this.sA = fqVar10;
        this.sB = str30;
        this.sC = str31;
        this.sD = str32;
        this.hN = str33;
        this.sE = str34;
        this.sF = str35;
    }

    protected Object mo856D(String str) {
        return null;
    }

    protected boolean mo857E(String str) {
        return false;
    }

    protected boolean mo1545a(C0722a c0722a) {
        return this.rI.contains(Integer.valueOf(c0722a.bw()));
    }

    protected Object mo1546b(C0722a c0722a) {
        switch (c0722a.bw()) {
            case 2:
                return this.rJ;
            case 3:
                return this.rK;
            case 4:
                return this.rL;
            case 5:
                return this.rM;
            case 6:
                return this.rN;
            case 7:
                return this.rO;
            case 8:
                return this.rP;
            case 9:
                return Integer.valueOf(this.rQ);
            case 10:
                return this.rR;
            case 11:
                return this.rS;
            case 12:
                return this.rT;
            case 13:
                return this.rU;
            case 14:
                return this.rV;
            case 15:
                return this.rW;
            case 16:
                return this.rX;
            case 17:
                return this.rY;
            case 18:
                return this.rZ;
            case TimeUtils.HUNDRED_DAY_FIELD_LEN /*19*/:
                return this.sa;
            case MessagesController.mediaCountDidLoaded /*20*/:
                return this.sb;
            case MessagesController.encryptedChatUpdated /*21*/:
                return this.sc;
            case MessagesController.messagesReadedEncrypted /*22*/:
                return this.sd;
            case MessagesController.encryptedChatCreated /*23*/:
                return this.mo;
            case MessagesController.userPhotosLoaded /*24*/:
                return this.se;
            case 25:
                return this.sf;
            case 26:
                return this.sg;
            case 27:
                return this.sh;
            case 28:
                return this.si;
            case 29:
                return this.sj;
            case 30:
                return this.sk;
            case 31:
                return this.sl;
            case 32:
                return this.sm;
            case 33:
                return this.sn;
            case 34:
                return this.so;
            case 36:
                return Double.valueOf(this.oE);
            case 37:
                return this.sp;
            case 38:
                return Double.valueOf(this.oF);
            case 39:
                return this.mName;
            case 40:
                return this.sq;
            case 41:
                return this.sr;
            case 42:
                return this.ss;
            case 43:
                return this.st;
            case 44:
                return this.su;
            case 45:
                return this.sv;
            case 46:
                return this.sw;
            case 47:
                return this.sx;
            case 48:
                return this.sy;
            case 49:
                return this.sz;
            case 50:
                return this.sA;
            case 51:
                return this.sB;
            case 52:
                return this.sC;
            case 53:
                return this.sD;
            case 54:
                return this.hN;
            case 55:
                return this.sE;
            case 56:
                return this.sF;
            default:
                throw new IllegalStateException("Unknown safe parcelable id=" + c0722a.bw());
        }
    }

    public HashMap<String, C0722a<?, ?>> bp() {
        return rH;
    }

    public int describeContents() {
        fr frVar = CREATOR;
        return 0;
    }

    Set<Integer> di() {
        return this.rI;
    }

    fq dj() {
        return this.rJ;
    }

    fq dk() {
        return this.rL;
    }

    List<fq> dl() {
        return this.rP;
    }

    List<fq> dm() {
        return this.rR;
    }

    fq dn() {
        return this.rS;
    }

    List<fq> m1484do() {
        return this.rT;
    }

    fq dp() {
        return this.rW;
    }

    List<fq> dq() {
        return this.sa;
    }

    fq dr() {
        return this.sj;
    }

    fq ds() {
        return this.so;
    }

    fq dt() {
        return this.sp;
    }

    fq du() {
        return this.sq;
    }

    List<fq> dv() {
        return this.sr;
    }

    fq dw() {
        return this.sw;
    }

    fq dx() {
        return this.sA;
    }

    public fq dy() {
        return this;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof fq)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        fq fqVar = (fq) obj;
        for (C0722a c0722a : rH.values()) {
            if (mo1545a(c0722a)) {
                if (!fqVar.mo1545a(c0722a)) {
                    return false;
                }
                if (!mo1546b(c0722a).equals(fqVar.mo1546b(c0722a))) {
                    return false;
                }
            } else if (fqVar.mo1545a(c0722a)) {
                return false;
            }
        }
        return true;
    }

    public /* synthetic */ Object freeze() {
        return dy();
    }

    public ItemScope getAbout() {
        return this.rJ;
    }

    public List<String> getAdditionalName() {
        return this.rK;
    }

    public ItemScope getAddress() {
        return this.rL;
    }

    public String getAddressCountry() {
        return this.rM;
    }

    public String getAddressLocality() {
        return this.rN;
    }

    public String getAddressRegion() {
        return this.rO;
    }

    public List<ItemScope> getAssociated_media() {
        return (ArrayList) this.rP;
    }

    public int getAttendeeCount() {
        return this.rQ;
    }

    public List<ItemScope> getAttendees() {
        return (ArrayList) this.rR;
    }

    public ItemScope getAudio() {
        return this.rS;
    }

    public List<ItemScope> getAuthor() {
        return (ArrayList) this.rT;
    }

    public String getBestRating() {
        return this.rU;
    }

    public String getBirthDate() {
        return this.rV;
    }

    public ItemScope getByArtist() {
        return this.rW;
    }

    public String getCaption() {
        return this.rX;
    }

    public String getContentSize() {
        return this.rY;
    }

    public String getContentUrl() {
        return this.rZ;
    }

    public List<ItemScope> getContributor() {
        return (ArrayList) this.sa;
    }

    public String getDateCreated() {
        return this.sb;
    }

    public String getDateModified() {
        return this.sc;
    }

    public String getDatePublished() {
        return this.sd;
    }

    public String getDescription() {
        return this.mo;
    }

    public String getDuration() {
        return this.se;
    }

    public String getEmbedUrl() {
        return this.sf;
    }

    public String getEndDate() {
        return this.sg;
    }

    public String getFamilyName() {
        return this.sh;
    }

    public String getGender() {
        return this.si;
    }

    public ItemScope getGeo() {
        return this.sj;
    }

    public String getGivenName() {
        return this.sk;
    }

    public String getHeight() {
        return this.sl;
    }

    public String getId() {
        return this.sm;
    }

    public String getImage() {
        return this.sn;
    }

    public ItemScope getInAlbum() {
        return this.so;
    }

    public double getLatitude() {
        return this.oE;
    }

    public ItemScope getLocation() {
        return this.sp;
    }

    public double getLongitude() {
        return this.oF;
    }

    public String getName() {
        return this.mName;
    }

    public ItemScope getPartOfTVSeries() {
        return this.sq;
    }

    public List<ItemScope> getPerformers() {
        return (ArrayList) this.sr;
    }

    public String getPlayerType() {
        return this.ss;
    }

    public String getPostOfficeBoxNumber() {
        return this.st;
    }

    public String getPostalCode() {
        return this.su;
    }

    public String getRatingValue() {
        return this.sv;
    }

    public ItemScope getReviewRating() {
        return this.sw;
    }

    public String getStartDate() {
        return this.sx;
    }

    public String getStreetAddress() {
        return this.sy;
    }

    public String getText() {
        return this.sz;
    }

    public ItemScope getThumbnail() {
        return this.sA;
    }

    public String getThumbnailUrl() {
        return this.sB;
    }

    public String getTickerSymbol() {
        return this.sC;
    }

    public String getType() {
        return this.sD;
    }

    public String getUrl() {
        return this.hN;
    }

    int getVersionCode() {
        return this.iM;
    }

    public String getWidth() {
        return this.sE;
    }

    public String getWorstRating() {
        return this.sF;
    }

    public boolean hasAbout() {
        return this.rI.contains(Integer.valueOf(2));
    }

    public boolean hasAdditionalName() {
        return this.rI.contains(Integer.valueOf(3));
    }

    public boolean hasAddress() {
        return this.rI.contains(Integer.valueOf(4));
    }

    public boolean hasAddressCountry() {
        return this.rI.contains(Integer.valueOf(5));
    }

    public boolean hasAddressLocality() {
        return this.rI.contains(Integer.valueOf(6));
    }

    public boolean hasAddressRegion() {
        return this.rI.contains(Integer.valueOf(7));
    }

    public boolean hasAssociated_media() {
        return this.rI.contains(Integer.valueOf(8));
    }

    public boolean hasAttendeeCount() {
        return this.rI.contains(Integer.valueOf(9));
    }

    public boolean hasAttendees() {
        return this.rI.contains(Integer.valueOf(10));
    }

    public boolean hasAudio() {
        return this.rI.contains(Integer.valueOf(11));
    }

    public boolean hasAuthor() {
        return this.rI.contains(Integer.valueOf(12));
    }

    public boolean hasBestRating() {
        return this.rI.contains(Integer.valueOf(13));
    }

    public boolean hasBirthDate() {
        return this.rI.contains(Integer.valueOf(14));
    }

    public boolean hasByArtist() {
        return this.rI.contains(Integer.valueOf(15));
    }

    public boolean hasCaption() {
        return this.rI.contains(Integer.valueOf(16));
    }

    public boolean hasContentSize() {
        return this.rI.contains(Integer.valueOf(17));
    }

    public boolean hasContentUrl() {
        return this.rI.contains(Integer.valueOf(18));
    }

    public boolean hasContributor() {
        return this.rI.contains(Integer.valueOf(19));
    }

    public boolean hasDateCreated() {
        return this.rI.contains(Integer.valueOf(20));
    }

    public boolean hasDateModified() {
        return this.rI.contains(Integer.valueOf(21));
    }

    public boolean hasDatePublished() {
        return this.rI.contains(Integer.valueOf(22));
    }

    public boolean hasDescription() {
        return this.rI.contains(Integer.valueOf(23));
    }

    public boolean hasDuration() {
        return this.rI.contains(Integer.valueOf(24));
    }

    public boolean hasEmbedUrl() {
        return this.rI.contains(Integer.valueOf(25));
    }

    public boolean hasEndDate() {
        return this.rI.contains(Integer.valueOf(26));
    }

    public boolean hasFamilyName() {
        return this.rI.contains(Integer.valueOf(27));
    }

    public boolean hasGender() {
        return this.rI.contains(Integer.valueOf(28));
    }

    public boolean hasGeo() {
        return this.rI.contains(Integer.valueOf(29));
    }

    public boolean hasGivenName() {
        return this.rI.contains(Integer.valueOf(30));
    }

    public boolean hasHeight() {
        return this.rI.contains(Integer.valueOf(31));
    }

    public boolean hasId() {
        return this.rI.contains(Integer.valueOf(32));
    }

    public boolean hasImage() {
        return this.rI.contains(Integer.valueOf(33));
    }

    public boolean hasInAlbum() {
        return this.rI.contains(Integer.valueOf(34));
    }

    public boolean hasLatitude() {
        return this.rI.contains(Integer.valueOf(36));
    }

    public boolean hasLocation() {
        return this.rI.contains(Integer.valueOf(37));
    }

    public boolean hasLongitude() {
        return this.rI.contains(Integer.valueOf(38));
    }

    public boolean hasName() {
        return this.rI.contains(Integer.valueOf(39));
    }

    public boolean hasPartOfTVSeries() {
        return this.rI.contains(Integer.valueOf(40));
    }

    public boolean hasPerformers() {
        return this.rI.contains(Integer.valueOf(41));
    }

    public boolean hasPlayerType() {
        return this.rI.contains(Integer.valueOf(42));
    }

    public boolean hasPostOfficeBoxNumber() {
        return this.rI.contains(Integer.valueOf(43));
    }

    public boolean hasPostalCode() {
        return this.rI.contains(Integer.valueOf(44));
    }

    public boolean hasRatingValue() {
        return this.rI.contains(Integer.valueOf(45));
    }

    public boolean hasReviewRating() {
        return this.rI.contains(Integer.valueOf(46));
    }

    public boolean hasStartDate() {
        return this.rI.contains(Integer.valueOf(47));
    }

    public boolean hasStreetAddress() {
        return this.rI.contains(Integer.valueOf(48));
    }

    public boolean hasText() {
        return this.rI.contains(Integer.valueOf(49));
    }

    public boolean hasThumbnail() {
        return this.rI.contains(Integer.valueOf(50));
    }

    public boolean hasThumbnailUrl() {
        return this.rI.contains(Integer.valueOf(51));
    }

    public boolean hasTickerSymbol() {
        return this.rI.contains(Integer.valueOf(52));
    }

    public boolean hasType() {
        return this.rI.contains(Integer.valueOf(53));
    }

    public boolean hasUrl() {
        return this.rI.contains(Integer.valueOf(54));
    }

    public boolean hasWidth() {
        return this.rI.contains(Integer.valueOf(55));
    }

    public boolean hasWorstRating() {
        return this.rI.contains(Integer.valueOf(56));
    }

    public int hashCode() {
        int i = 0;
        for (C0722a c0722a : rH.values()) {
            int hashCode;
            if (mo1545a(c0722a)) {
                hashCode = mo1546b(c0722a).hashCode() + (i + c0722a.bw());
            } else {
                hashCode = i;
            }
            i = hashCode;
        }
        return i;
    }

    public boolean isDataValid() {
        return true;
    }

    public void writeToParcel(Parcel out, int flags) {
        fr frVar = CREATOR;
        fr.m617a(this, out, flags);
    }
}
