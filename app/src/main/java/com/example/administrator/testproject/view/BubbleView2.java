package com.example.administrator.testproject.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.blankj.utilcode.util.ConvertUtils;

import java.util.ArrayList;

/**
 * Create  by  User:WS  Data:2019/6/20
 */

public class BubbleView2 extends View {

    private ArrayList<Bubble> mBubbleList = new ArrayList<>();
    private int mBubbleCount = 4;//气泡数量
    private ObjectAnimator mObjectAnimator;
    private boolean animStash = false;
    private Paint mPaint;
    private float progressBubble = 0;
    private float mBubbleMaxHeight = 0;

    public BubbleView2(Context context) {
        super(context);
        init();
    }

    public BubbleView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initBubble();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!animStash || mBubbleList == null || mBubbleList.size() == 0) return;

        float progress;
        if (progressBubble < 0.7) {
            progress = (float) (progressBubble / 0.6);
            onDrawBubble(canvas, 0, progress);
        }
        if (progressBubble > 0.1 && progressBubble < 0.8) {
            progress = (float) ((progressBubble - 0.1) / 0.6);
            onDrawBubble(canvas, 1, progress);
        }
        if (progressBubble > 0.2 && progressBubble < 0.9) {
            progress = (float) ((progressBubble - 0.2) / 0.6);
            onDrawBubble(canvas, 2, progress);
        }
        if (progressBubble > 0.3 && progressBubble < 1) {
            progress = (float) ((progressBubble - 0.3) / 0.6);
            onDrawBubble(canvas, 3, progress);
        }
    }

    private void onDrawBubble(Canvas canvas, int index, float progress) {
        float height = getMeasuredHeight() - ((getMeasuredHeight() - mBubbleMaxHeight) * progress);
        if (height <= mBubbleMaxHeight) return;
        mPaint.setAlpha((int) ((1 - progress) * 155));
        canvas.drawCircle(mBubbleList.get(index).x, height, mBubbleList.get(index).size, mPaint);
    }

    private void initBubble() {
        mBubbleList.clear();
        for (int i = 0; i < mBubbleCount; i++) {
            double random = Math.random();
            float bubbleSize = ConvertUtils.dp2px(3) + (float) (random * ConvertUtils.dp2px(3));

            float startX = (float) (Math.random() * ConvertUtils.dp2px(77));
            if (startX < bubbleSize) {
                startX = startX + bubbleSize;
            } else if (startX > ConvertUtils.dp2px(77) - bubbleSize) {
                startX = startX - bubbleSize;
            }

            mBubbleList.add(new Bubble(startX, bubbleSize));
        }
    }

    public void startAnim(float progress) {
        float p = progress >= 0 ? progress : 0;
        this.mBubbleMaxHeight = ConvertUtils.dp2px(447) * p;
        if (mObjectAnimator != null) {
            mObjectAnimator.cancel();
        }
        animStash = true;
        mObjectAnimator = ObjectAnimator.ofFloat(this, "progressBubble", 0, 1);
        //动画时间波动范围    1000 -- 3500
        mObjectAnimator.setDuration((int) (1000 + 2500 * (1 - p)));
        mObjectAnimator.setInterpolator(new AccelerateInterpolator());
        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                initBubble();
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

    class Bubble {
        float x;
        float size;

        Bubble(float x, float size) {
            this.x = x;
            this.size = size;
        }
    }

    public float getProgressBubble() {
        return progressBubble;
    }

    public void setProgressBubble(float progressBubble) {
        this.progressBubble = progressBubble;
        postInvalidate();
    }
}
