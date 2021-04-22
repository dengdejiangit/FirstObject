package com.example.administrator.testproject.code.main.presenter;

import com.example.administrator.testproject.base.BaseLogic;
import com.example.administrator.testproject.code.main.model.IMainModel;
import com.example.administrator.testproject.code.main.view.IMainView;

/**
 * Create  by
 * User:WS
 * Data:2019/5/22
 */

public class MainPresenter extends BaseLogic<IMainView, IMainModel> implements IMainPresenter, IMainView {

    @Override
    public void getData(int page, int api) {
        getModel().getPictureData(this, page, api);
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
