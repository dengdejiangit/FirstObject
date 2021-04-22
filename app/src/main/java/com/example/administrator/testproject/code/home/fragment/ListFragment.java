package com.example.administrator.testproject.code.home.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.example.administrator.testproject.R;
import com.example.administrator.testproject.base.BaseMvpFragment;
import com.example.administrator.testproject.bean.FemaleNameBean;
import com.example.administrator.testproject.code.home.adapter.ListAdapter;
import com.example.administrator.testproject.code.home.model.HomeModel;
import com.example.administrator.testproject.code.home.presenter.HomePresenter;
import com.example.administrator.testproject.code.home.view.IHomeView;
import com.example.administrator.testproject.config.ApiConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ListFragment extends BaseMvpFragment<HomePresenter, HomeModel> implements IHomeView {

    @BindView(R.id.view_list)
    RecyclerView mViewListList;
    @BindView(R.id.srl_list)
    SmartRefreshLayout mSmartRefreshLayout;
    private ListAdapter mListAdapter;
    private int page = 0;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mPresenter != null) {
            mPresenter.getData(ApiConfig.GET_FEMALE_NAME_LIST, page);
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_list;
    }

    @Override
    protected HomePresenter getPresenter() {
        return new HomePresenter();
    }

    @Override
    protected HomeModel getModel() {
        return new HomeModel();
    }

    @Override
    protected void initView() {
        mListAdapter = new ListAdapter(R.layout.item_list, null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mViewListList.setLayoutManager(linearLayoutManager);
        mViewListList.setAdapter(mListAdapter);
        mListAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            try {
                FemaleNameBean.DataBean dataBean = (FemaleNameBean.DataBean) adapter.getData().get(position);
                //获取剪贴板管理器
                ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", dataBean.getFemalename());
                // 将ClipData内容放到系统剪贴板里。
                if (cm != null){
                    cm.setPrimaryClip(mClipData);
                    ToastUtils.showShort("复制成功");
                }
                return true;
            } catch (Exception e) {
                ToastUtils.showShort("复制失败");
                return false;
            }
        });
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(mActivity));
        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(mActivity));
        mSmartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            if (mPresenter != null) {
                page = 0;
                mPresenter.getData(ApiConfig.GET_FEMALE_NAME_LIST, page);
            }
            mSmartRefreshLayout.setEnableRefresh(false);
        });
        mSmartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if (mPresenter != null) {
                page = page + 1;
                mPresenter.getData(ApiConfig.GET_FEMALE_NAME_LIST, page);
            }
            mSmartRefreshLayout.setEnableLoadMore(false);
        });
    }

    @Override
    protected void initData() {
        if (mPresenter != null) {
            mPresenter.getData(ApiConfig.GET_FEMALE_NAME_LIST, page);
        }
    }

    @Override
    public void onCompleted(Object object, int api) {
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setEnableLoadMore(true);
        mSmartRefreshLayout.finishLoadMore();
        mSmartRefreshLayout.finishRefresh();
        if (api == ApiConfig.GET_FEMALE_NAME_LIST && mListAdapter != null) {
            FemaleNameBean femaleNameBean = (FemaleNameBean) object;
            List<FemaleNameBean.DataBean> dataBeans = new ArrayList<>(femaleNameBean.getData());
            mListAdapter.addData(dataBeans);
        }
    }

    @Override
    public void onError(Throwable e, int api) {
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setEnableLoadMore(true);
        mSmartRefreshLayout.finishLoadMore();
        mSmartRefreshLayout.finishRefresh();
        if (api == ApiConfig.GET_FEMALE_NAME_LIST) {
            ToastUtils.showShort("请求失败，请检查网络或稍后重试:\n" + e.toString());
        }
    }
}
