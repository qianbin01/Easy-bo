package com.qb.easy_bo.presenter.media;


import android.view.View;

import com.qb.easy_bo.bean.NetWorkVideo;

public interface INetWorkMediaPresenter {
    void loadbegin();
    void loadDetail(NetWorkVideo netWorkVideo,View v);
}
