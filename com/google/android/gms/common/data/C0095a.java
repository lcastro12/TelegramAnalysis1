package com.google.android.gms.common.data;

import com.google.android.gms.internal.dm;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class C0095a<T> implements Iterator<T> {
    private final DataBuffer<T> jg;
    private int jh = -1;

    public C0095a(DataBuffer<T> dataBuffer) {
        this.jg = (DataBuffer) dm.m392e(dataBuffer);
    }

    public boolean hasNext() {
        return this.jh < this.jg.getCount() + -1;
    }

    public T next() {
        if (hasNext()) {
            DataBuffer dataBuffer = this.jg;
            int i = this.jh + 1;
            this.jh = i;
            return dataBuffer.get(i);
        }
        throw new NoSuchElementException("Cannot advance the iterator beyond " + this.jh);
    }

    public void remove() {
        throw new UnsupportedOperationException("Cannot remove elements from a DataBufferIterator");
    }
}
