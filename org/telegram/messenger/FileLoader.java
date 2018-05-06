package org.telegram.messenger;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build.VERSION;
import android.support.v4.widget.ExploreByTouchHelper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import org.telegram.TL.TLRPC.Document;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.InputEncryptedFile;
import org.telegram.TL.TLRPC.InputFile;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.TL.TLRPC.TL_fileEncryptedLocation;
import org.telegram.TL.TLRPC.TL_fileLocation;
import org.telegram.TL.TLRPC.TL_photoCachedSize;
import org.telegram.TL.TLRPC.TL_photoSize;
import org.telegram.TL.TLRPC.Video;
import org.telegram.messenger.FileLoadOperation.FileLoadOperationDelegate;
import org.telegram.messenger.FileUploadOperation.FileUploadOperationDelegate;
import org.telegram.objects.MessageObject;
import org.telegram.ui.ApplicationLoader;
import org.telegram.ui.Views.BackupImageView;
import org.telegram.ui.Views.ImageReceiver;

public class FileLoader {
    public static final int FileDidFailUpload = 10001;
    public static final int FileDidFailedLoad = 10005;
    public static final int FileDidLoaded = 10004;
    public static final int FileDidUpload = 10000;
    public static final int FileLoadProgressChanged = 10003;
    public static final int FileUploadProgressChanged = 10002;
    public static FileLoader Instance = new FileLoader();
    public static long lastCacheOutTime = 0;
    private HashMap<String, Integer> BitmapUseCounts = new HashMap();
    private int currentLoadOperationsCount = 0;
    private int currentUploadOperationsCount = 0;
    public ConcurrentHashMap<String, Float> fileProgresses = new ConcurrentHashMap();
    private String ignoreRemoval = null;
    private ConcurrentHashMap<String, CacheImage> imageLoading;
    private HashMap<Integer, CacheImage> imageLoadingByKeys;
    int lastImageNum;
    private long lastProgressUpdateTime = 0;
    private ConcurrentHashMap<String, FileLoadOperation> loadOperationPaths;
    private Queue<FileLoadOperation> loadOperationQueue;
    private final int maxConcurentLoadingOpertaionsCount = 2;
    public LruCache memCache;
    private Queue<FileLoadOperation> operationsQueue;
    private Queue<FileLoadOperation> runningOperation;
    public VMRuntimeHack runtimeHack = null;
    private ConcurrentHashMap<String, FileUploadOperation> uploadOperationPaths;
    private Queue<FileUploadOperation> uploadOperationQueue;

    private class CacheImage {
        public ArrayList<Object> imageViewArray;
        public String key;
        public FileLoadOperation loadOperation;

        class C03421 implements Runnable {
            C03421() {
            }

            public void run() {
                CacheImage.this.imageViewArray.clear();
                CacheImage.this.loadOperation = null;
            }
        }

        private CacheImage() {
        }

        public void addImageView(Object imageView) {
            if (this.imageViewArray == null) {
                this.imageViewArray = new ArrayList();
            }
            boolean exist = false;
            Iterator i$ = this.imageViewArray.iterator();
            while (i$.hasNext()) {
                if (i$.next() == imageView) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                this.imageViewArray.add(imageView);
            }
        }

        public void removeImageView(Object imageView) {
            if (this.imageViewArray != null) {
                int a = 0;
                while (a < this.imageViewArray.size()) {
                    Object obj = this.imageViewArray.get(a);
                    if (obj == null || obj == imageView) {
                        this.imageViewArray.remove(a);
                        a--;
                    }
                    a++;
                }
            }
        }

        public void callAndClear(Bitmap image) {
            if (image != null) {
                Iterator i$ = this.imageViewArray.iterator();
                while (i$.hasNext()) {
                    Object imgView = i$.next();
                    if (imgView instanceof BackupImageView) {
                        ((BackupImageView) imgView).setImageBitmap(image, this.key);
                    } else if (imgView instanceof ImageReceiver) {
                        ((ImageReceiver) imgView).setImageBitmap(image, this.key);
                    }
                }
            }
            Utilities.imageLoadQueue.postRunnable(new C03421());
        }

        public void cancelAndClear() {
            if (this.loadOperation != null) {
                this.loadOperation.cancel();
                this.loadOperation = null;
            }
            this.imageViewArray.clear();
        }
    }

    public class VMRuntimeHack {
        private Object runtime = null;
        private Method trackAllocation = null;
        private Method trackFree = null;

        public boolean trackAlloc(long size) {
            if (this.runtime == null) {
                return false;
            }
            try {
                Object res = this.trackAllocation.invoke(this.runtime, new Object[]{Long.valueOf(size)});
                if (res instanceof Boolean) {
                    return ((Boolean) res).booleanValue();
                }
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            } catch (IllegalAccessException e2) {
                return false;
            } catch (InvocationTargetException e3) {
                return false;
            }
        }

