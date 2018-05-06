package org.telegram.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class DelayedListView extends ListView {
    public OnScrollListener innerScrollListener;
    private boolean isScrolling = false;

    class C06031 implements OnScrollListener {
        C06031() {
        }

        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            DelayedListView.this.isScrolling = scrollState != 0;
        }

        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }

    public void init() {
        this.innerScrollListener = new C06031();
    }

    public DelayedListView(Context context) {
        super(context);
        init();
    }

    public DelayedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DelayedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
}
