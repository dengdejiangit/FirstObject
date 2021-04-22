package com.example.administrator.testproject.view;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Create  by
 *
 * @author User:WS
 * @date Data:2019/9/2
 */

public class DimAnimView extends View {

    private float mCenterHeightPoint;
    private float mCenterWidthPoint;
    private float mCenterCircleRadius;
    private Rect mRect;
    private Paint mDimAnimPaint;
    private Path mPath;
    private float ratio = 1;
    private ObjectAnimator mObjectAnimator;

    public DimAnimView(Context context) {
        super(context);
        init(context, null);
    }

    public DimAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        invalidate();
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        mDimAnimPaint = new Paint();
        mDimAnimPaint.setAntiAlias(true);
        mDimAnimPaint.setARGB(30, 255, 255, 255);

        mPath = new Path();
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDimAnim(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mCenterHeightPoint = (float) getMeasuredHeight() / 5 * 2;
        mCenterWidthPoint = (float) getMeasuredWidth() / 2;
        mCenterCircleRadius = (float) getMeasuredWidth() / 4;

    }

    private void drawDimAnim(Canvas canvas) {
        canvas.save();

        double height;
        if (ratio < 0.26) {
            height = mCenterCircleRadius * (ratio / 0.26);
        } else if (ratio > 0.26 && ratio < 0.4) {
            height = mCenterCircleRadius;
        } else if (ratio > 0.4 && ratio < 0.66) {
            height = mCenterCircleRadius * (1 - (ratio - 0.4) / 0.26);
        } else {
            height = 0;
        }

        mPath.addCircle(mCenterWidthPoint, mCenterHeightPoint, mCenterCircleRadius / 2, Path.Direction.CCW);
        canvas.clipPath(mPath);

        int left = (int) (mCenterWidthPoint - (mCenterCircleRadius / 2));
        int top = (int) (mCenterHeightPoint - (mCenterCircleRadius / 2) + height);
        int right = (int) (mCenterWidthPoint + (mCenterCircleRadius / 2));
        int bottom = (int) (mCenterHeightPoint + (mCenterCircleRadius / 2));
        mRect.set(left, top, right, bottom);

        canvas.drawRect(mRect, mDimAnimPaint);

        canvas.restore();
    }

    public void beginDimAnim(int animCount) {
        mObjectAnimator = ObjectAnimator.ofFloat(this, "ratio", 0, 1);
        mObjectAnimator.setDuration(3500);
        mObjectAnimator.setRepeatCount(animCount - 1);
        mObjectAnimator.start();
    }

    public void closeDimAnim() {
        mObjectAnimator.cancel();
    }
}