        public boolean trackFree(long size) {
            if (this.runtime == null) {
                return false;
            }
            try {
                Object res = this.trackFree.invoke(this.runtime, new Object[]{Long.valueOf(size)});
                if (res instanceof Boolean) {
                    return ((Boolean) res).booleanValue();
                }
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            } catch (IllegalAccessException e2) {
                return false;
            } catch (InvocationTargetException e3) {
                return false;
            }
        }

        public VMRuntimeHack() {
            boolean success = false;
            try {
                Class cl = Class.forName("dalvik.system.VMRuntime");
                this.runtime = cl.getMethod("getRuntime", new Class[0]).invoke(null, new Object[0]);
                this.trackAllocation = cl.getMethod("trackExternalAllocation", new Class[]{Long.TYPE});
                this.trackFree = cl.getMethod("trackExternalFree", new Class[]{Long.TYPE});
                success = true;
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
            } catch (Exception e2) {
                FileLog.m799e("tmessages", e2);
            } catch (Exception e22) {
                FileLog.m799e("tmessages", e22);
            } catch (Exception e222) {
                FileLog.m799e("tmessages", e222);
            } catch (Exception e2222) {
                FileLog.m799e("tmessages", e2222);
            } catch (Exception e22222) {
                FileLog.m799e("tmessages", e22222);
            }
            if (!success) {
                this.runtime = null;
                this.trackAllocation = null;
                this.trackFree = null;
            }
        }
    }

    public void incrementUseCount(String key) {
        Integer count = (Integer) this.BitmapUseCounts.get(key);
        if (count == null) {
            this.BitmapUseCounts.put(key, Integer.valueOf(1));
        } else {
            this.BitmapUseCounts.put(key, Integer.valueOf(count.intValue() + 1));
        }
    }

    public boolean decrementUseCount(String key) {
        Integer count = (Integer) this.BitmapUseCounts.get(key);
        if (count == null) {
            return true;
        }
        if (count.intValue() == 1) {
            this.BitmapUseCounts.remove(key);
            return true;
        }
        this.BitmapUseCounts.put(key, Integer.valueOf(count.intValue() - 1));
        return false;
    }

    public void removeImage(String key) {
        this.BitmapUseCounts.remove(key);
        this.memCache.remove(key);
    }

