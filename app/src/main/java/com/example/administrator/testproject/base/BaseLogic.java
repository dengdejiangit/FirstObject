package com.example.administrator.testproject.base;

import java.lang.ref.WeakReference;

public abstract class BaseLogic<V extends BaseView, M extends BaseModel> {
    private WeakReference<V> mWeakReferenceV;
    private WeakReference<M> mWeakReferenceM;
    private V view;
    private M model;

    void attach(V view, M model) {
        this.view = view;
        this.model = model;
        mWeakReferenceV = new WeakReference<>(view);
        mWeakReferenceM = new WeakReference<>(model);
    }

    void detach() {
        if (mWeakReferenceV != null) {
            mWeakReferenceV.clear();
            mWeakReferenceV = null;
        }
        if (mWeakReferenceM != null) {
            mWeakReferenceM.clear();
            mWeakReferenceM = null;
        }
    }

    protected V getView() {
        if (mWeakReferenceV == null){
            return new WeakReference<>(view).get();
        }
        return mWeakReferenceV.get();
    }

    protected M getModel() {
        if (mWeakReferenceM == null){
            return new WeakReference<>(model).get();
        }else {
            return mWeakReferenceM.get();
        }
    }
}
