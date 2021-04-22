package com.example.administrator.testproject.code.home.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.administrator.testproject.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EventBusFragment extends Fragment {

    Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_bus, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                if (!EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().register(this);
                break;
            case R.id.bt2:
                EventBus.getDefault().post("已经触发了EventBus事件1");
                break;
            case R.id.bt3:
                EventBus.getDefault().postSticky("已经触发了EventBus事件2");
                break;
            case R.id.bt4:
                if (EventBus.getDefault().isRegistered(this))
                    EventBus.getDefault().unregister(this);
                break;
        }
    }

    //黏性事件可以先发送，然后再注册，依旧可以收到该事件
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void stickyEvent(String event) {
//        ToastUtils.showShort(event);
//        LogUtils.e(event);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(String event) {
        if (StringUtils.equals(event, "已经触发了EventBus事件1")) {
            ToastUtils.showShort(event);
            LogUtils.e(event);
        } else if (StringUtils.equals(event, "已经触发了EventBus事件2")) {
            ToastUtils.showShort(event);
            LogUtils.e(event);
        }
    }
}
