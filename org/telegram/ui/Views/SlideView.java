package org.telegram.ui.Views;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import org.telegram.messenger.BuildConfig;

public class SlideView extends LinearLayout {
    public SlideViewDelegate delegate;

    public interface SlideViewDelegate {
        void needFinishActivity();

        void needHideProgress();

        void needShowAlert(String str);

        void needShowProgress();

        void onNextAction();

        void setPage(int i, boolean z, Bundle bundle, boolean z2);
    }

    public SlideView(Context context) {
        super(context);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public String getHeaderName() {
        return BuildConfig.FLAVOR;
    }

    public void onNextPressed() {
    }

    public void setParams(Bundle params) {
    }

    public void onBackPressed() {
    }

    public void onShow() {
    }

    public void onDestroyActivity() {
        this.delegate = null;
    }
}
