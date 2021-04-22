package com.example.administrator.testproject.code.home.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.administrator.testproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//揭露动画  ViewAnimationUtils  适用于android5.0以上版本
public class AnimFragment extends Fragment {
    @BindView(R.id.iv_icon)
    ImageView mImageView;
    Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_anim, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean animStatus = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onViewClicked(View view) {
        if (animStatus) {
            ToastUtils.showShort("动画未结束");
            return;
        }
        int width = mImageView.getWidth();
        int height = mImageView.getHeight();
        switch (view.getId()) {
            case R.id.btn1:
                Animator animator1 = ViewAnimationUtils.createCircularReveal(
                        mImageView, width / 2, height / 2, mImageView.getWidth(), 0);
                animator1.setDuration(2000);
                animator1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animStatus = false;
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        animStatus = true;
                    }
                });
                animator1.start();
                break;
            case R.id.btn2:
                Animator animator2 = ViewAnimationUtils.createCircularReveal(
                        mImageView, 0, 0, 0, (float) Math.hypot(width, height));//宽的平方加上高的平方的根号
                animator2.setDuration(2000);
                animator2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animStatus = false;
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        animStatus = true;
                    }
                });
                animator2.start();
                break;

            case R.id.btn3:
                Animator animator3 = ViewAnimationUtils.createCircularReveal(
                        mImageView, width / 2, height / 2, 0, (float) Math.hypot(width, height));
                animator3.setDuration(2000);
                animator3.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animStatus = false;
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        animStatus = true;
                    }
                });
                animator3.start();
                break;
        }
    }
}
