package com.mita.wanandroid.entity;

/**
 * 常用网站
 *
 * @author Mita
 * @date 2018/5/8 9:57
 **/
public class UsefulSite {

    private int id;
    private String link;
    private String name;

    public UsefulSite() {
    }

    public UsefulSite(int id, String link, String name) {
        this.id = id;
        this.link = link;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
