package org.telegram.messenger;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import android.support.v4.widget.ExploreByTouchHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import org.telegram.TL.TLObject;
import org.telegram.TL.TLRPC.Document;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.InputFileLocation;
import org.telegram.TL.TLRPC.TL_document;
import org.telegram.TL.TLRPC.TL_documentEncrypted;
import org.telegram.TL.TLRPC.TL_error;
import org.telegram.TL.TLRPC.TL_fileEncryptedLocation;
import org.telegram.TL.TLRPC.TL_fileLocation;
import org.telegram.TL.TLRPC.TL_inputDocumentFileLocation;
import org.telegram.TL.TLRPC.TL_inputEncryptedFileLocation;
import org.telegram.TL.TLRPC.TL_inputFileLocation;
import org.telegram.TL.TLRPC.TL_inputVideoFileLocation;
import org.telegram.TL.TLRPC.TL_upload_file;
import org.telegram.TL.TLRPC.TL_upload_getFile;
import org.telegram.TL.TLRPC.TL_video;
import org.telegram.TL.TLRPC.TL_videoEncrypted;
import org.telegram.TL.TLRPC.Video;
import org.telegram.messenger.RPCRequest.RPCProgressDelegate;
import org.telegram.messenger.RPCRequest.RPCRequestDelegate;

public class FileLoadOperation {
    private File cacheFileFinal;
    private File cacheFileTemp;
    private File cacheIvTemp;
    public int datacenter_id;
    public FileLoadOperationDelegate delegate;
    private int downloadChunkSize = 32768;
    private int downloadedBytes;
    private String ext;
    private RandomAccessFile fileOutputStream;
    public String filter;
    RandomAccessFile fiv;
    private URLConnection httpConnection;
    private InputStream httpConnectionStream;
    private String httpUrl;
    public Bitmap image;
    private byte[] iv;
    private byte[] key;
    public InputFileLocation location;
    public boolean needBitmapCreate = true;
    private long requestToken = 0;
    public volatile int state = 0;
    public int totalBytesCount;

    class C03091 implements Runnable {
        C03091() {
        }

        public void run() {
            FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
        }
    }

    class C03123 implements Runnable {
        C03123() {
        }

        public void run() {
            FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
        }
    }

    class C03134 implements Runnable {
        C03134() {
        }

        public void run() {
            FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
        }
    }

    class C03156 implements Runnable {
        C03156() {
        }

        public void run() {
            FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
        }
    }

    class C03167 implements Runnable {
        C03167() {
        }

        public void run() {
            FileLoadOperation.this.startDownloadHTTPRequest();
        }
    }

    class C03178 implements Runnable {
        C03178() {
        }

        public void run() {
            try {
                FileLoadOperation.this.onFinishLoadingFile();
            } catch (Exception e) {
                FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
            }
        }
    }

    class C03189 implements Runnable {
        C03189() {
        }

        public void run() {
            FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
        }
    }

    public interface FileLoadOperationDelegate {
        void didChangedLoadProgress(FileLoadOperation fileLoadOperation, float f);

        void didFailedLoadingFile(FileLoadOperation fileLoadOperation);

        void didFinishLoadingFile(FileLoadOperation fileLoadOperation);
    }

    static /* synthetic */ int access$812(FileLoadOperation x0, int x1) {
        int i = x0.downloadedBytes + x1;
        x0.downloadedBytes = i;
        return i;
    }

    public FileLoadOperation(FileLocation fileLocation) {
        if (fileLocation instanceof TL_fileEncryptedLocation) {
            this.location = new TL_inputEncryptedFileLocation();
            this.location.id = fileLocation.volume_id;
            this.location.volume_id = fileLocation.volume_id;
            this.location.access_hash = fileLocation.secret;
            this.location.local_id = fileLocation.local_id;
            this.iv = new byte[32];
            System.arraycopy(fileLocation.iv, 0, this.iv, 0, this.iv.length);
            this.key = fileLocation.key;
            this.datacenter_id = fileLocation.dc_id;
        } else if (fileLocation instanceof TL_fileLocation) {
            this.location = new TL_inputFileLocation();
            this.location.volume_id = fileLocation.volume_id;
            this.location.secret = fileLocation.secret;
            this.location.local_id = fileLocation.local_id;
            this.datacenter_id = fileLocation.dc_id;
        }
    }

