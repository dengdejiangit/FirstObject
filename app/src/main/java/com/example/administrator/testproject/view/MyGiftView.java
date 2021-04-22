package com.example.administrator.testproject.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.example.administrator.testproject.R;

public class MyGiftView extends RelativeLayout {
    private int screenWidth;
    private int screenHeight;
    private LayoutParams layoutParams;
    private Drawable[] drawables = new Drawable[5];

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
    private Path mPath;
    private Path mPath1;
    private Path mPath2;
    private Path mPath3;

    public MyGiftView(Context context) {
        super(context);
        init();
    }

    public MyGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        drawables[0] = ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher);
        drawables[1] = ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher_round);
        drawables[2] = ContextCompat.getDrawable(getContext(), R.mipmap.app_icon);
        drawables[3] = ContextCompat.getDrawable(getContext(), R.mipmap.app_icon_round);
        drawables[4] = ContextCompat.getDrawable(getContext(), R.mipmap.leak_canary_icon);

        mPath = new Path();
        mPath1 = new Path();
        mPath2 = new Path();
        mPath3 = new Path();
        mPoint = new PointF();
        mPoint1 = new PointF();
        mPoint2 = new PointF();
        mPoint3 = new PointF();
        mPoint4 = new PointF();
        mPoint5 = new PointF();


        layoutParams = new LayoutParams(100, 100);
        //代码设置布局方式，底部居中
        layoutParams.addRule(CENTER_HORIZONTAL, TRUE);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        paint.setAntiAlias(true);

        setWillNotDraw(false);
    }

    public void addImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawables[(int) (Math.random() * drawables.length)]);
        imageView.setLayoutParams(layoutParams);

        addView(imageView);
        //一个放大动画
        setAnim(imageView).start();
        //一个随贝塞尔曲线的位移动画
        getBezierValueAnimator(imageView).start();
    }

    private ValueAnimator getBezierValueAnimator(View target) {
        PointF pointFLeft = getPointF();
        leftX = (int) pointFLeft.x;
        leftY = (int) pointFLeft.y;
        PointF pointFRight = getPointF();
        rightX = (int) pointFRight.x;
        rightY = (int) pointFRight.y;

        BezierEvaluator evaluator = new BezierEvaluator(pointFLeft, pointFRight);

        startX = (float) (screenWidth / 2);
        startY = screenHeight;
        endX = (float) (Math.random() * screenWidth);
        endY = 0;

        PointF randomStartPoint = new PointF(startX, startY);
        PointF randomEndPoint = new PointF(endX, endY);

        ValueAnimator animator = ValueAnimator.ofObject(evaluator, randomStartPoint, randomEndPoint);
        animator.addUpdateListener(new BezierListener(target));
        animator.setTarget(target);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(8000);
        return animator;
    }

    private PointF getPointF() {
        PointF pointF = new PointF();
        pointF.x = (float) (Math.random() * screenWidth);
        pointF.y = (float) (Math.random() * screenHeight / 4);
        return pointF;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GRAY);
        //画4个点
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
        mPath.reset();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        mPath.moveTo(startX, startY);
        mPath.cubicTo(leftX, leftY, rightX, rightY, endX, endY);
        canvas.drawPath(mPath, paint);

        if (mPoint != null) {
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
            canvas.drawPath(mPath3,paint);
        }
    }

    private AnimatorSet setAnim(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.2f, 1f);

        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(8000);
        //速率变化器
        enter.setInterpolator(new LinearInterpolator());
        //两个动画一起执行
        enter.playTogether(scaleX, scaleY);
        enter.setTarget(view);

        return enter;
    }

    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            // 这里顺便做一个alpha动画
            target.setAlpha(1 - animation.getAnimatedFraction());
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

            getPoint(mPoint1,startValue,pointF1,time);
            getPoint(mPoint2,pointF1,pointF2,time);
            getPoint(mPoint3,pointF2,endValue,time);
            getPoint(mPoint4,mPoint1,mPoint2,time);
            getPoint(mPoint5,mPoint2,mPoint3,time);

            invalidate();
            return mPoint;
        }

        private void getPoint(PointF pointF, PointF point1, PointF point2,float i) {
            pointF.x = point1.x - (point1.x - point2.x) * i;
            pointF.y = point1.y - (point1.y - point2.y) * i;
        }
    }

}
