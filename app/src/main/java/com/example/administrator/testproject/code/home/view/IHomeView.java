package com.example.administrator.testproject.code.home.view;

import com.example.administrator.testproject.base.BaseView;

public interface IHomeView extends BaseView {
    void onCompleted(Object object, int api);

    void onError(Throwable e, int api);
}
