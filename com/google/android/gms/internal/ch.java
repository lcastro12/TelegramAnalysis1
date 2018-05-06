package com.google.android.gms.internal;

import android.os.Process;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ch {
    private static final ThreadFactory hF = new C01582();
    private static final ThreadPoolExecutor hG = new ThreadPoolExecutor(0, 10, 65, TimeUnit.SECONDS, new SynchronousQueue(true), hF);

    static class C01582 implements ThreadFactory {
        private final AtomicInteger hI = new AtomicInteger(1);

        C01582() {
        }

        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "AdWorker #" + this.hI.getAndIncrement());
        }
    }

    public static void execute(final Runnable task) {
        try {
            hG.execute(new Runnable() {
                public void run() {
                    Process.setThreadPriority(10);
                    task.run();
                }
            });
        } catch (Throwable e) {
            cn.m293b("Too many background threads already running. Aborting task.", e);
        }
    }
}
