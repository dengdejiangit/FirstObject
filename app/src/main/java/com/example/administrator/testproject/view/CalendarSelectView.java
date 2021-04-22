package com.example.administrator.testproject.view;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.example.administrator.testproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Create  by  User:WS  Data:2019/5/27
 */

public class CalendarSelectView extends FrameLayout {

    @BindView(R.id.calendar_year)
    CalendarView mCalendarYear;
    @BindView(R.id.calendar_month)
    CalendarView mCalendarMonth;
    @BindView(R.id.calendar_day)
    CalendarView mCalendarDay;
    @BindView(R.id.calendar_cancel_tv)
    TextView mCalendarCancelTv;
    @BindView(R.id.calendar_confirm_tv)
    TextView mCalendarConfirmTv;
    @BindView(R.id.fl_calendar_select)
    FrameLayout mFlCalendarSelect;
    @BindView(R.id.ll_calendar_select)
    LinearLayout mLlCalendarSelect;

    private Unbinder unbinder;
    private boolean mIsLeapYear = false;
    private boolean mIsTwoMonth = false;
    //没有选择时，将会显示的日期，也可以根据系统获取当前时间
    private String years = "2019", months = "11", days = "11";
    private String[] mStrings = new String[]{"4", "6", "9", "11"};//30天的月份
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private CalendarClickListener mCalendarClickListener;
    private boolean mIsLastYear = false;

    public CalendarSelectView(Context context) {
        this(context, null);
    }

    public CalendarSelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_calendar_select, this);
        setBackgroundColor(Color.TRANSPARENT);
        unbinder = ButterKnife.bind(this);
        initAnimation(context);
        initView();
    }

    public void setCalendarListener(CalendarClickListener calendarListener) {
        mCalendarClickListener = calendarListener;
    }

    private void initAnimation(Context context) {
        if (context == null) return;
        mBottomInAnim = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom);
        mBottomOutAnim = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom);
        mBottomInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (mLlCalendarSelect == null) return;
                mLlCalendarSelect.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mFlCalendarSelect == null) return;
                mFlCalendarSelect.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBottomOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (mFlCalendarSelect == null) return;
                mFlCalendarSelect.setVisibility(GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mLlCalendarSelect == null) return;
                mLlCalendarSelect.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initView() {
        ArrayList<String> gradeYear = new ArrayList<>();

        SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy", Locale.getDefault());
        final SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("dd", Locale.getDefault());
        final int thisYear = Integer.parseInt(simpleDateFormatYear.format(new Date()));
        final int thisMonth = Integer.parseInt(simpleDateFormatMonth.format(new Date()));
        final int thisDay = Integer.parseInt(simpleDateFormatDay.format(new Date()));
        for (int i = 1980; i <= thisYear; i++)
            gradeYear.add(i + "");

        mCalendarYear.setData(gradeYear, gradeYear.size() - 2);
        mCalendarMonth.setData(getData(12), 10);
        mCalendarDay.setData(getData(31), 10);
        mCalendarYear.setOnSelectListener(new CalendarView.onSelectListener() {
            @Override
            public void onSelect(String data) {
                years = data;

                int indexMonths = Integer.parseInt(months) - 1;
                if (mCalendarDay == null) return;
                mIsLeapYear = TimeUtils.isLeapYear(data, new SimpleDateFormat("yyyy", Locale.getDefault()));

                if (TextUtils.equals(data, thisYear + "")) {
                    mIsLastYear = true;
                    mCalendarMonth.setData(getData(thisMonth), 0);
                    mCalendarDay.setData(getData(thisDay), 0);
                } else {
                    mIsLastYear = false;
                    mCalendarMonth.setData(getData(12), indexMonths);
                    setDay();
                }
            }
        });
        mCalendarMonth.setOnSelectListener(new CalendarView.onSelectListener() {
            @Override
            public void onSelect(String data) {
                months = data;

                mIsTwoMonth = TextUtils.equals(data, "2");
                setDay();

                if (mIsLastYear && TextUtils.equals(data, thisMonth + "")) {
                    mCalendarDay.setData(getData(thisDay), 0);
                }
            }
        });
        mCalendarDay.setOnSelectListener(new CalendarView.onSelectListener() {
            @Override
            public void onSelect(String data) {
                days = data;
            }
        });
    }

    private void setDay() {
        int indexDay = Integer.parseInt(days) - 1;
        if (mCalendarDay == null) return;
        for (String string : mStrings) {
            if (TextUtils.equals(months, string)) {
                mCalendarDay.setData(getData(30), indexDay);
                return;
            } else {
                mCalendarDay.setData(getData(31), indexDay);
            }
        }
        if (mIsTwoMonth) {
            if (mIsLeapYear) {
                mCalendarDay.setData(getData(28), indexDay);
            } else {
                mCalendarDay.setData(getData(29), indexDay);
            }
        }
    }

    private List<String> getData(int lastValue) {
        List<String> days = new ArrayList<>();
        for (int i = 1; i <= lastValue; i++)
            days.add(i + "");
        return days;
    }

    public void show() {
        if (mBottomInAnim != null)
            startAnimation(mBottomInAnim);
        setVisibility(VISIBLE);
    }

    public void dismiss() {
        if (mBottomOutAnim != null)
            startAnimation(mBottomOutAnim);
        setVisibility(GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @OnClick({R.id.fl_calendar_select, R.id.calendar_cancel_tv, R.id.calendar_confirm_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_calendar_select:
                dismiss();
                break;
            case R.id.calendar_cancel_tv:
                dismiss();
                break;
            case R.id.calendar_confirm_tv:
                years = mCalendarYear.getSelect();
                months = mCalendarMonth.getSelect();
                days = mCalendarDay.getSelect();
                String string = years + " 年 " + months + " 月 " + days + " 日 ";
                mCalendarClickListener.onConfirmClickListener(string);
                dismiss();
                break;
        }
    }

    public interface CalendarClickListener {
        void onConfirmClickListener(String date);
    }

}
