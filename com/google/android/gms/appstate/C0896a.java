package com.google.android.gms.appstate;

import com.google.android.gms.internal.dl;

public final class C0896a implements AppState {
    private final int iu;
    private final String iv;
    private final byte[] iw;
    private final boolean ix;
    private final String iy;
    private final byte[] iz;

    public C0896a(AppState appState) {
        this.iu = appState.getKey();
        this.iv = appState.getLocalVersion();
        this.iw = appState.getLocalData();
        this.ix = appState.hasConflict();
        this.iy = appState.getConflictVersion();
        this.iz = appState.getConflictData();
    }

    static int m1314a(AppState appState) {
        return dl.hashCode(Integer.valueOf(appState.getKey()), appState.getLocalVersion(), appState.getLocalData(), Boolean.valueOf(appState.hasConflict()), appState.getConflictVersion(), appState.getConflictData());
    }

    static boolean m1315a(AppState appState, Object obj) {
        if (!(obj instanceof AppState)) {
            return false;
        }
        if (appState == obj) {
            return true;
        }
        AppState appState2 = (AppState) obj;
        return dl.equal(Integer.valueOf(appState2.getKey()), Integer.valueOf(appState.getKey())) && dl.equal(appState2.getLocalVersion(), appState.getLocalVersion()) && dl.equal(appState2.getLocalData(), appState.getLocalData()) && dl.equal(Boolean.valueOf(appState2.hasConflict()), Boolean.valueOf(appState.hasConflict())) && dl.equal(appState2.getConflictVersion(), appState.getConflictVersion()) && dl.equal(appState2.getConflictData(), appState.getConflictData());
    }

    static String m1316b(AppState appState) {
        return dl.m387d(appState).m386a("Key", Integer.valueOf(appState.getKey())).m386a("LocalVersion", appState.getLocalVersion()).m386a("LocalData", appState.getLocalData()).m386a("HasConflict", Boolean.valueOf(appState.hasConflict())).m386a("ConflictVersion", appState.getConflictVersion()).m386a("ConflictData", appState.getConflictData()).toString();
    }

    public AppState aE() {
        return this;
    }

    public boolean equals(Object obj) {
        return C0896a.m1315a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return aE();
    }

    public byte[] getConflictData() {
        return this.iz;
    }

    public String getConflictVersion() {
        return this.iy;
    }

    public int getKey() {
        return this.iu;
    }

    public byte[] getLocalData() {
        return this.iw;
    }

    public String getLocalVersion() {
        return this.iv;
    }

    public boolean hasConflict() {
        return this.ix;
    }

    public int hashCode() {
        return C0896a.m1314a(this);
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return C0896a.m1316b(this);
    }
}
