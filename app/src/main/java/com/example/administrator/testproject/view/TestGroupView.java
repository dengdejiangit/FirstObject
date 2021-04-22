package com.example.administrator.testproject.view;


import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Create  by  User:WS  Data:2019/6/14
 */

public class TestGroupView extends ViewGroup {

    public TestGroupView(Context context) {
        super(context);
        init();
    }

    public TestGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestGroupView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int startHeight = 0;
        int startWidth = 0;
        for (int count = 0; count < getChildCount(); count++) {
            View childAt = getChildAt(count);
            childAt.layout(startHeight, startWidth, startHeight + childAt.getMeasuredWidth(), startWidth + childAt.getMeasuredHeight());
            startHeight = startWidth + childAt.getMeasuredHeight();
            startWidth = startWidth + childAt.getMeasuredHeight();
        }
    }
}
