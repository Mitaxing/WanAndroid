package com.mita.wanandroid.http.request;

import android.content.Context;

import com.mita.wanandroid.entity.HierarchyClassify;
import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.api.ApiHierarchyClassify;
import com.mita.wanandroid.http.response.HierarchyClassifyBean;
import com.mita.wanandroid.http.result.ResultHierarchyClassifyListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 请求体系分类数据
 *
 * @author Mita
 * @date 2018/4/21 9:49
 **/
public class RequestHierarchyClassify {

    private ApiHierarchyClassify mApiHierarchyClassify;
    private ResultHierarchyClassifyListener listener;

    public RequestHierarchyClassify(Context context, ResultHierarchyClassifyListener listener) {
        mApiHierarchyClassify = ApiRequest.getInstance(context).create(ApiHierarchyClassify.class);
        this.listener = listener;
    }

    /**
     * 请求体系分类
     */
    public void getHierarchyClassify() {
        Call<HierarchyClassifyBean> request = mApiHierarchyClassify.getHierarchyClassify();
        request.enqueue(new Callback<HierarchyClassifyBean>() {
            @Override
            public void onResponse(Call<HierarchyClassifyBean> call, Response<HierarchyClassifyBean> response) {
                HierarchyClassifyBean hierarchyClassifyBean = response.body();
                if (hierarchyClassifyBean == null || listener == null) {
                    return;
                }
                if (hierarchyClassifyBean.getErrorCode() == 0) {
                    List<HierarchyClassify> list = covertHierarchyClassify(hierarchyClassifyBean);
                    listener.onHierarchyClassifySuccess(list);
                } else {
                    listener.onHierarchyClassifyFail(hierarchyClassifyBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(Call<HierarchyClassifyBean> call, Throwable t) {
                if (listener != null) {
                    listener.onHierarchyClassifyFail(t.getMessage());
                }
            }
        });
    }

    /**
     * 转换体系分类数据
     *
     * @param hierarchyClassifyBean 返回数据bean
     * @return List<HierarchyClassify>
     */
    private List<HierarchyClassify> covertHierarchyClassify(HierarchyClassifyBean hierarchyClassifyBean) {
        List<HierarchyClassify> list = new ArrayList<>();
        List<HierarchyClassifyBean.DataBean> dataBeanList = hierarchyClassifyBean.getData();
        for (HierarchyClassifyBean.DataBean dataBean : dataBeanList) {
            HierarchyClassify hierarchyClassify = new HierarchyClassify();
            hierarchyClassify.setClassifyId(dataBean.getId());
            hierarchyClassify.setClassifyName(dataBean.getName());

            List<HierarchyClassify.ChildClassify> childList = new ArrayList<>();
            List<HierarchyClassifyBean.DataBean.ChildrenBean> childrenBeanList = dataBean.getChildren();
            for (HierarchyClassifyBean.DataBean.ChildrenBean childrenBean : childrenBeanList) {
                HierarchyClassify.ChildClassify childClassify = hierarchyClassify.new ChildClassify();
                childClassify.setChildId(childrenBean.getId());
                childClassify.setChildName(childrenBean.getName());
                childList.add(childClassify);
            }
            hierarchyClassify.setChildList(childList);
            list.add(hierarchyClassify);
        }
        return list;
    }
}