    public FileLoadOperation(Video videoLocation) {
        if (videoLocation instanceof TL_video) {
            this.location = new TL_inputVideoFileLocation();
            this.datacenter_id = videoLocation.dc_id;
            this.location.id = videoLocation.id;
            this.location.access_hash = videoLocation.access_hash;
        } else if (videoLocation instanceof TL_videoEncrypted) {
            this.location = new TL_inputEncryptedFileLocation();
            this.location.id = videoLocation.id;
            this.location.access_hash = videoLocation.access_hash;
            this.datacenter_id = videoLocation.dc_id;
            this.iv = new byte[32];
            System.arraycopy(videoLocation.iv, 0, this.iv, 0, this.iv.length);
            this.key = videoLocation.key;
        }
        this.ext = ".mp4";
    }

    public FileLoadOperation(Document documentLocation) {
        if (documentLocation instanceof TL_document) {
            this.location = new TL_inputDocumentFileLocation();
            this.datacenter_id = documentLocation.dc_id;
            this.location.id = documentLocation.id;
            this.location.access_hash = documentLocation.access_hash;
        } else if (documentLocation instanceof TL_documentEncrypted) {
            this.location = new TL_inputEncryptedFileLocation();
            this.location.id = documentLocation.id;
            this.location.access_hash = documentLocation.access_hash;
            this.datacenter_id = documentLocation.dc_id;
            this.iv = new byte[32];
            System.arraycopy(documentLocation.iv, 0, this.iv, 0, this.iv.length);
            this.key = documentLocation.key;
        }
        this.ext = documentLocation.file_name;
        if (this.ext != null) {
            int idx = this.ext.lastIndexOf(".");
            if (idx != -1) {
                this.ext = this.ext.substring(idx);
                if (this.ext.length() <= 1) {
                    this.ext = BuildConfig.FLAVOR;
                    return;
                }
                return;
            }
        }
        this.ext = BuildConfig.FLAVOR;
    }

    public FileLoadOperation(String url) {
        this.httpUrl = url;
    }

