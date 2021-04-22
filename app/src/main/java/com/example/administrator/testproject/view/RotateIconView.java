package com.example.administrator.testproject.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.administrator.testproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Create  by
 *
 * @author User:WS
 * @date Data:2019/9/2
 */

public class RotateIconView extends View {

    private static final long ANIM_PERIOD_TIME = 2800;
    private static final long BACKGROUND_ANIM_PERIOD_TIME = 2200;
    private float mCenterWidthPoint;
    private float mCenterHeightPoint;
    private float mCenterCircleRadius;
    private float mBitmapWidth;
    private float mBitmapHeight;
    private Paint mIconPaint;
    private Paint mProgressPaint;
    private Paint mDimAnimPaint;
    private Path mIconPath;
    private Path mPath;
    private List<Bitmap> mBitmapList;
    private Bitmap mBitmap;
    private int mIconIndex = 0;
    private RectF mProgressRect;
    private Rect mRect;
    private int rotateBitmap = 0;
    private float degrees;
    private float ratio = 1;
    private ColorMatrixColorFilter mGrayColorFilter;
    private ObjectAnimator mRotateIconAnim;
    private ObjectAnimator mDimAnim;
    private AnimListener mAnimListener;
    //节点一：透明动画向下时间占比 0 - node1
    private double node1 = 0.28;
    //节点二：透明动画在下方停止时间占比 node1 - node2
    private double node2 = 0.46;
    //节点三：透明动画向上时间占比 node2 - node3
    private double node3 = 0.68;
    private int progressRadio = 70;

    public RotateIconView(Context context) {
        super(context);
        init(context, null);
    }

    public RotateIconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void drawProgress(Canvas canvas) {
        float size;
        if (degrees < node2) {
            size = 0;
        } else if (degrees > node2 && degrees < node3) {
            size = (float) ((degrees - node2) / node1);
        } else {
            size = 1;
        }
        float angle = (float) 360 / mBitmapList.size() * (mIconIndex + size);
        int left = (int) (mCenterWidthPoint - mCenterCircleRadius - progressRadio);
        int top = (int) (mCenterHeightPoint - mCenterCircleRadius - progressRadio);
        int right = (int) (mCenterWidthPoint + mCenterCircleRadius + progressRadio);
        int bottom = (int) (mCenterHeightPoint + mCenterCircleRadius + progressRadio);
        mProgressRect.set(left, top, right, bottom);
        canvas.drawArc(mProgressRect, -90, angle, false, mProgressPaint);
    }

