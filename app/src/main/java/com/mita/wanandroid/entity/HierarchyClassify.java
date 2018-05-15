package com.mita.wanandroid.entity;

import java.util.List;

/**
 * 体系分类
 *
 * @author Mita
 * @date 2018/4/21 11:30
 **/
public class HierarchyClassify {

    private int classifyId;
    private String classifyName;
    private List<ChildClassify> childList;

    public class ChildClassify {
        private int childId;
        private String childName;

        public ChildClassify() {
        }

        public ChildClassify(int childId, String childName) {
            this.childId = childId;
            this.childName = childName;
        }

        public int getChildId() {
            return childId;
        }

        public void setChildId(int childId) {
            this.childId = childId;
        }

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }
    }

    public HierarchyClassify() {
    }

    public HierarchyClassify(int classifyId, String classifyName, List<ChildClassify> childList) {
        this.classifyId = classifyId;
        this.classifyName = classifyName;
        this.childList = childList;
    }

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public List<ChildClassify> getChildList() {
        return childList;
    }

    public void setChildList(List<ChildClassify> childList) {
        this.childList = childList;
    }
}