    public void start() {
        if (this.state == 0) {
            this.state = 1;
            if (this.location == null && this.httpUrl == null) {
                Utilities.stageQueue.postRunnable(new C03091());
                return;
            }
            String fileNameFinal;
            boolean ignoreCache = false;
            boolean onlyCache = false;
            boolean isLocalFile = false;
            String fileNameTemp = null;
            String fileNameIv = null;
            if (this.httpUrl != null) {
                if (this.httpUrl.startsWith("http")) {
                    fileNameFinal = Utilities.MD5(this.httpUrl);
                    fileNameTemp = fileNameFinal + "_temp.jpg";
                    fileNameFinal = fileNameFinal + ".jpg";
                } else {
                    onlyCache = true;
                    isLocalFile = true;
                    fileNameFinal = this.httpUrl;
                }
            } else if (this.location.volume_id == 0 || this.location.local_id == 0) {
                ignoreCache = true;
                this.needBitmapCreate = false;
                fileNameTemp = this.datacenter_id + "_" + this.location.id + "_temp" + this.ext;
                fileNameFinal = this.datacenter_id + "_" + this.location.id + this.ext;
                if (this.key != null) {
                    fileNameIv = this.datacenter_id + "_" + this.location.id + ".iv";
                }
            } else {
                fileNameTemp = this.location.volume_id + "_" + this.location.local_id + "_temp.jpg";
                fileNameFinal = this.location.volume_id + "_" + this.location.local_id + ".jpg";
                if (this.key != null) {
                    fileNameIv = this.location.volume_id + "_" + this.location.local_id + ".iv";
                }
                if (this.datacenter_id == ExploreByTouchHelper.INVALID_ID || this.location.volume_id == -2147483648L) {
                    onlyCache = true;
                }
            }
            if (isLocalFile) {
                this.cacheFileFinal = new File(fileNameFinal);
            } else {
                this.cacheFileFinal = new File(Utilities.getCacheDir(), fileNameFinal);
            }
            final boolean dontDelete = isLocalFile;
            boolean exist = this.cacheFileFinal.exists();
            if (exist && !ignoreCache) {
                Utilities.cacheOutQueue.postRunnable(new Runnable() {

                    class C03101 implements Runnable {
                        C03101() {
                        }

                        public void run() {
                            FileLoadOperation.this.delegate.didFinishLoadingFile(FileLoadOperation.this);
                        }
                    }

                    public void run() {
                        int delay = 20;
                        try {
                            if (FileLoader.Instance.runtimeHack != null) {
                                delay = 60;
                            }
                            if (FileLoader.lastCacheOutTime != 0 && FileLoader.lastCacheOutTime > System.currentTimeMillis() - ((long) delay)) {
                                Thread.sleep((long) delay);
                            }
                            FileLoader.lastCacheOutTime = System.currentTimeMillis();
                            if (FileLoadOperation.this.state == 1) {
                                if (FileLoadOperation.this.needBitmapCreate) {
                                    FileInputStream is = new FileInputStream(FileLoadOperation.this.cacheFileFinal);
                                    Options opts = new Options();
                                    float w_filter = 0.0f;
                                    if (FileLoadOperation.this.filter != null) {
                                        String[] args = FileLoadOperation.this.filter.split("_");
                                        w_filter = Float.parseFloat(args[0]) * Utilities.density;
                                        float h_filter = Float.parseFloat(args[1]) * Utilities.density;
                                        opts.inJustDecodeBounds = true;
                                        BitmapFactory.decodeFile(FileLoadOperation.this.cacheFileFinal.getAbsolutePath(), opts);
                                        float scaleFactor = Math.max(((float) opts.outWidth) / w_filter, ((float) opts.outHeight) / h_filter);
                                        if (scaleFactor < 1.0f) {
                                            scaleFactor = 1.0f;
                                        }
                                        opts.inJustDecodeBounds = false;
                                        opts.inSampleSize = (int) scaleFactor;
                                    }
                                    opts.inPreferredConfig = Config.RGB_565;
                                    opts.inDither = false;
                                    FileLoadOperation.this.image = BitmapFactory.decodeStream(is, null, opts);
                                    is.close();
                                    if (FileLoadOperation.this.image != null) {
                                        if (!(FileLoadOperation.this.filter == null || FileLoadOperation.this.image == null)) {
                                            float bitmapW = (float) FileLoadOperation.this.image.getWidth();
                                            float bitmapH = (float) FileLoadOperation.this.image.getHeight();
                                            if (bitmapW != w_filter && bitmapW > w_filter) {
                                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(FileLoadOperation.this.image, (int) w_filter, (int) (bitmapH / (bitmapW / w_filter)), true);
                                                if (FileLoadOperation.this.image != scaledBitmap) {
                                                    if (VERSION.SDK_INT < 11) {
                                                        FileLoadOperation.this.image.recycle();
                                                    }
                                                    FileLoadOperation.this.image = scaledBitmap;
                                                }
                                            }
                                        }
                                        if (FileLoader.Instance.runtimeHack != null) {
                                            FileLoader.Instance.runtimeHack.trackFree((long) (FileLoadOperation.this.image.getRowBytes() * FileLoadOperation.this.image.getHeight()));
                                        }
                                    } else if (!dontDelete) {
                                        FileLoadOperation.this.cacheFileFinal.delete();
                                    }
                                }
                                Utilities.stageQueue.postRunnable(new C03101());
                            }
                        } catch (Exception e) {
                            if (!dontDelete) {
                                FileLoadOperation.this.cacheFileFinal.delete();
                            }
                            FileLog.m799e("tmessages", e);
                        }
                    }
                });
            } else if (onlyCache) {
                cleanup();
                Utilities.stageQueue.postRunnable(new C03123());
            } else {
                this.cacheFileTemp = new File(Utilities.getCacheDir(), fileNameTemp);
                if (this.cacheFileTemp.exists()) {
                    this.downloadedBytes = (int) this.cacheFileTemp.length();
                    this.downloadedBytes = (this.downloadedBytes / 1024) * 1024;
                }
                if (fileNameIv != null) {
                    this.cacheIvTemp = new File(Utilities.getCacheDir(), fileNameIv);
                    try {
                        this.fiv = new RandomAccessFile(this.cacheIvTemp, "rws");
                        long len = this.cacheIvTemp.length();
                        if (len <= 0 || len % 32 != 0) {
                            this.downloadedBytes = 0;
                        } else {
                            this.fiv.read(this.iv, 0, 32);
                        }
                    } catch (Exception e) {
                        FileLog.m799e("tmessages", e);
                        this.downloadedBytes = 0;
                    }
                }
                if (exist) {
                    this.cacheFileFinal.delete();
                }
                try {
                    this.fileOutputStream = new RandomAccessFile(this.cacheFileTemp, "rws");
                    if (this.downloadedBytes != 0) {
                        this.fileOutputStream.seek((long) this.downloadedBytes);
                    }
                } catch (Exception e2) {
                    FileLog.m799e("tmessages", e2);
                }
                if (this.fileOutputStream == null) {
                    cleanup();
                    Utilities.stageQueue.postRunnable(new C03134());
                } else if (this.httpUrl != null) {
                    startDownloadHTTPRequest();
                } else {
                    startDownloadRequest();
                }
            }
        }
    }

