package org.telegram.messenger;

import android.content.Intent;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.support.JobIntentService;

public class KeepAliveJob extends JobIntentService {
    private static volatile CountDownLatch countDownLatch;
    private static Runnable finishJobByTimeoutRunnable = new C02533();
    private static volatile boolean startingJob;
    private static final Object sync = new Object();

    static class C02511 implements Runnable {
        C02511() {
        }

        public void run() {
            if (!KeepAliveJob.startingJob && KeepAliveJob.countDownLatch == null) {
                try {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.m0d("starting keep-alive job");
                    }
                    synchronized (KeepAliveJob.sync) {
                        KeepAliveJob.startingJob = true;
                    }
                    JobIntentService.enqueueWork(ApplicationLoader.applicationContext, KeepAliveJob.class, 1000, new Intent());
                } catch (Exception e) {
                }
            }
        }
    }

    static class C02522 implements Runnable {
        C02522() {
        }

        public void run() {
            synchronized (KeepAliveJob.sync) {
                if (KeepAliveJob.countDownLatch != null) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.m0d("finish keep-alive job");
                    }
                    KeepAliveJob.countDownLatch.countDown();
                }
                if (KeepAliveJob.startingJob) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.m0d("finish queued keep-alive job");
                    }
                    KeepAliveJob.startingJob = false;
                }
            }
        }
    }

    static class C02533 implements Runnable {
        C02533() {
        }

        public void run() {
            KeepAliveJob.finishJob();
        }
    }

    public static void startJob() {
        Utilities.globalQueue.postRunnable(new C02511());
    }

    public static void finishJob() {
        Utilities.globalQueue.postRunnable(new C02522());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onHandleWork(android.content.Intent r5) {
        /*
        r4 = this;
        r1 = sync;
        monitor-enter(r1);
        r0 = startingJob;	 Catch:{ all -> 0x0044 }
        if (r0 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r1);	 Catch:{ all -> 0x0044 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = new java.util.concurrent.CountDownLatch;	 Catch:{ all -> 0x0044 }
        r2 = 1;
        r0.<init>(r2);	 Catch:{ all -> 0x0044 }
        countDownLatch = r0;	 Catch:{ all -> 0x0044 }
        monitor-exit(r1);	 Catch:{ all -> 0x0044 }
        r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r0 == 0) goto L_0x001c;
    L_0x0016:
        r0 = "started keep-alive job";
        org.telegram.messenger.FileLog.m0d(r0);
    L_0x001c:
        r0 = org.telegram.messenger.Utilities.globalQueue;
        r1 = finishJobByTimeoutRunnable;
        r2 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
        r0.postRunnable(r1, r2);
        r0 = countDownLatch;	 Catch:{ Throwable -> 0x004a }
        r0.await();	 Catch:{ Throwable -> 0x004a }
    L_0x002b:
        r0 = org.telegram.messenger.Utilities.globalQueue;
        r1 = finishJobByTimeoutRunnable;
        r0.cancelRunnable(r1);
        r1 = sync;
        monitor-enter(r1);
        r0 = 0;
        countDownLatch = r0;	 Catch:{ all -> 0x0047 }
        monitor-exit(r1);	 Catch:{ all -> 0x0047 }
        r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r0 == 0) goto L_0x0008;
    L_0x003d:
        r0 = "ended keep-alive job";
        org.telegram.messenger.FileLog.m0d(r0);
        goto L_0x0008;
    L_0x0044:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0044 }
        throw r0;
    L_0x0047:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0047 }
        throw r0;
    L_0x004a:
        r0 = move-exception;
        goto L_0x002b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.KeepAliveJob.onHandleWork(android.content.Intent):void");
    }
}
