package com.qb.easy_bo.presenter.media;


import android.view.View;

import com.qb.easy_bo.bean.NetWorkVideo;
import com.qb.easy_bo.model.media.INetWorkModel;
import com.qb.easy_bo.model.media.INetWorkModelImpl;
import com.qb.easy_bo.model.media.OnLoadCallback;
import com.qb.easy_bo.view.media.INetWorkView;

import java.util.List;

public class INetWorkMediaPresenterImpl implements INetWorkMediaPresenter, OnLoadCallback {
    private INetWorkView mView;
    private INetWorkModel mModel;

    public INetWorkMediaPresenterImpl(INetWorkView view) {
        this.mView = view;
        mModel = new INetWorkModelImpl();
    }

    @Override
    public void loadbegin() {
        mView.showProgress();
        mModel.onLoadNetModel(this);
    }

    @Override
    public void loadDetail(NetWorkVideo netWorkVideo,View v) {
        mView.openDetail(netWorkVideo,v);
    }

    @Override
    public void OnSuccess(List t) {
        mView.hideProgress();
        mView.addNetList(t);
    }

    @Override
    public void OnFailure(String msg) {
        mView.hideProgress();
        mView.showLoadFailMsg();
    }
}
