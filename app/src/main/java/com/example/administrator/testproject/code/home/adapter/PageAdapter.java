package com.example.administrator.testproject.code.home.adapter;


/*
 * Create  by  User:Administrator  Data:2019/5/13  WS
 *
 */

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.administrator.testproject.util.CollectionsUtil;

import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public PageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public int getCount() {
        return CollectionsUtil.getCount(mFragments);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }
}
