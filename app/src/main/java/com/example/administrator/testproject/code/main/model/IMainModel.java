package com.example.administrator.testproject.code.main.model;


import com.example.administrator.testproject.base.BaseModel;
import com.example.administrator.testproject.code.main.presenter.MainPresenter;

/**
 * Create  by
 * User:WS
 * Data:2019/5/22
 */

public interface IMainModel extends BaseModel {
    void getPictureData(MainPresenter mainPresenter, int page, int api);
}
