package org.telegram.ui.Views;

import android.content.Context;
import android.support.v4.widget.ExploreByTouchHelper;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import org.telegram.messenger.FileLog;

public class TightTextView extends TextView {
    private boolean hasMaxWidth;
    public int lastLineWidth;
    public int lines;
    public int linesMaxWidth;
    public int maxWidth;

    public TightTextView(Context context) {
        this(context, null, 0);
    }

    public TightTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.lastLineWidth = 0;
        this.linesMaxWidth = 0;
        this.lines = 0;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int measuredWidth = getMeasuredWidth();
            Layout layout = getLayout();
            this.lines = layout.getLineCount();
            float lastLeft = layout.getLineLeft(this.lines - 1);
            float lastLine = layout.getLineWidth(this.lines - 1);
            boolean hasNonRTL = false;
            int ceil = (int) Math.ceil((double) lastLine);
            this.lastLineWidth = ceil;
            this.linesMaxWidth = ceil;
            int lastLineWidthWithLeft = (int) Math.ceil((double) (lastLine + lastLeft));
            int linesMaxWidthWithLeft = lastLineWidthWithLeft;
            if (lastLeft == 0.0f) {
                hasNonRTL = true;
            }
            if (this.hasMaxWidth && MeasureSpec.getMode(widthMeasureSpec) != 1073741824) {
                if (this.lines > 1) {
                    float textRealMaxWidth = 0.0f;
                    float textRealMaxWidthWithLeft = 0.0f;
                    int n = 0;
                    while (n < this.lines) {
                        try {
                            float lineWidth = layout.getLineWidth(n);
                            float lineLeft = layout.getLineLeft(n);
                            if (lineLeft == 0.0f) {
                                hasNonRTL = true;
                            }
                            textRealMaxWidth = Math.max(textRealMaxWidth, lineWidth);
                            textRealMaxWidthWithLeft = Math.max(textRealMaxWidthWithLeft, lineWidth + lineLeft);
                            this.linesMaxWidth = Math.max(this.linesMaxWidth, (int) Math.ceil((double) lineWidth));
                            linesMaxWidthWithLeft = Math.max(linesMaxWidthWithLeft, (int) Math.ceil((double) (lineWidth + lineLeft)));
                            n++;
                        } catch (Exception e) {
                            FileLog.m799e("tmessages", e);
                            return;
                        }
                    }
                    if (hasNonRTL) {
                        textRealMaxWidth = textRealMaxWidthWithLeft;
                        this.lastLineWidth = lastLineWidthWithLeft;
                        this.linesMaxWidth = linesMaxWidthWithLeft;
                    } else {
                        this.lastLineWidth = this.linesMaxWidth;
                    }
                    int w = (int) Math.ceil((double) textRealMaxWidth);
                    if (w < getMeasuredWidth()) {
                        super.onMeasure(MeasureSpec.makeMeasureSpec(w, ExploreByTouchHelper.INVALID_ID), heightMeasureSpec);
                        return;
                    }
                    return;
                }
                super.onMeasure(MeasureSpec.makeMeasureSpec(Math.min(this.maxWidth, this.linesMaxWidth), ExploreByTouchHelper.INVALID_ID), heightMeasureSpec);
            }
        } catch (Exception e2) {
            FileLog.m799e("tmessages", e2);
            try {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } catch (Exception e22) {
                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
                FileLog.m799e("tmessages", e22);
            }
        }
    }

    public void setMaxWidth(int maxpixels) {
        super.setMaxWidth(maxpixels);
        this.hasMaxWidth = true;
        this.maxWidth = maxpixels;
    }

    public void setMaxEms(int maxems) {
        super.setMaxEms(maxems);
        this.hasMaxWidth = true;
    }
}
