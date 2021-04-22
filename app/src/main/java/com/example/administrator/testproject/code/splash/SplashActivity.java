package com.example.administrator.testproject.code.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.BarUtils;
import com.example.administrator.testproject.R;
import com.example.administrator.testproject.base.BaseActivity;
import com.example.administrator.testproject.code.main.view.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends BaseActivity {

    private static final int RC_CAMERA_AND_LOCATION = 0x111;
    public static final int VALUE_INT_REQUEST_CODE_SPLASH = 0x112;
    @BindView(R.id.splash_rl)
    RelativeLayout splashRl;

    private Handler mHandler = new Handler();

    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setNavBarVisibility(this, false);
        BarUtils.setStatusBarVisibility(this, false);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        requestPermission();
    }

    private void requestPermission() {
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.app_name), RC_CAMERA_AND_LOCATION, perms);
        } else {
            MainActivity.launchForResult(SplashActivity.this,VALUE_INT_REQUEST_CODE_SPLASH);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_CAMERA_AND_LOCATION) {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
            MainActivity.launchForResult(SplashActivity.this,VALUE_INT_REQUEST_CODE_SPLASH);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VALUE_INT_REQUEST_CODE_SPLASH) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 200);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
