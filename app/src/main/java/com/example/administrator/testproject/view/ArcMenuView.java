package com.example.administrator.testproject.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.administrator.testproject.R;

public class ArcMenuView extends ViewGroup implements OnClickListener {
    /**
     * 菜单按钮
     */
    private View mCBMenu;

    /**
     * 菜单的位置，为枚举类型
     *
     * @author fuly1314
     */
    private enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    /**
     * 菜单的状态
     *
     * @author fuly1314
     */
    private enum Status {
        OPEN, CLOSE
    }

    /**
     * 菜单为当前位置，默认为RIGHT_BOTTOM，在后面我们可以获取到
     */
    private Position mPosition = Position.RIGHT_BOTTOM;
    /**
     * 菜单的当前状态,默认为开启
     */
    private Status mCurStatus = Status.OPEN;

    /**
     * 菜单的半径，默认为120dp
     */
    private int mRadius = 0;

    public ArcMenuView(Context context) {
        this(context, null);
    }

    public ArcMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcMenuView, defStyle, 0);

        int position = ta.getInt(R.styleable.ArcMenuView_position, 3);
        switch (position) {
            case 0:
                mPosition = Position.LEFT_TOP;
                break;
            case 1:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case 2:
                mPosition = Position.RIGHT_TOP;
                break;
            case 3:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        mRadius = (int) ta.getDimension(R.styleable.ArcMenuView_radius, ConvertUtils.dp2px(120));
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed){
            layoutMainMenu();

            int count = getChildCount();

            for (int i = 0; i < count - 1; i++) {
                View childView = getChildAt(i + 1);

                int left = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));
                int top = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));

                switch (mPosition) {

                    case LEFT_TOP:
                        break;
                    case LEFT_BOTTOM:
                        top = getMeasuredHeight() - top - childView.getMeasuredHeight();
                        break;
                    case RIGHT_TOP:
                        left = getMeasuredWidth() - left - childView.getMeasuredWidth();
                        break;
                    case RIGHT_BOTTOM:
                        left = getMeasuredWidth() - left - childView.getMeasuredWidth();
                        top = getMeasuredHeight() - top - childView.getMeasuredHeight();
                        break;
                }

                childView.layout(left, top, left + childView.getMeasuredWidth(),
                        top + childView.getMeasuredHeight());
            }
        }
    }

    private void layoutMainMenu() {

        mCBMenu = getChildAt(0);

        mCBMenu.setOnClickListener(this);

        int left = 0;
        int top = 0;

        switch (mPosition) {
            case LEFT_TOP:
                left = 0;
                top = 0;
                break;
            case LEFT_BOTTOM:
                left = 0;
                top = getMeasuredHeight() - mCBMenu.getMeasuredHeight();
                break;
            case RIGHT_TOP:
                left = getMeasuredWidth() - mCBMenu.getMeasuredWidth();
                top = 0;
                break;
            case RIGHT_BOTTOM:
                left = getMeasuredWidth() - mCBMenu.getMeasuredWidth();
                top = getMeasuredHeight() - mCBMenu.getMeasuredHeight();
                break;
        }

        mCBMenu.layout(left, top, left + mCBMenu.getMeasuredWidth(), top + mCBMenu.getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawCircle(mCBMenu.getRight(),mCBMenu.getBottom(),ConvertUtils.dp2px(30),paint);
    }

    @Override
    public void onClick(View view) {
        Log.e("WS==--","x:"+view.getX()+"  y:"+view.getY()+"  left:"+view.getLeft()+"  right:"+view.getRight()+"  top:"+view.getTop()+"  bottom:"+view.getBottom());
        RotateAnimation rAnimation = new RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rAnimation.setDuration(300);

        rAnimation.setFillAfter(true);

        for (int i = 1; i < getChildCount(); i++) {
            childAnimation(getChildAt(i));
        }

        view.startAnimation(rAnimation);
    }

    private void childAnimation(View view){
        Log.e("WS==--","x:"+view.getX()+"  y:"+view.getY()+"  left:"+view.getLeft()+"  right:"+view.getRight()+"  top:"+view.getTop()+"  bottom:"+view.getBottom());
        TranslateAnimation translate = new TranslateAnimation(view.getLeft(),0,view.getTop(),0);
//        RotateAnimation rotate = new RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translate);
//        animationSet.addAnimation(rotate);
        animationSet.setDuration(2000);
        animationSet.setFillAfter(true);
        view.startAnimation(animationSet);
    }

}
