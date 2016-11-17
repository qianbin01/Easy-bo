package com.qb.easy_bo.presenter.media;


import com.qb.easy_bo.view.media.INetWorkDetailView;

public class INetWorkMediaDetailPresenterImpl implements INetWorkMediaDetailPresenter {

    private INetWorkDetailView mView;

    public INetWorkMediaDetailPresenterImpl(INetWorkDetailView view) {
        this.mView = view;
    }

    @Override
    public void loadBegin(String url) {
        mView.showProgress();
        mView.initWebView(url);
    }

    @Override
    public void loadSuccess() {
        mView.hideProgress();
    }

    @Override
    public void loadFailure() {
        mView.hideProgress();
        mView.showLoadFailMsg();
    }
}
