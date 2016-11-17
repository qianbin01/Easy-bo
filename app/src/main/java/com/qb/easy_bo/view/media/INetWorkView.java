package com.qb.easy_bo.view.media;


import android.view.View;

import com.qb.easy_bo.bean.NetWorkVideo;

import java.util.List;

public interface INetWorkView {
    void addNetList(List<NetWorkVideo> list);
    void showProgress();
    void hideProgress();
    void showLoadFailMsg();
    void openDetail(NetWorkVideo netWorkVideo,View v);
}
