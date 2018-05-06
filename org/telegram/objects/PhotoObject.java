package org.telegram.objects;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.TL.TLRPC.PhotoSize;
import org.telegram.TL.TLRPC.TL_photoCachedSize;
import org.telegram.messenger.FileLoader;

public class PhotoObject {
    public Bitmap image;
    public PhotoSize photoOwner;

    public PhotoObject(PhotoSize photo) {
        this.photoOwner = photo;
        if (photo instanceof TL_photoCachedSize) {
            Options opts = new Options();
            opts.inPreferredConfig = Config.RGB_565;
            opts.inDither = false;
            opts.outWidth = photo.f60w;
            opts.outHeight = photo.f59h;
            this.image = BitmapFactory.decodeByteArray(this.photoOwner.bytes, 0, this.photoOwner.bytes.length, opts);
            if (FileLoader.Instance.runtimeHack != null) {
                FileLoader.Instance.runtimeHack.trackFree((long) (this.image.getRowBytes() * this.image.getHeight()));
            }
        }
    }

    public static PhotoObject getClosestImageWithSize(ArrayList<PhotoObject> arr, int width, int height) {
        int closestWidth = 9999;
        int closestHeight = 9999;
        PhotoObject closestObject = null;
        Iterator i$ = arr.iterator();
        while (i$.hasNext()) {
            PhotoObject obj = (PhotoObject) i$.next();
            if (!(obj == null || obj.photoOwner == null)) {
                int diffW = Math.abs(obj.photoOwner.f60w - width);
                int diffH = Math.abs(obj.photoOwner.f59h - height);
                if (closestObject == null || closestWidth > diffW || closestHeight > diffH || (closestObject.photoOwner instanceof TL_photoCachedSize)) {
                    closestObject = obj;
                    closestWidth = diffW;
                    closestHeight = diffH;
                }
            }
        }
        return closestObject;
    }

    public static PhotoSize getClosestPhotoSizeWithSize(ArrayList<PhotoSize> sizes, int width, int height) {
        int closestWidth = 9999;
        int closestHeight = 9999;
        PhotoSize closestObject = null;
        Iterator i$ = sizes.iterator();
        while (i$.hasNext()) {
            PhotoSize obj = (PhotoSize) i$.next();
            int diffW = Math.abs(obj.f60w - width);
            int diffH = Math.abs(obj.f59h - height);
            if (closestObject == null || (closestWidth > diffW && closestHeight > diffH)) {
                closestObject = obj;
                closestWidth = diffW;
                closestHeight = diffH;
            }
        }
        return closestObject;
    }
}
