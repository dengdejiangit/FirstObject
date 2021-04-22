package com.example.administrator.testproject.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.example.administrator.testproject.util.GeometryUtil;

public class DragDotView extends View {

    private PointF mFixedCircle = new PointF(200f, 200f);
    float mFixedRadius = 14f;
    private PointF mDragCircle = new PointF(200f, 200f);
    float mDragRadius = 20f;

    private Paint mPaint;
    private boolean isDisappear;
    private boolean isOutToRange;
    private float farestDistance = 600f;
    private Path mPath;

    public DragDotView(Context context) {
        super(context);
        init();
    }

    public DragDotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragDotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setColor(Color.RED);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(200,200,600,mPaint);
        mPaint.setColor(Color.RED);

        float distance = getTempFiexdCircle();
        float yOffset = mFixedCircle.y - mDragCircle.y;
        float xOffset = mFixedCircle.x - mDragCircle.x;

        Double lineK = null;
        if (xOffset != 0) {
            lineK = (double) yOffset / xOffset;
        }

        PointF[] fixedPoints = GeometryUtil.getIntersectionPoints(mFixedCircle, distance, lineK);
        PointF[] dragPoints = GeometryUtil.getIntersectionPoints(mDragCircle, mDragRadius, lineK);
        PointF controlPoint = GeometryUtil.getMiddlePoint(mDragCircle, mFixedCircle);

        if (!isDisappear) {
            canvas.drawCircle(mDragCircle.x, mDragCircle.y, mDragRadius, mPaint);

            if (!isOutToRange) {
                canvas.drawCircle(mFixedCircle.x, mFixedCircle.y, distance, mPaint);

                mPath.reset();
                mPath.moveTo(fixedPoints[0].x, fixedPoints[0].y);
                mPath.quadTo(controlPoint.x, controlPoint.y, dragPoints[0].x, dragPoints[0].y);
                mPath.lineTo(dragPoints[1].x, dragPoints[1].y);
                mPath.quadTo(controlPoint.x, controlPoint.y, fixedPoints[1].x, fixedPoints[1].y);
                mPath.close();
                canvas.drawPath(mPath, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateDragCircle(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                updateDragCircle(x, y);

                float distance = GeometryUtil.getDistanceBetween2Points(mDragCircle, mFixedCircle);
                if (distance > farestDistance) {
                    isOutToRange = true;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isOutToRange) {
                    isOutToRange = false;
                    float d = GeometryUtil.getDistanceBetween2Points(mDragCircle, mFixedCircle);
                    if (d > farestDistance) {
                        isDisappear = true;
                        invalidate();
                    } else {
                        updateDragCircle(mFixedCircle.x, mFixedCircle.y);
                        isDisappear = false;
                    }

                } else {
                    final PointF tempDragCircle = new PointF(mDragCircle.x, mDragCircle.y);
                    ValueAnimator mAnim = ValueAnimator.ofFloat(1.0f);
                    mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float percent = animation.getAnimatedFraction();
                            PointF p = GeometryUtil.getPointByPercent(tempDragCircle, mFixedCircle, percent);
                            updateDragCircle(p.x, p.y);
                        }
                    });
                    //差值器，这个是设置弹性的
                    mAnim.setInterpolator(new OvershootInterpolator(4));
                    mAnim.setDuration(500);
                    mAnim.start();
                }
                break;
        }
        return true;
    }

    private void updateDragCircle(float rawX, float rawY) {
        mDragCircle.set(rawX, rawY);
        invalidate();
    }

    private float getTempFiexdCircle() {
        float instance = GeometryUtil.getDistanceBetween2Points(mDragCircle, mFixedCircle);
        instance = Math.min(instance, farestDistance);
        float percent = instance / farestDistance;

        return evaluate(percent, mFixedRadius, mFixedRadius * 0.2);

    }

    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

}
