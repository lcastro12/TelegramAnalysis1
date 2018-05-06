package org.telegram.messenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class NotificationCenter {
    public static NotificationCenter Instance = new NotificationCenter();
    private boolean broadcasting = false;
    private final HashMap<Integer, Object> memCache = new HashMap();
    private final HashMap<String, Object> memCacheString = new HashMap();
    private final HashMap<Integer, ArrayList<Object>> observers = new HashMap();
    private final HashMap<Integer, Object> removeAfterBroadcast = new HashMap();

    public interface NotificationCenterDelegate {
        void didReceivedNotification(int i, Object... objArr);
    }

    public void addToMemCache(int id, Object object) {
        this.memCache.put(Integer.valueOf(id), object);
    }

    public void addToMemCache(String id, Object object) {
        this.memCacheString.put(id, object);
    }

    public Object getFromMemCache(int id) {
        Object obj = this.memCache.get(Integer.valueOf(id));
        if (obj != null) {
            this.memCache.remove(Integer.valueOf(id));
        }
        return obj;
    }

    public Object getFromMemCache(String id, Object defaultValue) {
        Object obj = this.memCacheString.get(id);
        if (obj == null) {
            return defaultValue;
        }
        this.memCacheString.remove(id);
        return obj;
    }

    public void postNotificationName(int id, Object... args) {
        synchronized (this.observers) {
            Iterator i$;
            this.broadcasting = true;
            ArrayList<Object> objects = (ArrayList) this.observers.get(Integer.valueOf(id));
            if (objects != null) {
                i$ = objects.iterator();
                while (i$.hasNext()) {
                    ((NotificationCenterDelegate) i$.next()).didReceivedNotification(id, args);
                }
            }
            this.broadcasting = false;
            if (!this.removeAfterBroadcast.isEmpty()) {
                for (Entry<Integer, Object> entry : this.removeAfterBroadcast.entrySet()) {
                    removeObserver(entry.getValue(), ((Integer) entry.getKey()).intValue());
                }
                this.removeAfterBroadcast.clear();
            }
        }
    }

    public void addObserver(Object observer, int id) {
        synchronized (this.observers) {
            ArrayList<Object> objects = (ArrayList) this.observers.get(Integer.valueOf(id));
            if (objects == null) {
                objects = new ArrayList();
                this.observers.put(Integer.valueOf(id), objects);
            }
            if (objects.contains(observer)) {
                return;
            }
            objects.add(observer);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void removeObserver(java.lang.Object r5, int r6) {
        /*
        r4 = this;
        r2 = r4.observers;
        monitor-enter(r2);
        r1 = r4.broadcasting;	 Catch:{ all -> 0x0034 }
        if (r1 == 0) goto L_0x0012;
    L_0x0007:
        r1 = r4.removeAfterBroadcast;	 Catch:{ all -> 0x0034 }
        r3 = java.lang.Integer.valueOf(r6);	 Catch:{ all -> 0x0034 }
        r1.put(r3, r5);	 Catch:{ all -> 0x0034 }
        monitor-exit(r2);	 Catch:{ all -> 0x0034 }
    L_0x0011:
        return;
    L_0x0012:
        r1 = r4.observers;	 Catch:{ all -> 0x0034 }
        r3 = java.lang.Integer.valueOf(r6);	 Catch:{ all -> 0x0034 }
        r0 = r1.get(r3);	 Catch:{ all -> 0x0034 }
        r0 = (java.util.ArrayList) r0;	 Catch:{ all -> 0x0034 }
        if (r0 == 0) goto L_0x0032;
    L_0x0020:
        r0.remove(r5);	 Catch:{ all -> 0x0034 }
        r1 = r0.size();	 Catch:{ all -> 0x0034 }
        if (r1 != 0) goto L_0x0032;
    L_0x0029:
        r1 = r4.observers;	 Catch:{ all -> 0x0034 }
        r3 = java.lang.Integer.valueOf(r6);	 Catch:{ all -> 0x0034 }
        r1.remove(r3);	 Catch:{ all -> 0x0034 }
    L_0x0032:
        monitor-exit(r2);	 Catch:{ all -> 0x0034 }
        goto L_0x0011;
    L_0x0034:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0034 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NotificationCenter.removeObserver(java.lang.Object, int):void");
    }
}
