package com.example.administrator.testproject.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;

import java.lang.reflect.Method;

//对流行机型的刘海屏和全面屏手机进行适配工具类
public class ScreenUtil {

    //获取是否存在底部虚拟按键栏
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;

    }

    //判断是否为全面屏手机
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean navigationGestureEnabled(Context context) {
        int val = Settings.Global.getInt(context.getContentResolver(), getDeviceInfo(), 0);
        return val != 0;
    }

    // 获取设备信息（目前支持几大主流的全面屏手机，华为、小米、oppo、魅族、vivo都可以）
    private static String getDeviceInfo() {
        String brand = Build.BRAND;
        if (TextUtils.isEmpty(brand)) return "navigationbar_is_min";

        if (brand.equalsIgnoreCase("HUAWEI")) {
            return "navigationbar_is_min";
        } else if (brand.equalsIgnoreCase("XIAOMI")) {
            return "force_fsg_nav_bar";
        } else if (brand.equalsIgnoreCase("VIVO")) {
            return "navigation_gesture_on";
        } else if (brand.equalsIgnoreCase("OPPO")) {
            return "navigation_gesture_on";
        } else {
            return "navigationbar_is_min";
        }
    }

    //非全面屏下 虚拟键实际高度(隐藏后高度为0)
    public static int getCurrentNavigationBarHeight(Activity activity) {
        if (isNavigationBarShown(activity)) {
            return getNavigationBarHeight(activity);
        } else {
            return 0;
        }
    }

    // 非全面屏下 虚拟按键是否打开
    private static boolean isNavigationBarShown(Activity activity) {
        //虚拟键的view,为空或者不可见时是隐藏状态
        View view = activity.findViewById(android.R.id.navigationBarBackground);
        if (view == null) {
            return false;
        }
        int visible = view.getVisibility();
        return visible != View.GONE && visible != View.INVISIBLE;
    }

    //非全面屏下 虚拟键高度(无论是否隐藏)
    private static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //判断是否是刘海屏
    public static boolean hasNotchScreen(Activity activity) {
        //各种品牌，小米、华为、OPPO、Vivo、AndroidP
        return getNotchAtMi(activity) == 1 || hasNotchAtHuawei(activity) || hasNotchAtOPPO(activity)
                || hasNotchAtVivo(activity) || isAndroidP(activity) != null;
    }

    //Android P 刘海屏判断
    private static DisplayCutout isAndroidP(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= 28) {
            WindowInsets windowInsets = decorView.getRootWindowInsets();
            if (windowInsets != null)
                return windowInsets.getDisplayCutout();
        }
        return null;
    }

    //小米刘海屏判断.
    private static int getNotchAtMi(Activity activity) {
        int result = 0;
        if (isXiaomi()) {
            try {
                ClassLoader classLoader = activity.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
                //参数类型
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = int.class;
                Method getInt = SystemProperties.getMethod("getInt", paramTypes);
                //参数
                Object[] params = new Object[2];
                params[0] = "ro.miui.notch";
                params[1] = 0;
                result = (Integer) getInt.invoke(SystemProperties, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //华为刘海屏判断
    private static boolean hasNotchAtHuawei(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    //VIVO刘海屏判断
    private static boolean hasNotchAtVivo(Context context) {
        boolean ret = false;
        int VIVO_NOTCH = 0x00000020;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    //OPPO刘海屏判断
    private static boolean hasNotchAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    // 是否是小米手机
    private static boolean isXiaomi() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }

}
