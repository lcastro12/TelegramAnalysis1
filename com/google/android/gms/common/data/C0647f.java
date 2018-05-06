package com.google.android.gms.common.data;

import java.util.ArrayList;

public abstract class C0647f<T> extends DataBuffer<T> {
    private boolean jA = false;
    private ArrayList<Integer> jB;

    protected C0647f(C0646d c0646d) {
        super(c0646d);
    }

    private void aN() {
        synchronized (this) {
            if (!this.jA) {
                int count = this.jf.getCount();
                this.jB = new ArrayList();
                if (count > 0) {
                    this.jB.add(Integer.valueOf(0));
                    String primaryDataMarkerColumn = getPrimaryDataMarkerColumn();
                    String c = this.jf.m811c(primaryDataMarkerColumn, 0, this.jf.m816q(0));
                    int i = 1;
                    while (i < count) {
                        String c2 = this.jf.m811c(primaryDataMarkerColumn, i, this.jf.m816q(i));
                        if (c2.equals(c)) {
                            c2 = c;
                        } else {
                            this.jB.add(Integer.valueOf(i));
                        }
                        i++;
                        c = c2;
                    }
                }
                this.jA = true;
            }
        }
    }

    private int m817u(int i) {
        return (i < 0 || i == this.jB.size()) ? 0 : i == this.jB.size() + -1 ? this.jf.getCount() - ((Integer) this.jB.get(i)).intValue() : ((Integer) this.jB.get(i + 1)).intValue() - ((Integer) this.jB.get(i)).intValue();
    }

    protected abstract T mo1484a(int i, int i2);

    public final T get(int position) {
        aN();
        return mo1484a(m819t(position), m817u(position));
    }

    public int getCount() {
        aN();
        return this.jB.size();
    }

    protected abstract String getPrimaryDataMarkerColumn();

    int m819t(int i) {
        if (i >= 0 && i < this.jB.size()) {
            return ((Integer) this.jB.get(i)).intValue();
        }
        throw new IllegalArgumentException("Position " + i + " is out of bounds for this buffer");
    }
}
