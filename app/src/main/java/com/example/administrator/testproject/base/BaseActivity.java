package com.example.administrator.testproject.base;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.example.administrator.testproject.R;
import com.example.administrator.testproject.XApplication;
import com.example.administrator.testproject.manager.ActivityCollector;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = "activity";
    public XApplication mApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        mApplication = (XApplication) getApplication();
        LogUtils.iTag(TAG, "----" + this.getClass().getSimpleName() + "----");
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        startActivityAnimation();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        startActivityAnimation();
    }

    @Override
    public void finish() {
        super.finish();
        finishActivityAnimation();
    }

    protected void startActivityAnimation() {
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    protected void finishActivityAnimation() {
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    public Activity getActivityByContext(Context context){
        while(context instanceof ContextWrapper){
            if(context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
