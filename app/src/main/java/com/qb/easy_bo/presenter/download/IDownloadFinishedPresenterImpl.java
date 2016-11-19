package com.qb.easy_bo.presenter.download;


import android.content.Context;

import com.qb.easy_bo.model.dowanload.IDownloadFinishedModel;
import com.qb.easy_bo.model.dowanload.IDownloadFinishedModelImpl;
import com.qb.easy_bo.model.media.OnLoadCallback;
import com.qb.easy_bo.view.download.IDownloadFinishedView;

import java.util.List;

public class IDownloadFinishedPresenterImpl implements IDownloadFinishedPresenter, OnLoadCallback {
    private IDownloadFinishedView mView;
    private IDownloadFinishedModel mModel;

    public IDownloadFinishedPresenterImpl(Context context,IDownloadFinishedView view) {
        this.mView = view;
        mModel = new IDownloadFinishedModelImpl(context);
    }

    @Override
    public void OnSuccess(List t) {
        mView.hideProgress();
        mView.addList(t);
    }

    @Override
    public void OnFailure(String msg) {
        if ("1".equals(msg)) {
            mView.hideProgress();
            mView.showLoadFailMsg();
        }
    }

    @Override
    public void loadLocalMedias() {
        mView.showProgress();
        mModel.onLoadLocal(this);
    }
}