    public void cancel() {
        if (this.state == 1) {
            this.state = 2;
            cleanup();
            if (this.httpUrl == null && this.requestToken != 0) {
                ConnectionsManager.Instance.cancelRpc(this.requestToken, true);
            }
            this.delegate.didFailedLoadingFile(this);
        }
    }

    private void cleanup() {
        if (this.httpUrl != null) {
            try {
                if (this.httpConnectionStream != null) {
                    this.httpConnectionStream.close();
                }
                this.httpConnection = null;
                this.httpConnectionStream = null;
                return;
            } catch (Exception e) {
                FileLog.m799e("tmessages", e);
                return;
            }
        }
        try {
            if (this.fileOutputStream != null) {
                this.fileOutputStream.close();
                this.fileOutputStream = null;
            }
        } catch (Exception e2) {
            FileLog.m799e("tmessages", e2);
        }
        try {
            if (this.fiv != null) {
                this.fiv.close();
                this.fiv = null;
            }
        } catch (Exception e22) {
            FileLog.m799e("tmessages", e22);
        }
    }

    private void onFinishLoadingFile() throws Exception {
        if (this.state == 1) {
            this.state = 3;
            cleanup();
            if (this.cacheIvTemp != null) {
                this.cacheIvTemp.delete();
            }
            final boolean renamed = this.cacheFileTemp.renameTo(this.cacheFileFinal);
            if (this.needBitmapCreate) {
                Utilities.cacheOutQueue.postRunnable(new Runnable() {
                    public void run() {
                        int delay = 20;
                        if (FileLoader.Instance.runtimeHack != null) {
                            delay = 60;
                        }
                        if (FileLoader.lastCacheOutTime != 0 && FileLoader.lastCacheOutTime > System.currentTimeMillis() - ((long) delay)) {
                            try {
                                Thread.sleep((long) delay);
                            } catch (Exception e) {
                                FileLog.m799e("tmessages", e);
                            }
                        }
                        Options opts = new Options();
                        float w_filter = 0.0f;
                        if (FileLoadOperation.this.filter != null) {
                            String[] args = FileLoadOperation.this.filter.split("_");
                            w_filter = Float.parseFloat(args[0]) * Utilities.density;
                            float h_filter = Float.parseFloat(args[1]) * Utilities.density;
                            opts.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(FileLoadOperation.this.cacheFileFinal.getAbsolutePath(), opts);
                            float scaleFactor = Math.max(((float) opts.outWidth) / w_filter, ((float) opts.outHeight) / h_filter);
                            if (scaleFactor < 1.0f) {
                                scaleFactor = 1.0f;
                            }
                            opts.inJustDecodeBounds = false;
                            opts.inSampleSize = (int) scaleFactor;
                        }
                        opts.inPreferredConfig = Config.RGB_565;
                        opts.inDither = false;
                        try {
                            if (renamed) {
                                FileLoadOperation.this.image = BitmapFactory.decodeStream(new FileInputStream(FileLoadOperation.this.cacheFileFinal), null, opts);
                            } else {
                                FileLoadOperation.this.image = BitmapFactory.decodeStream(new FileInputStream(FileLoadOperation.this.cacheFileTemp), null, opts);
                            }
                            if (!(FileLoadOperation.this.filter == null || FileLoadOperation.this.image == null)) {
                                float bitmapW = (float) FileLoadOperation.this.image.getWidth();
                                float bitmapH = (float) FileLoadOperation.this.image.getHeight();
                                if (bitmapW != w_filter && bitmapW > w_filter) {
                                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(FileLoadOperation.this.image, (int) w_filter, (int) (bitmapH / (bitmapW / w_filter)), true);
                                    if (FileLoadOperation.this.image != scaledBitmap) {
                                        if (VERSION.SDK_INT < 11) {
                                            FileLoadOperation.this.image.recycle();
                                        }
                                        FileLoadOperation.this.image = scaledBitmap;
                                    }
                                }
                            }
                            if (FileLoader.Instance.runtimeHack != null) {
                                FileLoader.Instance.runtimeHack.trackFree((long) (FileLoadOperation.this.image.getRowBytes() * FileLoadOperation.this.image.getHeight()));
                            }
                        } catch (Exception e2) {
                            FileLog.m799e("tmessages", e2);
                        }
                        FileLoadOperation.this.delegate.didFinishLoadingFile(FileLoadOperation.this);
                    }
                });
            } else {
                this.delegate.didFinishLoadingFile(this);
            }
        }
    }

