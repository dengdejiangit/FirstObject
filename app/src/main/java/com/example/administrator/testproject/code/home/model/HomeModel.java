package com.example.administrator.testproject.code.home.model;

import com.example.administrator.testproject.base.BaseLogic;
import com.example.administrator.testproject.bean.FemaleNameBean;
import com.example.administrator.testproject.code.home.presenter.HomePresenter;
import com.example.administrator.testproject.code.home.view.IHomeView;
import com.example.administrator.testproject.config.NetConfig;
import com.example.administrator.testproject.http.BaseObserver;
import com.example.administrator.testproject.http.IService;
import com.example.administrator.testproject.http.RetrofitManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeModel extends BaseLogic<IHomeView, IHomeModel> implements IHomeModel {

    @Override
    public void getNetData(final HomePresenter homePresenter, final int api, int page) {
        RetrofitManager.create(IService.class, NetConfig.NET_TYPE_OPEN_API)
                .getFemaleName(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<FemaleNameBean>() {
                    @Override
                    public void next(FemaleNameBean femaleNameBean) {
                        homePresenter.onCompleted(femaleNameBean, api);
                    }

                    @Override
                    public void error(Throwable e) {
                        homePresenter.onError(e, api);
                    }

                    @Override
                    public void complete() {

                    }
                });
    }
}
