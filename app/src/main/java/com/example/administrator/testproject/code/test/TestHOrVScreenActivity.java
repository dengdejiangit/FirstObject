package com.example.administrator.testproject.code.test;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.example.administrator.testproject.R;

public class TestHOrVScreenActivity extends AppCompatActivity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, TestHOrVScreenActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);//设置屏幕方向为跟随重力传感器
        setContentView(R.layout.activity_test_h_or_v_screen);
        LogUtils.e("onCreate");
    }

    /*
     * 屏幕切换后，生命周期为 onPause -》 onSaveInstanceState -》 onStop -》 onDestroy -》 onCreate -》 onStart -》onRestoreInstanceState -》 onResume
     *           可以在onSaveInstanceState方法中对重要数据进行保存，到onRestoreInstanceState方法中取出，恢复数据设置
     * 在清单文件中进行配置：configChanges="keyboardHidden|orientation|screenSize" 后，只走 onConfigurationChanged 方法，生命周期不会重新执行，数据也不会销毁
     *
     * */

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e("onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e("onRestart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.e("onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.e("onRestoreInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.e("onConfigurationChanged");
    }
}
