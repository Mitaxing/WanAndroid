package com.mita.wanandroid.entity;

public class ProjectClassify {

    private int id;
    private String name;

    public ProjectClassify() {
    }

    public ProjectClassify(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
