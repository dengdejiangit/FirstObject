package com.example.administrator.testproject.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * Create  by
 *
 * @author User:Candymobi
 * @date Date:2020/10/15
 */

public class CubicView extends RelativeLayout {
    private int screenWidth;
    private Paint paint = new Paint();
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private int leftX;
    private int leftY;
    private int rightX;
    private int rightY;
    private PointF mPoint1;
    private PointF mPoint2;
    private PointF mPoint3;
    private PointF mPoint4;
    private PointF mPoint5;
    private PointF mPoint;
    private boolean isMoveLeft;
    private Path mPath1;
    private Path mPath2;
    private Path mPath3;
    private Path mPath;
    private boolean isAnimBegin = false;
    private boolean isShowTrack = false;

    public CubicView(Context context) {
        super(context);
        init();
    }

    public CubicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        mPoint = new PointF();
        mPoint1 = new PointF();
        mPoint2 = new PointF();
        mPoint3 = new PointF();
        mPoint4 = new PointF();
        mPoint5 = new PointF();

        mPath = new Path();
        mPath1 = new Path();
        mPath2 = new Path();
        mPath3 = new Path();
        setWillNotDraw(false);
    }

    public void startAnim() {
        isAnimBegin = true;
        isShowTrack = true;
        //一个随贝塞尔曲线的位移动画
        ValueAnimator bezierValueAnimator = getBezierValueAnimator();
        bezierValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimBegin = false;
            }
        });
        bezierValueAnimator.start();
    }

    //移动左边锚点
    public void moveLeft() {
        isMoveLeft = true;
    }

    //移动右边锚点
    public void moveRight() {
        isMoveLeft = false;
    }

    private ValueAnimator getBezierValueAnimator() {
        PointF pointLeft = new PointF();
        PointF pointRight = new PointF();
        pointLeft.x = leftX;
        pointLeft.y = leftY;
        pointRight.x = rightX;
        pointRight.y = rightY;

        BezierEvaluator evaluator = new BezierEvaluator(pointLeft, pointRight);

        endX = (float) (Math.random() * screenWidth);
        PointF randomStartPoint = new PointF(startX, startY);
        PointF randomEndPoint = new PointF(endX, endY);

        ValueAnimator animator = ValueAnimator.ofObject(evaluator, randomStartPoint, randomEndPoint);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(8000);
        return animator;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        screenWidth = getMeasuredWidth();
        int screenHeight = getMeasuredHeight();
        leftX = 0;
        leftY = 0;
        rightX = screenWidth;
        rightY = 0;

        startX = (float) (screenWidth / 2);
        startY = screenHeight;
        endX = (float) (Math.random() * screenWidth);
        endY = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (!isAnimBegin) {
                    if (isMoveLeft) {
                        leftX = (int) event.getX();
                        leftY = (int) event.getY();
                    } else {
                        rightX = (int) event.getX();
                        rightY = (int) event.getY();
                    }
                    invalidate();
                    isShowTrack = false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画4个点
        paint.setColor(Color.GRAY);
        canvas.drawCircle(startX, startY, 8, paint);
        canvas.drawCircle(endX, endY, 8, paint);
        canvas.drawCircle(leftX, leftY, 8, paint);
        canvas.drawCircle(rightX, rightY, 8, paint);

        //绘制连线
        paint.setStrokeWidth(3);
        canvas.drawLine(startX, startY, leftX, leftY, paint);
        canvas.drawLine(leftX, leftY, rightX, rightY, paint);
        canvas.drawLine(rightX, rightY, endX, endY, paint);

        //画二阶贝塞尔曲线
        paint.setColor(Color.GREEN);
        mPath.reset();
        mPath.moveTo(startX, startY);
        mPath.cubicTo(leftX, leftY, rightX, rightY, endX, endY);
        canvas.drawPath(mPath, paint);

        if (isShowTrack) {
            paint.setColor(Color.YELLOW);
            canvas.drawCircle(mPoint.x, mPoint.y, 6f, paint);

            paint.setColor(Color.RED);
            mPath1.reset();
            mPath1.moveTo(mPoint1.x, mPoint1.y);
            mPath1.lineTo(mPoint2.x, mPoint2.y);
            canvas.drawPath(mPath1, paint);

            mPath2.reset();
            mPath2.moveTo(mPoint2.x, mPoint2.y);
            mPath2.lineTo(mPoint3.x, mPoint3.y);
            canvas.drawPath(mPath2, paint);

            paint.setColor(Color.WHITE);
            mPath3.reset();
            mPath3.moveTo(mPoint4.x, mPoint4.y);
            mPath3.lineTo(mPoint5.x, mPoint5.y);
            canvas.drawPath(mPath3, paint);
        }
    }

    public class BezierEvaluator implements TypeEvaluator<PointF> {

        private PointF pointF1;
        private PointF pointF2;

        BezierEvaluator(PointF pointF1, PointF pointF2) {
            this.pointF1 = pointF1;
            this.pointF2 = pointF2;
        }

        @Override
        public PointF evaluate(float time, PointF startValue,
                               PointF endValue) {

            float timeLeft = 1.0f - time;

            mPoint.x = timeLeft * timeLeft * timeLeft * (startValue.x)
                    + 3 * timeLeft * timeLeft * time * (pointF1.x)
                    + 3 * timeLeft * time * time * (pointF2.x)
                    + time * time * time * (endValue.x);

            mPoint.y = timeLeft * timeLeft * timeLeft * (startValue.y)
                    + 3 * timeLeft * timeLeft * time * (pointF1.y)
                    + 3 * timeLeft * time * time * (pointF2.y)
                    + time * time * time * (endValue.y);

            getPoint(mPoint1, startValue, pointF1, time);
            getPoint(mPoint2, pointF1, pointF2, time);
            getPoint(mPoint3, pointF2, endValue, time);
            getPoint(mPoint4, mPoint1, mPoint2, time);
            getPoint(mPoint5, mPoint2, mPoint3, time);

            invalidate();
            return mPoint;
        }

        private void getPoint(PointF pointF, PointF point1, PointF point2, float i) {
            pointF.x = point1.x - (point1.x - point2.x) * i;
            pointF.y = point1.y - (point1.y - point2.y) * i;
        }
    }

}
