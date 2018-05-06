package org.telegram.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class LayoutListView extends ListView {
    private int height = -1;

    public LayoutListView(Context context) {
        super(context);
    }

    public LayoutListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LayoutListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View v = getChildAt(getChildCount() - 1);
        if (v == null || this.height <= 0 || !changed || bottom - top >= this.height) {
            super.onLayout(changed, left, top, right, bottom);
        } else {
            int b = this.height - v.getTop();
            final int scrollTo = getLastVisiblePosition();
            super.onLayout(changed, left, top, right, bottom);
            final int offset = (bottom - top) - b;
            post(new Runnable() {
                public void run() {
                    LayoutListView.this.setSelectionFromTop(scrollTo, offset - LayoutListView.this.getPaddingTop());
                }
            });
        }
        this.height = bottom - top;
    }
}
