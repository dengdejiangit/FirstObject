package com.example.administrator.testproject.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.administrator.testproject.R;
import com.example.administrator.testproject.xtimer.IXTimerListener;
import com.example.administrator.testproject.xtimer.XTimer;

/**
 * Create  by  User:WS  Data:2019/6/14
 */

public class BatteryView extends View {

    private int mItemHeight;
    private int mItemCount;
    private int mItemWidth;
    private Paint mPaint;
    private RectF mRectF;
    private int count = 0;
    private boolean rechargeStash = false;
    private XTimer mXTimer;

    public BatteryView(Context context) {
        super(context);
        init(context, null);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BatteryView);
        mItemHeight = (int) typedArray.getDimension(R.styleable.BatteryView_item_height, ConvertUtils.dp2px(10));
        mItemWidth = (int) typedArray.getDimension(R.styleable.BatteryView_item_width, ConvertUtils.dp2px(60));
        mItemCount = typedArray.getInt(R.styleable.BatteryView_item_count, 8);
        typedArray.recycle();

        mPaint = new Paint();
        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = mItemHeight * mItemCount + (mItemCount + 3) * mItemHeight / 2 + mItemHeight;
        int width = mItemWidth * 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);
        mRectF.set(0, 0, getWidth(), getHeight());
        canvas.drawRect(mRectF, mPaint);

        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        mRectF.set((int) (mItemWidth / 4 * 3), (int) (mItemHeight / 2), (int) (mItemWidth / 4 * 5), (int) (mItemHeight / 2 * 3));
        canvas.drawRect(mRectF, mPaint);

        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(ConvertUtils.dp2px(2));
        mRectF.set((int) (mItemWidth / 2), (int) (mItemHeight / 2 * 3), (int) (mItemWidth / 2 * 3), (int) (mItemHeight * 13));
        canvas.drawRect(mRectF, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        for (int i = 0; i < mItemCount - count; i++) {
            mRectF.set((int) (mItemWidth / 2), (int) (mItemHeight / 2 * (3 + i) + mItemHeight * i), (int) (mItemWidth / 2 * 3), (int) (mItemHeight / 2 * (3 + i) + mItemHeight * (i + 1)));
            canvas.drawRect(mRectF, mPaint);
        }

        mPaint.setColor(Color.GREEN);
        for (int i = mItemCount - count; i < mItemCount; i++) {
            mRectF.set((int) (mItemWidth / 2), (int) (mItemHeight / 2 * (3 + i) + mItemHeight * i), (int) (mItemWidth / 2 * 3), (int) (mItemHeight / 2 * (3 + i) + mItemHeight * (i + 1)));
            canvas.drawRect(mRectF, mPaint);
        }
    }

    public boolean getRechargeStash(){
        return rechargeStash;
    }

    public void stop(){
        rechargeStash = false;
        mXTimer.stop();
    }

    public void begin(){
        rechargeStash = true;
        mXTimer = new XTimer();
        mXTimer.stop();
        mXTimer.startRepeat(1000, 1000, new IXTimerListener() {
            @Override
            public void onTimerComplete() {

            }

            @Override
            public void onTimerRepeatComplete() {
                if (count >= mItemCount){
                    count = 0;
                }
                count = count + 1;
                invalidate();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mXTimer != null){
            mXTimer.stop();
        }
    }
}
