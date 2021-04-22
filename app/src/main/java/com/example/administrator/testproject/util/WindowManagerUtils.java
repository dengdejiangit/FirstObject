package com.example.administrator.testproject.util;


/*
 * Create  by  User:Administrator  Data:2019/5/5  WS
 *
 */

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

public class WindowManagerUtils {

    public static WindowManager.LayoutParams getWindowManager() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                PixelFormat.TRANSPARENT);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;

        return layoutParams;
    }

}
