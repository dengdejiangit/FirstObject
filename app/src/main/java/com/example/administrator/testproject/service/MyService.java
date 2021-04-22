package com.example.administrator.testproject.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.administrator.testproject.R;
import com.example.administrator.testproject.XApplication;
import com.example.administrator.testproject.code.main.view.MainActivity;

public class MyService extends Service {

    private int count = 0;
    private boolean threadStatus = true;
    private final int VALUE_STRING_HANDLER_WHAT_SERVICE = 1;

    private MyBinder myBinder = new MyBinder();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == VALUE_STRING_HANDLER_WHAT_SERVICE) {
                createNotification();
            }
        }
    };

    //定义onBinder方法所返回的对象
    public class MyBinder extends Binder {
        public int getCount() {
            return MyService.this.count;
        }
    }

    //必须实现的方法,绑定的时候调用
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    //Service被创建时调用
    @Override
    public void onCreate() {
        super.onCreate();
        mThread.start();
    }

    //Service断开连接时回调
    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    //Service重新绑定时调用
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    //Service被销毁时调用
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.threadStatus = false;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    private Thread mThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (threadStatus) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                mHandler.sendEmptyMessage(VALUE_STRING_HANDLER_WHAT_SERVICE);
            }
        }
    });

    private void createNotification(){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("APP运行中")
                .setContentText("正在使用测试专用app,持续时间 " + count + " 秒")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(XApplication.getContext().getResources(), R.mipmap.app_icon))
                .setContentIntent(pi)
                .build();
        startForeground(1, notification);
    }

}
