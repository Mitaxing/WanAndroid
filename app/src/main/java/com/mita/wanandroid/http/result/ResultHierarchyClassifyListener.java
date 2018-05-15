package com.mita.wanandroid.http.result;

import com.mita.wanandroid.entity.HierarchyClassify;

import java.util.List;

public interface ResultHierarchyClassifyListener {

    void onHierarchyClassifySuccess(List<HierarchyClassify> list);

    void onHierarchyClassifyFail(String msg);
}