    public FileLoader() {
        int cacheSize = (Math.min(15, ((ActivityManager) ApplicationLoader.applicationContext.getSystemService("activity")).getMemoryClass() / 7) * 1024) * 1024;
        if (VERSION.SDK_INT < 11) {
            this.runtimeHack = new VMRuntimeHack();
            cacheSize = 3145728;
        }
        this.memCache = new LruCache(cacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                if (VERSION.SDK_INT < 12) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
                return bitmap.getByteCount();
            }

            protected void entryRemoved(boolean evicted, String key, Bitmap oldBitmap, Bitmap newBitmap) {
                if (FileLoader.this.ignoreRemoval == null || key == null || !FileLoader.this.ignoreRemoval.equals(key)) {
                    Integer count = (Integer) FileLoader.this.BitmapUseCounts.get(key);
                    if (count == null || count.intValue() == 0) {
                        if (FileLoader.this.runtimeHack != null) {
                            FileLoader.this.runtimeHack.trackAlloc((long) (oldBitmap.getRowBytes() * oldBitmap.getHeight()));
                        }
                        if (VERSION.SDK_INT < 11 && !oldBitmap.isRecycled()) {
                            oldBitmap.recycle();
                        }
                    }
                }
            }
        };
        this.imageLoading = new ConcurrentHashMap();
        this.imageLoadingByKeys = new HashMap();
        this.operationsQueue = new LinkedList();
        this.runningOperation = new LinkedList();
        this.uploadOperationQueue = new LinkedList();
        this.uploadOperationPaths = new ConcurrentHashMap();
        this.loadOperationPaths = new ConcurrentHashMap();
        this.loadOperationQueue = new LinkedList();
    }

    public void cancelUploadFile(final String location) {
        Utilities.fileUploadQueue.postRunnable(new Runnable() {
            public void run() {
                FileUploadOperation operation = (FileUploadOperation) FileLoader.this.uploadOperationPaths.get(location);
                if (operation != null) {
                    FileLoader.this.uploadOperationQueue.remove(operation);
                    operation.cancel();
                }
            }
        });
    }

    public boolean isInCache(String key) {
        return this.memCache.get(key) != null;
    }

    public void uploadFile(final String location, final byte[] key, final byte[] iv) {
        Utilities.fileUploadQueue.postRunnable(new Runnable() {

            class C08471 implements FileUploadOperationDelegate {

                class C03201 implements Runnable {
                    C03201() {
                    }

                    public void run() {
                        FileLoader.this.uploadOperationPaths.remove(location);
                        FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount - 1;
                        if (FileLoader.this.currentUploadOperationsCount < 2) {
                            FileUploadOperation operation = (FileUploadOperation) FileLoader.this.uploadOperationQueue.poll();
                            if (operation != null) {
                                FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount + 1;
                                operation.start();
                            }
                        }
                    }
                }

                class C03212 implements Runnable {
                    C03212() {
                    }

                    public void run() {
                        FileLoader.this.uploadOperationPaths.remove(location);
                        FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount - 1;
                        if (FileLoader.this.currentUploadOperationsCount < 2) {
                            FileUploadOperation operation = (FileUploadOperation) FileLoader.this.uploadOperationQueue.poll();
                            if (operation != null) {
                                FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount + 1;
                                operation.start();
                            }
                        }
                    }
                }

                C08471() {
                }

                public void didFinishUploadingFile(FileUploadOperation operation, InputFile inputFile, InputEncryptedFile inputEncryptedFile) {
                    NotificationCenter.Instance.postNotificationName(FileLoader.FileDidUpload, location, inputFile, inputEncryptedFile);
                    FileLoader.this.fileProgresses.remove(location);
                    Utilities.fileUploadQueue.postRunnable(new C03201());
                }

                public void didFailedUploadingFile(FileUploadOperation operation) {
                    FileLoader.this.fileProgresses.remove(location);
                    if (operation.state != 2) {
                        NotificationCenter.Instance.postNotificationName(10001, location);
                    }
                    Utilities.fileUploadQueue.postRunnable(new C03212());
                }

                public void didChangedUploadProgress(FileUploadOperation operation, final float progress) {
                    if (operation.state != 2) {
                        FileLoader.this.fileProgresses.put(location, Float.valueOf(progress));
                    }
                    long currentTime = System.currentTimeMillis();
                    if (FileLoader.this.lastProgressUpdateTime == 0 || FileLoader.this.lastProgressUpdateTime < currentTime - 500) {
                        FileLoader.this.lastProgressUpdateTime = currentTime;
                        Utilities.RunOnUIThread(new Runnable() {
                            public void run() {
                                NotificationCenter.Instance.postNotificationName(10002, location, Float.valueOf(progress));
                            }
                        });
                    }
                }
            }

            public void run() {
                FileUploadOperation operation = new FileUploadOperation(location, key, iv);
                FileLoader.this.uploadOperationPaths.put(location, operation);
                operation.delegate = new C08471();
                if (FileLoader.this.currentUploadOperationsCount < 2) {
                    FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount + 1;
                    operation.start();
                    return;
                }
                FileLoader.this.uploadOperationQueue.add(operation);
            }
        });
    }

    public void cancelLoadFile(final Video video, final PhotoSize photo, final Document document) {
        if (video != null || photo != null || document != null) {
            Utilities.fileUploadQueue.postRunnable(new Runnable() {
                public void run() {
                    String fileName = null;
                    if (video != null) {
                        fileName = MessageObject.getAttachFileName(video);
                    } else if (photo != null) {
                        fileName = MessageObject.getAttachFileName(photo);
                    } else if (document != null) {
                        fileName = MessageObject.getAttachFileName(document);
                    }
                    if (fileName != null) {
                        FileLoadOperation operation = (FileLoadOperation) FileLoader.this.loadOperationPaths.get(fileName);
                        if (operation != null) {
                            FileLoader.this.loadOperationQueue.remove(operation);
                            operation.cancel();
                        }
                    }
                }
            });
        }
    }

    public boolean isLoadingFile(String fileName) {
        return this.loadOperationPaths.containsKey(fileName);
    }

    public void loadFile(final Video video, final PhotoSize photo, final Document document) {
        Utilities.fileUploadQueue.postRunnable(new Runnable() {
            public void run() {
                String fileName = null;
                if (video != null) {
                    fileName = MessageObject.getAttachFileName(video);
                } else if (photo != null) {
                    fileName = MessageObject.getAttachFileName(photo);
                } else if (document != null) {
                    fileName = MessageObject.getAttachFileName(document);
                }
                if (fileName != null && !FileLoader.this.loadOperationPaths.containsKey(fileName)) {
                    FileLoadOperation operation = null;
                    if (video != null) {
                        operation = new FileLoadOperation(video);
                        operation.totalBytesCount = video.size;
                    } else if (photo != null) {
                        operation = new FileLoadOperation(photo.location);
                        operation.totalBytesCount = photo.size;
                        operation.needBitmapCreate = false;
                    } else if (document != null) {
                        operation = new FileLoadOperation(document);
                        operation.totalBytesCount = document.size;
                    }
                    final String arg1 = fileName;
                    FileLoader.this.loadOperationPaths.put(fileName, operation);
                    operation.delegate = new FileLoadOperationDelegate() {

                        class C03251 implements Runnable {
                            C03251() {
                            }

                            public void run() {
                                NotificationCenter.Instance.postNotificationName(10003, arg1, Float.valueOf(1.0f));
                            }
                        }

                        class C03262 implements Runnable {
                            C03262() {
                            }

                            public void run() {
                                NotificationCenter.Instance.postNotificationName(10004, arg1);
                            }
                        }

                        class C03273 implements Runnable {
                            C03273() {
                            }

                            public void run() {
                                FileLoader.this.loadOperationPaths.remove(arg1);
                                FileLoader.this.currentLoadOperationsCount = FileLoader.this.currentLoadOperationsCount - 1;
                                if (FileLoader.this.currentLoadOperationsCount < 2) {
                                    FileLoadOperation operation = (FileLoadOperation) FileLoader.this.loadOperationQueue.poll();
                                    if (operation != null) {
                                        FileLoader.this.currentLoadOperationsCount = FileLoader.this.currentLoadOperationsCount + 1;
                                        operation.start();
                                    }
                                }
                            }
                        }

                        class C03284 implements Runnable {
                            C03284() {
                            }

                            public void run() {
                                NotificationCenter.Instance.postNotificationName(10005, arg1);
                            }
                        }

                        class C03295 implements Runnable {
                            C03295() {
                            }

                            public void run() {
                                FileLoader.this.loadOperationPaths.remove(arg1);
                                FileLoader.this.currentLoadOperationsCount = FileLoader.this.currentLoadOperationsCount - 1;
                                if (FileLoader.this.currentLoadOperationsCount < 2) {
                                    FileLoadOperation operation = (FileLoadOperation) FileLoader.this.loadOperationQueue.poll();
                                    if (operation != null) {
                                        FileLoader.this.currentLoadOperationsCount = FileLoader.this.currentLoadOperationsCount + 1;
                                        operation.start();
                                    }
                                }
                            }
                        }

                        public void didFinishLoadingFile(FileLoadOperation operation) {
                            Utilities.RunOnUIThread(new C03251());
                            Utilities.RunOnUIThread(new C03262());
                            Utilities.fileUploadQueue.postRunnable(new C03273());
                            FileLoader.this.fileProgresses.remove(arg1);
                        }

                        public void didFailedLoadingFile(FileLoadOperation operation) {
                            FileLoader.this.fileProgresses.remove(arg1);
                            if (operation.state != 2) {
                                Utilities.RunOnUIThread(new C03284());
                            }
                            Utilities.fileUploadQueue.postRunnable(new C03295());
                        }

                        public void didChangedLoadProgress(FileLoadOperation operation, final float progress) {
                            if (operation.state != 2) {
                                FileLoader.this.fileProgresses.put(arg1, Float.valueOf(progress));
                            }
                            long currentTime = System.currentTimeMillis();
                            if (FileLoader.this.lastProgressUpdateTime == 0 || FileLoader.this.lastProgressUpdateTime < currentTime - 500) {
                                FileLoader.this.lastProgressUpdateTime = currentTime;
                                Utilities.RunOnUIThread(new Runnable() {
                                    public void run() {
                                        NotificationCenter.Instance.postNotificationName(10003, arg1, Float.valueOf(progress));
                                    }
                                });
                            }
                        }
                    };
                    if (FileLoader.this.currentLoadOperationsCount < 2) {
                        FileLoader.this.currentLoadOperationsCount = FileLoader.this.currentLoadOperationsCount + 1;
                        operation.start();
                        return;
                    }
                    FileLoader.this.loadOperationQueue.add(operation);
                }
            }
        });
    }

    Bitmap imageFromKey(String key) {
        if (key == null) {
            return null;
        }
        return this.memCache.get(key);
    }

    public void clearMemory() {
        this.memCache.evictAll();
    }

    private Integer getTag(Object obj) {
        if (obj instanceof BackupImageView) {
            return (Integer) ((BackupImageView) obj).getTag(C0419R.string.CacheTag);
        }
        if (obj instanceof ImageReceiver) {
            return ((ImageReceiver) obj).TAG;
        }
        return Integer.valueOf(0);
    }

    private void setTag(Object obj, Integer tag) {
        if (obj instanceof BackupImageView) {
            ((BackupImageView) obj).setTag(C0419R.string.CacheTag, tag);
        } else if (obj instanceof ImageReceiver) {
            ((ImageReceiver) obj).TAG = tag;
        }
    }

    public void cancelLoadingForImageView(final Object imageView) {
        if (imageView != null) {
            Utilities.imageLoadQueue.postRunnable(new Runnable() {
                public void run() {
                    Integer TAG = FileLoader.this.getTag(imageView);
                    if (TAG == null) {
                        TAG = Integer.valueOf(FileLoader.this.lastImageNum);
                        FileLoader.this.setTag(imageView, TAG);
                        FileLoader fileLoader = FileLoader.this;
                        fileLoader.lastImageNum++;
                        if (FileLoader.this.lastImageNum == Integer.MAX_VALUE) {
                            FileLoader.this.lastImageNum = 0;
                        }
                    }
                    CacheImage ei = (CacheImage) FileLoader.this.imageLoadingByKeys.get(TAG);
                    if (ei != null) {
                        FileLoader.this.imageLoadingByKeys.remove(TAG);
                        ei.removeImageView(imageView);
                        if (ei.imageViewArray.size() == 0) {
                            FileLoader.this.checkOperationsAndClear(ei.loadOperation);
                            ei.cancelAndClear();
                            FileLoader.this.imageLoading.remove(ei.key);
                        }
                    }
                }
            });
        }
    }

    public Bitmap getImageFromMemory(FileLocation url, Object imageView, String filter, boolean cancel) {
        return getImageFromMemory(url, null, imageView, filter, cancel);
    }

    public Bitmap getImageFromMemory(String url, Object imageView, String filter, boolean cancel) {
        return getImageFromMemory(null, url, imageView, filter, cancel);
    }

    public Bitmap getImageFromMemory(FileLocation url, String httpUrl, Object imageView, String filter, boolean cancel) {
        if (url == null && httpUrl == null) {
            return null;
        }
        String key;
        if (httpUrl != null) {
            key = Utilities.MD5(httpUrl);
        } else {
            key = url.volume_id + "_" + url.local_id;
        }
        if (filter != null) {
            key = key + "@" + filter;
        }
        Bitmap img = imageFromKey(key);
        if (imageView == null || img == null || !cancel) {
            return img;
        }
        cancelLoadingForImageView(imageView);
        return img;
    }

    private void performReplace(String oldKey, String newKey) {
        Bitmap b = this.memCache.get(oldKey);
        if (b != null) {
            this.ignoreRemoval = oldKey;
            this.memCache.remove(oldKey);
            this.memCache.put(newKey, b);
            this.ignoreRemoval = null;
        }
        Integer val = (Integer) this.BitmapUseCounts.get(oldKey);
        if (val != null) {
            this.BitmapUseCounts.put(newKey, val);
            this.BitmapUseCounts.remove(oldKey);
        }
    }

    public void replaceImageInCache(final String oldKey, final String newKey) {
        Utilities.RunOnUIThread(new Runnable() {
            public void run() {
                ArrayList<String> arr = FileLoader.this.memCache.getFilterKeys(oldKey);
                if (arr != null) {
                    Iterator i$ = arr.iterator();
                    while (i$.hasNext()) {
                        String filter = (String) i$.next();
                        FileLoader.this.performReplace(oldKey + "@" + filter, newKey + "@" + filter);
                    }
                    return;
                }
                FileLoader.this.performReplace(oldKey, newKey);
            }
        });
    }

    public void loadImage(String url, Object imageView, String filter, boolean cancel) {
        loadImage(null, url, imageView, filter, cancel, 0);
    }

    public void loadImage(FileLocation url, Object imageView, String filter, boolean cancel) {
        loadImage(url, null, imageView, filter, cancel, 0);
    }

    public void loadImage(FileLocation url, Object imageView, String filter, boolean cancel, int size) {
        loadImage(url, null, imageView, filter, cancel, size);
    }

    public void loadImage(FileLocation url, String httpUrl, Object imageView, String filter, boolean cancel, int size) {
        if ((url != null || httpUrl != null) && imageView != null) {
            if (url == null || (url instanceof TL_fileLocation) || (url instanceof TL_fileEncryptedLocation)) {
                final String str = httpUrl;
                final FileLocation fileLocation = url;
                final String str2 = filter;
                final Object obj = imageView;
                final boolean z = cancel;
                final int i = size;
                Utilities.imageLoadQueue.postRunnable(new Runnable() {
                    public void run() {
                        String key;
                        if (str != null) {
                            key = Utilities.MD5(str);
                        } else {
                            key = fileLocation.volume_id + "_" + fileLocation.local_id;
                        }
                        if (str2 != null) {
                            key = key + "@" + str2;
                        }
                        Integer TAG = FileLoader.this.getTag(obj);
                        if (TAG == null) {
                            TAG = Integer.valueOf(FileLoader.this.lastImageNum);
                            FileLoader.this.setTag(obj, TAG);
                            FileLoader fileLoader = FileLoader.this;
                            fileLoader.lastImageNum++;
                            if (FileLoader.this.lastImageNum == Integer.MAX_VALUE) {
                                FileLoader.this.lastImageNum = 0;
                            }
                        }
                        boolean added = false;
                        boolean addToByKeys = true;
                        CacheImage alreadyLoadingImage = (CacheImage) FileLoader.this.imageLoading.get(key);
                        if (z) {
                            CacheImage ei = (CacheImage) FileLoader.this.imageLoadingByKeys.get(TAG);
                            if (ei != null) {
                                if (ei != alreadyLoadingImage) {
                                    ei.removeImageView(obj);
                                    if (ei.imageViewArray.size() == 0) {
                                        FileLoader.this.checkOperationsAndClear(ei.loadOperation);
                                        ei.cancelAndClear();
                                        FileLoader.this.imageLoading.remove(ei.key);
                                    }
                                } else {
                                    addToByKeys = false;
                                    added = true;
                                }
                            }
                        }
                        if (alreadyLoadingImage != null && addToByKeys) {
                            alreadyLoadingImage.addImageView(obj);
                            FileLoader.this.imageLoadingByKeys.put(TAG, alreadyLoadingImage);
                            added = true;
                        }
                        if (!added) {
                            FileLoadOperation loadOperation;
                            final CacheImage img = new CacheImage();
                            img.key = key;
                            img.addImageView(obj);
                            FileLoader.this.imageLoadingByKeys.put(TAG, img);
                            FileLoader.this.imageLoading.put(key, img);
                            final String arg2 = key;
                            if (str != null) {
                                loadOperation = new FileLoadOperation(str);
                            } else {
                                loadOperation = new FileLoadOperation(fileLocation);
                            }
                            loadOperation.totalBytesCount = i;
                            loadOperation.filter = str2;
                            loadOperation.delegate = new FileLoadOperationDelegate() {

                                class C03374 implements Runnable {
                                    C03374() {
                                    }

                                    public void run() {
                                        img.callAndClear(null);
                                    }
                                }

                                public void didFinishLoadingFile(FileLoadOperation operation) {
                                    FileLoader.this.enqueueImageProcessingOperationWithImage(operation.image, str2, arg2, img);
                                    if (operation.totalBytesCount != 0) {
                                        final String arg1 = operation.location.volume_id + "_" + operation.location.local_id + ".jpg";
                                        FileLoader.this.fileProgresses.remove(arg1);
                                        Utilities.RunOnUIThread(new Runnable() {
                                            public void run() {
                                                NotificationCenter.Instance.postNotificationName(10003, arg1, Float.valueOf(1.0f));
                                            }
                                        });
                                        Utilities.RunOnUIThread(new Runnable() {
                                            public void run() {
                                                NotificationCenter.Instance.postNotificationName(10004, arg1);
                                            }
                                        });
                                    }
                                }

                                public void didFailedLoadingFile(final FileLoadOperation operation) {
                                    Utilities.imageLoadQueue.postRunnable(new Runnable() {
                                        public void run() {
                                            Iterator i$ = img.imageViewArray.iterator();
                                            while (i$.hasNext()) {
                                                FileLoader.this.imageLoadingByKeys.remove(FileLoader.this.getTag(i$.next()));
                                                FileLoader.this.imageLoading.remove(arg2);
                                                FileLoader.this.checkOperationsAndClear(operation);
                                            }
                                        }
                                    });
                                    Utilities.RunOnUIThread(new C03374());
                                    if (operation.totalBytesCount != 0) {
                                        final String arg1 = operation.location.volume_id + "_" + operation.location.local_id + ".jpg";
                                        FileLoader.this.fileProgresses.remove(arg1);
                                        if (operation.state != 2) {
                                            Utilities.RunOnUIThread(new Runnable() {
                                                public void run() {
                                                    NotificationCenter.Instance.postNotificationName(10005, arg1);
                                                }
                                            });
                                        }
                                    }
                                }

                                public void didChangedLoadProgress(FileLoadOperation operation, final float progress) {
                                    if (operation.totalBytesCount != 0) {
                                        final String arg1 = operation.location.volume_id + "_" + operation.location.local_id + ".jpg";
                                        if (operation.state != 2) {
                                            FileLoader.this.fileProgresses.put(arg1, Float.valueOf(progress));
                                        }
                                        long currentTime = System.currentTimeMillis();
                                        if (FileLoader.this.lastProgressUpdateTime == 0 || FileLoader.this.lastProgressUpdateTime < currentTime - 50) {
                                            FileLoader.this.lastProgressUpdateTime = currentTime;
                                            Utilities.RunOnUIThread(new Runnable() {
                                                public void run() {
                                                    NotificationCenter.Instance.postNotificationName(10003, arg1, Float.valueOf(progress));
                                                }
                                            });
                                        }
                                    }
                                }
                            };
                            img.loadOperation = loadOperation;
                            if (FileLoader.this.runningOperation.size() < 2) {
                                loadOperation.start();
                                FileLoader.this.runningOperation.add(loadOperation);
                                return;
                            }
                            FileLoader.this.operationsQueue.add(loadOperation);
                        }
                    }
                });
            }
        }
    }

    private void checkOperationsAndClear(FileLoadOperation operation) {
        this.operationsQueue.remove(operation);
        this.runningOperation.remove(operation);
        while (this.runningOperation.size() < 2 && this.operationsQueue.size() != 0) {
            FileLoadOperation loadOperation = (FileLoadOperation) this.operationsQueue.poll();
            this.runningOperation.add(loadOperation);
            loadOperation.start();
        }
    }

    public void processImage(Bitmap image, Object imageView, String filter, boolean cancel) {
        if (filter != null && imageView != null) {
            Integer TAG = getTag(imageView);
            if (TAG == null) {
                TAG = Integer.valueOf(this.lastImageNum);
                setTag(image, TAG);
                this.lastImageNum++;
                if (this.lastImageNum == Integer.MAX_VALUE) {
                    this.lastImageNum = 0;
                }
            }
            boolean added = false;
            boolean addToByKeys = true;
            CacheImage alreadyLoadingImage = (CacheImage) this.imageLoading.get(filter);
            if (cancel) {
                CacheImage ei = (CacheImage) this.imageLoadingByKeys.get(TAG);
                if (ei != null) {
                    if (ei != alreadyLoadingImage) {
                        ei.removeImageView(imageView);
                        if (ei.imageViewArray.size() == 0) {
                            checkOperationsAndClear(ei.loadOperation);
                            ei.cancelAndClear();
                            this.imageLoading.remove(ei.key);
                        }
                    } else {
                        addToByKeys = false;
                        added = true;
                    }
                }
            }
            if (alreadyLoadingImage != null && addToByKeys) {
                alreadyLoadingImage.addImageView(imageView);
                this.imageLoadingByKeys.put(TAG, alreadyLoadingImage);
                added = true;
            }
            if (!added) {
                CacheImage img = new CacheImage();
                img.key = filter;
                img.addImageView(imageView);
                this.imageLoadingByKeys.put(TAG, img);
                this.imageLoading.put(filter, img);
                enqueueImageProcessingOperationWithImage(image, filter, filter, img);
            }
        }
    }

    void enqueueImageProcessingOperationWithImage(final Bitmap image, String filter, final String key, final CacheImage img) {
        if (image != null && key != null) {
            Utilities.imageLoadQueue.postRunnable(new Runnable() {
                public void run() {
                    Iterator i$ = img.imageViewArray.iterator();
                    while (i$.hasNext()) {
                        FileLoader.this.imageLoadingByKeys.remove(FileLoader.this.getTag(i$.next()));
                    }
                    FileLoader.this.checkOperationsAndClear(img.loadOperation);
                    FileLoader.this.imageLoading.remove(key);
                }
            });
            Utilities.RunOnUIThread(new Runnable() {
                public void run() {
                    img.callAndClear(image);
                    if (FileLoader.this.memCache.get(key) == null) {
                        FileLoader.this.memCache.put(key, image);
                    }
                }
            });
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap loadBitmap(java.lang.String r15, float r16, float r17) {
        /*
        r7 = new android.graphics.BitmapFactory$Options;
        r7.<init>();
        r1 = 1;
        r7.inJustDecodeBounds = r1;
        android.graphics.BitmapFactory.decodeFile(r15, r7);
        r1 = r7.outWidth;
        r13 = (float) r1;
        r1 = r7.outHeight;
        r12 = (float) r1;
        r1 = r13 / r16;
        r2 = r12 / r17;
        r14 = java.lang.Math.max(r1, r2);
        r1 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r1 = (r14 > r1 ? 1 : (r14 == r1 ? 0 : -1));
        if (r1 >= 0) goto L_0x0021;
    L_0x001f:
        r14 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x0021:
        r1 = 0;
        r7.inJustDecodeBounds = r1;
        r1 = (int) r14;
        r7.inSampleSize = r1;
        r5 = 0;
        r9 = new android.media.ExifInterface;	 Catch:{ Exception -> 0x0094 }
        r9.<init>(r15);	 Catch:{ Exception -> 0x0094 }
        r1 = "Orientation";
        r2 = 1;
        r11 = r9.getAttributeInt(r1, r2);	 Catch:{ Exception -> 0x0094 }
        r10 = new android.graphics.Matrix;	 Catch:{ Exception -> 0x0094 }
        r10.<init>();	 Catch:{ Exception -> 0x0094 }
        switch(r11) {
            case 3: goto L_0x0063;
            case 4: goto L_0x003c;
            case 5: goto L_0x003c;
            case 6: goto L_0x0055;
            case 7: goto L_0x003c;
            case 8: goto L_0x0069;
            default: goto L_0x003c;
        };
    L_0x003c:
        r5 = r10;
    L_0x003d:
        r0 = android.graphics.BitmapFactory.decodeFile(r15, r7);	 Catch:{ Exception -> 0x006f }
        if (r0 == 0) goto L_0x0054;
    L_0x0043:
        if (r5 == 0) goto L_0x0054;
    L_0x0045:
        r1 = 0;
        r2 = 0;
        r3 = r0.getWidth();	 Catch:{ Exception -> 0x006f }
        r4 = r0.getHeight();	 Catch:{ Exception -> 0x006f }
        r6 = 1;
        r0 = android.graphics.Bitmap.createBitmap(r0, r1, r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x006f }
    L_0x0054:
        return r0;
    L_0x0055:
        r1 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r10.postRotate(r1);	 Catch:{ Exception -> 0x005b }
        goto L_0x003c;
    L_0x005b:
        r8 = move-exception;
        r5 = r10;
    L_0x005d:
        r1 = "tmessages";
        org.telegram.messenger.FileLog.m799e(r1, r8);
        goto L_0x003d;
    L_0x0063:
        r1 = 1127481344; // 0x43340000 float:180.0 double:5.570497984E-315;
        r10.postRotate(r1);	 Catch:{ Exception -> 0x005b }
        goto L_0x003c;
    L_0x0069:
        r1 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r10.postRotate(r1);	 Catch:{ Exception -> 0x005b }
        goto L_0x003c;
    L_0x006f:
        r8 = move-exception;
        r1 = "tmessages";
        org.telegram.messenger.FileLog.m799e(r1, r8);
        r1 = Instance;
        r1 = r1.memCache;
        r1.evictAll();
        r0 = android.graphics.BitmapFactory.decodeFile(r15, r7);
        if (r0 == 0) goto L_0x0054;
    L_0x0082:
        if (r5 == 0) goto L_0x0054;
    L_0x0084:
        r1 = 0;
        r2 = 0;
        r3 = r0.getWidth();
        r4 = r0.getHeight();
        r6 = 1;
        r0 = android.graphics.Bitmap.createBitmap(r0, r1, r2, r3, r4, r5, r6);
        goto L_0x0054;
    L_0x0094:
        r8 = move-exception;
        goto L_0x005d;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.FileLoader.loadBitmap(java.lang.String, float, float):android.graphics.Bitmap");
    }

    public static PhotoSize scaleAndSaveImage(Bitmap bitmap, float maxWidth, float maxHeight, int quality, boolean cache) {
        if (bitmap == null) {
            return null;
        }
        float photoW = (float) bitmap.getWidth();
        float photoH = (float) bitmap.getHeight();
        if (photoW == 0.0f || photoH == 0.0f) {
            return null;
        }
        PhotoSize size;
        float scaleFactor = Math.max(photoW / maxWidth, photoH / maxHeight);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (photoW / scaleFactor), (int) (photoH / scaleFactor), true);
        TL_fileLocation location = new TL_fileLocation();
        location.volume_id = -2147483648L;
        location.dc_id = ExploreByTouchHelper.INVALID_ID;
        location.local_id = UserConfig.lastLocalId;
        UserConfig.lastLocalId--;
        if (cache) {
            size = new TL_photoCachedSize();
        } else {
            size = new TL_photoSize();
        }
        size.location = location;
        size.f60w = (int) (photoW / scaleFactor);
        size.f59h = (int) (photoH / scaleFactor);
        if (cache) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledBitmap.compress(CompressFormat.JPEG, quality, stream);
            size.bytes = stream.toByteArray();
            size.size = size.bytes.length;
        } else {
            try {
                FileOutputStream stream2 = new FileOutputStream(new File(Utilities.getCacheDir(), location.volume_id + "_" + location.local_id + ".jpg"));
                scaledBitmap.compress(CompressFormat.JPEG, quality, stream2);
                size.size = (int) stream2.getChannel().size();
            } catch (Exception e) {
                return null;
            }
        }
        if (VERSION.SDK_INT >= 11 || scaledBitmap == bitmap) {
            return size;
        }
        scaledBitmap.recycle();
        return size;
    }
}
