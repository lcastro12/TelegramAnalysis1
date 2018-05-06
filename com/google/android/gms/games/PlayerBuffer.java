package com.google.android.gms.games;

import com.google.android.gms.common.data.C0646d;
import com.google.android.gms.common.data.DataBuffer;

public final class PlayerBuffer extends DataBuffer<Player> {
    public PlayerBuffer(C0646d dataHolder) {
        super(dataHolder);
    }

    public Player get(int position) {
        return new C0900d(this.jf, position);
    }
}
