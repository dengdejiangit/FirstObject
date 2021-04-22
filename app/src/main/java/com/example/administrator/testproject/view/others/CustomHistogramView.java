package com.example.administrator.testproject.view.others;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.testproject.R;

import java.util.ArrayList;

/**
 * Create  by
 *
 * @author User:WS
 * @date Data:2019/7/18
 */

public class CustomHistogramView extends View {

    private int mLeftTextColor;
    private int mBottomTextColor;
    private int mCenterLineColor;
    private int mBottomLineColor;
    private int mCylinderColor;
    private float mLeftTextSize;
    private float mBottomTextSize;
    private float mCenterLineWidth;
    private float mBottomLineWidth;

    private Paint mLeftTextPaint;
    private Paint mBottomTextPaint;
    private Paint mCenterLinePaint;
    private Paint mBottomLinePaint;
    private Paint mCylinderPaint;

    private int paddingLeft;
    private int paddingBottom;
    private int paddingRight;
    private int mWidthSize;
    private int mHeightSize;

    private ArrayList<CylinderData> mCylinderData = new ArrayList<>();
    private float mChartHeight;
    private int mChartWidth;
    private int mBeginHeight;


    private boolean isScrolled = false;
    private float startXDown;
    private float mStartYDown;
    private long startTime;
    private boolean isFling;
    private float slide;
    private float tempLength;
    private HorizontalScrollRunnable horizontalScrollRunnable;
    private float mAveWidth;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startXDown = event.getX();
                mStartYDown = event.getY();
                startTime = System.currentTimeMillis();
                if (isFling) {
                    removeCallbacks(horizontalScrollRunnable);
                    isFling = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float currMove = event.getX();

                if (isScrolled) {
                    slide += currMove - startXDown;
                }

                if ((currMove - startXDown) > 0) {
                    if (slide > 0) {
                        slide = 0;
                    }
                } else {
                    if (-slide > (mCylinderData.size() * mAveWidth) - mChartWidth) {
                        slide = -(mCylinderData.size() * mAveWidth) + mChartWidth;
                    }
                }
                tempLength = currMove - startXDown;
                invalidate();

                startXDown = currMove;
                break;
            case MotionEvent.ACTION_UP:
                if (startXDown == event.getX() && mStartYDown == event.getY()){
                    return false;
                }
                long endTime = System.currentTimeMillis();
                float speed = tempLength / (endTime - startTime) * 1000;
                if (Math.abs(speed) > 100 && !isFling && mWidthSize < (mCylinderData.size() * mAveWidth)) {
                    this.post(horizontalScrollRunnable = new HorizontalScrollRunnable(speed));
                }
                break;
            default:
                break;
        }
        return true;
    }

    private class HorizontalScrollRunnable implements Runnable {

        private float speed;

        HorizontalScrollRunnable(float speed) {
            this.speed = speed;
        }

        @Override
        public void run() {
            if (Math.abs(speed) < 30) {
                isFling = false;
                return;
            }
            isFling = true;
            slide += speed / 15;
            speed = speed / 1.15f;
            if ((speed) > 0) {
                if (slide > 0) {
                    slide = 0;
                }
            } else {
                if (-slide > (mCylinderData.size() * mAveWidth) - mChartWidth) {
                    slide = -(mCylinderData.size() * mAveWidth) + mChartWidth;
                }
            }
            postDelayed(this, 20);
            invalidate();
        }
    }

    private void drawCylinder(Canvas canvas) {
        if (mCylinderData.size() > 0) {
            drawCenterLine(canvas);

            //在图表中平均每组数据所占宽度
            if (isScrolled) {
                mAveWidth = dp2Px(40);
            } else {
                mAveWidth = (float) ((mChartWidth - chartSpaceLeft - chartSpaceLeft) / mCylinderData.size());
            }

            for (int i = 0; i < mCylinderData.size(); i++) {
                float value = (mCylinderData.get(i).getValue() / (float) 15000);
                int left = (int) (i * mAveWidth + chartSpaceLeft + paddingLeft + leftSpace);
                int top = (int) (mBeginHeight - bottomSpaceLine - (mChartHeight) * value);
                int right = (int) (i * mAveWidth + chartSpaceLeft + mAveWidth / 2 + paddingLeft + leftSpace);
                int bottom = (mBeginHeight - bottomSpaceLine);

                if (left + slide > paddingLeft + leftSpace) {
                    left = (int) (left + slide);
                    right = (int) (right + slide);
                } else if (left + slide < paddingLeft + leftSpace && right + slide > paddingLeft + leftSpace) {
                    left = paddingLeft + leftSpace;
                    right = (int) (right + slide);
                } else if (right + slide < paddingLeft + leftSpace) {
                    continue;
                }

                canvas.drawRoundRect(new RectF(left, top, right, bottom), chartCornerSpace, chartCornerSpace, mCylinderPaint);
                canvas.drawText(mCylinderData.get(i).getParameter(), left,
                        mHeightSize - paddingBottom, mBottomTextPaint);
            }

        } else {
            drawNoData(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightSize = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(mWidthSize, mHeightSize);
        int paddingTop = getPaddingTop();
        paddingLeft = getPaddingLeft();
        paddingBottom = getPaddingBottom();
        paddingRight = getPaddingRight();

        mBeginHeight = mHeightSize - paddingBottom - bottomSpace;
        //图表高度 = 视图高度 - 距底部 - 距顶部 - 底部文字及间距 - 底部线距圆柱距离
        mChartHeight = mBeginHeight - paddingTop - bottomSpaceLine;
        //图表高宽度 = 视图宽度 - 距左 - 距右 - 右边文字及间距
        mChartWidth = mWidthSize - paddingLeft - paddingRight - leftSpace;

    }

    public CustomHistogramView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomHistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomHistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomHistogramView);
        mLeftTextColor = typedArray.getColor(R.styleable.CustomHistogramView_histogram_left_text, Color.parseColor("#FFFFFF"));
        mBottomTextColor = typedArray.getColor(R.styleable.CustomHistogramView_histogram_bottom_text, Color.parseColor("#FFFFFF"));
        mCenterLineColor = typedArray.getColor(R.styleable.CustomHistogramView_histogram_center_line, Color.parseColor("#FFFFFF"));
        mBottomLineColor = typedArray.getColor(R.styleable.CustomHistogramView_histogram_bottom_line, Color.parseColor("#FFFFFF"));
        mCylinderColor = typedArray.getColor(R.styleable.CustomHistogramView_histogram_cylinder, Color.parseColor("#FFFFFF"));

        mLeftTextSize = typedArray.getDimension(R.styleable.CustomHistogramView_histogram_left_text_size, dp2Px(12));
        mBottomTextSize = typedArray.getDimension(R.styleable.CustomHistogramView_histogram_bottom_text_size, dp2Px(12));
        mCenterLineWidth = typedArray.getDimension(R.styleable.CustomHistogramView_histogram_center_line_width, dp2Px(1));
        mBottomLineWidth = typedArray.getDimension(R.styleable.CustomHistogramView_histogram_bottom_line_width, dp2Px(2));
        typedArray.recycle();

        initPaint();
    }

    private int dp2Px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void initPaint() {
        mLeftTextPaint = new Paint();
        mLeftTextPaint.setTextSize(mLeftTextSize);
        mLeftTextPaint.setColor(mLeftTextColor);
        mLeftTextPaint.setAlpha((int) (255 * 0.5));
        mLeftTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mLeftTextPaint.setStyle(Paint.Style.FILL);
        mLeftTextPaint.setDither(true);

        mBottomTextPaint = new Paint();
        mBottomTextPaint.setTextSize(mBottomTextSize);
        mBottomTextPaint.setColor(mBottomTextColor);
        mBottomTextPaint.setAlpha((int) (255 * 0.6));
        mBottomTextPaint.setStrokeCap(Paint.Cap.ROUND);
        mBottomTextPaint.setStyle(Paint.Style.FILL);
        mBottomTextPaint.setDither(true);

        mCenterLinePaint = new Paint();
        mCenterLinePaint.setStrokeWidth(mCenterLineWidth);
        mCenterLinePaint.setColor(mCenterLineColor);
        mCenterLinePaint.setAlpha((int) (255 * 0.1));
        mCenterLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mCenterLinePaint.setStyle(Paint.Style.FILL);
        mCenterLinePaint.setDither(true);

        mBottomLinePaint = new Paint();
        mBottomLinePaint.setStrokeWidth(mBottomLineWidth);
        mBottomLinePaint.setColor(mBottomLineColor);
        mBottomLinePaint.setAlpha((int) (255 * 0.3));
        mBottomLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mBottomLinePaint.setStyle(Paint.Style.FILL);
        mBottomLinePaint.setDither(true);

        mCylinderPaint = new Paint();
        mCylinderPaint.setColor(mCylinderColor);
        mCylinderPaint.setStrokeCap(Paint.Cap.ROUND);
        mCylinderPaint.setStyle(Paint.Style.FILL);
        mCylinderPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBottomLine(canvas);
        drawLeftText(canvas);
        drawCylinder(canvas);
    }

    /**
     * 左边文字的宽度 + 左边文字距图表的间距
     */
    private int leftSpace = dp2Px(30);
    /**
     * 底部文字高度 + 底部文字距图表的间距
     */
    private int bottomSpace = dp2Px(24);
    /**
     * 左边文字向下偏移的距离
     */
    private int leftTextSpace = dp2Px(5);
    /**
     * 图表中圆柱距图表左右边框的间距
     */
    private int chartSpaceLeft = dp2Px(8);
    /**
     * 底部线距图表中圆柱的距离
     */
    private int bottomSpaceLine = dp2Px(3);
    /**
     * 图表中圆柱的圆角大小
     */
    private int chartCornerSpace = dp2Px(2);

    private void drawBottomLine(Canvas canvas) {
        float stop;
        if (isScrolled) {
            stop = mWidthSize;
        } else {
            stop = mWidthSize - paddingRight;
        }
        canvas.drawLine(paddingLeft + leftSpace, mBeginHeight,
                stop, mBeginHeight, mBottomLinePaint);
    }

    private void drawCenterLine(Canvas canvas) {
        float stop;
        if (isScrolled) {
            stop = mWidthSize;
        } else {
            stop = mWidthSize - paddingRight;
        }
        canvas.drawLine(paddingLeft + leftSpace, mBeginHeight - mChartHeight / 3 - bottomSpaceLine,
                stop, mBeginHeight - mChartHeight / 3 - bottomSpaceLine, mCenterLinePaint);
        canvas.drawLine(paddingLeft + leftSpace, mBeginHeight - mChartHeight / 3 * 2 - bottomSpaceLine,
                stop, mBeginHeight - mChartHeight / 3 * 2 - bottomSpaceLine, mCenterLinePaint);
        canvas.drawLine(paddingLeft + leftSpace, mBeginHeight - mChartHeight - bottomSpaceLine,
                stop, mBeginHeight - mChartHeight - bottomSpaceLine, mCenterLinePaint);
    }

    private void drawLeftText(Canvas canvas) {
        canvas.drawText("5k", paddingLeft, mBeginHeight - mChartHeight / 3 + leftTextSpace, mLeftTextPaint);
        canvas.drawText("10k", paddingLeft, mBeginHeight - mChartHeight / 3 * 2 + leftTextSpace, mLeftTextPaint);
        canvas.drawText("15k", paddingLeft, mBeginHeight - mChartHeight + leftTextSpace, mLeftTextPaint);
    }

    private void drawNoData(Canvas canvas) {
        canvas.drawText("Loading . . .", (float) (mWidthSize - paddingLeft - leftSpace) / 2,
                (float) (mBeginHeight) / 2, mLeftTextPaint);
    }

    public void setCylinderData(ArrayList<CylinderData> cylinderData, boolean scrolled) {
        isScrolled = scrolled;
        this.mCylinderData = cylinderData;
        postInvalidate();
    }

}
