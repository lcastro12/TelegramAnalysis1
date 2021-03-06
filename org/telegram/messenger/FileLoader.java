package org.telegram.messenger;

import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.FileLoadOperation.FileLoadOperationDelegate;
import org.telegram.messenger.FileUploadOperation.FileUploadOperationDelegate;
import org.telegram.messenger.exoplayer2.upstream.DataSource;
import org.telegram.messenger.exoplayer2.upstream.TransferListener;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputEncryptedFile;
import org.telegram.tgnet.TLRPC.InputFile;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_messageService;
import org.telegram.tgnet.TLRPC.TL_photoCachedSize;
import org.telegram.tgnet.TLRPC.TL_webDocument;
import org.telegram.tgnet.TLRPC.WebDocument;

public class FileLoader {
    private static volatile FileLoader[] Instance = new FileLoader[3];
    public static final int MEDIA_DIR_AUDIO = 1;
    public static final int MEDIA_DIR_CACHE = 4;
    public static final int MEDIA_DIR_DOCUMENT = 3;
    public static final int MEDIA_DIR_IMAGE = 0;
    public static final int MEDIA_DIR_VIDEO = 2;
    private static volatile DispatchQueue fileLoaderQueue = new DispatchQueue("fileUploadQueue");
    private static SparseArray<File> mediaDirs = null;
    private ArrayList<FileLoadOperation> activeFileLoadOperation = new ArrayList();
    private SparseArray<LinkedList<FileLoadOperation>> audioLoadOperationQueues = new SparseArray();
    private int currentAccount;
    private SparseIntArray currentAudioLoadOperationsCount = new SparseIntArray();
    private SparseIntArray currentLoadOperationsCount = new SparseIntArray();
    private SparseIntArray currentPhotoLoadOperationsCount = new SparseIntArray();
    private int currentUploadOperationsCount = 0;
    private int currentUploadSmallOperationsCount = 0;
    private FileLoaderDelegate delegate = null;
    private ConcurrentHashMap<String, FileLoadOperation> loadOperationPaths = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Boolean> loadOperationPathsUI = new ConcurrentHashMap(10, 1.0f, 2);
    private SparseArray<LinkedList<FileLoadOperation>> loadOperationQueues = new SparseArray();
    private SparseArray<LinkedList<FileLoadOperation>> photoLoadOperationQueues = new SparseArray();
    private ConcurrentHashMap<String, FileUploadOperation> uploadOperationPaths = new ConcurrentHashMap();
    private ConcurrentHashMap<String, FileUploadOperation> uploadOperationPathsEnc = new ConcurrentHashMap();
    private LinkedList<FileUploadOperation> uploadOperationQueue = new LinkedList();
    private HashMap<String, Long> uploadSizes = new HashMap();
    private LinkedList<FileUploadOperation> uploadSmallOperationQueue = new LinkedList();

    public interface FileLoaderDelegate {
        void fileDidFailedLoad(String str, int i);

        void fileDidFailedUpload(String str, boolean z);

        void fileDidLoaded(String str, File file, int i);

        void fileDidUploaded(String str, InputFile inputFile, InputEncryptedFile inputEncryptedFile, byte[] bArr, byte[] bArr2, long j);

        void fileLoadProgressChanged(String str, float f);

        void fileUploadProgressChanged(String str, float f, boolean z);
    }

