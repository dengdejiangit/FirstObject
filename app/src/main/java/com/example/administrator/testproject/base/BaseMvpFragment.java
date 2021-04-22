package com.example.administrator.testproject.base;


import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create  by  User:WS  Data:2019/6/10
 */

public abstract class BaseMvpFragment <P extends BaseLogic, M extends BaseLogic> extends Fragment implements BaseView {
    public P mPresenter;
    M mModel;
    private Unbinder mBind;
    public Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(initLayout(), container, false);
        mActivity = getActivity();
        mBind = ButterKnife.bind(this,view);
        initView();
        mPresenter = getPresenter();
        mModel = getModel();
        mPresenter.attach(this, (BaseModel) mModel);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract int initLayout();

    protected abstract P getPresenter();

    protected abstract M getModel();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind != null){
            mBind.unbind();
        }
        if (mPresenter != null){
            mPresenter.detach();
        }
    }
}
