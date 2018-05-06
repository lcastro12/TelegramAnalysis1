package org.telegram.ui.Views;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ListView;

public class OnSwipeTouchListener implements OnTouchListener {
    private boolean discard = false;
    private float downX;
    private float downY;

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.downX = event.getX();
                this.downY = event.getY();
                this.discard = false;
                if ((v instanceof ListView) || (v instanceof GridView)) {
                    return false;
                }
                return true;
            case 1:
                onTouchUp(event);
                return false;
            case 2:
                float upX = event.getX();
                float deltaX = this.downX - upX;
                float deltaY = this.downY - event.getY();
                if (Math.abs(deltaY) > 40.0f) {
                    this.discard = true;
                }
                if (!this.discard && Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > 90.0f) {
                    if (deltaX < 0.0f) {
                        onSwipeRight();
                        return true;
                    } else if (deltaX > 0.0f) {
                        onSwipeLeft();
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public void onTouchUp(MotionEvent event) {
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }
}
