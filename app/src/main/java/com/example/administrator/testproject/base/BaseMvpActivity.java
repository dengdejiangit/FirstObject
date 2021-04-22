package com.example.administrator.testproject.base;

import android.os.Bundle;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseMvpActivity<P extends BaseLogic, M extends BaseLogic> extends BaseActivity implements BaseView {
    public P mPresenter;
    M mModel;
    private Unbinder mBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        mBind = ButterKnife.bind(this);
        initView();
        mPresenter = getPresenter();
        mModel = getModel();
        mPresenter.attach(this, (BaseModel) mModel);
        initData();
    }

    protected abstract int initLayout();

    protected abstract P getPresenter();

    protected abstract M getModel();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null){
            mBind.unbind();
        }
        if (mPresenter != null){
            mPresenter.detach();
        }
    }
}
