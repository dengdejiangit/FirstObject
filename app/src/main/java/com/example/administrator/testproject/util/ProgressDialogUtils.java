package com.example.administrator.testproject.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

public class ProgressDialogUtils {


    private static ProgressDialog mProgressDialog;

    public static void showDialog(Context context, String message) {
        if (!(context instanceof Activity) || TextUtils.isEmpty(message)) {
            return;
        }
        showDialog((Activity) context, message);
    }

    public static void showDialog(Activity activity, String message) {
        showDialog(activity, message, false);
    }

    public static void showDialog(Activity activity, String message, boolean cancel) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(activity, message, cancel);
        }
        try {
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static ProgressDialog createProgressDialog(Context context, String message, boolean cancelAble) {
        if (!((Activity) context).isFinishing()) {
            try {
                ProgressDialog progressDialog = new ProgressDialog(context,
                        ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage(message);
                progressDialog.setCancelable(cancelAble);
                progressDialog.setCanceledOnTouchOutside(cancelAble);

                return progressDialog;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private static void dismissDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                progressDialog = null;
            }
        }
    }

    public static void dismissDialog() {
        dismissDialog(mProgressDialog);
        mProgressDialog = null;
    }

}
