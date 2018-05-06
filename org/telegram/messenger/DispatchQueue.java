package org.telegram.messenger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class DispatchQueue extends Thread {
    public Handler handler;
    private final Object handlerSyncObject = new Object();

    public DispatchQueue(String threadName) {
        setName(threadName);
        start();
    }

    private void sendMessage(Message msg, int delay) {
        if (this.handler == null) {
            try {
                synchronized (this.handlerSyncObject) {
                    this.handlerSyncObject.wait();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (this.handler == null) {
            return;
        }
        if (delay <= 0) {
            this.handler.sendMessage(msg);
        } else {
            this.handler.sendMessageDelayed(msg, (long) delay);
        }
    }

    public void postRunnable(Runnable runnable) {
        postRunnable(runnable, 0);
    }

    public void postRunnable(Runnable runnable, int delay) {
        if (this.handler == null) {
            try {
                synchronized (this.handlerSyncObject) {
                    this.handlerSyncObject.wait();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (this.handler == null) {
            return;
        }
        if (delay <= 0) {
            this.handler.post(runnable);
        } else {
            this.handler.postDelayed(runnable, (long) delay);
        }
    }

    public void run() {
        Looper.prepare();
        this.handler = new Handler();
        synchronized (this.handlerSyncObject) {
            this.handlerSyncObject.notify();
        }
        Looper.loop();
    }
}
