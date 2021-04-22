package com.example.administrator.testproject.code.view;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.testproject.R;

/*
 * 尝试获取当前动态壁纸，并加载为APP的壁纸
 * 结果：未完成
 * */

public class DynamicWallpaperActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DynamicWallpaperActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);

        WallpaperInfo wallpaperInfo = wallpaperManager.getWallpaperInfo();

        if (wallpaperInfo == null) {
            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            setContentView(ll);
            WallpaperManager manager = WallpaperManager.getInstance(this);
            Drawable drawable = manager.getDrawable();
            ll.setBackgroundDrawable(drawable);
        } else {
            setContentView(R.layout.activity_dynamic_wallpaper);
            View view = View.inflate(this, R.layout.activity_dynamic_wallpaper, null);
            WallpaperManager manager = WallpaperManager.getInstance(this);
            Drawable drawable = manager.getDrawable();
            view.setBackgroundDrawable(drawable);
        }
    }
}
