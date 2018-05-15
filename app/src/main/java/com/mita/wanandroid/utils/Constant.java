package com.mita.wanandroid.utils;

/**
 * 常量
 *
 * @author Mita
 * @date 2018/3/28 11:34
 **/
public class Constant {

    public static final String API_BASE = "http://www.wanandroid.com/";
    /**
     * 首页banner
     * 方法：GET
     * 参数：无
     */
    public static final String API_HOME_BANNER = "banner/json";

    /**
     * 体系分类
     * 方法：GET
     * 参数：无
     */
    public static final String API_HIERARCHY_CLASSIFY = "tree/json";

    /**
     * 用户登录
     * 方法：POST
     * 参数：username，password
     */
    public static final String API_LOGIN = "user/login";

    /**
     * 用户注册
     * 方法：POST
     * 参数：username,password,repassword
     */
    public static final String API_REGISTER = "user/register";

    /**
     * 搜索热词
     * 方法：GET
     */
    public static final String API_SEARCH = "hotkey/json";

    /**
     * 常用网站
     * 方法：GET
     */
    public static final String API_USEFUL_SITE = "friend/json";

    /**
     * 导航
     * 方法：GET
     */
    public static final String API_NAVIGATION = "navi/json";

    /**
     * 项目分类
     * 方法：GET
     */
    public static final String API_PROJECT_CLASSIFY = "project/tree/json";

    public static final String SP_NAME = "androidOne";

    /**
     * 不用收藏
     */
    public static final int NO_COLLOECT = 1;

    /**
     * 项目页标题点击广播
     */
    public static final String BROADCAST_TITLE_CLICK = "com.mita.wan.titleClick";
}
