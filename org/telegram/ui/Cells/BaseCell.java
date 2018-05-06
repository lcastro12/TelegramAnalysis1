package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class BaseCell extends View {
    public BaseCell(Context context) {
        super(context);
    }

    public BaseCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void setDrawableBounds(Drawable drawable, int x, int y) {
        setDrawableBounds(drawable, x, y, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    protected void setDrawableBounds(Drawable drawable, int x, int y, int w, int h) {
        drawable.setBounds(x, y, x + w, y + h);
    }
}
