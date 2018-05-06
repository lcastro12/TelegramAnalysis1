package org.telegram.ui.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import org.telegram.messenger.Utilities;

public class IdenticonView extends View {
    private int[] colors = new int[]{-1, -2758925, -13805707, -13657655};
    private byte[] data;
    private Paint paint = new Paint();

    public IdenticonView(Context context) {
        super(context);
    }

    public IdenticonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IdenticonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    int get_bits(int bitOffset, int numBits) {
        int offset = bitOffset / 8;
        return (((((this.data[offset + 3] << 24) | (this.data[offset + 2] << 16)) | (this.data[offset + 1] << 8)) | this.data[offset]) >> (bitOffset % 8)) & (((int) Math.pow(2.0d, (double) numBits)) - 1);
    }

    public void setBytes(byte[] bytes) {
        if (bytes != null) {
            this.data = Utilities.computeSHA1(bytes);
            if (this.data.length < 128) {
                byte[] temp = new byte[128];
                System.arraycopy(this.data, 0, temp, 0, this.data.length);
                this.data = temp;
            }
            invalidate();
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.data != null) {
            int bitPointer = 0;
            float rectSize = (float) Math.floor((double) (((float) Math.min(getWidth(), getHeight())) / 8.0f));
            float xOffset = Math.max(0.0f, (((float) getWidth()) - (8.0f * rectSize)) / 2.0f);
            float yOffset = Math.max(0.0f, (((float) getHeight()) - (8.0f * rectSize)) / 2.0f);
            for (int iy = 0; iy < 8; iy++) {
                for (int ix = 0; ix < 8; ix++) {
                    int byteValue = get_bits(bitPointer, 2);
                    bitPointer += 2;
                    this.paint.setColor(this.colors[Math.abs(byteValue) % 4]);
                    canvas.drawRect(xOffset + (((float) ix) * rectSize), (((float) iy) * rectSize) + yOffset, ((((float) ix) * rectSize) + xOffset) + rectSize, ((((float) iy) * rectSize) + rectSize) + yOffset, this.paint);
                }
            }
        }
    }
}
