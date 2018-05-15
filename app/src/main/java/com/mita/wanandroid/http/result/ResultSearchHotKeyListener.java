package com.mita.wanandroid.http.result;

import java.util.List;

public interface ResultSearchHotKeyListener {

    void requestSearchHotKeySuccess(List<String> list);

    void requestSearchHotKeyFail(String msg);
}
