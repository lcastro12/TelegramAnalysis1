package org.telegram.ui.Views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import org.telegram.messenger.Utilities;

public class SizeNotifierRelativeLayout extends RelativeLayout {
    public SizeNotifierRelativeLayoutDelegate delegate;
    private Rect rect = new Rect();

    public interface SizeNotifierRelativeLayoutDelegate {
        void onSizeChanged(int i);
    }

    public SizeNotifierRelativeLayout(Context context) {
        super(context);
    }

    public SizeNotifierRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SizeNotifierRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (this.delegate != null) {
            int usableViewHeight = getRootView().getHeight() - Utilities.statusBarHeight;
            getWindowVisibleDisplayFrame(this.rect);
            this.delegate.onSizeChanged(usableViewHeight - (this.rect.bottom - this.rect.top));
        }
    }
}
