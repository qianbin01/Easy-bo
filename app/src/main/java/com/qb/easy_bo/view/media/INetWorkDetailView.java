package com.qb.easy_bo.view.media;



public interface INetWorkDetailView {
    void initWebView(String url);
    void showProgress();

    void hideProgress();

    void showLoadFailMsg();
}
