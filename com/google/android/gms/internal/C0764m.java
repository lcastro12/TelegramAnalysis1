package com.google.android.gms.internal;

import java.io.IOException;

class C0764m implements C0196k {
    private gl dD;
    private byte[] dE;
    private final int dF;

    public C0764m(int i) {
        this.dF = i;
        reset();
    }

    public void mo1055b(int i, long j) throws IOException {
        this.dD.m653b(i, j);
    }

    public void mo1056b(int i, String str) throws IOException {
        this.dD.m654b(i, str);
    }

    public byte[] mo1057h() throws IOException {
        int ec = this.dD.ec();
        if (ec < 0) {
            throw new IOException();
        } else if (ec == 0) {
            return this.dE;
        } else {
            Object obj = new byte[(this.dE.length - ec)];
            System.arraycopy(this.dE, 0, obj, 0, obj.length);
            return obj;
        }
    }

    public void reset() {
        this.dE = new byte[this.dF];
        this.dD = gl.m651g(this.dE);
    }
}