    private void drawRotateBitmap(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotateBitmap, mCenterWidthPoint, mCenterHeightPoint);
        canvas.drawBitmap(mBitmap, mBitmapWidth, mBitmapHeight, mIconPaint);
        canvas.restore();
    }

    private void drawRotateIcon(Canvas canvas) {
        canvas.save();

        float size = 0;
        if (degrees < node1) {
            size = 0;
        } else if (degrees > node1 && degrees < node2) {
            size = (float) ((degrees - node1) / (node2 - node1));
        } else {
            size = 1;
        }

        float degreesSize = size * 110;
        if (degreesSize >= 10 && degreesSize <= 20) {
            degreesSize = 20 - degreesSize;
        } else if (degreesSize > 20) {
            degreesSize = -degreesSize + 20;
        }
        if (mIconIndex + 1 < mBitmapList.size()) {
            Bitmap bitmap = mBitmapList.get(mIconIndex + 1);
            float iconWidth = mCenterWidthPoint - (float) bitmap.getWidth() / 2;
            float iconHeight = mCenterHeightPoint - (float) bitmap.getHeight() / 2;
            canvas.drawBitmap(bitmap, iconWidth, iconHeight, mIconPaint);
        }
        mIconPath.addCircle(mCenterWidthPoint, mCenterHeightPoint, mCenterCircleRadius, Path.Direction.CCW);
        canvas.clipPath(mIconPath);
        canvas.rotate(degreesSize, mCenterWidthPoint, mCenterHeightPoint + mCenterCircleRadius);
        if (mIconIndex < mBitmapList.size()) {
            if (degrees > 0.21){
                mIconPaint.setColorFilter(mGrayColorFilter);
            }
            Bitmap bitmap = mBitmapList.get(mIconIndex);
            float iconWidth = mCenterWidthPoint - (float) bitmap.getWidth() / 2;
            float iconHeight = mCenterHeightPoint - (float) bitmap.getHeight() / 2;
            canvas.drawBitmap(bitmap, iconWidth, iconHeight, mIconPaint);
            setIconPaint();
        }
        canvas.restore();
    }

    public void beginRotate() {
        mRotateIconAnim.start();
        mDimAnim.setRepeatCount(mBitmapList.size() - 1);
        mDimAnim.start();
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        initPaint();
        initData(context);
        initGrayPoint();
        initAnim();
    }

    private void initPaint() {
        mIconPath = new Path();
        mProgressRect = new RectF();
        mPath = new Path();
        mRect = new Rect();

        mIconPaint = new Paint();
        setIconPaint();

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStrokeWidth(12);
        mProgressPaint.setColor(Color.WHITE);
        mProgressPaint.setDither(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mDimAnimPaint = new Paint();
        mDimAnimPaint.setAntiAlias(true);
        mDimAnimPaint.setARGB(30, 255, 255, 255);
    }

    private void initData(Context context) {
        mBitmapList = new ArrayList<>();
        mBitmapList.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon));
        mBitmapList.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon_round));
        mBitmapList.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        mBitmapList.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round));
        mBitmapList.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.leak_canary_icon));

        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_rotate);
    }

    private void initGrayPoint() {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        mGrayColorFilter = new ColorMatrixColorFilter(cm);
    }

    private void initAnim() {
        ObjectAnimator backgroundAnim = ObjectAnimator.ofInt(this, "rotateBitmap", 0, 360);
        backgroundAnim.setDuration(BACKGROUND_ANIM_PERIOD_TIME);
        backgroundAnim.setRepeatCount(ValueAnimator.INFINITE);
        backgroundAnim.setInterpolator(new LinearInterpolator());
        backgroundAnim.start();

        mRotateIconAnim = ObjectAnimator.ofFloat(this, "degrees", 0, 1);
        mRotateIconAnim.setInterpolator(new LinearInterpolator());
        mRotateIconAnim.setDuration(ANIM_PERIOD_TIME);
        mRotateIconAnim.setRepeatCount(ValueAnimator.INFINITE);
        mRotateIconAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mIconIndex += 1;
                if (mIconIndex >= mBitmapList.size()) {
                    mRotateIconAnim.cancel();
                    if (mAnimListener != null) {
                        mAnimListener.onAnimEnd();
                    }
                }
            }
        });

        mDimAnim = ObjectAnimator.ofFloat(this, "ratio", 0, 1);
        mDimAnim.setDuration(ANIM_PERIOD_TIME);
        mDimAnim.setInterpolator(new LinearInterpolator());
    }

    private void drawDimAnim(Canvas canvas) {
        canvas.save();

        double height;
        if (ratio < node1) {
            height = mCenterCircleRadius * (ratio / node1);
        } else if (ratio > node1 && ratio < node2) {
            height = mCenterCircleRadius;
        } else if (ratio > node2 && ratio < node3) {
            height = mCenterCircleRadius * (1 - (ratio - node2) / node1);
        } else {
            height = 0;
        }

        mPath.addCircle(mCenterWidthPoint, mCenterHeightPoint, mCenterCircleRadius / 2, Path.Direction.CCW);
        canvas.clipPath(mPath);

        int left = (int) (mCenterWidthPoint - (mCenterCircleRadius / 2));
        int top = (int) (mCenterHeightPoint - (mCenterCircleRadius / 2) + height);
        int right = (int) (mCenterWidthPoint + (mCenterCircleRadius / 2));
        int bottom = (int) (mCenterHeightPoint + (mCenterCircleRadius / 2));
        mRect.set(left, top, right, bottom);

        canvas.drawRect(mRect, mDimAnimPaint);

        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();

        mCenterHeightPoint = (float) getMeasuredHeight() / 5 * 2;
        mCenterWidthPoint = (float) getMeasuredWidth() / 2;
        mCenterCircleRadius = (float) getMeasuredWidth() / 4;

        mBitmapWidth = mCenterWidthPoint - (float) bitmapWidth / 2;
        mBitmapHeight = mCenterHeightPoint - (float) bitmapHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRotateIcon(canvas);
        drawRotateBitmap(canvas);
        drawProgress(canvas);
        drawDimAnim(canvas);
    }

    private void setIconPaint() {
        mIconPaint.reset();
        mIconPaint.setAntiAlias(true);
        mIconPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mIconPaint.setDither(true);
    }

    public void setAnimListener(AnimListener animListener) {
        mAnimListener = animListener;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public void setRotateBitmap(int rotateBitmap) {
        this.rotateBitmap = rotateBitmap;
        invalidate();
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.mBitmapList = bitmapList;
        beginRotate();
    }

    public interface AnimListener {
        /**
         * 动画结束调用
         */
        void onAnimEnd();
    }

}
