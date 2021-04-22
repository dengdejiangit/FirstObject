package com.example.administrator.testproject.code.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;

import com.example.administrator.testproject.R;
import com.example.administrator.testproject.view.BatteryView;
import com.example.administrator.testproject.view.BubbleView;
import com.example.administrator.testproject.view.DrawCubicView;
import com.example.administrator.testproject.view.DrawQuadToView;
import com.example.administrator.testproject.view.MyGiftView;
import com.example.administrator.testproject.view.RotateIconView;
import com.example.administrator.testproject.view.ScrollTextView;
import com.example.administrator.testproject.view.SportsView;
import com.example.administrator.testproject.view.WeChatButtonView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomViewActivity extends AppCompatActivity {

    @BindView(R.id.wechat_button_view)
    WeChatButtonView mWechatButtonView;
    @BindView(R.id.view_batter)
    BatteryView mViewBatter;
    @BindView(R.id.view_sports)
    SportsView mViewSports;
    @BindView(R.id.view_cubic)
    DrawCubicView mViewCubic;
    @BindView(R.id.view_quad_to)
    DrawQuadToView mViewQuadTo;
    @BindView(R.id.view_gif)
    MyGiftView mViewGif;
    @BindView(R.id.iv_gif)
    ImageView mIvGif;
    @BindView(R.id.view_bubble)
    BubbleView mViewBubble;
    @BindView(R.id.scroll_text_view)
    ScrollTextView mScrollTextView;

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CustomViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ButterKnife.bind(this);
        RotateIconView riv = findViewById(R.id.riv);
        riv.setOnClickListener(v -> {
            riv.beginRotate();
        });
        mScrollTextView.setSpeed(1);
        mScrollTextView.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.iv_gif)
    public void onViewClicked() {
        if (mViewGif != null) {
            for (int i = 0; i < 1; i++) {
                mViewGif.addImageView();
            }
        }
    }
}
