package org.telegram.ui.Views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;
import java.lang.ref.WeakReference;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.TL_fileEncryptedLocation;
import org.telegram.TL.TLRPC.TL_fileLocation;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Utilities;

public class ImageReceiver {
    public Integer TAG = null;
    private Drawable currentImage = null;
    private String currentPath = null;
    public int imageH = 0;
    public int imageW = 0;
    public int imageX = 0;
    public int imageY = 0;
    private boolean isPlaceholder = false;
    private String last_filter = null;
    private String last_httpUrl = null;
    private FileLocation last_path = null;
    private Drawable last_placeholder = null;
    private int last_size = 0;
    public WeakReference<View> parentView = null;

    public void setImage(FileLocation path, String filter, Drawable placeholder) {
        setImage(path, null, filter, placeholder, 0);
    }

    public void setImage(FileLocation path, String filter, Drawable placeholder, int size) {
        setImage(path, null, filter, placeholder, size);
    }

    public void setImage(String path, String filter, Drawable placeholder) {
        setImage(null, path, filter, placeholder, 0);
    }

    public void setImage(FileLocation path, String httpUrl, String filter, Drawable placeholder, int size) {
        if (!(path == null && httpUrl == null) && (path == null || (path instanceof TL_fileLocation) || (path instanceof TL_fileEncryptedLocation))) {
            String key;
            Bitmap img;
            if (path != null) {
                key = path.volume_id + "_" + path.local_id;
            } else {
                key = Utilities.MD5(httpUrl);
            }
            if (filter != null) {
                key = key + "@" + filter;
            }
            if (this.currentPath == null) {
                img = FileLoader.Instance.getImageFromMemory(path, httpUrl, this, filter, true);
            } else if (!this.currentPath.equals(key)) {
                img = FileLoader.Instance.getImageFromMemory(path, httpUrl, this, filter, true);
                recycleBitmap(img);
            } else {
                return;
            }
            this.currentPath = key;
            this.last_path = path;
            this.last_httpUrl = httpUrl;
            this.last_filter = filter;
            this.last_placeholder = placeholder;
            this.last_size = size;
            if (img == null) {
                this.isPlaceholder = true;
                FileLoader.Instance.loadImage(path, httpUrl, this, filter, true, size);
                return;
            }
            setImageBitmap(img, this.currentPath);
            return;
        }
        recycleBitmap(null);
        this.currentPath = null;
        this.isPlaceholder = true;
        this.last_path = null;
        this.last_httpUrl = null;
        this.last_filter = null;
        this.last_placeholder = placeholder;
        this.last_size = 0;
        this.currentImage = null;
        FileLoader.Instance.cancelLoadingForImageView(this);
    }

    public void setImageBitmap(Bitmap bitmap, String imgKey) {
        if (this.currentPath != null && imgKey.equals(this.currentPath)) {
            this.isPlaceholder = false;
            FileLoader.Instance.incrementUseCount(this.currentPath);
            this.currentImage = new BitmapDrawable(null, bitmap);
            if (this.parentView.get() == null) {
                return;
            }
            if (this.imageW != 0) {
                ((View) this.parentView.get()).invalidate(this.imageX, this.imageY, this.imageX + this.imageW, this.imageY + this.imageH);
            } else {
                ((View) this.parentView.get()).invalidate();
            }
        }
    }

    public void clearImage() {
        recycleBitmap(null);
    }

    private void recycleBitmap(Bitmap newBitmap) {
        if (this.currentImage != null && !this.isPlaceholder && (this.currentImage instanceof BitmapDrawable)) {
            Bitmap bitmap = ((BitmapDrawable) this.currentImage).getBitmap();
            if (bitmap != null && bitmap != newBitmap && this.currentPath != null) {
                boolean canDelete = FileLoader.Instance.decrementUseCount(this.currentPath);
                if (FileLoader.Instance.isInCache(this.currentPath)) {
                    this.currentImage = null;
                    return;
                }
                if (FileLoader.Instance.runtimeHack != null) {
                    FileLoader.Instance.runtimeHack.trackAlloc((long) (bitmap.getRowBytes() * bitmap.getHeight()));
                }
                if (canDelete) {
                    this.currentImage = null;
                    if (VERSION.SDK_INT < 11) {
                        bitmap.recycle();
                    }
                }
            }
        }
    }

    public void draw(Canvas canvas, int x, int y, int w, int h) {
        try {
            if (this.currentImage != null) {
                this.currentImage.setBounds(x, y, x + w, y + h);
                this.currentImage.draw(canvas);
            } else if (this.last_placeholder != null) {
                this.last_placeholder.setBounds(x, y, x + w, y + h);
                this.last_placeholder.draw(canvas);
            }
        } catch (Exception e) {
            if (this.currentPath != null) {
                FileLoader.Instance.removeImage(this.currentPath);
                this.currentPath = null;
            }
            setImage(this.last_path, this.last_httpUrl, this.last_filter, this.last_placeholder, this.last_size);
            FileLog.m799e("tmessages", e);
        }
    }

    protected void finalize() throws Throwable {
        recycleBitmap(null);
        super.finalize();
    }
}
