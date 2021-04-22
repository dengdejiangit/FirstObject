package com.example.administrator.testproject.code.home.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.testproject.R;
import com.example.administrator.testproject.code.home.other.UpdateUiCallBack;
import com.example.administrator.testproject.service.BindService;

public class PedometerFragment extends Fragment {

    private Activity mActivity;
    private BindService bindService;
    private TextView textView;
    private boolean isBind;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                textView.setText(msg.arg1 + "");
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.busu);
        Intent intent = new Intent(mActivity, BindService.class);
        isBind = mActivity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        mActivity.startService(intent);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BindService.LcBinder lcBinder = (BindService.LcBinder) service;
            bindService = lcBinder.getService();
            bindService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    Message message = Message.obtain();
                    message.what = 1;
                    message.arg1 = stepCount;
                    handler.sendMessage(message);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isBind) {
            mActivity.unbindService(serviceConnection);
        }
    }
}
