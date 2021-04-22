package com.example.administrator.testproject.manager;


import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Create  by
 *
 * @author User:WS
 * @date Data:2019/9/5
 */

public class ActivityCollector {
    private static List<Activity> activities=new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
        if (!activity.isFinishing()) {
            activity.finish();
        }
    }

    public static Activity getLastActivity(){
        if (activities == null){
            return null;
        }
        return activities.get(activities.size() - 1);
    }

    public static void finishAll() {
        for(Activity activity:activities){
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }
}
