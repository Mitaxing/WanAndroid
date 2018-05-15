package com.mita.wanandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 *
 * @author Mita
 * @date 2018/4/11 10:08
 **/
public class SpUtils {

    /**
     * 保存登录名和密码
     *
     * @param context Context
     * @param name    用户名
     * @param pwd     密码
     */
    public static void saveLoginInfo(Context context, String name, String pwd) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("loginName", name).putString("loginPwd", pwd).apply();
    }

    /**
     * 清除登录信息
     *
     * @param context Context
     */
    public static void clearLoginInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }

    /**
     * 获取保存的用户名
     *
     * @param context Context
     * @return 用户名
     */
    public static String getLoginName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        return sp.getString("loginName", "");
    }

    /**
     * 获取保存的登录密码
     *
     * @param context Context
     * @return 登录密码
     */
    public static String getLoginPwd(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        return sp.getString("loginPwd", "");
    }
}
