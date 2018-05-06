package org.telegram.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.Utilities;

public class MessageLayout extends FrameLayoutFixed {
    public int maxWidth;
    public TightTextView messageTextView;
    public View timeLayout;
    public TextView timeTextView;

    public MessageLayout(Context context) {
        super(context);
    }

    public MessageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int timeWidth = this.timeLayout != null ? this.timeLayout.getMeasuredWidth() : this.timeTextView.getMeasuredWidth();
        int totalWidth = getMeasuredWidth();
        int maxChildWidth = getChildAt(0).getMeasuredWidth();
        int count = getChildCount();
        for (int a = 1; a < count - 1; a++) {
            maxChildWidth = Math.max(maxChildWidth, getChildAt(a).getMeasuredWidth());
        }
        int timeMore = timeWidth + Utilities.dp(6);
        int fields = totalWidth - Math.max(maxChildWidth, timeWidth);
        int height = getMeasuredHeight();
        if (this.maxWidth - this.messageTextView.lastLineWidth < timeMore) {
            setMeasuredDimension(Math.max(maxChildWidth, this.messageTextView.lastLineWidth) + fields, Utilities.dp(14) + height);
            return;
        }
        int diff = maxChildWidth - this.messageTextView.lastLineWidth;
        if (diff < 0 || diff > timeMore) {
            setMeasuredDimension(Math.max(maxChildWidth, this.messageTextView.lastLineWidth + timeMore) + fields, height);
        } else {
            setMeasuredDimension(((maxChildWidth + timeMore) - diff) + fields, height);
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.timeTextView = (TextView) findViewById(C0419R.id.chat_time_text);
        this.messageTextView = (TightTextView) findViewById(C0419R.id.chat_message_text);
        this.timeLayout = findViewById(C0419R.id.chat_time_layout);
    }
}
