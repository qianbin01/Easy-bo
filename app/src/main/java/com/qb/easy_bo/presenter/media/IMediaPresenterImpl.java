package com.qb.easy_bo.presenter.media;


import android.content.Context;

import com.qb.easy_bo.model.media.ILocalModel;
import com.qb.easy_bo.model.media.ILocalMusicModelImpl;
import com.qb.easy_bo.model.media.ILocalVideoModelImpl;
import com.qb.easy_bo.model.media.OnLoadCallback;
import com.qb.easy_bo.view.media.ILocalView;

import java.util.List;

public class IMediaPresenterImpl implements ILocalMediaPresenter, OnLoadCallback {
    private ILocalModel mModel;
    private ILocalView mView;

    public IMediaPresenterImpl(ILocalView mView, Context context, int type) {
        this.mView = mView;
        if (type == 0) {
            mModel = new ILocalMusicModelImpl(context);
        } else if (type == 1) {
            mModel = new ILocalVideoModelImpl();
        }
    }

    @Override
    public void loadLocalMedias() {
        mView.showProgress();
        mModel.onLoadLocal(this);
    }


    @Override
    public void OnSuccess(List t) {
        mView.addMedias(t);
        mView.hideProgress();
    }

    @Override
    public void OnFailure(String msg) {
        if ("1".equals(msg)) {
            mView.hideProgress();
            mView.showLoadFailMsg();
        }
    }
}
