package com.example.administrator.testproject.code.test;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.administrator.testproject.R;
import com.example.administrator.testproject.service.MyService;
import com.example.administrator.testproject.util.BroadCastReceiverUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestServiceActivity extends AppCompatActivity {

    @BindView(R.id.bt_start_service)
    Button mBtStartService;
    @BindView(R.id.bt_stop_service)
    Button mBtStopService;
    @BindView(R.id.bt_service_status)
    Button mBtServiceStatus;
    @BindView(R.id.bt3_center)
    Button mBt3Center;
    private Intent intent;
    //保持所启动的Service的IBinder对象,同时定义一个ServiceConnection对象
    private MyService.MyBinder myBinder;
    private BroadcastReceiver mBroadcastReceiver;
    private boolean bindStatus = false;

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TestServiceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_service);
        ButterKnife.bind(this);

        intent = new Intent(this, MyService.class);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //系统广播：网络状态改变 和 自定义广播的使用
        mBroadcastReceiver = BroadCastReceiverUtil.registerBroadcastReceiver(this, new String[]{"android.net.conn.CONNECTIVITY_CHANGE","998101"}, mOnReceiveBroadcast);
    }

    private BroadCastReceiverUtil.OnReceiveBroadcast mOnReceiveBroadcast = new BroadCastReceiverUtil.OnReceiveBroadcast() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null)return;
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();//获取网络状态
            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                ToastUtils.showLong("网络已连接！");
            } else {
                ToastUtils.showLong("网络已断开！");
            }

            if (StringUtils.equals(intent.getAction(),"998101")){
                ToastUtils.showLong(intent.getAction());
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadCastReceiverUtil.unRegisterBroadcastReceiver(this, mBroadcastReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (intent != null && serviceConnection != null && !bindStatus){
            bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceConnection  != null && bindStatus){
            bindStatus = false;
            unbindService(serviceConnection);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Activity与Service连接成功时回调该方法
            myBinder = (MyService.MyBinder) service;
            bindStatus = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bindStatus = false;
            // Service 被破坏了或者被杀死的时候调用. 例如, 系统资源不足, 要关闭一些Services, 刚好连接绑定的 Service 是被关闭者之一,
            // 这个时候onServiceDisconnected() 就会被调用.
        }
    };

    @OnClick({R.id.bt_start_service, R.id.bt_stop_service, R.id.bt_service_status, R.id.bt_sent_custom_broadcast_receiver})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_start_service:
                if (!bindStatus){
                    bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
                }
                break;
            case R.id.bt_stop_service:
                if (bindStatus){
                    unbindService(serviceConnection);
                    bindStatus = false;
                }
                break;
            case R.id.bt_service_status:
                if (bindStatus) {
                    ToastUtils.showShort("Service的count值：" + myBinder.getCount());
                }
                break;
            case R.id.bt_sent_custom_broadcast_receiver:
                BroadCastReceiverUtil.sendBroadcast(this,"998101");
                break;
        }
    }
}
