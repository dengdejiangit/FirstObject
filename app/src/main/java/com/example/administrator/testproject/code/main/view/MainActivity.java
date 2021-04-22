package com.example.administrator.testproject.code.main.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.administrator.testproject.R;
import com.example.administrator.testproject.base.BaseMvpActivity;
import com.example.administrator.testproject.bean.WelfareBean;
import com.example.administrator.testproject.code.home.view.HomeActivity;
import com.example.administrator.testproject.code.main.model.MainModel;
import com.example.administrator.testproject.code.main.presenter.MainPresenter;
import com.example.administrator.testproject.code.view.CustomViewActivity;
import com.example.administrator.testproject.code.view.PictureActivity;
import com.example.administrator.testproject.code.view.WebViewActivity;
import com.example.administrator.testproject.config.ApiConfig;
import com.example.administrator.testproject.http.BaseObserver;
import com.example.administrator.testproject.util.GlideUtil;
import com.example.administrator.testproject.util.PictureUtils;
import com.example.administrator.testproject.util.ProgressDialogUtils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author WS
 */

public class MainActivity extends BaseMvpActivity<MainPresenter, MainModel> implements IMainView, View.OnClickListener, View.OnLongClickListener {

    private final static String VALUE_SP_STRING_PICTURE_INDEX = "sp_picture_page_index";
    private final static int VALUE_CACHE_PICTURE_SUCCESS = 0x100;
    @BindView(R.id.main_rl)
    RelativeLayout mRlMain;
    @BindView(R.id.banner)
    Banner mBanner;
    private int mPage = 1;
    private int mClickCount = 0;
    private List<WelfareBean.ResultsBean> mData;
    private List<String> mImgList;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler();

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void launchForResult(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }

    @Override
    protected MainModel getModel() {
        return new MainModel();
    }

    @Override
    protected void initView() {
        ScreenUtils.setFullScreen(this);
        setWallpaperAsBg();
        initBanner();
        mRlMain.setOnClickListener(this);
        mRlMain.setOnLongClickListener(this);
    }

    private void initBanner() {
        mBanner.setOnBannerListener(position -> {
            if (position == mImgList.size() - 1) {
                mPage++;
                mPresenter.getData(mPage, ApiConfig.GET_WELFARE_PICTURE);
            }
            if (position == 0) {
                mPage--;
                mPresenter.getData(mPage, ApiConfig.GET_WELFARE_PICTURE);
            }
        });
        mBanner.isAutoPlay(false);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                GlideUtil.getInstance().showImg(MainActivity.this, path, imageView, 2);
            }
        });
    }

    private void setWallpaperAsBg() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        mRlMain.setBackground(wallpaperDrawable);
    }

    private void takeFailPath() {
        LogUtils.e("手机存储路径：" +
                "\ngetFilesDir = " + getFilesDir() +
                "\ngetExternalFilesDir = " + ObjectUtils.getOrDefault(getExternalFilesDir("exter_test"), null).getAbsolutePath() +
                "\ngetDownloadCacheDirectory = " + Environment.getDownloadCacheDirectory().getAbsolutePath() +
                "\ngetDataDirectory = " + Environment.getDataDirectory().getAbsolutePath() +
                "\ngetExternalStorageDirectory = " + Environment.getExternalStorageDirectory().getAbsolutePath() +
                "\ngetExternalStoragePublicDirectory = " + Environment.getExternalStoragePublicDirectory("pub_test"));
    }

    private void beginCache() {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            File filePath = Environment.getExternalStoragePublicDirectory("picture");
            for (int i = 0; i < mData.size(); i++) {
                Bitmap bitmap = PictureUtils.getInstance().getBitmap(mData.get(i).getUrl());
                PictureUtils.getInstance().saveBitmap2File(bitmap, mData.get(i).get_id(), filePath.getPath(), PictureUtils.Format.JPEG);
            }
            emitter.onNext(0);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Integer>() {
                    @Override
                    public void next(Integer integer) {
                        ToastUtils.showShort("图片全部缓存成功");
                    }
                });
    }

    @Override
    public void initData() {
        mPage = SPUtils.getInstance().getInt(VALUE_SP_STRING_PICTURE_INDEX, 1);
    }

    @Override
    public void onClick(View view) {
        mClickCount++;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mClickCount) {
                    case 1:
                        WebViewActivity.launch(MainActivity.this);
                        break;
                    case 2:
                        HomeActivity.launch(MainActivity.this);
                        break;
                    case 3:
                        CustomViewActivity.launch(MainActivity.this);
                        break;
                    case 4:
                        PictureActivity.launch(MainActivity.this);
                        break;
                    case 5:
                        showPicBanner();
                        break;
                    default:
                        break;
                }
                mClickCount = 0;
            }
        }, 500);
    }

    @Override
    public boolean onLongClick(View view) {
//        TestServiceActivity.launch(this);
//        TestHOrVScreenActivity.launch(this);
//        RxJavaTestActivity.start(this);
//        VideoActivity.start(this);
        return true;
    }

    private void showPicBanner() {
        if (mBanner != null && mBanner.getVisibility() == View.GONE) {
            mBanner.setVisibility(View.VISIBLE);
        } else if (mBanner != null && mBanner.getVisibility() == View.VISIBLE) {
            ProgressDialogUtils.showDialog(this, "请等待");
            mPresenter.getData(mPage, ApiConfig.GET_WELFARE_PICTURE);
            mBanner.setVisibility(View.GONE);
        }
    }

    @Override
    public void finish() {
        if (mBanner != null && mBanner.getVisibility() == View.VISIBLE) {
            mBanner.setVisibility(View.GONE);
            return;
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onCompleted(Object o, int api) {
        ProgressDialogUtils.dismissDialog();
        ToastUtils.showShort("请求成功");
        if (ApiConfig.GET_WELFARE_PICTURE == api) {
            WelfareBean welfareBean = (WelfareBean) o;
            mData = welfareBean.getResults();
            mImgList = new ArrayList<>();
            for (int i = 0; i < mData.size(); i++) {
                mImgList.add(mData.get(i).getUrl());
            }
            mBanner.setImages(mImgList);
            mBanner.start();
            SPUtils.getInstance().put(VALUE_SP_STRING_PICTURE_INDEX, mPage);
        }
    }

    @Override
    public void onError(Throwable e, int api) {
        ProgressDialogUtils.dismissDialog();
        if (mBanner != null && mBanner.getVisibility() == View.VISIBLE) {
            mBanner.setVisibility(View.GONE);
        }
        ToastUtils.showShort(e.getMessage());
    }
}