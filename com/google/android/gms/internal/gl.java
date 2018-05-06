package com.google.android.gms.internal;

import java.io.IOException;

public final class gl {
    private final byte[] buffer;
    private int position;
    private final int ux;

    public static class C0192a extends IOException {
        C0192a(int i, int i2) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space (pos " + i + " limit " + i2 + ").");
        }
    }

    private gl(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.position = i;
        this.ux = i + i2;
    }

    public static gl m650a(byte[] bArr, int i, int i2) {
        return new gl(bArr, i, i2);
    }

    public static gl m651g(byte[] bArr) {
        return m650a(bArr, 0, bArr.length);
    }

    public void aD(int i) throws IOException {
        m652b((byte) i);
    }

    public void aE(int i) throws IOException {
        while ((i & -128) != 0) {
            aD((i & 127) | 128);
            i >>>= 7;
        }
        aD(i);
    }

    public void ab(String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        aE(bytes.length);
        m657h(bytes);
    }

    public void m652b(byte b) throws IOException {
        if (this.position == this.ux) {
            throw new C0192a(this.position, this.ux);
        }
        byte[] bArr = this.buffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = b;
    }

    public void m653b(int i, long j) throws IOException {
        m656d(i, 0);
        m658i(j);
    }

    public void m654b(int i, String str) throws IOException {
        m656d(i, 2);
        ab(str);
    }

    public void m655b(byte[] bArr, int i, int i2) throws IOException {
        if (this.ux - this.position >= i2) {
            System.arraycopy(bArr, i, this.buffer, this.position, i2);
            this.position += i2;
            return;
        }
        throw new C0192a(this.position, this.ux);
    }

    public void m656d(int i, int i2) throws IOException {
        aE(gm.m660e(i, i2));
    }

    public int ec() {
        return this.ux - this.position;
    }

    public void m657h(byte[] bArr) throws IOException {
        m655b(bArr, 0, bArr.length);
    }

    public void m658i(long j) throws IOException {
        m659j(j);
    }

    public void m659j(long j) throws IOException {
        while ((-128 & j) != 0) {
            aD((((int) j) & 127) | 128);
            j >>>= 7;
        }
        aD((int) j);
    }
}
