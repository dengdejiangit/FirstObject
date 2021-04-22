package com.example.administrator.testproject.http;

import com.blankj.utilcode.util.LogUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {
    private Disposable mDisposable;

    @Override
    public void onSubscribe(Disposable disposable) {
        this.mDisposable = disposable;
    }

    @Override
    public void onNext(T t) {
        next(t);
    }

    @Override
    public void onError(Throwable e) {
        error(e);
        detach();
    }

    @Override
    public void onComplete() {
        complete();
        detach();
    }

    private void detach() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public abstract void next(T t);

    public void error(Throwable e){
        LogUtils.e(e.getMessage());
    }

    public void complete(){

    }
}
