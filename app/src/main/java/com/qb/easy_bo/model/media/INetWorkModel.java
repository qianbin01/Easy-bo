package com.qb.easy_bo.model.media;


import com.qb.easy_bo.bean.NetWorkVideo;

public interface INetWorkModel {
    void onLoadNetModel(OnLoadCallback<NetWorkVideo> callback);
}
