package com.example.administrator.testproject.view;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Create  by  User:WS  Data:2019/6/21
 */

public class NewBubbleView extends ViewGroup {

    private int mMeasuredHeight;
    private int mMeasuredWidth;

    public NewBubbleView(Context context) {
        super(context);
    }

    public NewBubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredHeight = getMeasuredHeight();
        mMeasuredWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
