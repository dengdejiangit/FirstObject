package com.example.administrator.testproject.manager;

import android.app.Activity;

import java.lang.ref.WeakReference;

public class XActivityManager {

    private static XActivityManager sInstance = new XActivityManager();

    private WeakReference<Activity> sCurrentActivityWeakRef;

    private XActivityManager() {

    }

    public static XActivityManager getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

}