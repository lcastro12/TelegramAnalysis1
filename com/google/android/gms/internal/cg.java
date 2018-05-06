package com.google.android.gms.internal;

public abstract class cg {
    private final Runnable el = new C01561(this);
    private volatile Thread hD;

    class C01561 implements Runnable {
        final /* synthetic */ cg hE;

        C01561(cg cgVar) {
            this.hE = cgVar;
        }

        public final void run() {
            this.hE.hD = Thread.currentThread();
            this.hE.ac();
        }
    }

    public abstract void ac();

    public final void cancel() {
        onStop();
        if (this.hD != null) {
            this.hD.interrupt();
        }
    }

    public abstract void onStop();

    public final void start() {
        ch.execute(this.el);
    }
}
