package org.telegram.ui.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.ImageView;
import org.telegram.TL.TLRPC.FileLocation;
import org.telegram.TL.TLRPC.TL_fileEncryptedLocation;
import org.telegram.TL.TLRPC.TL_fileLocation;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Utilities;

public class BackupImageView extends ImageView {
    public String currentPath;
    private boolean ignoreLayout = true;
    private boolean isPlaceholder;
    String last_filter;
    String last_httpUrl;
    FileLocation last_path;
    int last_placeholder;
    Bitmap last_placeholderBitmap;
    int last_size;
    boolean makeRequest = true;

    public BackupImageView(Context context) {
        super(context);
    }

    public BackupImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackupImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImage(FileLocation path, String filter, int placeholder) {
        setImage(path, null, filter, placeholder, null, 0);
    }

    public void setImage(FileLocation path, String filter, Bitmap placeholderBitmap) {
        setImage(path, null, filter, 0, placeholderBitmap, 0);
    }

    public void setImage(FileLocation path, String filter, int placeholder, int size) {
        setImage(path, null, filter, placeholder, null, size);
    }

    public void setImage(FileLocation path, String filter, Bitmap placeholderBitmap, int size) {
        setImage(path, null, filter, 0, placeholderBitmap, size);
    }

    public void setImage(String path, String filter, int placeholder) {
        setImage(null, path, filter, placeholder, null, 0);
    }

    public void setImage(FileLocation path, String httpUrl, String filter, int placeholder, Bitmap placeholderBitmap, int size) {
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
            this.last_placeholderBitmap = placeholderBitmap;
            this.last_size = size;
            if (img == null) {
                this.isPlaceholder = true;
                if (placeholder != 0) {
                    setImageResourceMy(placeholder);
                } else if (placeholderBitmap != null) {
                    setImageBitmapMy(placeholderBitmap);
                }
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
        this.last_placeholder = 0;
        this.last_size = 0;
        this.last_placeholderBitmap = null;
        FileLoader.Instance.cancelLoadingForImageView(this);
        if (placeholder != 0) {
            setImageResourceMy(placeholder);
        } else if (placeholderBitmap != null) {
            setImageBitmapMy(placeholderBitmap);
        }
    }

    public void setImageBitmap(Bitmap bitmap, String imgKey) {
        if (this.currentPath != null && imgKey.equals(this.currentPath)) {
            this.isPlaceholder = false;
            FileLoader.Instance.incrementUseCount(this.currentPath);
            if (this.ignoreLayout) {
                this.makeRequest = false;
            }
            super.setImageBitmap(bitmap);
            if (this.ignoreLayout) {
                this.makeRequest = true;
            }
        }
    }

    public void clearImage() {
        recycleBitmap(null);
    }

    private void recycleBitmap(Bitmap newBitmap) {
        Drawable drawable = getDrawable();
        if (drawable != null && !this.isPlaceholder) {
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                if (bitmap != null && bitmap != newBitmap && this.currentPath != null) {
                    boolean canDelete = FileLoader.Instance.decrementUseCount(this.currentPath);
                    if (FileLoader.Instance.isInCache(this.currentPath)) {
                        setImageBitmap(null);
                        return;
                    }
                    if (FileLoader.Instance.runtimeHack != null) {
                        FileLoader.Instance.runtimeHack.trackAlloc((long) (bitmap.getRowBytes() * bitmap.getHeight()));
                    }
                    if (canDelete) {
                        setImageBitmap(null);
                        if (VERSION.SDK_INT < 11) {
                            bitmap.recycle();
                        }
                    }
                }
            } else if (!(drawable instanceof NinePatchDrawable)) {
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            FileLoader.Instance.removeImage(this.currentPath);
            this.currentPath = null;
            setImage(this.last_path, this.last_httpUrl, this.last_filter, this.last_placeholder, this.last_placeholderBitmap, this.last_size);
            FileLog.m799e("tmessages", e);
        }
    }

    protected void finalize() throws Throwable {
        recycleBitmap(null);
        super.finalize();
    }

    public void setImageResourceMy(int resId) {
        if (this.ignoreLayout) {
            this.makeRequest = false;
        }
        super.setImageResource(resId);
        if (this.ignoreLayout) {
            this.makeRequest = true;
        }
    }

    public void setImageResource(int resId) {
        if (resId != 0) {
            recycleBitmap(null);
        }
        this.currentPath = null;
        this.last_path = null;
        this.last_httpUrl = null;
        this.last_filter = null;
        this.last_placeholder = 0;
        this.last_size = 0;
        this.last_placeholderBitmap = null;
        if (this.ignoreLayout) {
            this.makeRequest = false;
        }
        super.setImageResource(resId);
        if (this.ignoreLayout) {
            this.makeRequest = true;
        }
    }

    public void setImageBitmapMy(Bitmap bitmap) {
        if (this.ignoreLayout) {
            this.makeRequest = false;
        }
        super.setImageBitmap(bitmap);
        if (this.ignoreLayout) {
            this.makeRequest = true;
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            recycleBitmap(null);
        }
        this.currentPath = null;
        this.last_path = null;
        this.last_httpUrl = null;
        this.last_filter = null;
        this.last_placeholder = 0;
        this.last_size = 0;
        this.last_placeholderBitmap = null;
        if (this.ignoreLayout) {
            this.makeRequest = false;
        }
        super.setImageBitmap(bitmap);
        if (this.ignoreLayout) {
            this.makeRequest = true;
        }
    }

    public void requestLayout() {
        if (this.makeRequest) {
            super.requestLayout();
        }
    }
}
