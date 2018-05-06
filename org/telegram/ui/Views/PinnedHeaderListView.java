package org.telegram.ui.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.android.gms.maps.model.GroundOverlayOptions;

public class PinnedHeaderListView extends ListView implements OnScrollListener, OnTouchListener {
    public int exHeaderRightPadding = 0;
    private PinnedSectionedHeaderAdapter mAdapter;
    private View mCurrentHeader;
    private int mCurrentHeaderViewType = 0;
    private int mCurrentSection = 0;
    private OnTouchListener mForwardingTouchListener = null;
    private float mHeaderOffset;
    private float mLastUpEventY = GroundOverlayOptions.NO_DIMENSION;
    private OnScrollListener mOnScrollListener;
    private boolean mShouldPin = true;
    private int mWidthMode;

    public static abstract class OnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        public abstract void onItemClick(AdapterView<?> adapterView, View view, int i, int i2, long j);

        public abstract void onSectionClick(AdapterView<?> adapterView, View view, int i, long j);

        public void onItemClick(AdapterView<?> adapterView, View view, int rawPosition, long id) {
            SectionedBaseAdapter adapter;
            if (adapterView.getAdapter() instanceof HeaderViewListAdapter) {
                adapter = (SectionedBaseAdapter) ((HeaderViewListAdapter) adapterView.getAdapter()).getWrappedAdapter();
            } else {
                adapter = (SectionedBaseAdapter) adapterView.getAdapter();
            }
            int section = adapter.getSectionForPosition(rawPosition);
            int position = adapter.getPositionInSectionForPosition(rawPosition);
            if (position == -1) {
                onSectionClick(adapterView, view, section, id);
            } else {
                onItemClick(adapterView, view, section, position, id);
            }
        }
    }

    public interface PinnedSectionedHeaderAdapter {
        int getCount();

        int getSectionForPosition(int i);

        View getSectionHeaderView(int i, View view, ViewGroup viewGroup);

        int getSectionHeaderViewType(int i);

        boolean isSectionHeader(int i);
    }

    public PinnedHeaderListView(Context context) {
        super(context);
        super.setOnScrollListener(this);
        super.setOnTouchListener(this);
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnScrollListener(this);
        super.setOnTouchListener(this);
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setOnScrollListener(this);
        super.setOnTouchListener(this);
    }

    public void setPinHeaders(boolean shouldPin) {
        this.mShouldPin = shouldPin;
    }

    public void setAdapter(ListAdapter adapter) {
        this.mCurrentHeader = null;
        if (adapter instanceof PinnedSectionedHeaderAdapter) {
            this.mAdapter = (PinnedSectionedHeaderAdapter) adapter;
        } else {
            this.mAdapter = null;
        }
        super.setAdapter(adapter);
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.mAdapter != null) {
            if (this.mOnScrollListener != null) {
                this.mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
            int i;
            View header;
            if (this.mAdapter == null || this.mAdapter.getCount() == 0 || !this.mShouldPin || firstVisibleItem < getHeaderViewsCount()) {
                this.mCurrentHeader = null;
                this.mHeaderOffset = 0.0f;
                for (i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
                    header = getChildAt(i);
                    if (header != null) {
                        header.setVisibility(0);
                    }
                }
                return;
            }
            View view2;
            firstVisibleItem -= getHeaderViewsCount();
            int section = this.mAdapter.getSectionForPosition(firstVisibleItem);
            int viewType = this.mAdapter.getSectionHeaderViewType(section);
            if (this.mCurrentHeaderViewType != viewType) {
                view2 = null;
            } else {
                view2 = this.mCurrentHeader;
            }
            this.mCurrentHeader = getSectionHeaderView(section, view2);
            if (!(this.mCurrentHeader == null || this.mCurrentHeader.getPaddingLeft() == getPaddingLeft())) {
                this.mCurrentHeader.setPadding(getPaddingLeft(), this.mCurrentHeader.getPaddingTop(), getPaddingRight() + ((int) (getResources().getDisplayMetrics().density * ((float) this.exHeaderRightPadding))), 0);
            }
            ensurePinnedHeaderLayout(this.mCurrentHeader, false);
            this.mCurrentHeaderViewType = viewType;
            this.mHeaderOffset = 0.0f;
            for (i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
                if (this.mAdapter.isSectionHeader(i)) {
                    header = getChildAt(i - firstVisibleItem);
                    float headerTop = (float) header.getTop();
                    float pinnedHeaderHeight = (float) this.mCurrentHeader.getMeasuredHeight();
                    header.setVisibility(0);
                    if (pinnedHeaderHeight >= headerTop && headerTop > GroundOverlayOptions.NO_DIMENSION) {
                        this.mHeaderOffset = headerTop - ((float) header.getHeight());
                    } else if (headerTop <= 0.0f) {
                        header.setVisibility(4);
                    }
                }
            }
            invalidate();
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.mAdapter != null && this.mOnScrollListener != null) {
            this.mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    private View getSectionHeaderView(int section, View oldView) {
        boolean shouldLayout;
        if (section != this.mCurrentSection || oldView == null) {
            shouldLayout = true;
        } else {
            shouldLayout = false;
        }
        View view = this.mAdapter.getSectionHeaderView(section, oldView, this);
        if (shouldLayout) {
            ensurePinnedHeaderLayout(view, false);
            this.mCurrentSection = section;
        }
        return view;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mAdapter != null && this.mCurrentHeader != null) {
            ensurePinnedHeaderLayout(this.mCurrentHeader, true);
        }
    }

    private void ensurePinnedHeaderLayout(View header, boolean forceLayout) {
        if (header.isLayoutRequested() || forceLayout) {
            int heightSpec;
            int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), this.mWidthMode);
            LayoutParams layoutParams = header.getLayoutParams();
            if (layoutParams == null || layoutParams.height <= 0) {
                heightSpec = MeasureSpec.makeMeasureSpec(0, 0);
            } else {
                heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
            }
            header.measure(widthSpec, heightSpec);
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mAdapter != null && this.mShouldPin && this.mCurrentHeader != null) {
            int saveCount = canvas.save();
            canvas.translate(0.0f, this.mHeaderOffset);
            canvas.clipRect(0, 0, getWidth(), this.mCurrentHeader.getMeasuredHeight());
            this.mCurrentHeader.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    public boolean performItemClick(View view, int position, long id) {
        if (this.mAdapter == null || this.mLastUpEventY <= 0.0f || this.mCurrentHeader == null || this.mLastUpEventY >= ((float) this.mCurrentHeader.getBottom())) {
            return super.performItemClick(view, position, id);
        }
        this.mCurrentHeader.performClick();
        this.mLastUpEventY = GroundOverlayOptions.NO_DIMENSION;
        return true;
    }

    public void setOnTouchListener(OnTouchListener l) {
        this.mForwardingTouchListener = l;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (this.mForwardingTouchListener != null) {
            this.mForwardingTouchListener.onTouch(v, event);
        }
        if (this.mCurrentHeader != null && event.getY() < ((float) this.mCurrentHeader.getHeight()) && event.getAction() == 1) {
            this.mLastUpEventY = event.getY();
        }
        return false;
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }
}
