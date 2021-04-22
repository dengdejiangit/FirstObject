package com.example.administrator.testproject.code.home.presenter;

import com.example.administrator.testproject.base.BaseLogic;
import com.example.administrator.testproject.code.home.model.IHomeModel;
import com.example.administrator.testproject.code.home.view.IHomeView;
import com.example.administrator.testproject.code.main.model.IMainModel;
import com.example.administrator.testproject.code.main.view.IMainView;

/**
 * Create  by
 * User:WS
 * Data:2019/5/22
 */

public class HomePresenter extends BaseLogic<IHomeView, IHomeModel> implements IHomePresenter, IHomeView {

    @Override
    public void getData(int api, int page) {
        getModel().getNetData(this, api, page);
    }

    @Override
    public void onCompleted(Object o, int api) {
        getView().onCompleted(o, api);
    }

    @Override
    public void onError(Throwable e, int api) {
        getView().onError(e, api);
    }
}
