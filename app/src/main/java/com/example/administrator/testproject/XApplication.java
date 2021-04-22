package com.example.administrator.testproject;


/*
 * Create  by  User:Administrator  Data:2019/3/25  WS
 *
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.administrator.testproject.manager.XActivityManager;
import com.tencent.mmkv.MMKV;

public class XApplication extends Application {

    public static XApplication sContext;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);

        sContext = this;

        MMKV.initialize(this);

        CrashUtils.init();

        LogUtils.getConfig()
                .setLogSwitch(BuildConfig.DEBUG)// 设置log总开关，默认开
                //.setGlobalTag("WS==--")// 设置log全局标签，默认为空
                // 当全局标签不为空时，我们输出的log全部为该tag，
                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
                .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
                .setBorderSwitch(false)// 输出日志是否带边框开关，默认开
                .setLogHeadSwitch(false);//头部信息，默认开

        //对activity进行监听，添加当前显示的activity，可以通过manager类获取当前显示的activity
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                XActivityManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static Context getContext() {
        return sContext.getApplicationContext();
    }

    public static XApplication getApplication() {
        return sContext;
    }

}
