package com.google.android.gms.common.data;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.dm;

public abstract class C0096b {
    protected final C0646d jf;
    protected final int ji;
    private final int jj;

    public C0096b(C0646d c0646d, int i) {
        this.jf = (C0646d) dm.m392e(c0646d);
        boolean z = i >= 0 && i < c0646d.getCount();
        dm.m393k(z);
        this.ji = i;
        this.jj = c0646d.m816q(this.ji);
    }

    protected void m40a(String str, CharArrayBuffer charArrayBuffer) {
        this.jf.m808a(str, this.ji, this.jj, charArrayBuffer);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof C0096b)) {
            return false;
        }
        C0096b c0096b = (C0096b) obj;
        return dl.equal(Integer.valueOf(c0096b.ji), Integer.valueOf(this.ji)) && dl.equal(Integer.valueOf(c0096b.jj), Integer.valueOf(this.jj)) && c0096b.jf == this.jf;
    }

    protected boolean getBoolean(String column) {
        return this.jf.m812d(column, this.ji, this.jj);
    }

    protected byte[] getByteArray(String column) {
        return this.jf.m813e(column, this.ji, this.jj);
    }

    protected int getInteger(String column) {
        return this.jf.m809b(column, this.ji, this.jj);
    }

    protected long getLong(String column) {
        return this.jf.m807a(column, this.ji, this.jj);
    }

    protected String getString(String column) {
        return this.jf.m811c(column, this.ji, this.jj);
    }

    public int hashCode() {
        return dl.hashCode(Integer.valueOf(this.ji), Integer.valueOf(this.jj), this.jf);
    }

    public boolean isDataValid() {
        return !this.jf.isClosed();
    }

    protected Uri m41u(String str) {
        return this.jf.m814f(str, this.ji, this.jj);
    }

    protected boolean m42v(String str) {
        return this.jf.m815g(str, this.ji, this.jj);
    }
}
