package com.example.administrator.testproject.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * Create  by  User:WS  Data:2019/6/14
 */

public class TestView extends View {

    private Paint mPaint;

    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    private float[] mPts;
    private static final int SEGS = 32;
    private static final int X = 0;
    private static final int Y = 1;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        buildPoints();
        //使用Canvas绘图
        //画布移动到(10,10)位置
        canvas.translate(10, 10);
        canvas.drawColor(Color.WHITE);
        //创建红色画笔，使用单像素宽度，绘制直线
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(0);
        canvas.drawLines(mPts, mPaint);
        //创建蓝色画笔，宽度为3，绘制相关点
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(3);
        canvas.drawPoints(mPts, mPaint);
        //创建Path, 并沿着path显示文字信息
        RectF rectF = new RectF(getWidth()/2, getHeight()/2, getWidth(), getHeight());
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectF,paint);
        Path path = new Path();
        path.addArc(rectF, 180, 180);
        mPaint.setTextSize(ConvertUtils.sp2px(18));
        mPaint.setColor(Color.BLUE);
        canvas.drawTextOnPath("在自定义View中使用Canvas对象绘图实例", path, 0, 0, mPaint);
    }

    private void buildPoints() {
        float widthSize = getWidth();
        float heightSize = getHeight();
        //生成一系列点
        final int ptCount = (SEGS + 1) * 2;
        mPts = new float[ptCount * 2];

        float widthValue = 0;
        float heightValue = 0;
        final float widthDelta = widthSize / SEGS;
        final float heightDelta = heightSize / SEGS;
        for (int i = 0; i <= SEGS; i++) {
            mPts[i * 4 + X] = widthSize - widthValue;
            mPts[i * 4 + Y] = 0;
            mPts[i * 4 + X + 2] = 0;
            mPts[i * 4 + Y + 2] = heightValue;
            widthValue += widthDelta;
            heightValue += heightDelta;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);

        //对match_parent进行监控，进行重新设置，对应AT_MOST
        //wrap_content对应EXACTLY
//        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(300, 300);
//        } else if (widthMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(300, heightSize);
//        } else if (heightMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(widthSize, 300);
//        }

        //有两个参数size和measureSpec
        //1、size表示View的默认大小，它的值是通过`getSuggestedMinimumWidth()方法来获取的，之后我们再分析。
        //2、measureSpec则是我们之前分析的MeasureSpec，里面存储了View的测量值以及测量模式
        int heightDefaultSize = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int widthDefaultSize = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        //当View没有设置背景时，默认大小就是mMinWidth，这个值对应Android:minWidth属性，如果没有设置时默认为0.
        //如果有设置背景，则默认大小为mMinWidth和mBackground.getMinimumWidth()当中的较大值。
        int suggestedMinimumHeight = getSuggestedMinimumHeight();
        int suggestedMinimumWidth = getSuggestedMinimumWidth();
    }
}
