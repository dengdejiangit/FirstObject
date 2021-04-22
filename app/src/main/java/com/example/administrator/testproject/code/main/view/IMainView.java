package com.example.administrator.testproject.code.main.view;

import com.example.administrator.testproject.base.BaseView;

public interface IMainView extends BaseView {
    void onCompleted(Object object, int api);

    void onError(Throwable e, int api);
}
