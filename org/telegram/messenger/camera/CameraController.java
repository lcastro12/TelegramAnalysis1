package org.telegram.messenger.camera;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.media.ThumbnailUtils;
import android.os.Build;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;

public class CameraController implements OnInfoListener {
    private static final int CORE_POOL_SIZE = 1;
    private static volatile CameraController Instance = null;
    private static final int KEEP_ALIVE_SECONDS = 60;
    private static final int MAX_POOL_SIZE = 1;
    protected ArrayList<String> availableFlashModes = new ArrayList();
    protected ArrayList<CameraInfo> cameraInfos = null;
    private boolean cameraInitied;
    private boolean loadingCameras;
    private VideoTakeCallback onVideoTakeCallback;
    private String recordedFile;
    private MediaRecorder recorder;
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());

    class C05871 implements Runnable {

        class C05831 implements Comparator<Size> {
            C05831() {
            }

            public int compare(Size o1, Size o2) {
                if (o1.mWidth < o2.mWidth) {
                    return 1;
                }
                if (o1.mWidth > o2.mWidth) {
                    return -1;
                }
                if (o1.mHeight < o2.mHeight) {
                    return 1;
                }
                if (o1.mHeight > o2.mHeight) {
                    return -1;
                }
                return 0;
            }
        }

        class C05842 implements Runnable {
            C05842() {
            }

            public void run() {
                CameraController.this.loadingCameras = false;
                CameraController.this.cameraInitied = true;
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.cameraInitied, new Object[0]);
            }
        }

        class C05853 implements Runnable {
            C05853() {
            }

            public void run() {
                CameraController.this.loadingCameras = false;
                CameraController.this.cameraInitied = false;
            }
        }

        C05871() {
        }

        public void run() {
            try {
                if (CameraController.this.cameraInfos == null) {
                    int count = Camera.getNumberOfCameras();
                    ArrayList<CameraInfo> result = new ArrayList();
                    CameraInfo info = new CameraInfo();
                    for (int cameraId = 0; cameraId < count; cameraId++) {
                        int a;
                        Size size;
                        Camera.getCameraInfo(cameraId, info);
                        CameraInfo cameraInfo = new CameraInfo(cameraId, info);
                        Camera camera = Camera.open(cameraInfo.getCameraId());
                        Parameters params = camera.getParameters();
                        List<Size> list = params.getSupportedPreviewSizes();
                        for (a = 0; a < list.size(); a++) {
                            size = (Size) list.get(a);
                            if ((size.width != 1280 || size.height == 720) && size.height < 2160 && size.width < 2160) {
                                cameraInfo.previewSizes.add(new Size(size.width, size.height));
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.m0d("preview size = " + size.width + " " + size.height);
                                }
                            }
                        }
                        list = params.getSupportedPictureSizes();
                        for (a = 0; a < list.size(); a++) {
                            size = (Size) list.get(a);
                            if ((size.width != 1280 || size.height == 720) && !("samsung".equals(Build.MANUFACTURER) && "jflteuc".equals(Build.PRODUCT) && size.width >= 2048)) {
                                cameraInfo.pictureSizes.add(new Size(size.width, size.height));
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.m0d("picture size = " + size.width + " " + size.height);
                                }
                            }
                        }
                        camera.release();
                        result.add(cameraInfo);
                        Comparator<Size> comparator = new C05831();
                        Collections.sort(cameraInfo.previewSizes, comparator);
                        Collections.sort(cameraInfo.pictureSizes, comparator);
                    }
                    CameraController.this.cameraInfos = result;
                }
                AndroidUtilities.runOnUIThread(new C05842());
            } catch (Throwable e) {
                AndroidUtilities.runOnUIThread(new C05853());
                FileLog.m3e(e);
            }
        }
    }

    class C05882 implements Runnable {
        C05882() {
        }

        public void run() {
            if (CameraController.this.cameraInfos != null && !CameraController.this.cameraInfos.isEmpty()) {
                for (int a = 0; a < CameraController.this.cameraInfos.size(); a++) {
                    CameraInfo info = (CameraInfo) CameraController.this.cameraInfos.get(a);
                    if (info.camera != null) {
                        info.camera.stopPreview();
                        info.camera.setPreviewCallbackWithBuffer(null);
                        info.camera.release();
                        info.camera = null;
                    }
                }
                CameraController.this.cameraInfos = null;
            }
        }
    }

    static class CompareSizesByArea implements Comparator<Size> {
        CompareSizesByArea() {
        }

        public int compare(Size lhs, Size rhs) {
            return Long.signum((((long) lhs.getWidth()) * ((long) lhs.getHeight())) - (((long) rhs.getWidth()) * ((long) rhs.getHeight())));
        }
    }

    public interface VideoTakeCallback {
        void onFinishVideoRecording(String str, long j);
    }

    public static CameraController getInstance() {
        CameraController localInstance = Instance;
        if (localInstance == null) {
            synchronized (CameraController.class) {
                try {
                    localInstance = Instance;
                    if (localInstance == null) {
                        CameraController localInstance2 = new CameraController();
                        try {
                            Instance = localInstance2;
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

    public void initCamera() {
        if (!this.loadingCameras && !this.cameraInitied) {
            this.loadingCameras = true;
            this.threadPool.execute(new C05871());
        }
    }

    public boolean isCameraInitied() {
        return (!this.cameraInitied || this.cameraInfos == null || this.cameraInfos.isEmpty()) ? false : true;
    }

    public void cleanup() {
        this.threadPool.execute(new C05882());
    }

    public void close(final CameraSession session, final CountDownLatch countDownLatch, final Runnable beforeDestroyRunnable) {
        session.destroy();
        this.threadPool.execute(new Runnable() {
            public void run() {
                if (beforeDestroyRunnable != null) {
                    beforeDestroyRunnable.run();
                }
                if (session.cameraInfo.camera != null) {
                    try {
                        session.cameraInfo.camera.stopPreview();
                        session.cameraInfo.camera.setPreviewCallbackWithBuffer(null);
                    } catch (Throwable e) {
                        FileLog.m3e(e);
                    }
                    try {
                        session.cameraInfo.camera.release();
                    } catch (Throwable e2) {
                        FileLog.m3e(e2);
                    }
                    session.cameraInfo.camera = null;
                    if (countDownLatch != null) {
                        countDownLatch.countDown();
                    }
                }
            }
        });
        if (countDownLatch != null) {
            try {
                countDownLatch.await();
            } catch (Throwable e) {
                FileLog.m3e(e);
            }
        }
    }

    public ArrayList<CameraInfo> getCameras() {
        return this.cameraInfos;
    }

    private static int getOrientation(byte[] jpeg) {
        boolean littleEndian = true;
        if (jpeg == null) {
            return 0;
        }
        int offset = 0;
        int length = 0;
        while (offset + 3 < jpeg.length) {
            int offset2 = offset + 1;
            if ((jpeg[offset] & 255) != 255) {
                offset = offset2;
                break;
            }
            int marker = jpeg[offset2] & 255;
            if (marker != 255) {
                offset = offset2 + 1;
                if (!(marker == 216 || marker == 1)) {
                    if (marker != 217 && marker != 218) {
                        length = pack(jpeg, offset, 2, false);
                        if (length >= 2 && offset + length <= jpeg.length) {
                            if (marker == 225 && length >= 8 && pack(jpeg, offset + 2, 4, false) == 1165519206 && pack(jpeg, offset + 6, 2, false) == 0) {
                                offset += 8;
                                length -= 8;
                                break;
                            }
                            offset += length;
                            length = 0;
                        } else {
                            return 0;
                        }
                    }
                    break;
                }
            }
            offset = offset2;
        }
        if (length <= 8) {
            return 0;
        }
        int tag = pack(jpeg, offset, 4, false);
        if (tag != 1229531648 && tag != 1296891946) {
            return 0;
        }
        if (tag != 1229531648) {
            littleEndian = false;
        }
        int count = pack(jpeg, offset + 4, 4, littleEndian) + 2;
        if (count < 10 || count > length) {
            return 0;
        }
        offset += count;
        length -= count;
        int count2 = pack(jpeg, offset - 2, 2, littleEndian);
        while (true) {
            count = count2 - 1;
            if (count2 <= 0 || length < 12) {
                return 0;
            }
            if (pack(jpeg, offset, 2, littleEndian) == 274) {
                break;
            }
            offset += 12;
            length -= 12;
            count2 = count;
        }
        switch (pack(jpeg, offset + 8, 2, littleEndian)) {
            case 1:
                return 0;
            case 3:
                return 180;
            case 6:
                return 90;
            case 8:
                return 270;
            default:
                return 0;
        }
    }

    private static int pack(byte[] bytes, int offset, int length, boolean littleEndian) {
        int step = 1;
        if (littleEndian) {
            offset += length - 1;
            step = -1;
        }
        int value = 0;
        int length2 = length;
        while (true) {
            length = length2 - 1;
            if (length2 <= 0) {
                return value;
            }
            value = (value << 8) | (bytes[offset] & 255);
            offset += step;
            length2 = length;
        }
    }

    public boolean takePicture(final File path, CameraSession session, final Runnable callback) {
        if (session == null) {
            return false;
        }
        final CameraInfo info = session.cameraInfo;
        try {
            info.camera.takePicture(null, null, new PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    Bitmap bitmap = null;
                    int size = (int) (((float) AndroidUtilities.getPhotoSize()) / AndroidUtilities.density);
                    String key = String.format(Locale.US, "%s@%d_%d", new Object[]{Utilities.MD5(path.getAbsolutePath()), Integer.valueOf(size), Integer.valueOf(size)});
                    try {
                        Options options = new Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeByteArray(data, 0, data.length, options);
                        float scaleFactor = Math.max(((float) options.outWidth) / ((float) AndroidUtilities.getPhotoSize()), ((float) options.outHeight) / ((float) AndroidUtilities.getPhotoSize()));
                        if (scaleFactor < 1.0f) {
                            scaleFactor = 1.0f;
                        }
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = (int) scaleFactor;
                        options.inPurgeable = true;
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                    } catch (Throwable e) {
                        FileLog.m3e(e);
                    }
                    try {
                        FileOutputStream outputStream;
                        if (info.frontCamera != 0) {
                            try {
                                Matrix matrix = new Matrix();
                                matrix.setRotate((float) CameraController.getOrientation(data));
                                matrix.postScale(-1.0f, 1.0f);
                                Bitmap scaled = Bitmaps.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                                if (scaled != bitmap) {
                                    bitmap.recycle();
                                }
                                outputStream = new FileOutputStream(path);
                                scaled.compress(CompressFormat.JPEG, 80, outputStream);
                                outputStream.flush();
                                outputStream.getFD().sync();
                                outputStream.close();
                                if (scaled != null) {
                                    ImageLoader.getInstance().putImageToCache(new BitmapDrawable(scaled), key);
                                }
                                if (callback != null) {
                                    callback.run();
                                    return;
                                }
                                return;
                            } catch (Throwable e2) {
                                FileLog.m3e(e2);
                            }
                        }
                        outputStream = new FileOutputStream(path);
                        outputStream.write(data);
                        outputStream.flush();
                        outputStream.getFD().sync();
                        outputStream.close();
                        if (bitmap != null) {
                            ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmap), key);
                        }
                    } catch (Throwable e22) {
                        FileLog.m3e(e22);
                    }
                    if (callback != null) {
                        callback.run();
                    }
                }
            });
            return true;
        } catch (Throwable e) {
            FileLog.m3e(e);
            return false;
        }
    }

    public void startPreview(final CameraSession session) {
        if (session != null) {
            this.threadPool.execute(new Runnable() {
                @SuppressLint({"NewApi"})
                public void run() {
                    Camera camera = session.cameraInfo.camera;
                    if (camera == null) {
                        try {
                            CameraInfo cameraInfo = session.cameraInfo;
                            Camera camera2 = Camera.open(session.cameraInfo.cameraId);
                            cameraInfo.camera = camera2;
                            camera = camera2;
                        } catch (Throwable e) {
                            session.cameraInfo.camera = null;
                            if (camera != null) {
                                camera.release();
                            }
                            FileLog.m3e(e);
                            return;
                        }
                    }
                    camera.startPreview();
                }
            });
        }
    }

    public void stopPreview(final CameraSession session) {
        if (session != null) {
            this.threadPool.execute(new Runnable() {
                @SuppressLint({"NewApi"})
                public void run() {
                    Camera camera = session.cameraInfo.camera;
                    if (camera == null) {
                        try {
                            CameraInfo cameraInfo = session.cameraInfo;
                            Camera camera2 = Camera.open(session.cameraInfo.cameraId);
                            cameraInfo.camera = camera2;
                            camera = camera2;
                        } catch (Throwable e) {
                            session.cameraInfo.camera = null;
                            if (camera != null) {
                                camera.release();
                            }
                            FileLog.m3e(e);
                            return;
                        }
                    }
                    camera.stopPreview();
                }
            });
        }
    }

    public void openRound(CameraSession session, SurfaceTexture texture, Runnable callback, Runnable configureCallback) {
        if (session != null && texture != null) {
            final CameraSession cameraSession = session;
            final Runnable runnable = configureCallback;
            final SurfaceTexture surfaceTexture = texture;
            final Runnable runnable2 = callback;
            this.threadPool.execute(new Runnable() {
                @SuppressLint({"NewApi"})
                public void run() {
                    Camera camera = cameraSession.cameraInfo.camera;
                    try {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.m0d("start creating round camera session");
                        }
                        if (camera == null) {
                            CameraInfo cameraInfo = cameraSession.cameraInfo;
                            Camera camera2 = Camera.open(cameraSession.cameraInfo.cameraId);
                            cameraInfo.camera = camera2;
                            camera = camera2;
                        }
                        Parameters params = camera.getParameters();
                        cameraSession.configureRoundCamera();
                        if (runnable != null) {
                            runnable.run();
                        }
                        camera.setPreviewTexture(surfaceTexture);
                        camera.startPreview();
                        if (runnable2 != null) {
                            AndroidUtilities.runOnUIThread(runnable2);
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.m0d("round camera session created");
                        }
                    } catch (Throwable e) {
                        cameraSession.cameraInfo.camera = null;
                        if (camera != null) {
                            camera.release();
                        }
                        FileLog.m3e(e);
                    }
                }
            });
        } else if (BuildVars.LOGS_ENABLED) {
            FileLog.m0d("failed to open round " + session + " tex = " + texture);
        }
    }

    public void open(CameraSession session, SurfaceTexture texture, Runnable callback, Runnable prestartCallback) {
        if (session != null && texture != null) {
            final CameraSession cameraSession = session;
            final Runnable runnable = prestartCallback;
            final SurfaceTexture surfaceTexture = texture;
            final Runnable runnable2 = callback;
            this.threadPool.execute(new Runnable() {
                @SuppressLint({"NewApi"})
                public void run() {
                    Camera camera = cameraSession.cameraInfo.camera;
                    if (camera == null) {
                        try {
                            CameraInfo cameraInfo = cameraSession.cameraInfo;
                            Camera camera2 = Camera.open(cameraSession.cameraInfo.cameraId);
                            cameraInfo.camera = camera2;
                            camera = camera2;
                        } catch (Throwable e) {
                            cameraSession.cameraInfo.camera = null;
                            if (camera != null) {
                                camera.release();
                            }
                            FileLog.m3e(e);
                            return;
                        }
                    }
                    List<String> rawFlashModes = camera.getParameters().getSupportedFlashModes();
                    CameraController.this.availableFlashModes.clear();
                    if (rawFlashModes != null) {
                        for (int a = 0; a < rawFlashModes.size(); a++) {
                            String rawFlashMode = (String) rawFlashModes.get(a);
                            if (rawFlashMode.equals("off") || rawFlashMode.equals("on") || rawFlashMode.equals("auto")) {
                                CameraController.this.availableFlashModes.add(rawFlashMode);
                            }
                        }
                        cameraSession.checkFlashMode((String) CameraController.this.availableFlashModes.get(0));
                    }
                    if (runnable != null) {
                        runnable.run();
                    }
                    cameraSession.configurePhotoCamera();
                    camera.setPreviewTexture(surfaceTexture);
                    camera.startPreview();
                    if (runnable2 != null) {
                        AndroidUtilities.runOnUIThread(runnable2);
                    }
                }
            });
        }
    }

    public void recordVideo(CameraSession session, File path, VideoTakeCallback callback, Runnable onVideoStartRecord) {
        if (session != null) {
            final CameraInfo info = session.cameraInfo;
            final Camera camera = info.camera;
            final CameraSession cameraSession = session;
            final File file = path;
            final VideoTakeCallback videoTakeCallback = callback;
            final Runnable runnable = onVideoStartRecord;
            this.threadPool.execute(new Runnable() {
                public void run() {
                    try {
                        if (camera != null) {
                            try {
                                Parameters params = camera.getParameters();
                                params.setFlashMode(cameraSession.getCurrentFlashMode().equals("on") ? "torch" : "off");
                                camera.setParameters(params);
                            } catch (Throwable e) {
                                FileLog.m3e(e);
                            }
                            camera.unlock();
                            try {
                                CameraController.this.recorder = new MediaRecorder();
                                CameraController.this.recorder.setCamera(camera);
                                CameraController.this.recorder.setVideoSource(1);
                                CameraController.this.recorder.setAudioSource(5);
                                cameraSession.configureRecorder(1, CameraController.this.recorder);
                                CameraController.this.recorder.setOutputFile(file.getAbsolutePath());
                                CameraController.this.recorder.setMaxFileSize(1073741824);
                                CameraController.this.recorder.setVideoFrameRate(30);
                                CameraController.this.recorder.setMaxDuration(0);
                                Size pictureSize = CameraController.chooseOptimalSize(info.getPictureSizes(), 720, 480, new Size(16, 9));
                                CameraController.this.recorder.setVideoEncodingBitRate(1800000);
                                CameraController.this.recorder.setVideoSize(pictureSize.getWidth(), pictureSize.getHeight());
                                CameraController.this.recorder.setOnInfoListener(CameraController.this);
                                CameraController.this.recorder.prepare();
                                CameraController.this.recorder.start();
                                CameraController.this.onVideoTakeCallback = videoTakeCallback;
                                CameraController.this.recordedFile = file.getAbsolutePath();
                                if (runnable != null) {
                                    AndroidUtilities.runOnUIThread(runnable);
                                }
                            } catch (Throwable e2) {
                                CameraController.this.recorder.release();
                                CameraController.this.recorder = null;
                                FileLog.m3e(e2);
                            }
                        }
                    } catch (Throwable e22) {
                        FileLog.m3e(e22);
                    }
                }
            });
        }
    }

    private void finishRecordingVideo() {
        Throwable e;
        final Bitmap bitmap;
        final File cacheFile;
        final long durationFinal;
        Throwable th;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        long duration = 0;
        try {
            MediaMetadataRetriever mediaMetadataRetriever2 = new MediaMetadataRetriever();
            try {
                mediaMetadataRetriever2.setDataSource(this.recordedFile);
                String d = mediaMetadataRetriever2.extractMetadata(9);
                if (d != null) {
                    duration = (long) ((int) Math.ceil((double) (((float) Long.parseLong(d)) / 1000.0f)));
                }
                if (mediaMetadataRetriever2 != null) {
                    try {
                        mediaMetadataRetriever2.release();
                    } catch (Throwable e2) {
                        FileLog.m3e(e2);
                        mediaMetadataRetriever = mediaMetadataRetriever2;
                    }
                }
                mediaMetadataRetriever = mediaMetadataRetriever2;
            } catch (Exception e3) {
                e2 = e3;
                mediaMetadataRetriever = mediaMetadataRetriever2;
                try {
                    FileLog.m3e(e2);
                    if (mediaMetadataRetriever != null) {
                        try {
                            mediaMetadataRetriever.release();
                        } catch (Throwable e22) {
                            FileLog.m3e(e22);
                        }
                    }
                    bitmap = ThumbnailUtils.createVideoThumbnail(this.recordedFile, 1);
                    cacheFile = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                    bitmap.compress(CompressFormat.JPEG, 55, new FileOutputStream(cacheFile));
                    SharedConfig.saveConfig();
                    durationFinal = duration;
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (CameraController.this.onVideoTakeCallback != null) {
                                String path = cacheFile.getAbsolutePath();
                                if (bitmap != null) {
                                    ImageLoader.getInstance().putImageToCache(new BitmapDrawable(bitmap), Utilities.MD5(path));
                                }
                                CameraController.this.onVideoTakeCallback.onFinishVideoRecording(path, durationFinal);
                                CameraController.this.onVideoTakeCallback = null;
                            }
                        }
                    });
                } catch (Throwable th2) {
                    th = th2;
                    if (mediaMetadataRetriever != null) {
                        try {
                            mediaMetadataRetriever.release();
                        } catch (Throwable e222) {
                            FileLog.m3e(e222);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                mediaMetadataRetriever = mediaMetadataRetriever2;
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                }
                throw th;
            }
        } catch (Exception e4) {
            e222 = e4;
            FileLog.m3e(e222);
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
            bitmap = ThumbnailUtils.createVideoThumbnail(this.recordedFile, 1);
            cacheFile = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
            bitmap.compress(CompressFormat.JPEG, 55, new FileOutputStream(cacheFile));
            SharedConfig.saveConfig();
            durationFinal = duration;
            AndroidUtilities.runOnUIThread(/* anonymous class already generated */);
        }
        bitmap = ThumbnailUtils.createVideoThumbnail(this.recordedFile, 1);
        cacheFile = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
        try {
            bitmap.compress(CompressFormat.JPEG, 55, new FileOutputStream(cacheFile));
        } catch (Throwable e2222) {
            FileLog.m3e(e2222);
        }
        SharedConfig.saveConfig();
        durationFinal = duration;
        AndroidUtilities.runOnUIThread(/* anonymous class already generated */);
    }

    public void onInfo(MediaRecorder mediaRecorder, int what, int extra) {
        if (what == 800 || what == 801 || what == 1) {
            MediaRecorder tempRecorder = this.recorder;
            this.recorder = null;
            if (tempRecorder != null) {
                tempRecorder.stop();
                tempRecorder.release();
            }
            if (this.onVideoTakeCallback != null) {
                finishRecordingVideo();
            }
        }
    }

    public void stopVideoRecording(final CameraSession session, final boolean abandon) {
        this.threadPool.execute(new Runnable() {
            public void run() {
                try {
                    final Camera camera = session.cameraInfo.camera;
                    if (!(camera == null || CameraController.this.recorder == null)) {
                        MediaRecorder tempRecorder = CameraController.this.recorder;
                        CameraController.this.recorder = null;
                        try {
                            tempRecorder.stop();
                        } catch (Throwable e) {
                            FileLog.m3e(e);
                        }
                        try {
                            tempRecorder.release();
                        } catch (Throwable e2) {
                            FileLog.m3e(e2);
                        }
                        try {
                            camera.reconnect();
                            camera.startPreview();
                        } catch (Throwable e22) {
                            FileLog.m3e(e22);
                        }
                        try {
                            session.stopVideoRecording();
                        } catch (Throwable e222) {
                            FileLog.m3e(e222);
                        }
                    }
                    try {
                        Parameters params = camera.getParameters();
                        params.setFlashMode("off");
                        camera.setParameters(params);
                    } catch (Throwable e2222) {
                        FileLog.m3e(e2222);
                    }
                    CameraController.this.threadPool.execute(new Runnable() {
                        public void run() {
                            try {
                                Parameters params = camera.getParameters();
                                params.setFlashMode(session.getCurrentFlashMode());
                                camera.setParameters(params);
                            } catch (Throwable e) {
                                FileLog.m3e(e);
                            }
                        }
                    });
                    if (abandon || CameraController.this.onVideoTakeCallback == null) {
                        CameraController.this.onVideoTakeCallback = null;
                    } else {
                        CameraController.this.finishRecordingVideo();
                    }
                } catch (Exception e3) {
                }
            }
        });
    }

    public static Size chooseOptimalSize(List<Size> choices, int width, int height, Size aspectRatio) {
        List<Size> bigEnough = new ArrayList();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (int a = 0; a < choices.size(); a++) {
            Size option = (Size) choices.get(a);
            if (option.getHeight() == (option.getWidth() * h) / w && option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }
        if (bigEnough.size() > 0) {
            return (Size) Collections.min(bigEnough, new CompareSizesByArea());
        }
        return (Size) Collections.max(choices, new CompareSizesByArea());
    }
}
