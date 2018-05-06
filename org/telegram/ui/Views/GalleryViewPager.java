package org.telegram.ui.Views;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import org.telegram.messenger.FileLog;

public class GalleryViewPager extends ViewPager {
    PointF last;
    public PZSImageView mCurrentView;

    public GalleryViewPager(Context context) {
        super(context);
    }

    public GalleryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float[] handleMotionEvent(MotionEvent event) {
        switch (event.getAction() & MotionEventCompat.ACTION_MASK) {
            case 0:
                this.last = new PointF(event.getX(0), event.getY(0));
                break;
            case 1:
            case 2:
                PointF curr = new PointF(event.getX(0), event.getY(0));
                return new float[]{curr.x - this.last.x, curr.y - this.last.y};
        }
        return null;
    }

    public boolean onTouchEvent(MotionEvent event) {
        try {
            if ((event.getAction() & MotionEventCompat.ACTION_MASK) == 1) {
                super.onTouchEvent(event);
            }
            if (this.mCurrentView == null) {
                return super.onTouchEvent(event);
            }
            float[] difference = handleMotionEvent(event);
            if (difference != null && this.mCurrentView.getOnRightSide() && difference[0] < 0.0f) {
                return super.onTouchEvent(event);
            }
            if (difference != null && this.mCurrentView.getOnLeftSide() && difference[0] > 0.0f) {
                return super.onTouchEvent(event);
            }
            if (difference != null) {
                return false;
            }
            if (this.mCurrentView.getOnLeftSide() || this.mCurrentView.getOnRightSide()) {
                return super.onTouchEvent(event);
            }
            return false;
        } catch (Exception e) {
            try {
                getAdapter().notifyDataSetChanged();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            FileLog.m799e("tmessages", e);
            return false;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            if ((event.getAction() & MotionEventCompat.ACTION_MASK) == 1) {
                super.onInterceptTouchEvent(event);
            }
            if (this.mCurrentView == null) {
                return super.onInterceptTouchEvent(event);
            }
            float[] difference = handleMotionEvent(event);
            if (difference != null && difference.length > 0 && this.mCurrentView.getOnRightSide() && difference[0] < 0.0f) {
                return super.onInterceptTouchEvent(event);
            }
            if (difference != null && difference.length > 0 && this.mCurrentView.getOnLeftSide() && difference[0] > 0.0f) {
                return super.onInterceptTouchEvent(event);
            }
            if (difference != null && difference.length != 0) {
                return false;
            }
            if (this.mCurrentView.getOnLeftSide() || this.mCurrentView.getOnRightSide()) {
                return super.onInterceptTouchEvent(event);
            }
            return false;
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            return false;
        }
    }
}
