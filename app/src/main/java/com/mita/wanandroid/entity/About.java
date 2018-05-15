package com.mita.wanandroid.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class About implements MultiItemEntity {

    /**
     * 0:标题
     * 1：内容
     * 2：缩进更深的内容
     * 3：图片
     */
    private int type;
    private String content;
    private int imgRes;

    public About() {
    }

    public About(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
