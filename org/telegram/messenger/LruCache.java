package org.telegram.messenger;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LruCache {
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<String, Bitmap> map;
    private final LinkedHashMap<String, ArrayList<String>> mapFilters;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap(0, 0.75f, true);
        this.mapFilters = new LinkedHashMap();
    }

    public final Bitmap get(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            Bitmap mapValue = (Bitmap) this.map.get(key);
            if (mapValue != null) {
                this.hitCount++;
                return mapValue;
            }
            this.missCount++;
            return null;
        }
    }

    public ArrayList<String> getFilterKeys(String key) {
        ArrayList<String> arr = (ArrayList) this.mapFilters.get(key);
        if (arr != null) {
            return new ArrayList(arr);
        }
        return null;
    }

    public Bitmap put(String key, Bitmap value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        Bitmap previous;
        synchronized (this) {
            this.putCount++;
            this.size += safeSizeOf(key, value);
            previous = (Bitmap) this.map.put(key, value);
            if (previous != null) {
                this.size -= safeSizeOf(key, previous);
            }
        }
        String[] args = key.split("@");
        if (args.length > 1) {
            ArrayList<String> arr = (ArrayList) this.mapFilters.get(args[0]);
            if (arr == null) {
                arr = new ArrayList();
                this.mapFilters.put(args[0], arr);
            }
            arr.add(args[1]);
        }
        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }
        trimToSize(this.maxSize);
        return previous;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void trimToSize(int r9) {
        /*
        r8 = this;
        r7 = 1;
    L_0x0001:
        monitor-enter(r8);
        r5 = r8.size;	 Catch:{ all -> 0x0033 }
        if (r5 < 0) goto L_0x0012;
    L_0x0006:
        r5 = r8.map;	 Catch:{ all -> 0x0033 }
        r5 = r5.isEmpty();	 Catch:{ all -> 0x0033 }
        if (r5 == 0) goto L_0x0036;
    L_0x000e:
        r5 = r8.size;	 Catch:{ all -> 0x0033 }
        if (r5 == 0) goto L_0x0036;
    L_0x0012:
        r5 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0033 }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0033 }
        r6.<init>();	 Catch:{ all -> 0x0033 }
        r7 = r8.getClass();	 Catch:{ all -> 0x0033 }
        r7 = r7.getName();	 Catch:{ all -> 0x0033 }
        r6 = r6.append(r7);	 Catch:{ all -> 0x0033 }
        r7 = ".sizeOf() is reporting inconsistent results!";
        r6 = r6.append(r7);	 Catch:{ all -> 0x0033 }
        r6 = r6.toString();	 Catch:{ all -> 0x0033 }
        r5.<init>(r6);	 Catch:{ all -> 0x0033 }
        throw r5;	 Catch:{ all -> 0x0033 }
    L_0x0033:
        r5 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x0033 }
        throw r5;
    L_0x0036:
        r5 = r8.size;	 Catch:{ all -> 0x0033 }
        if (r5 <= r9) goto L_0x0042;
    L_0x003a:
        r5 = r8.map;	 Catch:{ all -> 0x0033 }
        r5 = r5.isEmpty();	 Catch:{ all -> 0x0033 }
        if (r5 == 0) goto L_0x0044;
    L_0x0042:
        monitor-exit(r8);	 Catch:{ all -> 0x0033 }
        return;
    L_0x0044:
        r5 = r8.map;	 Catch:{ all -> 0x0033 }
        r5 = r5.entrySet();	 Catch:{ all -> 0x0033 }
        r5 = r5.iterator();	 Catch:{ all -> 0x0033 }
        r3 = r5.next();	 Catch:{ all -> 0x0033 }
        r3 = (java.util.Map.Entry) r3;	 Catch:{ all -> 0x0033 }
        r2 = r3.getKey();	 Catch:{ all -> 0x0033 }
        r2 = (java.lang.String) r2;	 Catch:{ all -> 0x0033 }
        r4 = r3.getValue();	 Catch:{ all -> 0x0033 }
        r4 = (android.graphics.Bitmap) r4;	 Catch:{ all -> 0x0033 }
        r5 = r8.map;	 Catch:{ all -> 0x0033 }
        r5.remove(r2);	 Catch:{ all -> 0x0033 }
        r5 = r8.size;	 Catch:{ all -> 0x0033 }
        r6 = r8.safeSizeOf(r2, r4);	 Catch:{ all -> 0x0033 }
        r5 = r5 - r6;
        r8.size = r5;	 Catch:{ all -> 0x0033 }
        r5 = r8.evictionCount;	 Catch:{ all -> 0x0033 }
        r5 = r5 + 1;
        r8.evictionCount = r5;	 Catch:{ all -> 0x0033 }
        monitor-exit(r8);	 Catch:{ all -> 0x0033 }
        r5 = "@";
        r0 = r2.split(r5);
        r5 = r0.length;
        if (r5 <= r7) goto L_0x009b;
    L_0x007e:
        r5 = r8.mapFilters;
        r6 = 0;
        r6 = r0[r6];
        r1 = r5.get(r6);
        r1 = (java.util.ArrayList) r1;
        if (r1 == 0) goto L_0x009b;
    L_0x008b:
        r1.remove(r2);
        r5 = r1.isEmpty();
        if (r5 == 0) goto L_0x009b;
    L_0x0094:
        r5 = r8.mapFilters;
        r6 = r0[r7];
        r5.remove(r6);
    L_0x009b:
        r5 = 0;
        r8.entryRemoved(r7, r2, r4, r5);
        goto L_0x0001;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.LruCache.trimToSize(int):void");
    }

    public final Bitmap remove(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        Bitmap previous;
        synchronized (this) {
            previous = (Bitmap) this.map.remove(key);
            if (previous != null) {
                this.size -= safeSizeOf(key, previous);
            }
        }
        if (previous != null) {
            String[] args = key.split("@");
            if (args.length > 1) {
                ArrayList<String> arr = (ArrayList) this.mapFilters.get(args[0]);
                if (arr != null) {
                    arr.remove(key);
                    if (arr.isEmpty()) {
                        this.mapFilters.remove(args[1]);
                    }
                }
            }
            entryRemoved(false, key, previous, null);
        }
        return previous;
    }

    public boolean contains(String key) {
        return this.map.containsKey(key);
    }

    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
    }

    private int safeSizeOf(String key, Bitmap value) {
        int result = sizeOf(key, value);
        if (result >= 0) {
            return result;
        }
        throw new IllegalStateException("Negative size: " + key + "=" + value);
    }

    protected int sizeOf(String key, Bitmap value) {
        return 1;
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final synchronized int size() {
        return this.size;
    }

    public final synchronized int maxSize() {
        return this.maxSize;
    }

    public final synchronized int hitCount() {
        return this.hitCount;
    }

    public final synchronized int missCount() {
        return this.missCount;
    }

    public final synchronized int putCount() {
        return this.putCount;
    }

    public final synchronized int evictionCount() {
        return this.evictionCount;
    }

    public final synchronized String toString() {
        String format;
        int hitPercent = 0;
        synchronized (this) {
            int accesses = this.hitCount + this.missCount;
            if (accesses != 0) {
                hitPercent = (this.hitCount * 100) / accesses;
            }
            format = String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.maxSize), Integer.valueOf(this.hitCount), Integer.valueOf(this.missCount), Integer.valueOf(hitPercent)});
        }
        return format;
    }
}
