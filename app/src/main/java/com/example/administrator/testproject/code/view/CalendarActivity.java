package com.example.administrator.testproject.code.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.testproject.R;
import com.example.administrator.testproject.view.CalendarSelectView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity implements CalendarSelectView.CalendarClickListener {

    @BindView(R.id.tv_now_date)
    TextView mTvNowDate;
    @BindView(R.id.bt_choose_time)
    Button mBtChooseTime;
    @BindView(R.id.view_calendar_select)
    CalendarSelectView mViewCalendarSelect;
    @BindView(R.id.ll_calendar)
    LinearLayout mLlCalendar;

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CalendarActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        mViewCalendarSelect.setCalendarListener(this);
        mBtChooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewCalendarSelect != null)
                    mViewCalendarSelect.show();
            }
        });
    }

    @Override
    public void onConfirmClickListener(String date) {
        if (mTvNowDate != null) {
            mTvNowDate.setText(date);
        }
    }

    @Override
    public void finish() {
        if (mViewCalendarSelect != null && mViewCalendarSelect.getVisibility() == View.VISIBLE) {
            mViewCalendarSelect.setVisibility(View.GONE);
            return;
        }
        super.finish();
    }
}
