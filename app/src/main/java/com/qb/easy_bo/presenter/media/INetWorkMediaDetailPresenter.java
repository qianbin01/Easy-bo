package com.qb.easy_bo.presenter.media;



public interface INetWorkMediaDetailPresenter {
    void loadBegin(String url);
    void loadSuccess();
    void loadFailure();
}
