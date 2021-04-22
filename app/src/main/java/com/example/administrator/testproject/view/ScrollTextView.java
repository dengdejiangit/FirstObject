package com.example.administrator.testproject.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.example.administrator.testproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScrollTextView extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = "ScrollTextView";
    public boolean isHorizontal = true;
    boolean isSetNewText = false;
    boolean isScrollForever = true;
    private SurfaceHolder surfaceHolder;
    private Paint paint = null;
    private boolean stopScroll = false;
    private boolean clickEnable = false;
    private int speed = 1;
    private String text = "";
    private float textSize = 15f;
    private int needScrollTimes = Integer.MAX_VALUE;
    private int viewWidth = 0;
    private int viewHeight = 0;
    private float textWidth = 0f;
    private float textX = 0f;
    private float textY = 0f;
    private float viewWidth_plus_textLength = 0.0f;
    private ScheduledExecutorService scheduledExecutorService;

    public ScrollTextView(Context context) {
        super(context);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.ScrollTextView);
        clickEnable = arr.getBoolean(R.styleable.ScrollTextView_clickEnable, clickEnable);
        isHorizontal = arr.getBoolean(R.styleable.ScrollTextView_isHorizontal, isHorizontal);
        speed = arr.getInteger(R.styleable.ScrollTextView_speed, speed);
        text = arr.getString(R.styleable.ScrollTextView_text);
        int textColor = arr.getColor(R.styleable.ScrollTextView_text_color, Color.BLACK);
        textSize = arr.getDimension(R.styleable.ScrollTextView_text_size, textSize);
        needScrollTimes = arr.getInteger(R.styleable.ScrollTextView_times, Integer.MAX_VALUE);
        isScrollForever = arr.getBoolean(R.styleable.ScrollTextView_isScrollForever, true);

        paint = new Paint();
        paint.setColor(textColor);
        paint.setTextSize(textSize);

        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        setFocusable(true);

        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mHeight = getFontHeight(textSize);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(viewWidth, mHeight);
            viewHeight = mHeight;
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(viewWidth, viewHeight);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(viewWidth, mHeight);
            viewHeight = mHeight;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.d(TAG, "arg0:" + arg0.toString() + "  arg1:" + arg1 + "  arg2:" + arg2 + "  arg3:" + arg3);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        stopScroll = false;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTextThread(), 10, 10, TimeUnit.MILLISECONDS);
        Log.d(TAG, "ScrollTextTextView is created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        stopScroll = true;
        scheduledExecutorService.shutdownNow();
        Log.d(TAG, "ScrollTextTextView is destroyed");
    }

    private int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    public void setTimes(int times) {
        if (times <= 0) {
            throw new IllegalArgumentException("times was invalid integer, it must between > 0");
        } else {
            needScrollTimes = times;
            isScrollForever = false;
        }
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public void setText(String newText) {
        isSetNewText = true;

        stopScroll = false;
        this.text = newText;

        measureVarious();
    }

    public void setSpeed(int speed) {
        if (speed > 10 || speed < 0) {
            throw new IllegalArgumentException("Speed was invalid integer, it must between 0 and 10");
        } else {
            this.speed = speed;
        }
    }

    public void setScrollForever(boolean scrollForever) {
        isScrollForever = scrollForever;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!clickEnable) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            stopScroll = !stopScroll;
            if (!stopScroll && needScrollTimes == 0) {
                needScrollTimes = Integer.MAX_VALUE;
            }
        }
        return true;
    }

    private void drawVerticalScroll() {
        List<String> strings = new ArrayList<>();
        int start = 0, end = 0;
        while (end < text.length()) {
            while (paint.measureText(text.substring(start, end)) < viewWidth && end < text.length()) {
                end++;
            }
            if (end == text.length()) {
                strings.add(text.substring(start, end));
                break;
            } else {
                end--;
                strings.add(text.substring(start, end));
                start = end;
            }
        }

        float fontHeight = paint.getFontMetrics().bottom - paint.getFontMetrics().top;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        float baseLine = viewHeight / 2 + distance;

        for (int n = 0; n < strings.size(); n++) {

            for (float i = viewHeight + fontHeight; i > -fontHeight; i = i - 3) {

                if (stopScroll || isSetNewText) {
                    return;
                }

                Canvas canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
                canvas.drawText(strings.get(n), 0, i, paint);
                surfaceHolder.unlockCanvasAndPost(canvas);

                if (i - baseLine < 4 && i - baseLine > 0) {
                    if (stopScroll) {
                        return;
                    }
                    try {
                        Thread.sleep(speed * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private synchronized void draw(float x, float y) {
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        canvas.drawText(text, x, y, paint);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void measureVarious() {
        textWidth = paint.measureText(text);
        viewWidth_plus_textLength = viewWidth + textWidth;
        textX = viewWidth - viewWidth / 5f;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        textY = viewHeight / 2f + distance;
    }

    class ScrollTextThread implements Runnable {
        @Override
        public void run() {
            measureVarious();
            while (!stopScroll) {
                if (textWidth < getWidth()) {
                    draw(1, textY);
                    stopScroll = true;
                    break;
                }

                if (isHorizontal) {
                    draw(viewWidth - textX, textY);
                    textX += speed;
                    if (textX > viewWidth_plus_textLength) {
                        textX = 0;
                        --needScrollTimes;
                    }
                } else {
                    drawVerticalScroll();
                    isSetNewText = false;
                    --needScrollTimes;
                }

                if (needScrollTimes <= 0 && isScrollForever) {
                    stopScroll = true;
                }

            }
        }
    }

}
