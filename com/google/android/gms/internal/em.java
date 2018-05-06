package com.google.android.gms.internal;

import android.content.Context;
import android.content.Intent;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameBuffer;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.OnGamesLoadedListener;
import com.google.android.gms.games.OnPlayersLoadedListener;
import com.google.android.gms.games.OnSignOutCompleteListener;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.OnAchievementUpdatedListener;
import com.google.android.gms.games.achievement.OnAchievementsLoadedListener;
import com.google.android.gms.games.leaderboard.LeaderboardBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.OnLeaderboardMetadataLoadedListener;
import com.google.android.gms.games.leaderboard.OnLeaderboardScoresLoadedListener;
import com.google.android.gms.games.leaderboard.OnScoreSubmittedListener;
import com.google.android.gms.games.leaderboard.SubmitScoreResult;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationBuffer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.OnInvitationsLoadedListener;
import com.google.android.gms.games.multiplayer.ParticipantUtils;
import com.google.android.gms.games.multiplayer.realtime.C0905a;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeReliableMessageSentListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeSocket;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.internal.de.C0176b;
import com.google.android.gms.internal.de.C0713c;
import com.google.android.gms.internal.de.C0911d;
import com.google.android.gms.internal.er.C0736a;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class em extends de<er> {
    private final String it;
    private final String mF;
    private final Map<String, et> mG;
    private PlayerEntity mH;
    private GameEntity mI;
    private final es mJ;
    private boolean mK = false;
    private final Binder mL;
    private final long mM;
    private final boolean mN;

    final class ag extends C0176b<RealTimeReliableMessageSentListener> {
        private final int iC;
        final /* synthetic */ em mP;
        private final String nf;
        private final int ng;

        ag(em emVar, RealTimeReliableMessageSentListener realTimeReliableMessageSentListener, int i, int i2, String str) {
            this.mP = emVar;
            super(emVar, realTimeReliableMessageSentListener);
            this.iC = i;
            this.ng = i2;
            this.nf = str;
        }

        public void m1034a(RealTimeReliableMessageSentListener realTimeReliableMessageSentListener) {
            if (realTimeReliableMessageSentListener != null) {
                realTimeReliableMessageSentListener.onRealTimeMessageSent(this.iC, this.ng, this.nf);
            }
        }

        protected void aF() {
        }
    }

    final class ao extends C0176b<OnSignOutCompleteListener> {
        final /* synthetic */ em mP;

        public ao(em emVar, OnSignOutCompleteListener onSignOutCompleteListener) {
            this.mP = emVar;
            super(emVar, onSignOutCompleteListener);
        }

        public void m1036a(OnSignOutCompleteListener onSignOutCompleteListener) {
            onSignOutCompleteListener.onSignOutComplete();
        }

        protected void aF() {
        }
    }

    final class aq extends C0176b<OnScoreSubmittedListener> {
        final /* synthetic */ em mP;
        private final SubmitScoreResult nn;

        public aq(em emVar, OnScoreSubmittedListener onScoreSubmittedListener, SubmitScoreResult submitScoreResult) {
            this.mP = emVar;
            super(emVar, onScoreSubmittedListener);
            this.nn = submitScoreResult;
        }

        public void m1038a(OnScoreSubmittedListener onScoreSubmittedListener) {
            onScoreSubmittedListener.onScoreSubmitted(this.nn.getStatusCode(), this.nn);
        }

        protected void aF() {
        }
    }

    final class C0726e extends C0176b<OnAchievementUpdatedListener> {
        private final int iC;
        final /* synthetic */ em mP;
        private final String mR;

        C0726e(em emVar, OnAchievementUpdatedListener onAchievementUpdatedListener, int i, String str) {
            this.mP = emVar;
            super(emVar, onAchievementUpdatedListener);
            this.iC = i;
            this.mR = str;
        }

        protected void m1040a(OnAchievementUpdatedListener onAchievementUpdatedListener) {
            onAchievementUpdatedListener.onAchievementUpdated(this.iC, this.mR);
        }

        protected void aF() {
        }
    }

    final class C0727m extends C0176b<OnInvitationReceivedListener> {
        final /* synthetic */ em mP;
        private final Invitation mV;

        C0727m(em emVar, OnInvitationReceivedListener onInvitationReceivedListener, Invitation invitation) {
            this.mP = emVar;
            super(emVar, onInvitationReceivedListener);
            this.mV = invitation;
        }

        protected void m1042a(OnInvitationReceivedListener onInvitationReceivedListener) {
            onInvitationReceivedListener.onInvitationReceived(this.mV);
        }

        protected void aF() {
        }
    }

    final class C0728r extends C0176b<OnLeaderboardScoresLoadedListener> {
        final /* synthetic */ em mP;
        private final C0646d mY;
        private final C0646d mZ;

        C0728r(em emVar, OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener, C0646d c0646d, C0646d c0646d2) {
            this.mP = emVar;
            super(emVar, onLeaderboardScoresLoadedListener);
            this.mY = c0646d;
            this.mZ = c0646d2;
        }

        protected void m1044a(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener) {
            C0646d c0646d = null;
            C0646d c0646d2 = this.mY;
            C0646d c0646d3 = this.mZ;
            if (onLeaderboardScoresLoadedListener != null) {
                try {
                    onLeaderboardScoresLoadedListener.onLeaderboardScoresLoaded(c0646d3.getStatusCode(), new LeaderboardBuffer(c0646d2), new LeaderboardScoreBuffer(c0646d3));
                    c0646d3 = null;
                } catch (Throwable th) {
                    if (c0646d2 != null) {
                        c0646d2.close();
                    }
                    if (c0646d3 != null) {
                        c0646d3.close();
                    }
                }
            } else {
                c0646d = c0646d3;
                c0646d3 = c0646d2;
            }
            if (c0646d3 != null) {
                c0646d3.close();
            }
            if (c0646d != null) {
                c0646d.close();
            }
        }

        protected void aF() {
            if (this.mY != null) {
                this.mY.close();
            }
            if (this.mZ != null) {
                this.mZ.close();
            }
        }
    }

    final class C0729u extends C0176b<RoomUpdateListener> {
        private final int iC;
        final /* synthetic */ em mP;
        private final String nb;

        C0729u(em emVar, RoomUpdateListener roomUpdateListener, int i, String str) {
            this.mP = emVar;
            super(emVar, roomUpdateListener);
            this.iC = i;
            this.nb = str;
        }

        public void m1046a(RoomUpdateListener roomUpdateListener) {
            roomUpdateListener.onLeftRoom(this.iC, this.nb);
        }

        protected void aF() {
        }
    }

    final class C0730v extends C0176b<RealTimeMessageReceivedListener> {
        final /* synthetic */ em mP;
        private final RealTimeMessage nc;

        C0730v(em emVar, RealTimeMessageReceivedListener realTimeMessageReceivedListener, RealTimeMessage realTimeMessage) {
            this.mP = emVar;
            super(emVar, realTimeMessageReceivedListener);
            this.nc = realTimeMessage;
        }

        public void m1048a(RealTimeMessageReceivedListener realTimeMessageReceivedListener) {
            ep.m454b("GamesClient", "Deliver Message received callback");
            if (realTimeMessageReceivedListener != null) {
                realTimeMessageReceivedListener.onRealTimeMessageReceived(this.nc);
            }
        }

        protected void aF() {
        }
    }

    final class C0731w extends C0176b<RoomStatusUpdateListener> {
        final /* synthetic */ em mP;
        private final String nd;

        C0731w(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, String str) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener);
            this.nd = str;
        }

        public void m1050a(RoomStatusUpdateListener roomStatusUpdateListener) {
            if (roomStatusUpdateListener != null) {
                roomStatusUpdateListener.onP2PConnected(this.nd);
            }
        }

        protected void aF() {
        }
    }

    final class C0732x extends C0176b<RoomStatusUpdateListener> {
        final /* synthetic */ em mP;
        private final String nd;

        C0732x(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, String str) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener);
            this.nd = str;
        }

        public void m1052a(RoomStatusUpdateListener roomStatusUpdateListener) {
            if (roomStatusUpdateListener != null) {
                roomStatusUpdateListener.onP2PDisconnected(this.nd);
            }
        }

        protected void aF() {
        }
    }

    final class af extends C0713c<OnPlayersLoadedListener> {
        final /* synthetic */ em mP;

        af(em emVar, OnPlayersLoadedListener onPlayersLoadedListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, onPlayersLoadedListener, c0646d);
        }

        protected void m1404a(OnPlayersLoadedListener onPlayersLoadedListener, C0646d c0646d) {
            onPlayersLoadedListener.onPlayersLoaded(c0646d.getStatusCode(), new PlayerBuffer(c0646d));
        }
    }

    abstract class C0912b extends C0713c<RoomUpdateListener> {
        final /* synthetic */ em mP;

        C0912b(em emVar, RoomUpdateListener roomUpdateListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, roomUpdateListener, c0646d);
        }

        protected void m1406a(RoomUpdateListener roomUpdateListener, C0646d c0646d) {
            mo1835a(roomUpdateListener, this.mP.m1422x(c0646d), c0646d.getStatusCode());
        }

        protected abstract void mo1835a(RoomUpdateListener roomUpdateListener, Room room, int i);
    }

    abstract class C0913c extends C0713c<RoomStatusUpdateListener> {
        final /* synthetic */ em mP;

        C0913c(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d);
        }

        protected void m1409a(RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d) {
            mo1834a(roomStatusUpdateListener, this.mP.m1422x(c0646d));
        }

        protected abstract void mo1834a(RoomStatusUpdateListener roomStatusUpdateListener, Room room);
    }

    final class C0914g extends C0713c<OnAchievementsLoadedListener> {
        final /* synthetic */ em mP;

        C0914g(em emVar, OnAchievementsLoadedListener onAchievementsLoadedListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, onAchievementsLoadedListener, c0646d);
        }

        protected void m1412a(OnAchievementsLoadedListener onAchievementsLoadedListener, C0646d c0646d) {
            onAchievementsLoadedListener.onAchievementsLoaded(c0646d.getStatusCode(), new AchievementBuffer(c0646d));
        }
    }

    final class C0915k extends C0713c<OnGamesLoadedListener> {
        final /* synthetic */ em mP;

        C0915k(em emVar, OnGamesLoadedListener onGamesLoadedListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, onGamesLoadedListener, c0646d);
        }

        protected void m1414a(OnGamesLoadedListener onGamesLoadedListener, C0646d c0646d) {
            onGamesLoadedListener.onGamesLoaded(c0646d.getStatusCode(), new GameBuffer(c0646d));
        }
    }

    final class C0916o extends C0713c<OnInvitationsLoadedListener> {
        final /* synthetic */ em mP;

        C0916o(em emVar, OnInvitationsLoadedListener onInvitationsLoadedListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, onInvitationsLoadedListener, c0646d);
        }

        protected void m1416a(OnInvitationsLoadedListener onInvitationsLoadedListener, C0646d c0646d) {
            onInvitationsLoadedListener.onInvitationsLoaded(c0646d.getStatusCode(), new InvitationBuffer(c0646d));
        }
    }

    final class C0917t extends C0713c<OnLeaderboardMetadataLoadedListener> {
        final /* synthetic */ em mP;

        C0917t(em emVar, OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, onLeaderboardMetadataLoadedListener, c0646d);
        }

        protected void m1418a(OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener, C0646d c0646d) {
            onLeaderboardMetadataLoadedListener.onLeaderboardMetadataLoaded(c0646d.getStatusCode(), new LeaderboardBuffer(c0646d));
        }
    }

    abstract class C0955a extends C0913c {
        private final ArrayList<String> mO = new ArrayList();
        final /* synthetic */ em mP;

        C0955a(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d, String[] strArr) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d);
            for (Object add : strArr) {
                this.mO.add(add);
            }
        }

        protected void mo1834a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            mo1837a(roomStatusUpdateListener, room, this.mO);
        }

        protected abstract void mo1837a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList);
    }

    final class ae extends el {
        final /* synthetic */ em mP;
        private final OnPlayersLoadedListener ne;

        ae(em emVar, OnPlayersLoadedListener onPlayersLoadedListener) {
            this.mP = emVar;
            this.ne = (OnPlayersLoadedListener) dm.m388a((Object) onPlayersLoadedListener, (Object) "Listener must not be null");
        }

        public void mo875e(C0646d c0646d) {
            this.mP.m957a(new af(this.mP, this.ne, c0646d));
        }
    }

    final class ah extends el {
        final /* synthetic */ em mP;
        final RealTimeReliableMessageSentListener nh;

        public ah(em emVar, RealTimeReliableMessageSentListener realTimeReliableMessageSentListener) {
            this.mP = emVar;
            this.nh = realTimeReliableMessageSentListener;
        }

        public void mo864a(int i, int i2, String str) {
            this.mP.m957a(new ag(this.mP, this.nh, i, i2, str));
        }
    }

    final class ai extends C0913c {
        final /* synthetic */ em mP;

        ai(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d);
        }

        public void mo1834a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onRoomAutoMatching(room);
        }
    }

    final class aj extends el {
        final /* synthetic */ em mP;
        private final RoomUpdateListener ni;
        private final RoomStatusUpdateListener nj;
        private final RealTimeMessageReceivedListener nk;

        public aj(em emVar, RoomUpdateListener roomUpdateListener) {
            this.mP = emVar;
            this.ni = (RoomUpdateListener) dm.m388a((Object) roomUpdateListener, (Object) "Callbacks must not be null");
            this.nj = null;
            this.nk = null;
        }

        public aj(em emVar, RoomUpdateListener roomUpdateListener, RoomStatusUpdateListener roomStatusUpdateListener, RealTimeMessageReceivedListener realTimeMessageReceivedListener) {
            this.mP = emVar;
            this.ni = (RoomUpdateListener) dm.m388a((Object) roomUpdateListener, (Object) "Callbacks must not be null");
            this.nj = roomStatusUpdateListener;
            this.nk = realTimeMessageReceivedListener;
        }

        public void mo867a(C0646d c0646d, String[] strArr) {
            this.mP.m957a(new ab(this.mP, this.nj, c0646d, strArr));
        }

        public void mo869b(C0646d c0646d, String[] strArr) {
            this.mP.m957a(new ac(this.mP, this.nj, c0646d, strArr));
        }

        public void mo872c(C0646d c0646d, String[] strArr) {
            this.mP.m957a(new ad(this.mP, this.nj, c0646d, strArr));
        }

        public void mo874d(C0646d c0646d, String[] strArr) {
            this.mP.m957a(new C0971z(this.mP, this.nj, c0646d, strArr));
        }

        public void mo876e(C0646d c0646d, String[] strArr) {
            this.mP.m957a(new C0970y(this.mP, this.nj, c0646d, strArr));
        }

        public void mo878f(C0646d c0646d, String[] strArr) {
            this.mP.m957a(new aa(this.mP, this.nj, c0646d, strArr));
        }

        public void mo886n(C0646d c0646d) {
            this.mP.m957a(new am(this.mP, this.ni, c0646d));
        }

        public void mo887o(C0646d c0646d) {
            this.mP.m957a(new C0963p(this.mP, this.ni, c0646d));
        }

        public void onLeftRoom(int statusCode, String externalRoomId) {
            this.mP.m957a(new C0729u(this.mP, this.ni, statusCode, externalRoomId));
        }

        public void onP2PConnected(String participantId) {
            this.mP.m957a(new C0731w(this.mP, this.nj, participantId));
        }

        public void onP2PDisconnected(String participantId) {
            this.mP.m957a(new C0732x(this.mP, this.nj, participantId));
        }

        public void onRealTimeMessageReceived(RealTimeMessage message) {
            ep.m454b("GamesClient", "RoomBinderCallbacks: onRealTimeMessageReceived");
            this.mP.m957a(new C0730v(this.mP, this.nk, message));
        }

        public void mo894p(C0646d c0646d) {
            this.mP.m957a(new al(this.mP, this.nj, c0646d));
        }

        public void mo895q(C0646d c0646d) {
            this.mP.m957a(new ai(this.mP, this.nj, c0646d));
        }

        public void mo896r(C0646d c0646d) {
            this.mP.m957a(new ak(this.mP, this.ni, c0646d));
        }

        public void mo897s(C0646d c0646d) {
            this.mP.m957a(new C0958h(this.mP, this.nj, c0646d));
        }

        public void mo898t(C0646d c0646d) {
            this.mP.m957a(new C0959i(this.mP, this.nj, c0646d));
        }
    }

    final class ak extends C0912b {
        final /* synthetic */ em mP;

        ak(em emVar, RoomUpdateListener roomUpdateListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, roomUpdateListener, c0646d);
        }

        public void mo1835a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onRoomConnected(i, room);
        }
    }

    final class al extends C0913c {
        final /* synthetic */ em mP;

        al(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d);
        }

        public void mo1834a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onRoomConnecting(room);
        }
    }

    final class am extends C0912b {
        final /* synthetic */ em mP;

        public am(em emVar, RoomUpdateListener roomUpdateListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, roomUpdateListener, c0646d);
        }

        public void mo1835a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onRoomCreated(i, room);
        }
    }

    final class an extends el {
        final /* synthetic */ em mP;
        private final OnSignOutCompleteListener nl;

        public an(em emVar, OnSignOutCompleteListener onSignOutCompleteListener) {
            this.mP = emVar;
            this.nl = (OnSignOutCompleteListener) dm.m388a((Object) onSignOutCompleteListener, (Object) "Listener must not be null");
        }

        public void onSignOutComplete() {
            this.mP.m957a(new ao(this.mP, this.nl));
        }
    }

    final class ap extends el {
        final /* synthetic */ em mP;
        private final OnScoreSubmittedListener nm;

        public ap(em emVar, OnScoreSubmittedListener onScoreSubmittedListener) {
            this.mP = emVar;
            this.nm = (OnScoreSubmittedListener) dm.m388a((Object) onScoreSubmittedListener, (Object) "Listener must not be null");
        }

        public void mo873d(C0646d c0646d) {
            this.mP.m957a(new aq(this.mP, this.nm, new SubmitScoreResult(c0646d)));
        }
    }

    final class C0956d extends el {
        final /* synthetic */ em mP;
        private final OnAchievementUpdatedListener mQ;

        C0956d(em emVar, OnAchievementUpdatedListener onAchievementUpdatedListener) {
            this.mP = emVar;
            this.mQ = (OnAchievementUpdatedListener) dm.m388a((Object) onAchievementUpdatedListener, (Object) "Listener must not be null");
        }

        public void onAchievementUpdated(int statusCode, String achievementId) {
            this.mP.m957a(new C0726e(this.mP, this.mQ, statusCode, achievementId));
        }
    }

    final class C0957f extends el {
        final /* synthetic */ em mP;
        private final OnAchievementsLoadedListener mS;

        C0957f(em emVar, OnAchievementsLoadedListener onAchievementsLoadedListener) {
            this.mP = emVar;
            this.mS = (OnAchievementsLoadedListener) dm.m388a((Object) onAchievementsLoadedListener, (Object) "Listener must not be null");
        }

        public void mo868b(C0646d c0646d) {
            this.mP.m957a(new C0914g(this.mP, this.mS, c0646d));
        }
    }

    final class C0958h extends C0913c {
        final /* synthetic */ em mP;

        C0958h(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d);
        }

        public void mo1834a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onConnectedToRoom(room);
        }
    }

    final class C0959i extends C0913c {
        final /* synthetic */ em mP;

        C0959i(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d);
        }

        public void mo1834a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onDisconnectedFromRoom(room);
        }
    }

    final class C0960j extends el {
        final /* synthetic */ em mP;
        private final OnGamesLoadedListener mT;

        C0960j(em emVar, OnGamesLoadedListener onGamesLoadedListener) {
            this.mP = emVar;
            this.mT = (OnGamesLoadedListener) dm.m388a((Object) onGamesLoadedListener, (Object) "Listener must not be null");
        }

        public void mo879g(C0646d c0646d) {
            this.mP.m957a(new C0915k(this.mP, this.mT, c0646d));
        }
    }

    final class C0961l extends el {
        final /* synthetic */ em mP;
        private final OnInvitationReceivedListener mU;

        C0961l(em emVar, OnInvitationReceivedListener onInvitationReceivedListener) {
            this.mP = emVar;
            this.mU = onInvitationReceivedListener;
        }

        public void mo883k(C0646d c0646d) {
            InvitationBuffer invitationBuffer = new InvitationBuffer(c0646d);
            Invitation invitation = null;
            try {
                if (invitationBuffer.getCount() > 0) {
                    invitation = (Invitation) ((Invitation) invitationBuffer.get(0)).freeze();
                }
                invitationBuffer.close();
                if (invitation != null) {
                    this.mP.m957a(new C0727m(this.mP, this.mU, invitation));
                }
            } catch (Throwable th) {
                invitationBuffer.close();
            }
        }
    }

    final class C0962n extends el {
        final /* synthetic */ em mP;
        private final OnInvitationsLoadedListener mW;

        C0962n(em emVar, OnInvitationsLoadedListener onInvitationsLoadedListener) {
            this.mP = emVar;
            this.mW = onInvitationsLoadedListener;
        }

        public void mo882j(C0646d c0646d) {
            this.mP.m957a(new C0916o(this.mP, this.mW, c0646d));
        }
    }

    final class C0963p extends C0912b {
        final /* synthetic */ em mP;

        public C0963p(em emVar, RoomUpdateListener roomUpdateListener, C0646d c0646d) {
            this.mP = emVar;
            super(emVar, roomUpdateListener, c0646d);
        }

        public void mo1835a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onJoinedRoom(i, room);
        }
    }

    final class C0964q extends el {
        final /* synthetic */ em mP;
        private final OnLeaderboardScoresLoadedListener mX;

        C0964q(em emVar, OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener) {
            this.mP = emVar;
            this.mX = (OnLeaderboardScoresLoadedListener) dm.m388a((Object) onLeaderboardScoresLoadedListener, (Object) "Listener must not be null");
        }

        public void mo866a(C0646d c0646d, C0646d c0646d2) {
            this.mP.m957a(new C0728r(this.mP, this.mX, c0646d, c0646d2));
        }
    }

    final class C0965s extends el {
        final /* synthetic */ em mP;
        private final OnLeaderboardMetadataLoadedListener na;

        C0965s(em emVar, OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener) {
            this.mP = emVar;
            this.na = (OnLeaderboardMetadataLoadedListener) dm.m388a((Object) onLeaderboardMetadataLoadedListener, (Object) "Listener must not be null");
        }

        public void mo871c(C0646d c0646d) {
            this.mP.m957a(new C0917t(this.mP, this.na, c0646d));
        }
    }

    final class aa extends C0955a {
        final /* synthetic */ em mP;

        aa(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d, String[] strArr) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d, strArr);
        }

        protected void mo1837a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeersDisconnected(room, arrayList);
        }
    }

    final class ab extends C0955a {
        final /* synthetic */ em mP;

        ab(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d, String[] strArr) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d, strArr);
        }

        protected void mo1837a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerInvitedToRoom(room, arrayList);
        }
    }

    final class ac extends C0955a {
        final /* synthetic */ em mP;

        ac(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d, String[] strArr) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d, strArr);
        }

        protected void mo1837a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerJoined(room, arrayList);
        }
    }

    final class ad extends C0955a {
        final /* synthetic */ em mP;

        ad(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d, String[] strArr) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d, strArr);
        }

        protected void mo1837a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerLeft(room, arrayList);
        }
    }

    final class C0970y extends C0955a {
        final /* synthetic */ em mP;

        C0970y(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d, String[] strArr) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d, strArr);
        }

        protected void mo1837a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeersConnected(room, arrayList);
        }
    }

    final class C0971z extends C0955a {
        final /* synthetic */ em mP;

        C0971z(em emVar, RoomStatusUpdateListener roomStatusUpdateListener, C0646d c0646d, String[] strArr) {
            this.mP = emVar;
            super(emVar, roomStatusUpdateListener, c0646d, strArr);
        }

        protected void mo1837a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerDeclined(room, arrayList);
        }
    }

    public em(Context context, String str, String str2, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String[] strArr, int i, View view, boolean z) {
        super(context, connectionCallbacks, onConnectionFailedListener, strArr);
        this.mF = str;
        this.it = (String) dm.m392e(str2);
        this.mL = new Binder();
        this.mG = new HashMap();
        this.mJ = es.m568a(this, i);
        setViewForPopups(view);
        this.mM = (long) hashCode();
        this.mN = z;
    }

    private et m1420K(String str) {
        try {
            String M = ((er) bd()).mo903M(str);
            if (M == null) {
                return null;
            }
            ep.m457e("GamesClient", "Creating a socket to bind to:" + M);
            LocalSocket localSocket = new LocalSocket();
            try {
                localSocket.connect(new LocalSocketAddress(M));
                et etVar = new et(localSocket, str);
                this.mG.put(str, etVar);
                return etVar;
            } catch (IOException e) {
                ep.m456d("GamesClient", "connect() call failed on socket: " + e.getMessage());
                return null;
            }
        } catch (RemoteException e2) {
            ep.m456d("GamesClient", "Unable to create socket. Service died.");
            return null;
        }
    }

    private void bR() {
        this.mH = null;
    }

    private void bS() {
        for (et close : this.mG.values()) {
            try {
                close.close();
            } catch (Throwable e) {
                ep.m453a("GamesClient", "IOException:", e);
            }
        }
        this.mG.clear();
    }

    private Room m1422x(C0646d c0646d) {
        C0905a c0905a = new C0905a(c0646d);
        Room room = null;
        try {
            if (c0905a.getCount() > 0) {
                room = (Room) ((Room) c0905a.get(0)).freeze();
            }
            c0905a.close();
            return room;
        } catch (Throwable th) {
            c0905a.close();
        }
    }

    protected er m1423A(IBinder iBinder) {
        return C0736a.m1165C(iBinder);
    }

    public int m1424a(byte[] bArr, String str, String[] strArr) {
        dm.m388a((Object) strArr, (Object) "Participant IDs must not be null");
        try {
            return ((er) bd()).mo931b(bArr, str, strArr);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
            return -1;
        }
    }

    protected void mo1539a(int i, IBinder iBinder, Bundle bundle) {
        if (i == 0 && bundle != null) {
            this.mK = bundle.getBoolean("show_welcome_popup");
        }
        super.mo1539a(i, iBinder, bundle);
    }

    public void m1426a(IBinder iBinder, Bundle bundle) {
        if (isConnected()) {
            try {
                ((er) bd()).mo908a(iBinder, bundle);
            } catch (RemoteException e) {
                ep.m455c("GamesClient", "service died");
            }
        }
    }

    protected void mo1540a(ConnectionResult connectionResult) {
        super.mo1540a(connectionResult);
        this.mK = false;
    }

    public void m1428a(OnPlayersLoadedListener onPlayersLoadedListener, int i, boolean z, boolean z2) {
        try {
            ((er) bd()).mo911a(new ae(this, onPlayersLoadedListener), i, z, z2);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void m1429a(OnAchievementUpdatedListener onAchievementUpdatedListener, String str) {
        if (onAchievementUpdatedListener == null) {
            eq eqVar = null;
        } else {
            Object c0956d = new C0956d(this, onAchievementUpdatedListener);
        }
        try {
            ((er) bd()).mo923a(eqVar, str, this.mJ.bZ(), this.mJ.bY());
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void m1430a(OnAchievementUpdatedListener onAchievementUpdatedListener, String str, int i) {
        try {
            ((er) bd()).mo918a(onAchievementUpdatedListener == null ? null : new C0956d(this, onAchievementUpdatedListener), str, i, this.mJ.bZ(), this.mJ.bY());
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void m1431a(OnScoreSubmittedListener onScoreSubmittedListener, String str, long j, String str2) {
        try {
            ((er) bd()).mo922a(onScoreSubmittedListener == null ? null : new ap(this, onScoreSubmittedListener), str, j, str2);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    protected void mo1533a(dj djVar, C0911d c0911d) throws RemoteException {
        String locale = getContext().getResources().getConfiguration().locale.toString();
        Bundle bundle = new Bundle();
        bundle.putBoolean("com.google.android.gms.games.key.isHeadless", this.mN);
        djVar.mo838a(c0911d, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), this.it, aY(), this.mF, this.mJ.bZ(), locale, bundle);
    }

    protected void mo1538a(String... strArr) {
        int i = 0;
        boolean z = false;
        for (String str : strArr) {
            if (str.equals(Scopes.GAMES)) {
                z = true;
            } else if (str.equals("https://www.googleapis.com/auth/games.firstparty")) {
                i = 1;
            }
        }
        if (i != 0) {
            dm.m389a(!z, String.format("Cannot have both %s and %s!", new Object[]{Scopes.GAMES, "https://www.googleapis.com/auth/games.firstparty"}));
            return;
        }
        dm.m389a(z, String.format("GamesClient requires %s to function.", new Object[]{Scopes.GAMES}));
    }

    protected void aZ() {
        super.aZ();
        if (this.mK) {
            this.mJ.bX();
            this.mK = false;
        }
    }

    protected String ag() {
        return "com.google.android.gms.games.service.START";
    }

    protected String ah() {
        return "com.google.android.gms.games.internal.IGamesService";
    }

    public void m1434b(OnAchievementUpdatedListener onAchievementUpdatedListener, String str) {
        if (onAchievementUpdatedListener == null) {
            eq eqVar = null;
        } else {
            Object c0956d = new C0956d(this, onAchievementUpdatedListener);
        }
        try {
            ((er) bd()).mo938b(eqVar, str, this.mJ.bZ(), this.mJ.bY());
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void m1435b(OnAchievementUpdatedListener onAchievementUpdatedListener, String str, int i) {
        try {
            ((er) bd()).mo936b(onAchievementUpdatedListener == null ? null : new C0956d(this, onAchievementUpdatedListener), str, i, this.mJ.bZ(), this.mJ.bY());
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void bT() {
        if (isConnected()) {
            try {
                ((er) bd()).bT();
            } catch (RemoteException e) {
                ep.m455c("GamesClient", "service died");
            }
        }
    }

    protected Bundle ba() {
        try {
            Bundle ba = ((er) bd()).ba();
            if (ba == null) {
                return ba;
            }
            ba.setClassLoader(em.class.getClassLoader());
            return ba;
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
            return null;
        }
    }

    public void clearNotifications(int notificationTypes) {
        try {
            ((er) bd()).clearNotifications(notificationTypes);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void connect() {
        bR();
        super.connect();
    }

    public void createRoom(RoomConfig config) {
        try {
            ((er) bd()).mo914a(new aj(this, config.getRoomUpdateListener(), config.getRoomStatusUpdateListener(), config.getMessageReceivedListener()), this.mL, config.getVariant(), config.getInvitedPlayerIds(), config.getAutoMatchCriteria(), config.isSocketEnabled(), this.mM);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void disconnect() {
        this.mK = false;
        if (isConnected()) {
            try {
                er erVar = (er) bd();
                erVar.bT();
                erVar.mo968g(this.mM);
                erVar.mo964f(this.mM);
            } catch (RemoteException e) {
                ep.m455c("GamesClient", "Failed to notify client disconnect.");
            }
        }
        bS();
        super.disconnect();
    }

    public Intent getAchievementsIntent() {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.VIEW_ACHIEVEMENTS");
        intent.addFlags(67108864);
        return eo.m452c(intent);
    }

    public Intent getAllLeaderboardsIntent() {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.VIEW_LEADERBOARDS");
        intent.putExtra("com.google.android.gms.games.GAME_PACKAGE_NAME", this.mF);
        intent.addFlags(67108864);
        return eo.m452c(intent);
    }

    public String getAppId() {
        try {
            return ((er) bd()).getAppId();
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
            return null;
        }
    }

    public String getCurrentAccountName() {
        try {
            return ((er) bd()).getCurrentAccountName();
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
            return null;
        }
    }

    public Game getCurrentGame() {
        GameBuffer gameBuffer;
        bc();
        synchronized (this) {
            if (this.mI == null) {
                try {
                    gameBuffer = new GameBuffer(((er) bd()).bW());
                    if (gameBuffer.getCount() > 0) {
                        this.mI = (GameEntity) gameBuffer.get(0).freeze();
                    }
                    gameBuffer.close();
                } catch (RemoteException e) {
                    ep.m455c("GamesClient", "service died");
                } catch (Throwable th) {
                    gameBuffer.close();
                }
            }
        }
        return this.mI;
    }

    public Player getCurrentPlayer() {
        PlayerBuffer playerBuffer;
        bc();
        synchronized (this) {
            if (this.mH == null) {
                try {
                    playerBuffer = new PlayerBuffer(((er) bd()).bU());
                    if (playerBuffer.getCount() > 0) {
                        this.mH = (PlayerEntity) playerBuffer.get(0).freeze();
                    }
                    playerBuffer.close();
                } catch (RemoteException e) {
                    ep.m455c("GamesClient", "service died");
                } catch (Throwable th) {
                    playerBuffer.close();
                }
            }
        }
        return this.mH;
    }

    public String getCurrentPlayerId() {
        try {
            return ((er) bd()).getCurrentPlayerId();
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
            return null;
        }
    }

    public Intent getInvitationInboxIntent() {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.SHOW_INVITATIONS");
        intent.putExtra("com.google.android.gms.games.GAME_PACKAGE_NAME", this.mF);
        return eo.m452c(intent);
    }

    public Intent getLeaderboardIntent(String leaderboardId) {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.VIEW_LEADERBOARD_SCORES");
        intent.putExtra("com.google.android.gms.games.LEADERBOARD_ID", leaderboardId);
        intent.addFlags(67108864);
        return eo.m452c(intent);
    }

    public RealTimeSocket getRealTimeSocketForParticipant(String roomId, String participantId) {
        if (participantId == null || !ParticipantUtils.m153Q(participantId)) {
            throw new IllegalArgumentException("Bad participant ID");
        }
        et etVar = (et) this.mG.get(participantId);
        return (etVar == null || etVar.isClosed()) ? m1420K(participantId) : etVar;
    }

    public Intent getRealTimeWaitingRoomIntent(Room room, int minParticipantsToStart) {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.SHOW_REAL_TIME_WAITING_ROOM");
        dm.m388a((Object) room, (Object) "Room parameter must not be null");
        intent.putExtra(GamesClient.EXTRA_ROOM, (Parcelable) room.freeze());
        dm.m389a(minParticipantsToStart >= 0, (Object) "minParticipantsToStart must be >= 0");
        intent.putExtra("com.google.android.gms.games.MIN_PARTICIPANTS_TO_START", minParticipantsToStart);
        return eo.m452c(intent);
    }

    public Intent getSelectPlayersIntent(int minPlayers, int maxPlayers) {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.SELECT_PLAYERS");
        intent.putExtra("com.google.android.gms.games.MIN_SELECTIONS", minPlayers);
        intent.putExtra("com.google.android.gms.games.MAX_SELECTIONS", maxPlayers);
        return eo.m452c(intent);
    }

    public Intent getSettingsIntent() {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.SHOW_SETTINGS");
        intent.putExtra("com.google.android.gms.games.GAME_PACKAGE_NAME", this.mF);
        intent.addFlags(67108864);
        return eo.m452c(intent);
    }

    public void m1436i(String str, int i) {
        try {
            ((er) bd()).mo978i(str, i);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void m1437j(String str, int i) {
        try {
            ((er) bd()).mo980j(str, i);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void joinRoom(RoomConfig config) {
        try {
            ((er) bd()).mo915a(new aj(this, config.getRoomUpdateListener(), config.getRoomStatusUpdateListener(), config.getMessageReceivedListener()), this.mL, config.getInvitationId(), config.isSocketEnabled(), this.mM);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void leaveRoom(RoomUpdateListener listener, String roomId) {
        try {
            ((er) bd()).mo963e(new aj(this, listener), roomId);
            bS();
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void loadAchievements(OnAchievementsLoadedListener listener, boolean forceReload) {
        try {
            ((er) bd()).mo943b(new C0957f(this, listener), forceReload);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void loadGame(OnGamesLoadedListener listener) {
        try {
            ((er) bd()).mo956d(new C0960j(this, listener));
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void loadInvitations(OnInvitationsLoadedListener listener) {
        try {
            ((er) bd()).mo961e(new C0962n(this, listener));
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener listener, String leaderboardId, boolean forceReload) {
        try {
            ((er) bd()).mo953c(new C0965s(this, listener), leaderboardId, forceReload);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener listener, boolean forceReload) {
        try {
            ((er) bd()).mo954c(new C0965s(this, listener), forceReload);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void loadMoreScores(OnLeaderboardScoresLoadedListener listener, LeaderboardScoreBuffer buffer, int maxResults, int pageDirection) {
        try {
            ((er) bd()).mo913a(new C0964q(this, listener), buffer.cb().cc(), maxResults, pageDirection);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void loadPlayer(OnPlayersLoadedListener listener, String playerId) {
        try {
            ((er) bd()).mo951c(new ae(this, listener), playerId);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void loadPlayerCenteredScores(OnLeaderboardScoresLoadedListener listener, String leaderboardId, int span, int leaderboardCollection, int maxResults, boolean forceReload) {
        try {
            ((er) bd()).mo935b(new C0964q(this, listener), leaderboardId, span, leaderboardCollection, maxResults, forceReload);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void loadTopScores(OnLeaderboardScoresLoadedListener listener, String leaderboardId, int span, int leaderboardCollection, int maxResults, boolean forceReload) {
        try {
            ((er) bd()).mo917a(new C0964q(this, listener), leaderboardId, span, leaderboardCollection, maxResults, forceReload);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    protected /* synthetic */ IInterface mo1536p(IBinder iBinder) {
        return m1423A(iBinder);
    }

    public void registerInvitationListener(OnInvitationReceivedListener listener) {
        try {
            ((er) bd()).mo912a(new C0961l(this, listener), this.mM);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public int sendReliableRealTimeMessage(RealTimeReliableMessageSentListener listener, byte[] messageData, String roomId, String recipientParticipantId) {
        try {
            return ((er) bd()).mo907a(new ah(this, listener), messageData, roomId, recipientParticipantId);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
            return -1;
        }
    }

    public int sendUnreliableRealTimeMessageToAll(byte[] messageData, String roomId) {
        try {
            return ((er) bd()).mo931b(messageData, roomId, null);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
            return -1;
        }
    }

    public void setGravityForPopups(int gravity) {
        this.mJ.setGravity(gravity);
    }

    public void setUseNewPlayerNotificationsFirstParty(boolean newPlayerStyle) {
        try {
            ((er) bd()).setUseNewPlayerNotificationsFirstParty(newPlayerStyle);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void setViewForPopups(View gamesContentView) {
        this.mJ.mo988e(gamesContentView);
    }

    public void signOut(OnSignOutCompleteListener listener) {
        if (listener == null) {
            eq eqVar = null;
        } else {
            Object anVar = new an(this, listener);
        }
        try {
            ((er) bd()).mo909a(eqVar);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }

    public void unregisterInvitationListener() {
        try {
            ((er) bd()).mo968g(this.mM);
        } catch (RemoteException e) {
            ep.m455c("GamesClient", "service died");
        }
    }
}
