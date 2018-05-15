package com.mita.wanandroid.entity;

/**
 * 首页tab
 *
 * @author Mita
 * @date 2018/3/27 11:56
 **/
public class HomeTab {

    private int imgRes;
    private int name;
    private boolean focus;

    public HomeTab(int imgRes, int name, boolean focus) {
        this.imgRes = imgRes;
        this.name = name;
        this.focus = focus;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

}
