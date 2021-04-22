package com.example.administrator.testproject.code.home.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.administrator.testproject.R;
import com.example.administrator.testproject.bean.FemaleNameBean;

import java.util.List;

public class ListAdapter extends BaseQuickAdapter<FemaleNameBean.DataBean, BaseViewHolder> {

    public ListAdapter(int layoutResId, @Nullable List<FemaleNameBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FemaleNameBean.DataBean item) {
        helper.setText(R.id.tv_net_name, item.getFemalename());
    }
}
