package com.example.administrator.testproject.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.testproject.R;

//Glide的工具类
public class GlideUtil {

    private GlideUtil() {
    }

    private static class GlideUtilHolder {
        private static final GlideUtil INSTANCE = new GlideUtil();
    }

    public static GlideUtil getInstance() {
        return GlideUtilHolder.INSTANCE;
    }

    @SuppressLint("CheckResult")
    private RequestOptions getOptions(int scaleType) {
        RequestOptions requestOptions = new RequestOptions();
        if (scaleType == 1) {
            requestOptions.centerCrop();
        } else if (scaleType == 2) {
            requestOptions.fitCenter();
        } else {
            requestOptions.centerCrop();
        }
//        requestOptions.placeholder(R.drawable.no_banner);
        return requestOptions;
    }

    public void showImg(Context context, Object url, ImageView imageView, int scaleType) {
        Glide.with(context)
                .load(url)
                .apply(getOptions(scaleType))
                .into(imageView);
    }

}
