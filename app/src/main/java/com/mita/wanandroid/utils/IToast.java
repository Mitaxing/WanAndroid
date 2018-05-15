package com.mita.wanandroid.utils;

import android.content.Context;
import android.widget.Toast;

public class IToast {

    /**
     * 显示toast
     *
     * @param context Context
     * @param msg     显示内容
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
