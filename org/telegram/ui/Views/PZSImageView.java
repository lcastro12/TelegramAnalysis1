package org.telegram.ui.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import org.telegram.messenger.C0419R;
import org.telegram.messenger.FileLog;

public class PZSImageView extends BackupImageView {
    private static final float MAX_SCALE_TO_SCREEN = 2.0f;
    private static final float MIN_SCALE_SPAN = 10.0f;
    private static final float MIN_SCALE_TO_SCREEN = 1.0f;
    protected static final int PZS_ACTION_CANCEL = -1;
    protected static final int PZS_ACTION_CENTER_CROP = 1006;
    protected static final int PZS_ACTION_FIT_CENTER = 1005;
    protected static final int PZS_ACTION_INIT = 100;
    protected static final int PZS_ACTION_SCALE = 1001;
    protected static final int PZS_ACTION_SCALE_TO_TRANSLATE = 1003;
    protected static final int PZS_ACTION_TOP_CROP = 1009;
    protected static final int PZS_ACTION_TO_LEFT_SIDE = 1007;
    protected static final int PZS_ACTION_TO_RIGHT_SIDE = 1008;
    protected static final int PZS_ACTION_TRANSLATE = 1002;
    protected static final int PZS_ACTION_TRANSLATE_TO_SCALE = 1004;
    public ImageScaleType defaultScaleType = ImageScaleType.FitCenter;
    public ImageScaleType doubleTapScaleType = ImageScaleType.TopCrop;
    GestureDetector gd;
    public boolean isVideo = false;
    private Context mContext;
    private Matrix mCurrentMatrix = new Matrix();
    private int mImageHeight;
    private int mImageWidth;
    private float mInitScaleSpan = MIN_SCALE_TO_SCREEN;
    private boolean mIsFirstDraw = true;
    private float mMaxScaleFactor = MAX_SCALE_TO_SCREEN;
    private PointF mMidPoint = new PointF();
    private float mMinScaleFactor = MIN_SCALE_TO_SCREEN;
    private Matrix mSavedMatrix = new Matrix();
    private PointF mStartPoint = new PointF();
    private RectF mTraslateLimitRect = new RectF();
    public TextView videoText = null;

    class C06181 extends SimpleOnGestureListener {
        C06181() {
        }

        public boolean onSingleTapConfirmed(MotionEvent ev) {
            ((AbstractGalleryActivity) PZSImageView.this.mContext).topBtn();
            return true;
        }
    }

    enum ImageScaleType {
        FitCenter,
        TopCrop,
        CenterCrop
    }

