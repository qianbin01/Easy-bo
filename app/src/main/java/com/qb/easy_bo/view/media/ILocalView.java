package com.qb.easy_bo.view.media;


import java.util.List;

public interface ILocalView<T> {
    void addMedias(List<T> t);

    void showProgress();

    void hideProgress();

    void showLoadFailMsg();
}
