package com.google.android.gms.common.data;

import java.util.Iterator;

public abstract class DataBuffer<T> implements Iterable<T> {
    protected final C0646d jf;

    protected DataBuffer(C0646d dataHolder) {
        this.jf = dataHolder;
        if (this.jf != null) {
            this.jf.m810b(this);
        }
    }

    public void close() {
        this.jf.close();
    }

    public int describeContents() {
        return 0;
    }

    public abstract T get(int i);

    public int getCount() {
        return this.jf.getCount();
    }

    public boolean isClosed() {
        return this.jf.isClosed();
    }

    public Iterator<T> iterator() {
        return new C0095a(this);
    }
}