    public static FileLoader getInstance(int num) {
        FileLoader localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (FileLoader.class) {
                try {
                    localInstance = Instance[num];
                    if (localInstance == null) {
                        FileLoader[] fileLoaderArr = Instance;
                        FileLoader localInstance2 = new FileLoader(num);
                        try {
                            fileLoaderArr[num] = localInstance2;
                            localInstance = localInstance2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            localInstance = localInstance2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return localInstance;
    }

    public FileLoader(int instance) {
        this.currentAccount = instance;
    }

    public static void setMediaDirs(SparseArray<File> dirs) {
        mediaDirs = dirs;
    }

    public static File checkDirectory(int type) {
        return (File) mediaDirs.get(type);
    }

    public static File getDirectory(int type) {
        File dir = (File) mediaDirs.get(type);
        if (dir == null && type != 4) {
            dir = (File) mediaDirs.get(4);
        }
        try {
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
        }
        return dir;
    }

    public void cancelUploadFile(final String location, final boolean enc) {
        fileLoaderQueue.postRunnable(new Runnable() {
            public void run() {
                FileUploadOperation operation;
                if (enc) {
                    operation = (FileUploadOperation) FileLoader.this.uploadOperationPathsEnc.get(location);
                } else {
                    operation = (FileUploadOperation) FileLoader.this.uploadOperationPaths.get(location);
                }
                FileLoader.this.uploadSizes.remove(location);
                if (operation != null) {
                    FileLoader.this.uploadOperationPathsEnc.remove(location);
                    FileLoader.this.uploadOperationQueue.remove(operation);
                    FileLoader.this.uploadSmallOperationQueue.remove(operation);
                    operation.cancel();
                }
            }
        });
    }

    public void checkUploadNewDataAvailable(String location, boolean encrypted, long newAvailableSize, long finalSize) {
        final boolean z = encrypted;
        final String str = location;
        final long j = newAvailableSize;
        final long j2 = finalSize;
        fileLoaderQueue.postRunnable(new Runnable() {
            public void run() {
                FileUploadOperation operation;
                if (z) {
                    operation = (FileUploadOperation) FileLoader.this.uploadOperationPathsEnc.get(str);
                } else {
                    operation = (FileUploadOperation) FileLoader.this.uploadOperationPaths.get(str);
                }
                if (operation != null) {
                    operation.checkNewDataAvailable(j, j2);
                } else if (j2 != 0) {
                    FileLoader.this.uploadSizes.put(str, Long.valueOf(j2));
                }
            }
        });
    }

    public void uploadFile(String location, boolean encrypted, boolean small, int type) {
        uploadFile(location, encrypted, small, 0, type);
    }

    public void uploadFile(String location, boolean encrypted, boolean small, int estimatedSize, int type) {
        if (location != null) {
            final boolean z = encrypted;
            final String str = location;
            final int i = estimatedSize;
            final int i2 = type;
            final boolean z2 = small;
            fileLoaderQueue.postRunnable(new Runnable() {

                class C01891 implements FileUploadOperationDelegate {

                    class C01882 implements Runnable {
                        C01882() {
                        }

                        public void run() {
                            if (z) {
                                FileLoader.this.uploadOperationPathsEnc.remove(str);
                            } else {
                                FileLoader.this.uploadOperationPaths.remove(str);
                            }
                            if (FileLoader.this.delegate != null) {
                                FileLoader.this.delegate.fileDidFailedUpload(str, z);
                            }
                            FileUploadOperation operation;
                            if (z2) {
                                FileLoader.this.currentUploadSmallOperationsCount = FileLoader.this.currentUploadSmallOperationsCount - 1;
                                if (FileLoader.this.currentUploadSmallOperationsCount < 1) {
                                    operation = (FileUploadOperation) FileLoader.this.uploadSmallOperationQueue.poll();
                                    if (operation != null) {
                                        FileLoader.this.currentUploadSmallOperationsCount = FileLoader.this.currentUploadSmallOperationsCount + 1;
                                        operation.start();
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount - 1;
                            if (FileLoader.this.currentUploadOperationsCount < 1) {
                                operation = (FileUploadOperation) FileLoader.this.uploadOperationQueue.poll();
                                if (operation != null) {
                                    FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount + 1;
                                    operation.start();
                                }
                            }
                        }
                    }

                    C01891() {
                    }

                    public void didFinishUploadingFile(FileUploadOperation operation, InputFile inputFile, InputEncryptedFile inputEncryptedFile, byte[] key, byte[] iv) {
                        final InputFile inputFile2 = inputFile;
                        final InputEncryptedFile inputEncryptedFile2 = inputEncryptedFile;
                        final byte[] bArr = key;
                        final byte[] bArr2 = iv;
                        final FileUploadOperation fileUploadOperation = operation;
                        FileLoader.fileLoaderQueue.postRunnable(new Runnable() {
                            public void run() {
                                if (z) {
                                    FileLoader.this.uploadOperationPathsEnc.remove(str);
                                } else {
                                    FileLoader.this.uploadOperationPaths.remove(str);
                                }
                                FileUploadOperation operation;
                                if (z2) {
                                    FileLoader.this.currentUploadSmallOperationsCount = FileLoader.this.currentUploadSmallOperationsCount - 1;
                                    if (FileLoader.this.currentUploadSmallOperationsCount < 1) {
                                        operation = (FileUploadOperation) FileLoader.this.uploadSmallOperationQueue.poll();
                                        if (operation != null) {
                                            FileLoader.this.currentUploadSmallOperationsCount = FileLoader.this.currentUploadSmallOperationsCount + 1;
                                            operation.start();
                                        }
                                    }
                                } else {
                                    FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount - 1;
                                    if (FileLoader.this.currentUploadOperationsCount < 1) {
                                        operation = (FileUploadOperation) FileLoader.this.uploadOperationQueue.poll();
                                        if (operation != null) {
                                            FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount + 1;
                                            operation.start();
                                        }
                                    }
                                }
                                if (FileLoader.this.delegate != null) {
                                    FileLoader.this.delegate.fileDidUploaded(str, inputFile2, inputEncryptedFile2, bArr, bArr2, fileUploadOperation.getTotalFileSize());
                                }
                            }
                        });
                    }

                    public void didFailedUploadingFile(FileUploadOperation operation) {
                        FileLoader.fileLoaderQueue.postRunnable(new C01882());
                    }

                    public void didChangedUploadProgress(FileUploadOperation operation, float progress) {
                        if (FileLoader.this.delegate != null) {
                            FileLoader.this.delegate.fileUploadProgressChanged(str, progress, z);
                        }
                    }
                }

                public void run() {
                    if (z) {
                        if (FileLoader.this.uploadOperationPathsEnc.containsKey(str)) {
                            return;
                        }
                    } else if (FileLoader.this.uploadOperationPaths.containsKey(str)) {
                        return;
                    }
                    int esimated = i;
                    if (!(esimated == 0 || ((Long) FileLoader.this.uploadSizes.get(str)) == null)) {
                        esimated = 0;
                        FileLoader.this.uploadSizes.remove(str);
                    }
                    FileUploadOperation operation = new FileUploadOperation(FileLoader.this.currentAccount, str, z, esimated, i2);
                    if (z) {
                        FileLoader.this.uploadOperationPathsEnc.put(str, operation);
                    } else {
                        FileLoader.this.uploadOperationPaths.put(str, operation);
                    }
                    operation.setDelegate(new C01891());
                    if (z2) {
                        if (FileLoader.this.currentUploadSmallOperationsCount < 1) {
                            FileLoader.this.currentUploadSmallOperationsCount = FileLoader.this.currentUploadSmallOperationsCount + 1;
                            operation.start();
                            return;
                        }
                        FileLoader.this.uploadSmallOperationQueue.add(operation);
                    } else if (FileLoader.this.currentUploadOperationsCount < 1) {
                        FileLoader.this.currentUploadOperationsCount = FileLoader.this.currentUploadOperationsCount + 1;
                        operation.start();
                    } else {
                        FileLoader.this.uploadOperationQueue.add(operation);
                    }
                }
            });
        }
    }

    private LinkedList<FileLoadOperation> getAudioLoadOperationQueue(int datacenterId) {
        LinkedList<FileLoadOperation> audioLoadOperationQueue = (LinkedList) this.audioLoadOperationQueues.get(datacenterId);
        if (audioLoadOperationQueue != null) {
            return audioLoadOperationQueue;
        }
        audioLoadOperationQueue = new LinkedList();
        this.audioLoadOperationQueues.put(datacenterId, audioLoadOperationQueue);
        return audioLoadOperationQueue;
    }

    private LinkedList<FileLoadOperation> getPhotoLoadOperationQueue(int datacenterId) {
        LinkedList<FileLoadOperation> photoLoadOperationQueue = (LinkedList) this.photoLoadOperationQueues.get(datacenterId);
        if (photoLoadOperationQueue != null) {
            return photoLoadOperationQueue;
        }
        photoLoadOperationQueue = new LinkedList();
        this.photoLoadOperationQueues.put(datacenterId, photoLoadOperationQueue);
        return photoLoadOperationQueue;
    }

    private LinkedList<FileLoadOperation> getLoadOperationQueue(int datacenterId) {
        LinkedList<FileLoadOperation> loadOperationQueue = (LinkedList) this.loadOperationQueues.get(datacenterId);
        if (loadOperationQueue != null) {
            return loadOperationQueue;
        }
        loadOperationQueue = new LinkedList();
        this.loadOperationQueues.put(datacenterId, loadOperationQueue);
        return loadOperationQueue;
    }

    public void cancelLoadFile(Document document) {
        cancelLoadFile(document, null, null, null);
    }

    public void cancelLoadFile(TL_webDocument document) {
        cancelLoadFile(null, document, null, null);
    }

    public void cancelLoadFile(PhotoSize photo) {
        cancelLoadFile(null, null, photo.location, null);
    }

    public void cancelLoadFile(FileLocation location, String ext) {
        cancelLoadFile(null, null, location, ext);
    }

    private void cancelLoadFile(Document document, TL_webDocument webDocument, FileLocation location, String locationExt) {
        if (location != null || document != null || webDocument != null) {
            String fileName;
            if (location != null) {
                fileName = getAttachFileName(location, locationExt);
            } else if (document != null) {
                fileName = getAttachFileName(document);
            } else if (webDocument != null) {
                fileName = getAttachFileName(webDocument);
            } else {
                fileName = null;
            }
            if (fileName != null) {
                this.loadOperationPathsUI.remove(fileName);
                final Document document2 = document;
                final TL_webDocument tL_webDocument = webDocument;
                final FileLocation fileLocation = location;
                fileLoaderQueue.postRunnable(new Runnable() {
                    public void run() {
                        FileLoadOperation operation = (FileLoadOperation) FileLoader.this.loadOperationPaths.remove(fileName);
                        if (operation != null) {
                            int datacenterId = operation.getDatacenterId();
                            if (MessageObject.isVoiceDocument(document2) || MessageObject.isVoiceWebDocument(tL_webDocument)) {
                                if (!FileLoader.this.getAudioLoadOperationQueue(datacenterId).remove(operation)) {
                                    FileLoader.this.currentAudioLoadOperationsCount.put(datacenterId, FileLoader.this.currentAudioLoadOperationsCount.get(datacenterId) - 1);
                                }
                            } else if (fileLocation == null && !MessageObject.isImageWebDocument(tL_webDocument)) {
                                if (!FileLoader.this.getLoadOperationQueue(datacenterId).remove(operation)) {
                                    FileLoader.this.currentLoadOperationsCount.put(datacenterId, FileLoader.this.currentLoadOperationsCount.get(datacenterId) - 1);
                                }
                                FileLoader.this.activeFileLoadOperation.remove(operation);
                            } else if (!FileLoader.this.getPhotoLoadOperationQueue(datacenterId).remove(operation)) {
                                FileLoader.this.currentPhotoLoadOperationsCount.put(datacenterId, FileLoader.this.currentPhotoLoadOperationsCount.get(datacenterId) - 1);
                            }
                            operation.cancel();
                        }
                    }
                });
            }
        }
    }

    public boolean isLoadingFile(String fileName) {
        return this.loadOperationPathsUI.containsKey(fileName);
    }

    public float getBufferedProgressFromPosition(float position, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return 0.0f;
        }
        FileLoadOperation loadOperation = (FileLoadOperation) this.loadOperationPaths.get(fileName);
        if (loadOperation != null) {
            return loadOperation.getDownloadedLengthFromOffset(position);
        }
        return 0.0f;
    }

    public void loadFile(PhotoSize photo, String ext, int cacheType) {
        if (photo != null) {
            if (cacheType == 0 && photo != null && (photo.size == 0 || photo.location.key != null)) {
                cacheType = 1;
            }
            loadFile(null, null, photo.location, ext, photo.size, false, cacheType);
        }
    }

    public void loadFile(Document document, boolean force, int cacheType) {
        if (document != null) {
            if (!(cacheType != 0 || document == null || document.key == null)) {
                cacheType = 1;
            }
            loadFile(document, null, null, null, 0, force, cacheType);
        }
    }

    public void loadFile(TL_webDocument document, boolean force, int cacheType) {
        loadFile(null, document, null, null, 0, force, cacheType);
    }

    public void loadFile(FileLocation location, String ext, int size, int cacheType) {
        if (location != null) {
            if (cacheType == 0 && (size == 0 || !(location == null || location.key == null))) {
                cacheType = 1;
            }
            loadFile(null, null, location, ext, size, true, cacheType);
        }
    }

    private void pauseCurrentFileLoadOperations(FileLoadOperation newOperation) {
        int a = 0;
        while (a < this.activeFileLoadOperation.size()) {
            FileLoadOperation operation = (FileLoadOperation) this.activeFileLoadOperation.get(a);
            if (operation != newOperation) {
                this.activeFileLoadOperation.remove(operation);
                a--;
                operation.pause();
                int datacenterId = operation.getDatacenterId();
                getLoadOperationQueue(datacenterId).add(0, operation);
                if (operation.wasStarted()) {
                    this.currentLoadOperationsCount.put(datacenterId, this.currentLoadOperationsCount.get(datacenterId) - 1);
                }
            }
            a++;
        }
    }

    private FileLoadOperation loadFileInternal(Document document, TL_webDocument webDocument, FileLocation location, String locationExt, int locationSize, boolean force, FileStreamLoadOperation stream, int streamOffset, int cacheType) {
        String fileName = null;
        if (location != null) {
            fileName = getAttachFileName(location, locationExt);
        } else if (document != null) {
            fileName = getAttachFileName(document);
        } else if (webDocument != null) {
            fileName = getAttachFileName(webDocument);
        }
        if (fileName == null || fileName.contains("-2147483648")) {
            return null;
        }
        if (!(TextUtils.isEmpty(fileName) || fileName.contains("-2147483648"))) {
            this.loadOperationPathsUI.put(fileName, Boolean.valueOf(true));
        }
        FileLoadOperation operation = (FileLoadOperation) this.loadOperationPaths.get(fileName);
        int datacenterId;
        LinkedList<FileLoadOperation> audioLoadOperationQueue;
        LinkedList<FileLoadOperation> photoLoadOperationQueue;
        LinkedList<FileLoadOperation> loadOperationQueue;
        if (operation == null) {
            File tempDir = getDirectory(4);
            File storeDir = tempDir;
            int type = 4;
            FileLoadOperation fileLoadOperation;
            if (location != null) {
                fileLoadOperation = new FileLoadOperation(location, locationExt, locationSize);
                type = 0;
            } else if (document != null) {
                fileLoadOperation = new FileLoadOperation(document);
                if (MessageObject.isVoiceDocument(document)) {
                    type = 1;
                } else if (MessageObject.isVideoDocument(document)) {
                    type = 2;
                } else {
                    type = 3;
                }
            } else if (webDocument != null) {
                fileLoadOperation = new FileLoadOperation(webDocument);
                if (MessageObject.isVoiceWebDocument(webDocument)) {
                    type = 1;
                } else if (MessageObject.isVideoWebDocument(webDocument)) {
                    type = 2;
                } else if (MessageObject.isImageWebDocument(webDocument)) {
                    type = 0;
                } else {
                    type = 3;
                }
            }
            if (cacheType == 0) {
                storeDir = getDirectory(type);
            } else if (cacheType == 2) {
                operation.setEncryptFile(true);
            }
            operation.setPaths(this.currentAccount, storeDir, tempDir);
            final String finalFileName = fileName;
            final int finalType = type;
            final Document document2 = document;
            final TL_webDocument tL_webDocument = webDocument;
            final FileLocation fileLocation = location;
            operation.setDelegate(new FileLoadOperationDelegate() {
                public void didFinishLoadingFile(FileLoadOperation operation, File finalFile) {
                    FileLoader.this.loadOperationPathsUI.remove(finalFileName);
                    if (FileLoader.this.delegate != null) {
                        FileLoader.this.delegate.fileDidLoaded(finalFileName, finalFile, finalType);
                    }
                    FileLoader.this.checkDownloadQueue(operation.getDatacenterId(), document2, tL_webDocument, fileLocation, finalFileName);
                }

                public void didFailedLoadingFile(FileLoadOperation operation, int reason) {
                    FileLoader.this.loadOperationPathsUI.remove(finalFileName);
                    FileLoader.this.checkDownloadQueue(operation.getDatacenterId(), document2, tL_webDocument, fileLocation, finalFileName);
                    if (FileLoader.this.delegate != null) {
                        FileLoader.this.delegate.fileDidFailedLoad(finalFileName, reason);
                    }
                }

                public void didChangedLoadProgress(FileLoadOperation operation, float progress) {
                    if (FileLoader.this.delegate != null) {
                        FileLoader.this.delegate.fileLoadProgressChanged(finalFileName, progress);
                    }
                }
            });
            datacenterId = operation.getDatacenterId();
            audioLoadOperationQueue = getAudioLoadOperationQueue(datacenterId);
            photoLoadOperationQueue = getPhotoLoadOperationQueue(datacenterId);
            loadOperationQueue = getLoadOperationQueue(datacenterId);
            this.loadOperationPaths.put(fileName, operation);
            int maxCount = force ? 3 : 1;
            int count;
            if (type == 1) {
                count = this.currentAudioLoadOperationsCount.get(datacenterId);
                if (streamOffset != 0 || count < maxCount) {
                    if (!operation.start(stream, streamOffset)) {
                        return operation;
                    }
                    this.currentAudioLoadOperationsCount.put(datacenterId, count + 1);
                    return operation;
                } else if (force) {
                    audioLoadOperationQueue.add(0, operation);
                    return operation;
                } else {
                    audioLoadOperationQueue.add(operation);
                    return operation;
                }
            } else if (location != null || MessageObject.isImageWebDocument(webDocument)) {
                count = this.currentPhotoLoadOperationsCount.get(datacenterId);
                if (streamOffset != 0 || count < maxCount) {
                    if (!operation.start(stream, streamOffset)) {
                        return operation;
                    }
                    this.currentPhotoLoadOperationsCount.put(datacenterId, count + 1);
                    return operation;
                } else if (force) {
                    photoLoadOperationQueue.add(0, operation);
                    return operation;
                } else {
                    photoLoadOperationQueue.add(operation);
                    return operation;
                }
            } else {
                count = this.currentLoadOperationsCount.get(datacenterId);
                if (streamOffset != 0 || count < maxCount) {
                    if (operation.start(stream, streamOffset)) {
                        this.currentLoadOperationsCount.put(datacenterId, count + 1);
                        this.activeFileLoadOperation.add(operation);
                    }
                    if (!operation.wasStarted() || stream == null) {
                        return operation;
                    }
                    pauseCurrentFileLoadOperations(operation);
                    return operation;
                } else if (force) {
                    loadOperationQueue.add(0, operation);
                    return operation;
                } else {
                    loadOperationQueue.add(operation);
                    return operation;
                }
            }
        } else if (streamOffset == 0 && !force) {
            return operation;
        } else {
            LinkedList<FileLoadOperation> downloadQueue;
            datacenterId = operation.getDatacenterId();
            audioLoadOperationQueue = getAudioLoadOperationQueue(datacenterId);
            photoLoadOperationQueue = getPhotoLoadOperationQueue(datacenterId);
            loadOperationQueue = getLoadOperationQueue(datacenterId);
            operation.setForceRequest(true);
            if (MessageObject.isVoiceDocument(document) || MessageObject.isVoiceWebDocument(webDocument)) {
                downloadQueue = audioLoadOperationQueue;
            } else if (location != null || MessageObject.isImageWebDocument(webDocument)) {
                downloadQueue = photoLoadOperationQueue;
            } else {
                downloadQueue = loadOperationQueue;
            }
            if (downloadQueue == null) {
                return operation;
            }
            int index = downloadQueue.indexOf(operation);
            if (index > 0) {
                downloadQueue.remove(index);
                if (streamOffset == 0) {
                    downloadQueue.add(0, operation);
                    return operation;
                } else if (downloadQueue == audioLoadOperationQueue) {
                    if (!operation.start(stream, streamOffset)) {
                        return operation;
                    }
                    this.currentAudioLoadOperationsCount.put(datacenterId, this.currentAudioLoadOperationsCount.get(datacenterId) + 1);
                    return operation;
                } else if (downloadQueue != photoLoadOperationQueue) {
                    if (operation.start(stream, streamOffset)) {
                        this.currentLoadOperationsCount.put(datacenterId, this.currentLoadOperationsCount.get(datacenterId) + 1);
                    }
                    if (!operation.wasStarted() || this.activeFileLoadOperation.contains(operation)) {
                        return operation;
                    }
                    if (stream != null) {
                        pauseCurrentFileLoadOperations(operation);
                    }
                    this.activeFileLoadOperation.add(operation);
                    return operation;
                } else if (!operation.start(stream, streamOffset)) {
                    return operation;
                } else {
                    this.currentPhotoLoadOperationsCount.put(datacenterId, this.currentPhotoLoadOperationsCount.get(datacenterId) + 1);
                    return operation;
                }
            }
            if (stream != null) {
                pauseCurrentFileLoadOperations(operation);
            }
            operation.start(stream, streamOffset);
            if (downloadQueue != loadOperationQueue || this.activeFileLoadOperation.contains(operation)) {
                return operation;
            }
            this.activeFileLoadOperation.add(operation);
            return operation;
        }
    }

    private void loadFile(Document document, TL_webDocument webDocument, FileLocation location, String locationExt, int locationSize, boolean force, int cacheType) {
        String fileName;
        if (location != null) {
            fileName = getAttachFileName(location, locationExt);
        } else if (document != null) {
            fileName = getAttachFileName(document);
        } else if (webDocument != null) {
            fileName = getAttachFileName(webDocument);
        } else {
            fileName = null;
        }
        if (!(TextUtils.isEmpty(fileName) || fileName.contains("-2147483648"))) {
            this.loadOperationPathsUI.put(fileName, Boolean.valueOf(true));
        }
        final Document document2 = document;
        final TL_webDocument tL_webDocument = webDocument;
        final FileLocation fileLocation = location;
        final String str = locationExt;
        final int i = locationSize;
        final boolean z = force;
        final int i2 = cacheType;
        fileLoaderQueue.postRunnable(new Runnable() {
            public void run() {
                FileLoader.this.loadFileInternal(document2, tL_webDocument, fileLocation, str, i, z, null, 0, i2);
            }
        });
    }

    protected FileLoadOperation loadStreamFile(FileStreamLoadOperation stream, Document document, int offset) {
        final CountDownLatch semaphore = new CountDownLatch(1);
        final FileLoadOperation[] result = new FileLoadOperation[1];
        final Document document2 = document;
        final FileStreamLoadOperation fileStreamLoadOperation = stream;
        final int i = offset;
        fileLoaderQueue.postRunnable(new Runnable() {
            public void run() {
                result[0] = FileLoader.this.loadFileInternal(document2, null, null, null, 0, true, fileStreamLoadOperation, i, 0);
                semaphore.countDown();
            }
        });
        try {
            semaphore.await();
        } catch (Throwable e) {
            FileLog.m3e(e);
        }
        return result[0];
    }

    private void checkDownloadQueue(int datacenterId, Document document, TL_webDocument webDocument, FileLocation location, String arg1) {
        final int i = datacenterId;
        final String str = arg1;
        final Document document2 = document;
        final TL_webDocument tL_webDocument = webDocument;
        final FileLocation fileLocation = location;
        fileLoaderQueue.postRunnable(new Runnable() {
            public void run() {
                LinkedList<FileLoadOperation> audioLoadOperationQueue = FileLoader.this.getAudioLoadOperationQueue(i);
                LinkedList<FileLoadOperation> photoLoadOperationQueue = FileLoader.this.getPhotoLoadOperationQueue(i);
                LinkedList<FileLoadOperation> loadOperationQueue = FileLoader.this.getLoadOperationQueue(i);
                FileLoadOperation operation = (FileLoadOperation) FileLoader.this.loadOperationPaths.remove(str);
                int count;
                int maxCount;
                if (MessageObject.isVoiceDocument(document2) || MessageObject.isVoiceWebDocument(tL_webDocument)) {
                    count = FileLoader.this.currentAudioLoadOperationsCount.get(i);
                    if (operation != null) {
                        if (operation.wasStarted()) {
                            count--;
                            FileLoader.this.currentAudioLoadOperationsCount.put(i, count);
                        } else {
                            audioLoadOperationQueue.remove(operation);
                        }
                    }
                    while (!audioLoadOperationQueue.isEmpty()) {
                        if (((FileLoadOperation) audioLoadOperationQueue.get(0)).isForceRequest()) {
                            maxCount = 3;
                        } else {
                            maxCount = 1;
                        }
                        if (count < maxCount) {
                            operation = (FileLoadOperation) audioLoadOperationQueue.poll();
                            if (operation != null && operation.start()) {
                                count++;
                                FileLoader.this.currentAudioLoadOperationsCount.put(i, count);
                            }
                        } else {
                            return;
                        }
                    }
                } else if (fileLocation != null || MessageObject.isImageWebDocument(tL_webDocument)) {
                    count = FileLoader.this.currentPhotoLoadOperationsCount.get(i);
                    if (operation != null) {
                        if (operation.wasStarted()) {
                            count--;
                            FileLoader.this.currentPhotoLoadOperationsCount.put(i, count);
                        } else {
                            photoLoadOperationQueue.remove(operation);
                        }
                    }
                    while (!photoLoadOperationQueue.isEmpty()) {
                        if (((FileLoadOperation) photoLoadOperationQueue.get(0)).isForceRequest()) {
                            maxCount = 3;
                        } else {
                            maxCount = 1;
                        }
                        if (count < maxCount) {
                            operation = (FileLoadOperation) photoLoadOperationQueue.poll();
                            if (operation != null && operation.start()) {
                                count++;
                                FileLoader.this.currentPhotoLoadOperationsCount.put(i, count);
                            }
                        } else {
                            return;
                        }
                    }
                } else {
                    count = FileLoader.this.currentLoadOperationsCount.get(i);
                    if (operation != null) {
                        if (operation.wasStarted()) {
                            count--;
                            FileLoader.this.currentLoadOperationsCount.put(i, count);
                        } else {
                            loadOperationQueue.remove(operation);
                        }
                        FileLoader.this.activeFileLoadOperation.remove(operation);
                    }
                    while (!loadOperationQueue.isEmpty()) {
                        if (((FileLoadOperation) loadOperationQueue.get(0)).isForceRequest()) {
                            maxCount = 3;
                        } else {
                            maxCount = 1;
                        }
                        if (count < maxCount) {
                            operation = (FileLoadOperation) loadOperationQueue.poll();
                            if (operation != null && operation.start()) {
                                count++;
                                FileLoader.this.currentLoadOperationsCount.put(i, count);
                                if (!FileLoader.this.activeFileLoadOperation.contains(operation)) {
                                    FileLoader.this.activeFileLoadOperation.add(operation);
                                }
                            }
                        } else {
                            return;
                        }
                    }
                }
            }
        });
    }

    public void setDelegate(FileLoaderDelegate delegate) {
        this.delegate = delegate;
    }

    public static String getMessageFileName(Message message) {
        if (message == null) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
        ArrayList<PhotoSize> sizes;
        PhotoSize sizeFull;
        if (message instanceof TL_messageService) {
            if (message.action.photo != null) {
                sizes = message.action.photo.sizes;
                if (sizes.size() > 0) {
                    sizeFull = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                    if (sizeFull != null) {
                        return getAttachFileName(sizeFull);
                    }
                }
            }
        } else if (message.media instanceof TL_messageMediaDocument) {
            return getAttachFileName(message.media.document);
        } else {
            if (message.media instanceof TL_messageMediaPhoto) {
                sizes = message.media.photo.sizes;
                if (sizes.size() > 0) {
                    sizeFull = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                    if (sizeFull != null) {
                        return getAttachFileName(sizeFull);
                    }
                }
            } else if (message.media instanceof TL_messageMediaWebPage) {
                if (message.media.webpage.document != null) {
                    return getAttachFileName(message.media.webpage.document);
                }
                if (message.media.webpage.photo != null) {
                    sizes = message.media.webpage.photo.sizes;
                    if (sizes.size() > 0) {
                        sizeFull = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                        if (sizeFull != null) {
                            return getAttachFileName(sizeFull);
                        }
                    }
                } else if (message.media instanceof TL_messageMediaInvoice) {
                    return getAttachFileName(((TL_messageMediaInvoice) message.media).photo);
                }
            } else if (message.media instanceof TL_messageMediaInvoice) {
                WebDocument document = ((TL_messageMediaInvoice) message.media).photo;
                if (document != null) {
                    return Utilities.MD5(document.url) + "." + ImageLoader.getHttpUrlExtension(document.url, getExtensionByMime(document.mime_type));
                }
            }
        }
        return TtmlNode.ANONYMOUS_REGION_ID;
    }

    public static File getPathToMessage(Message message) {
        boolean z = false;
        boolean z2 = true;
        if (message == null) {
            return new File(TtmlNode.ANONYMOUS_REGION_ID);
        }
        ArrayList<PhotoSize> sizes;
        PhotoSize sizeFull;
        if (message instanceof TL_messageService) {
            if (message.action.photo != null) {
                sizes = message.action.photo.sizes;
                if (sizes.size() > 0) {
                    sizeFull = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                    if (sizeFull != null) {
                        return getPathToAttach(sizeFull);
                    }
                }
            }
        } else if (message.media instanceof TL_messageMediaDocument) {
            TLObject tLObject = message.media.document;
            if (message.media.ttl_seconds != 0) {
                z = true;
            }
            return getPathToAttach(tLObject, z);
        } else if (message.media instanceof TL_messageMediaPhoto) {
            sizes = message.media.photo.sizes;
            if (sizes.size() > 0) {
                sizeFull = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                if (sizeFull != null) {
                    if (message.media.ttl_seconds == 0) {
                        z2 = false;
                    }
                    return getPathToAttach(sizeFull, z2);
                }
            }
        } else if (message.media instanceof TL_messageMediaWebPage) {
            if (message.media.webpage.document != null) {
                return getPathToAttach(message.media.webpage.document);
            }
            if (message.media.webpage.photo != null) {
                sizes = message.media.webpage.photo.sizes;
                if (sizes.size() > 0) {
                    sizeFull = getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize());
                    if (sizeFull != null) {
                        return getPathToAttach(sizeFull);
                    }
                }
            }
        } else if (message.media instanceof TL_messageMediaInvoice) {
            return getPathToAttach(((TL_messageMediaInvoice) message.media).photo, true);
        }
        return new File(TtmlNode.ANONYMOUS_REGION_ID);
    }

    public static File getPathToAttach(TLObject attach) {
        return getPathToAttach(attach, null, false);
    }

    public static File getPathToAttach(TLObject attach, boolean forceCache) {
        return getPathToAttach(attach, null, forceCache);
    }

    public static File getPathToAttach(TLObject attach, String ext, boolean forceCache) {
        File dir = null;
        if (forceCache) {
            dir = getDirectory(4);
        } else if (attach instanceof Document) {
            Document document = (Document) attach;
            if (document.key != null) {
                dir = getDirectory(4);
            } else if (MessageObject.isVoiceDocument(document)) {
                dir = getDirectory(1);
            } else if (MessageObject.isVideoDocument(document)) {
                dir = getDirectory(2);
            } else {
                dir = getDirectory(3);
            }
        } else if (attach instanceof PhotoSize) {
            PhotoSize photoSize = (PhotoSize) attach;
            if (photoSize.location == null || photoSize.location.key != null || ((photoSize.location.volume_id == -2147483648L && photoSize.location.local_id < 0) || photoSize.size < 0)) {
                dir = getDirectory(4);
            } else {
                dir = getDirectory(0);
            }
        } else if (attach instanceof FileLocation) {
            FileLocation fileLocation = (FileLocation) attach;
            if (fileLocation.key != null || (fileLocation.volume_id == -2147483648L && fileLocation.local_id < 0)) {
                dir = getDirectory(4);
            } else {
                dir = getDirectory(0);
            }
        } else if (attach instanceof TL_webDocument) {
            TL_webDocument document2 = (TL_webDocument) attach;
            if (document2.mime_type.startsWith("image/")) {
                dir = getDirectory(0);
            } else if (document2.mime_type.startsWith("audio/")) {
                dir = getDirectory(1);
            } else if (document2.mime_type.startsWith("video/")) {
                dir = getDirectory(2);
            } else {
                dir = getDirectory(3);
            }
        }
        if (dir == null) {
            return new File(TtmlNode.ANONYMOUS_REGION_ID);
        }
        return new File(dir, getAttachFileName(attach, ext));
    }

    public static FileStreamLoadOperation getStreamLoadOperation(TransferListener<? super DataSource> listener) {
        return new FileStreamLoadOperation(listener);
    }

    public static PhotoSize getClosestPhotoSizeWithSize(ArrayList<PhotoSize> sizes, int side) {
        return getClosestPhotoSizeWithSize(sizes, side, false);
    }

    public static PhotoSize getClosestPhotoSizeWithSize(ArrayList<PhotoSize> sizes, int side, boolean byMinSide) {
        if (sizes == null || sizes.isEmpty()) {
            return null;
        }
        int lastSide = 0;
        PhotoSize closestObject = null;
        for (int a = 0; a < sizes.size(); a++) {
            PhotoSize obj = (PhotoSize) sizes.get(a);
            if (obj != null) {
                int currentSide;
                if (byMinSide) {
                    currentSide = obj.f24h >= obj.f25w ? obj.f25w : obj.f24h;
                    if (closestObject == null || ((side > 100 && closestObject.location != null && closestObject.location.dc_id == Integer.MIN_VALUE) || (obj instanceof TL_photoCachedSize) || (side > lastSide && lastSide < currentSide))) {
                        closestObject = obj;
                        lastSide = currentSide;
                    }
                } else {
                    currentSide = obj.f25w >= obj.f24h ? obj.f25w : obj.f24h;
                    if (closestObject == null || ((side > 100 && closestObject.location != null && closestObject.location.dc_id == Integer.MIN_VALUE) || (obj instanceof TL_photoCachedSize) || (currentSide <= side && lastSide < currentSide))) {
                        closestObject = obj;
                        lastSide = currentSide;
                    }
                }
            }
        }
        return closestObject;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(46) + 1);
        } catch (Exception e) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
    }

    public static String fixFileName(String fileName) {
        if (fileName != null) {
            return fileName.replaceAll("[\u0001-\u001f<>:\"/\\\\|?*]+", TtmlNode.ANONYMOUS_REGION_ID).trim();
        }
        return fileName;
    }

    public static String getDocumentFileName(Document document) {
        String fileName = null;
        if (document != null) {
            if (document.file_name != null) {
                fileName = document.file_name;
            } else {
                for (int a = 0; a < document.attributes.size(); a++) {
                    DocumentAttribute documentAttribute = (DocumentAttribute) document.attributes.get(a);
                    if (documentAttribute instanceof TL_documentAttributeFilename) {
                        fileName = documentAttribute.file_name;
                    }
                }
            }
        }
        fileName = fixFileName(fileName);
        return fileName != null ? fileName : TtmlNode.ANONYMOUS_REGION_ID;
    }

    public static String getExtensionByMime(String mime) {
        int index = mime.lastIndexOf(47);
        if (index != -1) {
            return mime.substring(index + 1);
        }
        return TtmlNode.ANONYMOUS_REGION_ID;
    }

    public static File getInternalCacheDir() {
        return ApplicationLoader.applicationContext.getCacheDir();
    }

    public static String getDocumentExtension(Document document) {
        String fileName = getDocumentFileName(document);
        int idx = fileName.lastIndexOf(46);
        String ext = null;
        if (idx != -1) {
            ext = fileName.substring(idx + 1);
        }
        if (ext == null || ext.length() == 0) {
            ext = document.mime_type;
        }
        if (ext == null) {
            ext = TtmlNode.ANONYMOUS_REGION_ID;
        }
        return ext.toUpperCase();
    }

    public static String getAttachFileName(TLObject attach) {
        return getAttachFileName(attach, null);
    }

    public static String getAttachFileName(TLObject attach, String ext) {
        Object obj = -1;
        if (attach instanceof Document) {
            Document document = (Document) attach;
            String docExt = null;
            if (null == null) {
                docExt = getDocumentFileName(document);
                if (docExt != null) {
                    int idx = docExt.lastIndexOf(46);
                    if (idx != -1) {
                        docExt = docExt.substring(idx);
                    }
                }
                docExt = TtmlNode.ANONYMOUS_REGION_ID;
            }
            if (docExt.length() <= 1) {
                if (document.mime_type != null) {
                    String str = document.mime_type;
                    switch (str.hashCode()) {
                        case 187091926:
                            if (str.equals("audio/ogg")) {
                                int i = 1;
                                break;
                            }
                            break;
                        case 1331848029:
                            if (str.equals(MimeTypes.VIDEO_MP4)) {
                                obj = null;
                                break;
                            }
                            break;
                    }
                    switch (obj) {
                        case null:
                            docExt = ".mp4";
                            break;
                        case 1:
                            docExt = ".ogg";
                            break;
                        default:
                            docExt = TtmlNode.ANONYMOUS_REGION_ID;
                            break;
                    }
                }
                docExt = TtmlNode.ANONYMOUS_REGION_ID;
            }
            if (document.version == 0) {
                if (docExt.length() > 1) {
                    return document.dc_id + "_" + document.id + docExt;
                }
                return document.dc_id + "_" + document.id;
            } else if (docExt.length() > 1) {
                return document.dc_id + "_" + document.id + "_" + document.version + docExt;
            } else {
                return document.dc_id + "_" + document.id + "_" + document.version;
            }
        } else if (attach instanceof TL_webDocument) {
            TL_webDocument document2 = (TL_webDocument) attach;
            return Utilities.MD5(document2.url) + "." + ImageLoader.getHttpUrlExtension(document2.url, getExtensionByMime(document2.mime_type));
        } else if (attach instanceof PhotoSize) {
            PhotoSize photo = (PhotoSize) attach;
            if (photo.location == null || (photo.location instanceof TL_fileLocationUnavailable)) {
                return TtmlNode.ANONYMOUS_REGION_ID;
            }
            r5 = new StringBuilder().append(photo.location.volume_id).append("_").append(photo.location.local_id).append(".");
            if (ext == null) {
                ext = "jpg";
            }
            return r5.append(ext).toString();
        } else if (!(attach instanceof FileLocation)) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        } else {
            if (attach instanceof TL_fileLocationUnavailable) {
                return TtmlNode.ANONYMOUS_REGION_ID;
            }
            FileLocation location = (FileLocation) attach;
            r5 = new StringBuilder().append(location.volume_id).append("_").append(location.local_id).append(".");
            if (ext == null) {
                ext = "jpg";
            }
            return r5.append(ext).toString();
        }
    }

    public void deleteFiles(final ArrayList<File> files, final int type) {
        if (files != null && !files.isEmpty()) {
            fileLoaderQueue.postRunnable(new Runnable() {
                public void run() {
                    for (int a = 0; a < files.size(); a++) {
                        File file = (File) files.get(a);
                        File encrypted = new File(file.getAbsolutePath() + ".enc");
                        if (encrypted.exists()) {
                            try {
                                if (!encrypted.delete()) {
                                    encrypted.deleteOnExit();
                                }
                            } catch (Throwable e) {
                                FileLog.m3e(e);
                            }
                            try {
                                File key = new File(FileLoader.getInternalCacheDir(), file.getName() + ".enc.key");
                                if (!key.delete()) {
                                    key.deleteOnExit();
                                }
                            } catch (Throwable e2) {
                                FileLog.m3e(e2);
                            }
                        } else if (file.exists()) {
                            try {
                                if (!file.delete()) {
                                    file.deleteOnExit();
                                }
                            } catch (Throwable e22) {
                                FileLog.m3e(e22);
                            }
                        }
                        try {
                            File qFile = new File(file.getParentFile(), "q_" + file.getName());
                            if (qFile.exists() && !qFile.delete()) {
                                qFile.deleteOnExit();
                            }
                        } catch (Throwable e222) {
                            FileLog.m3e(e222);
                        }
                    }
                    if (type == 2) {
                        ImageLoader.getInstance().clearMemory();
                    }
                }
            });
        }
    }
}
