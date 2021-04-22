package com.example.administrator.testproject.view;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.blankj.utilcode.util.ConvertUtils;

import java.util.ArrayList;

/**
 * Create  by  User:WS  Data:2019/6/20
 */

public class BubbleView extends View {

    private ArrayList<Bubble> mBubbleList = new ArrayList<>();
    private int mBubbleCount = 6;//气泡数量
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private ObjectAnimator mObjectAnimator;
    private boolean animStash = false;
    private Paint mPaint;
    private float progressBubble = 0;
    private float mWaterHeight = 0;
    private int mI;

    public BubbleView(Context context) {
        super(context);
        init(context,null);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attr){
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!animStash || mBubbleList == null || mBubbleList.size() == 0)return;

        mPaint.setAlpha((int) ((progressBubble > 0.5 ? (1 - progressBubble) : progressBubble) * 255));
        for (int i = 0; i < mBubbleList.size(); i++) {
            canvas.drawCircle(mBubbleList.get(i).x,mBubbleList.get(i).y - (float) ((mBubbleList.get(i).y + mMeasuredHeight - mWaterHeight) * progressBubble),
                    mBubbleList.get(i).size, mPaint);
        }
    }

    private void initBubble(){
        mBubbleList.clear();
        for (int i = 0; i < mBubbleCount; i++) {
            //随机 4到10 大小的气泡
            float bubbleSize = ConvertUtils.dp2px(4) + (float)(Math.random() * ConvertUtils.dp2px(6));

            float startX = (float)((mMeasuredWidth - ConvertUtils.dp2px(77))/2) + (float)(Math.random() * ConvertUtils.dp2px(77));
            float startY = mMeasuredHeight + (float)(mMeasuredHeight * Math.random());

            mBubbleList.add(new Bubble(startX,startY+bubbleSize,bubbleSize));
        }
    }

    class Bubble{
        float x;
        float y;
        float size;

        Bubble(float x, float y, float size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }
    }

    public void startAnim(float height){
        this.mWaterHeight = height;
        if (mObjectAnimator != null){
            mObjectAnimator.cancel();
        }
        initBubble();
        animStash = true;
        mObjectAnimator = ObjectAnimator.ofFloat(this, "progressBubble", 0, 1);
        mObjectAnimator.setDuration(3000);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animStash = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animStash = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mObjectAnimator.start();
    }

    public float getProgressBubble() {
        return progressBubble;
    }

    public void setProgressBubble(float progressBubble) {
        this.progressBubble = progressBubble;
        postInvalidate();
    }


}
