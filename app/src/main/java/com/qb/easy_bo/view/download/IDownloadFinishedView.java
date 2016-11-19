package com.qb.easy_bo.view.download;


import com.qb.easy_bo.bean.DownloadFinishedBean;

import java.util.List;

public interface IDownloadFinishedView {
    void addList(List<DownloadFinishedBean> list);

    void showProgress();

    void hideProgress();

    void showLoadFailMsg();
}
