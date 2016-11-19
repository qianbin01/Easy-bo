package com.qb.easy_bo.model.dowanload;


import com.qb.easy_bo.model.media.OnLoadCallback;

public interface IDownloadFinishedModel<T> {
    void onLoadLocal(OnLoadCallback callback);

}