    private void startDownloadHTTPRequest() {
        if (this.state == 1) {
            if (this.httpConnection == null) {
                try {
                    this.httpConnection = new URL(this.httpUrl).openConnection();
                    this.httpConnection.setConnectTimeout(5000);
                    this.httpConnection.setReadTimeout(5000);
                    this.httpConnection.connect();
                    this.httpConnectionStream = this.httpConnection.getInputStream();
                } catch (Exception e) {
                    FileLog.m799e("tmessages", e);
                    cleanup();
                    Utilities.stageQueue.postRunnable(new C03156());
                    return;
                }
            }
            try {
                byte[] data = new byte[2048];
                int readed = this.httpConnectionStream.read(data);
                if (readed > 0) {
                    this.fileOutputStream.write(data, 0, readed);
                    Utilities.imageLoadQueue.postRunnable(new C03167());
                } else if (readed == -1) {
                    cleanup();
                    Utilities.stageQueue.postRunnable(new C03178());
                } else {
                    cleanup();
                    Utilities.stageQueue.postRunnable(new C03189());
                }
            } catch (Exception e2) {
                cleanup();
                FileLog.m799e("tmessages", e2);
                Utilities.stageQueue.postRunnable(new Runnable() {
                    public void run() {
                        FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
                    }
                });
            }
        }
    }

