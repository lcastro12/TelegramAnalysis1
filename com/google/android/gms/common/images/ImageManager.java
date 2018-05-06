package com.google.android.gms.common.images;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.common.images.C0105a.C0104a;
import com.google.android.gms.internal.db;
import com.google.android.gms.internal.dq;
import com.google.android.gms.internal.ek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ImageManager {
    private static final Object jC = new Object();
    private static HashSet<Uri> jD = new HashSet();
    private static ImageManager jE;
    private static ImageManager jF;
    private final ExecutorService jG = Executors.newFixedThreadPool(4);
    private final C0648b jH;
    private final Map<C0105a, ImageReceiver> jI;
    private final Map<Uri, ImageReceiver> jJ;
    private final Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private final class ImageReceiver extends ResultReceiver {
        private final ArrayList<C0105a> jK;
        boolean jL = false;
        final /* synthetic */ ImageManager jM;
        private final Uri mUri;

        ImageReceiver(ImageManager imageManager, Uri uri) {
            this.jM = imageManager;
            super(new Handler(Looper.getMainLooper()));
            this.mUri = uri;
            this.jK = new ArrayList();
        }

        public void aR() {
            Intent intent = new Intent("com.google.android.gms.common.images.LOAD_IMAGE");
            intent.putExtra("com.google.android.gms.extras.uri", this.mUri);
            intent.putExtra("com.google.android.gms.extras.resultReceiver", this);
            intent.putExtra("com.google.android.gms.extras.priority", 3);
            this.jM.mContext.sendBroadcast(intent);
        }

        public void m49c(C0105a c0105a) {
            db.m341a(!this.jL, "Cannot add an ImageRequest when mHandlingRequests is true");
            db.m344w("ImageReceiver.addImageRequest() must be called in the main thread");
            this.jK.add(c0105a);
        }

        public void m50d(C0105a c0105a) {
            db.m341a(!this.jL, "Cannot remove an ImageRequest when mHandlingRequests is true");
            db.m344w("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.jK.remove(c0105a);
        }

        public void onReceiveResult(int resultCode, Bundle resultData) {
            this.jM.jG.execute(new C0100c(this.jM, this.mUri, (ParcelFileDescriptor) resultData.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }
    }

    public interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable);
    }

    private static final class C0099a {
        static int m51a(ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }

    private final class C0100c implements Runnable {
        final /* synthetic */ ImageManager jM;
        private final ParcelFileDescriptor jN;
        private final Uri mUri;

        public C0100c(ImageManager imageManager, Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.jM = imageManager;
            this.mUri = uri;
            this.jN = parcelFileDescriptor;
        }

        public void run() {
            db.m345x("LoadBitmapFromDiskRunnable can't be executed in the main thread");
            boolean z = false;
            Bitmap bitmap = null;
            if (this.jN != null) {
                try {
                    bitmap = BitmapFactory.decodeFileDescriptor(this.jN.getFileDescriptor());
                } catch (Throwable e) {
                    Log.e("ImageManager", "OOM while loading bitmap for uri: " + this.mUri, e);
                    z = true;
                }
                try {
                    this.jN.close();
                } catch (Throwable e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            this.jM.mHandler.post(new C0103f(this.jM, this.mUri, bitmap, z, countDownLatch));
            try {
                countDownLatch.await();
            } catch (InterruptedException e3) {
                Log.w("ImageManager", "Latch interrupted while posting " + this.mUri);
            }
        }
    }

    private final class C0101d implements Runnable {
        final /* synthetic */ ImageManager jM;
        private final C0105a jO;

        public C0101d(ImageManager imageManager, C0105a c0105a) {
            this.jM = imageManager;
            this.jO = c0105a;
        }

        public void run() {
            db.m344w("LoadImageRunnable must be executed on the main thread");
            this.jM.m59b(this.jO);
            C0104a c0104a = this.jO.jS;
            if (c0104a.uri == null) {
                this.jO.m73b(this.jM.mContext, true);
                return;
            }
            Bitmap a = this.jM.m55a(c0104a);
            if (a != null) {
                this.jO.m70a(this.jM.mContext, a, true);
                return;
            }
            this.jO.m74r(this.jM.mContext);
            ImageReceiver imageReceiver = (ImageReceiver) this.jM.jJ.get(c0104a.uri);
            if (imageReceiver == null) {
                imageReceiver = new ImageReceiver(this.jM, c0104a.uri);
                this.jM.jJ.put(c0104a.uri, imageReceiver);
            }
            imageReceiver.m49c(this.jO);
            if (this.jO.jV != 1) {
                this.jM.jI.put(this.jO, imageReceiver);
            }
            synchronized (ImageManager.jC) {
                if (!ImageManager.jD.contains(c0104a.uri)) {
                    ImageManager.jD.add(c0104a.uri);
                    imageReceiver.aR();
                }
            }
        }
    }

    private static final class C0102e implements ComponentCallbacks2 {
        private final C0648b jH;

        public C0102e(C0648b c0648b) {
            this.jH = c0648b;
        }

        public void onConfigurationChanged(Configuration newConfig) {
        }

        public void onLowMemory() {
            this.jH.evictAll();
        }

        public void onTrimMemory(int level) {
            if (level >= 60) {
                this.jH.evictAll();
            } else if (level >= 20) {
                this.jH.trimToSize(this.jH.size() / 2);
            }
        }
    }

    private final class C0103f implements Runnable {
        final /* synthetic */ ImageManager jM;
        private final Bitmap jP;
        private final CountDownLatch jQ;
        private boolean jR;
        private final Uri mUri;

        public C0103f(ImageManager imageManager, Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.jM = imageManager;
            this.mUri = uri;
            this.jP = bitmap;
            this.jR = z;
            this.jQ = countDownLatch;
        }

        private void m52a(ImageReceiver imageReceiver, boolean z) {
            imageReceiver.jL = true;
            ArrayList a = imageReceiver.jK;
            int size = a.size();
            for (int i = 0; i < size; i++) {
                C0105a c0105a = (C0105a) a.get(i);
                if (z) {
                    c0105a.m70a(this.jM.mContext, this.jP, false);
                } else {
                    c0105a.m73b(this.jM.mContext, false);
                }
                if (c0105a.jV != 1) {
                    this.jM.jI.remove(c0105a);
                }
            }
            imageReceiver.jL = false;
        }

        public void run() {
            db.m344w("OnBitmapLoadedRunnable must be executed in the main thread");
            boolean z = this.jP != null;
            if (this.jM.jH != null) {
                if (this.jR) {
                    this.jM.jH.evictAll();
                    System.gc();
                    this.jR = false;
                    this.jM.mHandler.post(this);
                    return;
                } else if (z) {
                    this.jM.jH.put(new C0104a(this.mUri), this.jP);
                }
            }
            ImageReceiver imageReceiver = (ImageReceiver) this.jM.jJ.remove(this.mUri);
            if (imageReceiver != null) {
                m52a(imageReceiver, z);
            }
            this.jQ.countDown();
            synchronized (ImageManager.jC) {
                ImageManager.jD.remove(this.mUri);
            }
        }
    }

    private static final class C0648b extends dq<C0104a, Bitmap> {
        public C0648b(Context context) {
            super(C0648b.m820q(context));
        }

        private static int m820q(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            int memoryClass = (((context.getApplicationInfo().flags & AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START) != 0 ? 1 : null) == null || !ek.bJ()) ? activityManager.getMemoryClass() : C0099a.m51a(activityManager);
            return (int) (((float) (memoryClass * AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START)) * 0.33f);
        }

        protected int m821a(C0104a c0104a, Bitmap bitmap) {
            return bitmap.getHeight() * bitmap.getRowBytes();
        }

        protected void m822a(boolean z, C0104a c0104a, Bitmap bitmap, Bitmap bitmap2) {
            super.entryRemoved(z, c0104a, bitmap, bitmap2);
        }

        protected /* synthetic */ void entryRemoved(boolean x0, Object x1, Object x2, Object x3) {
            m822a(x0, (C0104a) x1, (Bitmap) x2, (Bitmap) x3);
        }

        protected /* synthetic */ int sizeOf(Object x0, Object x1) {
            return m821a((C0104a) x0, (Bitmap) x1);
        }
    }

    private ImageManager(Context context, boolean withMemoryCache) {
        this.mContext = context.getApplicationContext();
        if (withMemoryCache) {
            this.jH = new C0648b(this.mContext);
            if (ek.bM()) {
                aO();
            }
        } else {
            this.jH = null;
        }
        this.jI = new HashMap();
        this.jJ = new HashMap();
    }

    private Bitmap m55a(C0104a c0104a) {
        return this.jH == null ? null : (Bitmap) this.jH.get(c0104a);
    }

    public static ImageManager m56a(Context context, boolean z) {
        if (z) {
            if (jF == null) {
                jF = new ImageManager(context, true);
            }
            return jF;
        }
        if (jE == null) {
            jE = new ImageManager(context, false);
        }
        return jE;
    }

    private void aO() {
        this.mContext.registerComponentCallbacks(new C0102e(this.jH));
    }

    private boolean m59b(C0105a c0105a) {
        db.m344w("ImageManager.cleanupHashMaps() must be called in the main thread");
        if (c0105a.jV == 1) {
            return true;
        }
        ImageReceiver imageReceiver = (ImageReceiver) this.jI.get(c0105a);
        if (imageReceiver == null) {
            return true;
        }
        if (imageReceiver.jL) {
            return false;
        }
        this.jI.remove(c0105a);
        imageReceiver.m50d(c0105a);
        return true;
    }

    public static ImageManager create(Context context) {
        return m56a(context, false);
    }

    public void m64a(C0105a c0105a) {
        db.m344w("ImageManager.loadImage() must be called in the main thread");
        boolean b = m59b(c0105a);
        Runnable c0101d = new C0101d(this, c0105a);
        if (b) {
            c0101d.run();
        } else {
            this.mHandler.post(c0101d);
        }
    }

    public void loadImage(ImageView imageView, int resId) {
        C0105a c0105a = new C0105a(resId);
        c0105a.m71a(imageView);
        m64a(c0105a);
    }

    public void loadImage(ImageView imageView, Uri uri) {
        C0105a c0105a = new C0105a(uri);
        c0105a.m71a(imageView);
        m64a(c0105a);
    }

    public void loadImage(ImageView imageView, Uri uri, int defaultResId) {
        C0105a c0105a = new C0105a(uri);
        c0105a.m75v(defaultResId);
        c0105a.m71a(imageView);
        m64a(c0105a);
    }

    public void loadImage(OnImageLoadedListener listener, Uri uri) {
        C0105a c0105a = new C0105a(uri);
        c0105a.m72a(listener);
        m64a(c0105a);
    }

    public void loadImage(OnImageLoadedListener listener, Uri uri, int defaultResId) {
        C0105a c0105a = new C0105a(uri);
        c0105a.m75v(defaultResId);
        c0105a.m72a(listener);
        m64a(c0105a);
    }
}
