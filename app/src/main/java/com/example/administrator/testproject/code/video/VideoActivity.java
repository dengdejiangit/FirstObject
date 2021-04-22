package com.example.administrator.testproject.code.video;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.administrator.testproject.R;
import com.example.administrator.testproject.base.BaseMvpActivity;
import com.example.administrator.testproject.code.video.model.VideoModule;
import com.example.administrator.testproject.code.video.presenter.VideoPresenter;
import com.example.administrator.testproject.code.video.view.IVideoView;
import com.example.administrator.testproject.util.GlideUtil;

import cn.jzvd.JzvdStd;

/**
 * @author WS
 */

public class VideoActivity extends BaseMvpActivity<VideoPresenter, VideoModule> implements IVideoView {

    public static void start(Context context) {
        Intent starter = new Intent(context, VideoActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_video;
    }

    @Override
    protected VideoPresenter getPresenter() {
        return new VideoPresenter();
    }

    @Override
    protected VideoModule getModel() {
        return new VideoModule();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
//        playVideoMethodOne();
//        playVideoMethodTwo();
        playVideoMethodThree();
    }


    /**
     * 调用系统的播放器进行视频的播放
     */
    private void playVideoMethodOne() {
        Uri uri = Uri.parse("http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4");
        //调用系统自带的播放器
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/mp4");
        startActivity(intent);
    }


    /**
     * 使用Android 自带的控件VideoView
     */
    private void playVideoMethodTwo() {
        Uri uri = Uri.parse("http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4");
        VideoView videoView = this.findViewById(R.id.video_view);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.requestFocus();
    }

    /**
     * 使用三方的饺子视频
     */
    private void playVideoMethodThree() {
        JzvdStd jzvdStd = findViewById(R.id.video_player);
        jzvdStd.setUp("http://f.video.weibocdn.com/000WZObOlx07w0B2xevK0104120aQL4F0E040.mp4?label=mp4_hd&template=640x360.24.0&trans_finger=ac6fb6d5c49a67fe2901ae638b222ab2&Expires=1565091479&ssig=Jh3otVfe%2BO&KID=unistore,video","视频");
        Glide.with(this).load("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640").into(jzvdStd.posterImageView);
    }

}
