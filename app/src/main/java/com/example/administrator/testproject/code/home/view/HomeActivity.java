package com.example.administrator.testproject.code.home.view;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.example.administrator.testproject.R;
import com.example.administrator.testproject.base.BaseMvpActivity;
import com.example.administrator.testproject.code.home.adapter.PageAdapter;
import com.example.administrator.testproject.code.home.fragment.AnimFragment;
import com.example.administrator.testproject.code.home.fragment.EventBusFragment;
import com.example.administrator.testproject.code.home.fragment.ListFragment;
import com.example.administrator.testproject.code.home.fragment.PedometerFragment;
import com.example.administrator.testproject.code.home.model.HomeModel;
import com.example.administrator.testproject.code.home.presenter.HomePresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author WS
 */

public class HomeActivity extends BaseMvpActivity<HomePresenter, HomeModel> implements IHomeView {

    @BindView(R.id.view_home_view_pager)
    ViewPager mViewHomeViewPager;
    @BindView(R.id.line_above_bottom_navigation_view)
    View mLineAboveBottomNavigationView;
    @BindView(R.id.view_home_bottom_navigation_view)
    BottomNavigationView mViewHomeBottomNavigationView;

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_home;
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
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new AnimFragment());
        fragments.add(new EventBusFragment());
        fragments.add(new ListFragment());
        fragments.add(new PedometerFragment());

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), fragments);
        mViewHomeViewPager.setAdapter(pageAdapter);
        mViewHomeBottomNavigationView.setOnNavigationItemSelectedListener(mChangeFragment);
        mViewHomeViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    protected void initData() {

    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            if (mViewHomeBottomNavigationView != null) {
                mViewHomeBottomNavigationView.setSelectedItemId(mViewHomeBottomNavigationView.getMenu().getItem(i).getItemId());
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mChangeFragment = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (mViewHomeViewPager == null) {
                return false;
            }
            switch (item.getItemId()) {
                case R.id.item_home:
                    mViewHomeViewPager.setCurrentItem(0);
                    return true;
                case R.id.item_music:
                    mViewHomeViewPager.setCurrentItem(1);
                    return true;
                case R.id.item_list:
                    mViewHomeViewPager.setCurrentItem(2);
                    return true;
                case R.id.item_find:
                    mViewHomeViewPager.setCurrentItem(3);
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    public void onCompleted(Object object, int api) {

    }

    @Override
    public void onError(Throwable e, int api) {

    }
}
