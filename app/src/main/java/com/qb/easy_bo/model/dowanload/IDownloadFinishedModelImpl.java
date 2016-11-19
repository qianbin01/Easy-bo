package com.qb.easy_bo.model.dowanload;


import android.content.Context;
import android.os.Environment;

import com.qb.easy_bo.bean.DownloadFinishedBean;
import com.qb.easy_bo.model.media.OnLoadCallback;
import com.qb.easy_bo.utils.common.MediaUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IDownloadFinishedModelImpl implements IDownloadFinishedModel<DownloadFinishedBean> {

    private List<DownloadFinishedBean> mList = new ArrayList<>();
    private Context mContext;

    public IDownloadFinishedModelImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void onLoadLocal(final OnLoadCallback callback) {
        Observable.create(new Observable.OnSubscribe<List<DownloadFinishedBean>>() {
            @Override
            public void call(Subscriber<? super List<DownloadFinishedBean>> subscriber) {
                mList = MediaUtil.getDownloadInfo(mContext, mList, Environment.getExternalStorageDirectory()+File.separator+"MyDownloads");
                if (mList.size() == 0) {
                    subscriber.onError(new Throwable());
                } else {
                    subscriber.onNext(mList);
                }
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DownloadFinishedBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.OnFailure("1");
                    }

                    @Override
                    public void onNext(List<DownloadFinishedBean> localVideos) {
                        callback.OnSuccess(localVideos);
                    }
                });

    }
}
