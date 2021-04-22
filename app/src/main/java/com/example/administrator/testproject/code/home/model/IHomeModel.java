package com.example.administrator.testproject.code.home.model;


import com.example.administrator.testproject.base.BaseModel;
import com.example.administrator.testproject.code.home.presenter.HomePresenter;
import com.example.administrator.testproject.code.main.presenter.MainPresenter;

/**
 * Create  by
 * User:WS
 * Data:2019/5/22
 */

public interface IHomeModel extends BaseModel {
    void getNetData(HomePresenter homePresenter, int api, int page);
}
