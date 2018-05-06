package org.telegram.ui.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.Utilities;

public class MessageActionLayout extends FrameLayout {
    public TightTextView messageTextView;

    public MessageActionLayout(Context context) {
        super(context);
    }

    public MessageActionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageActionLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(this.messageTextView.linesMaxWidth + Utilities.dp(14), getMeasuredHeight());
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.messageTextView = (TightTextView) findViewById(C0419R.id.chat_message_text);
    }
}
