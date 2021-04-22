package com.example.administrator.testproject.code.main.model;

import com.example.administrator.testproject.bean.WelfareBean;
import com.example.administrator.testproject.base.BaseLogic;
import com.example.administrator.testproject.config.ApiConfig;
import com.example.administrator.testproject.config.NetConfig;
import com.example.administrator.testproject.http.BaseObserver;
import com.example.administrator.testproject.http.IService;
import com.example.administrator.testproject.http.RetrofitManager;
import com.example.administrator.testproject.code.main.presenter.MainPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainModel extends BaseLogic implements IMainModel {

    @Override
    public void getPictureData(final MainPresenter mainPresenter, int page, final int api) {
        RetrofitManager.create(IService.class, NetConfig.NET_TYPE_GANK)
                .getWelfareData(10, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<WelfareBean>() {
                    @Override
                    public void next(WelfareBean welfareBean) {
                        mainPresenter.onCompleted(welfareBean, api);
                    }

                    @Override
                    public void error(Throwable e) {
                        mainPresenter.onError(e, api);
                    }

                    @Override
                    public void complete() {

                    }
                });
    }
}
