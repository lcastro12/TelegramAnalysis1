package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C0553R;
import org.telegram.messenger.FileLoader;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_wallPaperSolid;
import org.telegram.tgnet.TLRPC.WallPaper;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;

public class WallpaperCell extends FrameLayout {
    private BackupImageView imageView;
    private ImageView imageView2;
    private View selectionView;

    public WallpaperCell(Context context) {
        super(context);
        this.imageView = new BackupImageView(context);
        addView(this.imageView, LayoutHelper.createFrame(100, 100, 83));
        this.imageView2 = new ImageView(context);
        this.imageView2.setImageResource(C0553R.drawable.ic_gallery_background);
        this.imageView2.setScaleType(ScaleType.CENTER);
        addView(this.imageView2, LayoutHelper.createFrame(100, 100, 83));
        this.selectionView = new View(context);
        this.selectionView.setBackgroundResource(C0553R.drawable.wall_selection);
        addView(this.selectionView, LayoutHelper.createFrame(100, 102.0f));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(102.0f), 1073741824));
    }

    public void setWallpaper(WallPaper wallpaper, int selectedBackground) {
        int i = 0;
        if (wallpaper == null) {
            int i2;
            this.imageView.setVisibility(4);
            this.imageView2.setVisibility(0);
            this.selectionView.setVisibility(selectedBackground == -1 ? 0 : 4);
            ImageView imageView = this.imageView2;
            if (selectedBackground == -1 || selectedBackground == 1000001) {
                i2 = 1514625126;
            } else {
                i2 = 1509949440;
            }
            imageView.setBackgroundColor(i2);
            return;
        }
        this.imageView.setVisibility(0);
        this.imageView2.setVisibility(4);
        View view = this.selectionView;
        if (selectedBackground != wallpaper.id) {
            i = 4;
        }
        view.setVisibility(i);
        if (wallpaper instanceof TL_wallPaperSolid) {
            this.imageView.setImageBitmap(null);
            this.imageView.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK | wallpaper.bg_color);
            return;
        }
        PhotoSize size = FileLoader.getClosestPhotoSizeWithSize(wallpaper.sizes, AndroidUtilities.dp(100.0f));
        if (!(size == null || size.location == null)) {
            this.imageView.setImage(size.location, "100_100", (Drawable) null);
        }
        this.imageView.setBackgroundColor(1514625126);
    }
}
