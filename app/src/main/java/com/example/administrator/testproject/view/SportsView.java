package com.example.administrator.testproject.view;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.View;

/**
 * Create  by  User:WS  Data:2019/6/17
 */

public class SportsView extends View {
    private RectF mRectF;
    private Paint mPaint;

    private float progressTop = 0f;
    private float progressBottom = 0f;
    private ObjectAnimator mAnimatorTop;
    private ObjectAnimator mAnimatorBottom;
    private boolean mViewType = false;
    private float mAngle = 60;
    private float mHalfAngle = mAngle / 2;

    public SportsView(Context context) {
        super(context);
        init(context, null);
    }

    public SportsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SportsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mRectF = new RectF();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);

        initAnimation();
    }

    private void initAnimation() {
        mAnimatorTop = ObjectAnimator.ofFloat(this, "progressTop", 0, mHalfAngle + 1);
        mAnimatorTop.setDuration(2000).setRepeatCount(ValueAnimator.INFINITE);

        mAnimatorBottom = ObjectAnimator.ofFloat(this, "progressBottom", 0, -(mHalfAngle - 1));
        mAnimatorBottom.setDuration(2000).setRepeatCount(ValueAnimator.INFINITE);

        mAnimatorTop.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                mViewType = !mViewType;
            }
        });
    }

    public void startAnim() {
        if (mAnimatorTop != null && mAnimatorBottom != null) {
            mAnimatorTop.start();
            mAnimatorBottom.start();
        }
    }

    public void endAnim() {
        if (mAnimatorTop != null && mAnimatorBottom != null) {
            mAnimatorTop.cancel();
            mAnimatorBottom.cancel();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = (float) ((getWidth() - getHeight() / 2) / 2);
        float y = (float) (getHeight() / 4);
        mRectF.set(x, y, getWidth() - x, getHeight() - y);

        mPaint.setColor(Color.BLACK);

        canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (getWidth() - 2 * x - 1) / 3, mPaint);
        if (mViewType) {
            mPaint.setColor(Color.WHITE);
            canvas.drawArc(mRectF, 0, progressTop, true, mPaint);
            canvas.drawArc(mRectF, 1, progressBottom, true, mPaint);
        } else {
            mPaint.setColor(Color.WHITE);
            canvas.drawArc(mRectF, -(mHalfAngle - 1), mHalfAngle, true, mPaint);
            canvas.drawArc(mRectF, mHalfAngle + 1, -mHalfAngle, true, mPaint);
            mPaint.setColor(Color.BLACK);
            canvas.drawArc(mRectF, mHalfAngle, -progressTop, true, mPaint);
            canvas.drawArc(mRectF, -mHalfAngle, -progressBottom, true, mPaint);
        }
    }

    public float getProgressTop() {
        return progressTop;
    }

    public void setProgressTop(float progressTop) {
        this.progressTop = progressTop;
        invalidate();
    }

    public float getProgressBottom() {
        return progressBottom;
    }

    public void setProgressBottom(float progressBottom) {
        this.progressBottom = progressBottom;
    }
}