    private void startDownloadRequest() {
        if (this.state == 1) {
            TL_upload_getFile req = new TL_upload_getFile();
            req.location = this.location;
            if (this.totalBytesCount == -1) {
                req.offset = 0;
                req.limit = 0;
            } else {
                req.offset = this.downloadedBytes;
                req.limit = this.downloadChunkSize;
            }
            this.requestToken = ConnectionsManager.Instance.performRpc(req, new RPCRequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    FileLoadOperation.this.requestToken = 0;
                    if (error == null) {
                        TL_upload_file res = (TL_upload_file) response;
                        try {
                            if (res.bytes.length == 0) {
                                FileLoadOperation.this.onFinishLoadingFile();
                                return;
                            }
                            if (FileLoadOperation.this.key != null) {
                                res.bytes = Utilities.aesIgeEncryption(res.bytes, FileLoadOperation.this.key, FileLoadOperation.this.iv, false, true);
                            }
                            if (FileLoadOperation.this.fileOutputStream != null) {
                                FileLoadOperation.this.fileOutputStream.write(res.bytes);
                            }
                            if (FileLoadOperation.this.fiv != null) {
                                FileLoadOperation.this.fiv.seek(0);
                                FileLoadOperation.this.fiv.write(FileLoadOperation.this.iv);
                            }
                            FileLoadOperation.access$812(FileLoadOperation.this, res.bytes.length);
                            res.bytes = null;
                            if (FileLoadOperation.this.totalBytesCount > 0) {
                                FileLoadOperation.this.delegate.didChangedLoadProgress(FileLoadOperation.this, Math.min(1.0f, ((float) FileLoadOperation.this.downloadedBytes) / ((float) FileLoadOperation.this.totalBytesCount)));
                            }
                            if (FileLoadOperation.this.downloadedBytes % FileLoadOperation.this.downloadChunkSize == 0 || (FileLoadOperation.this.totalBytesCount > 0 && FileLoadOperation.this.totalBytesCount != FileLoadOperation.this.downloadedBytes)) {
                                FileLoadOperation.this.startDownloadRequest();
                            } else {
                                FileLoadOperation.this.onFinishLoadingFile();
                            }
                        } catch (Exception e) {
                            FileLoadOperation.this.cleanup();
                            FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
                            FileLog.m799e("tmessages", e);
                        }
                    } else if (error.text.contains("FILE_MIGRATE_")) {
                        Integer val;
                        Scanner scanner = new Scanner(error.text.replace("FILE_MIGRATE_", BuildConfig.FLAVOR));
                        scanner.useDelimiter(BuildConfig.FLAVOR);
                        try {
                            val = Integer.valueOf(scanner.nextInt());
                        } catch (Exception e2) {
                            val = null;
                        }
                        if (val == null) {
                            FileLoadOperation.this.cleanup();
                            FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
                            return;
                        }
                        FileLoadOperation.this.datacenter_id = val.intValue();
                        FileLoadOperation.this.startDownloadRequest();
                    } else if (!error.text.contains("OFFSET_INVALID")) {
                        FileLoadOperation.this.cleanup();
                        FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
                    } else if (FileLoadOperation.this.downloadedBytes % FileLoadOperation.this.downloadChunkSize == 0) {
                        try {
                            FileLoadOperation.this.onFinishLoadingFile();
                        } catch (Exception e3) {
                            FileLog.m799e("tmessages", e3);
                            FileLoadOperation.this.cleanup();
                            FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
                        }
                    } else {
                        FileLoadOperation.this.cleanup();
                        FileLoadOperation.this.delegate.didFailedLoadingFile(FileLoadOperation.this);
                    }
                }
            }, new RPCProgressDelegate() {
                public void progress(int length, int progress) {
                    if (FileLoadOperation.this.totalBytesCount > 0) {
                        FileLoadOperation.this.delegate.didChangedLoadProgress(FileLoadOperation.this, Math.min(1.0f, ((float) (FileLoadOperation.this.downloadedBytes + progress)) / ((float) FileLoadOperation.this.totalBytesCount)));
                    } else if (FileLoadOperation.this.totalBytesCount == -1) {
                        FileLoadOperation.this.delegate.didChangedLoadProgress(FileLoadOperation.this, Math.min(1.0f, ((float) progress) / ((float) length)));
                    }
                }
            }, null, true, RPCRequest.RPCRequestClassDownloadMedia, this.datacenter_id);
        }
    }
}