    public PZSImageView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public PZSImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public PZSImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
        Matrix mat = getImageMatrix();
        mat.reset();
        setImageMatrix(mat);
        this.gd = new GestureDetector(this.mContext, new C06181());
        this.videoText = new TextView(getContext());
        this.videoText.setText(getResources().getString(C0419R.string.NoResult));
        this.videoText.setTextColor(-1);
        this.videoText.setBackgroundColor(1711276032);
        this.videoText.setGravity(17);
        this.videoText.setTextSize(2, 24.0f);
        this.videoText.setText(getResources().getString(C0419R.string.NoChats));
        this.videoText.setLayoutParams(new LayoutParams(-1, -1));
    }

    public void setImageBitmap(Bitmap bitmap, String imgKey) {
        super.setImageBitmap(bitmap, imgKey);
        this.mIsFirstDraw = true;
        if (bitmap != null) {
            this.mImageWidth = bitmap.getWidth();
            this.mImageHeight = bitmap.getHeight();
            return;
        }
        this.mImageWidth = getWidth();
        this.mImageHeight = getHeight();
    }

    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        this.mIsFirstDraw = true;
        if (bitmap != null) {
            this.mImageWidth = bitmap.getWidth();
            this.mImageHeight = bitmap.getHeight();
            return;
        }
        this.mImageWidth = getWidth();
        this.mImageHeight = getHeight();
    }

    public void setImageBitmapMy(Bitmap bitmap) {
        super.setImageBitmapMy(bitmap);
        this.mIsFirstDraw = true;
        if (bitmap != null) {
            this.mImageWidth = bitmap.getWidth();
            this.mImageHeight = bitmap.getHeight();
            return;
        }
        this.mImageWidth = getWidth();
        this.mImageHeight = getHeight();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mIsFirstDraw = true;
        if (getDrawable() == null) {
            this.mImageHeight = h;
            this.mImageWidth = w;
        }
    }

    protected void onDraw(Canvas canvas) {
        if (this.mIsFirstDraw) {
            this.mIsFirstDraw = false;
            if (this.defaultScaleType == ImageScaleType.FitCenter) {
                fitCenter();
            } else if (this.defaultScaleType == ImageScaleType.TopCrop) {
                topCrop();
            } else if (this.defaultScaleType == ImageScaleType.CenterCrop) {
                centerCrop();
            }
            calculateScaleFactorLimit();
            validateMatrix();
        }
        setImageMatrix(this.mCurrentMatrix);
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            FileLog.m799e("tmessages", e);
            FileLog.m800e("tmessages", "trying draw " + this.currentPath);
        }
    }

    private void calculateScaleFactorLimit() {
        this.mMaxScaleFactor = Math.max((((float) getHeight()) * MAX_SCALE_TO_SCREEN) / ((float) this.mImageHeight), (((float) getWidth()) * MAX_SCALE_TO_SCREEN) / ((float) this.mImageWidth));
        this.mMinScaleFactor = Math.min((((float) getHeight()) * MIN_SCALE_TO_SCREEN) / ((float) this.mImageHeight), (((float) getWidth()) * MIN_SCALE_TO_SCREEN) / ((float) this.mImageWidth));
        if (getDrawable() == null) {
            this.mMaxScaleFactor = this.mMinScaleFactor;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.gd.onTouchEvent(event)) {
            touchAction(parseMotionEvent(event), event);
        }
        return true;
    }

    private void touchAction(int action, MotionEvent event) {
        switch (action) {
            case 100:
                initGestureAction(event.getX(), event.getY());
                break;
            case 1001:
                handleScale(event);
                break;
            case 1002:
                handleTranslate(event);
                break;
            case PZS_ACTION_SCALE_TO_TRANSLATE /*1003*/:
                int activeIndex = event.getActionIndex() == 0 ? 1 : 0;
                initGestureAction(event.getX(activeIndex), event.getY(activeIndex));
                break;
            case PZS_ACTION_TRANSLATE_TO_SCALE /*1004*/:
                initGestureAction(event.getX(), event.getY());
                break;
            case PZS_ACTION_FIT_CENTER /*1005*/:
                fitCenter();
                initGestureAction(event.getX(), event.getY());
                break;
            case PZS_ACTION_CENTER_CROP /*1006*/:
                centerCrop();
                initGestureAction(event.getX(), event.getY());
                break;
            case PZS_ACTION_TO_LEFT_SIDE /*1007*/:
                toLeftSide();
                break;
            case PZS_ACTION_TO_RIGHT_SIDE /*1008*/:
                toRightSide();
                break;
            case PZS_ACTION_TOP_CROP /*1009*/:
                topCrop();
                initGestureAction(event.getX(), event.getY());
                break;
        }
        validateMatrix();
        updateMatrix();
    }

    private int parseDoubleTapMotionEvent(MotionEvent ev) {
        float[] values = new float[9];
        this.mCurrentMatrix.getValues(values);
        float scaleNow = values[0];
        float scaleX = ((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) / ((float) this.mImageWidth);
        float scaleY = ((float) ((getHeight() - getPaddingTop()) - getPaddingBottom())) / ((float) this.mImageHeight);
        if (scaleNow >= Math.max(scaleX, scaleY) || scaleNow >= Math.max(scaleX, scaleY) || this.doubleTapScaleType == ImageScaleType.FitCenter) {
            return PZS_ACTION_FIT_CENTER;
        }
        if (this.doubleTapScaleType == ImageScaleType.TopCrop) {
            return PZS_ACTION_TOP_CROP;
        }
        if (this.doubleTapScaleType == ImageScaleType.CenterCrop) {
            return PZS_ACTION_CENTER_CROP;
        }
        return PZS_ACTION_FIT_CENTER;
    }

    private int parseMotionEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEventCompat.ACTION_MASK) {
            case 0:
                return 100;
            case 1:
            case 6:
                if (ev.getPointerCount() == 2) {
                    return PZS_ACTION_SCALE_TO_TRANSLATE;
                }
                return 100;
            case 2:
                if (ev.getPointerCount() == 1) {
                    return 1002;
                }
                return ev.getPointerCount() == 2 ? 1001 : 0;
            case 5:
                return PZS_ACTION_TRANSLATE_TO_SCALE;
            default:
                return 0;
        }
    }

    protected void initGestureAction(float x, float y) {
        this.mSavedMatrix.set(this.mCurrentMatrix);
        this.mStartPoint.set(x, y);
        this.mInitScaleSpan = 0.0f;
    }

    protected void handleScale(MotionEvent event) {
        if (!this.isVideo) {
            float newSpan = spacing(event);
            if (newSpan < MIN_SCALE_SPAN) {
                return;
            }
            if (this.mInitScaleSpan == 0.0f) {
                this.mInitScaleSpan = newSpan;
                midPoint(this.mMidPoint, event);
                return;
            }
            float scale = normalizeScaleFactor(this.mSavedMatrix, newSpan, this.mInitScaleSpan);
            this.mCurrentMatrix.set(this.mSavedMatrix);
            this.mCurrentMatrix.postScale(scale, scale, this.mMidPoint.x, this.mMidPoint.y);
        }
    }

    private float normalizeScaleFactor(Matrix curMat, float newSpan, float stdSpan) {
        float[] values = new float[9];
        curMat.getValues(values);
        float scale = values[0];
        if (stdSpan == newSpan) {
            return scale;
        }
        float newScaleFactor = newSpan / stdSpan;
        float candinateScale = scale * newScaleFactor;
        if (candinateScale > this.mMaxScaleFactor) {
            return this.mMaxScaleFactor / scale;
        }
        return candinateScale < this.mMinScaleFactor ? this.mMinScaleFactor / scale : newScaleFactor;
    }

    protected void handleTranslate(MotionEvent event) {
        this.mCurrentMatrix.set(this.mSavedMatrix);
        this.mCurrentMatrix.postTranslate(event.getX() - this.mStartPoint.x, event.getY() - this.mStartPoint.y);
    }

    public boolean getOnLeftSide() {
        float[] values = new float[9];
        this.mCurrentMatrix.getValues(values);
        return values[2] >= this.mTraslateLimitRect.right;
    }

    public boolean getOnRightSide() {
        float[] values = new float[9];
        this.mCurrentMatrix.getValues(values);
        return values[2] <= this.mTraslateLimitRect.left;
    }

    private void validateMatrix() {
        float[] values = new float[9];
        this.mCurrentMatrix.getValues(values);
        float scale = values[0];
        float tranX = values[2];
        float tranY = values[5];
        int imageHeight = (int) (((float) this.mImageHeight) * scale);
        int imageWidth = (int) (((float) this.mImageWidth) * scale);
        if (imageHeight == 0 || imageWidth == 0) {
            imageHeight = getHeight();
            imageWidth = getWidth();
        }
        this.mTraslateLimitRect.setEmpty();
        if (imageHeight > getHeight()) {
            this.mTraslateLimitRect.top = (float) (((getHeight() - imageHeight) - getPaddingTop()) - getPaddingBottom());
            this.mTraslateLimitRect.bottom = 0.0f;
        } else {
            RectF rectF = this.mTraslateLimitRect;
            float height = ((float) (((getHeight() - imageHeight) - getPaddingTop()) - getPaddingBottom())) / MAX_SCALE_TO_SCREEN;
            this.mTraslateLimitRect.bottom = height;
            rectF.top = height;
        }
        if (imageWidth > getWidth()) {
            this.mTraslateLimitRect.left = (float) (((getWidth() - imageWidth) - getPaddingRight()) - getPaddingLeft());
            this.mTraslateLimitRect.right = 0.0f;
        } else {
            rectF = this.mTraslateLimitRect;
            height = ((float) (((getWidth() - imageWidth) - getPaddingLeft()) - getPaddingRight())) / MAX_SCALE_TO_SCREEN;
            this.mTraslateLimitRect.right = height;
            rectF.left = height;
        }
        float newTranX = Math.min(Math.max(tranX, this.mTraslateLimitRect.left), this.mTraslateLimitRect.right);
        float newTranY = Math.min(Math.max(tranY, this.mTraslateLimitRect.top), this.mTraslateLimitRect.bottom);
        values[2] = newTranX;
        values[5] = newTranY;
        this.mCurrentMatrix.setValues(values);
        if (!this.mTraslateLimitRect.contains(tranX, tranY)) {
            this.mStartPoint.offset(tranX - newTranX, tranY - newTranY);
        }
    }

    protected void updateMatrix() {
        setImageMatrix(this.mCurrentMatrix);
    }

    protected void fitCenter() {
        this.mCurrentMatrix.reset();
        float scale = Math.min(((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) / ((float) this.mImageWidth), ((float) ((getHeight() - getPaddingTop()) - getPaddingBottom())) / ((float) this.mImageHeight));
        float dx = (((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) - (((float) this.mImageWidth) * scale)) / MAX_SCALE_TO_SCREEN;
        float dy = (((float) ((getHeight() - getPaddingTop()) - getPaddingBottom())) - (((float) this.mImageHeight) * scale)) / MAX_SCALE_TO_SCREEN;
        this.mCurrentMatrix.postScale(scale, scale);
        this.mCurrentMatrix.postTranslate(dx, dy);
        setImageMatrix(this.mCurrentMatrix);
    }

    public void toLeftSide() {
        float[] values = new float[9];
        this.mCurrentMatrix.getValues(values);
        this.mCurrentMatrix.postTranslate(this.mTraslateLimitRect.right - values[2], 0.0f);
        setImageMatrix(this.mCurrentMatrix);
    }

    public void toRightSide() {
        float[] values = new float[9];
        this.mCurrentMatrix.getValues(values);
        this.mCurrentMatrix.postTranslate(this.mTraslateLimitRect.left - values[2], 0.0f);
        setImageMatrix(this.mCurrentMatrix);
    }

    protected void centerCrop() {
        this.mCurrentMatrix.reset();
        float scale = Math.max(((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) / ((float) this.mImageWidth), ((float) ((getHeight() - getPaddingTop()) - getPaddingBottom())) / ((float) this.mImageHeight));
        float dx = (((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) - (((float) this.mImageWidth) * scale)) / MAX_SCALE_TO_SCREEN;
        float dy = (((float) ((getHeight() - getPaddingTop()) - getPaddingBottom())) - (((float) this.mImageHeight) * scale)) / MAX_SCALE_TO_SCREEN;
        this.mCurrentMatrix.postScale(scale, scale);
        this.mCurrentMatrix.postTranslate(dx, dy);
        setImageMatrix(this.mCurrentMatrix);
    }

    protected void topCrop() {
        this.mCurrentMatrix.reset();
        float scale = Math.max(((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) / ((float) this.mImageWidth), ((float) ((getHeight() - getPaddingTop()) - getPaddingBottom())) / ((float) this.mImageHeight));
        this.mCurrentMatrix.postScale(scale, scale);
        this.mCurrentMatrix.postTranslate(0.0f, 0.0f);
        setImageMatrix(this.mCurrentMatrix);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt((x * x) + (y * y));
    }

    private void midPoint(PointF point, MotionEvent event) {
        point.set((event.getX(0) + event.getX(1)) / MAX_SCALE_TO_SCREEN, (event.getY(0) + event.getY(1)) / MAX_SCALE_TO_SCREEN);
    }
}
