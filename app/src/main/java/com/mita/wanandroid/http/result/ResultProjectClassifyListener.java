package com.mita.wanandroid.http.result;

import com.mita.wanandroid.entity.ProjectClassify;

import java.util.List;

public interface ResultProjectClassifyListener {

    void onProjectClassifySuccess(List<ProjectClassify> list);

    void onProjectClassifyFail(String msg);
}
