package com.example.administrator.testproject.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Create  by
 *
 * @author User:WS
 * @date Data:2019/7/31
 */

public class WaterShape extends View {

    private Paint mPaint;
    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private Path mPathDrop;

    public WaterShape(Context context) {
        super(context);
        init();
    }

    public WaterShape(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterShape(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPathDrop = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawShape(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();

        mPathDrop.reset();
        int point = (mMeasuredWidth / 2);
        int yCenter = mMeasuredHeight / 4 * 3;

        mPathDrop.moveTo(point, 0);
        mPathDrop.quadTo(-(int) (mMeasuredWidth / 4), yCenter, point, mMeasuredHeight);
        mPathDrop.quadTo((int) (mMeasuredWidth / 4 * 5), yCenter, point, 0);
    }


    private void drawShape(Canvas canvas) {

        canvas.drawPath(mPathDrop, mPaint);
    }
}
