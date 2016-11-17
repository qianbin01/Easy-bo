package com.qb.easy_bo.model.media;


import android.os.Environment;

import com.qb.easy_bo.bean.LocalVideo;
import com.qb.easy_bo.utils.MediaUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ILocalVideoModelImpl implements ILocalModel<LocalVideo> {
    private List<LocalVideo> list = new ArrayList<>();
    @Override
    public void onLoadLocal(final OnLoadCallback callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalVideo>>() {
            @Override
            public void call(Subscriber<? super List<LocalVideo>> subscriber) {
                list = MediaUtil.filterVideo(MediaUtil.getLocalVideo(list, Environment.getExternalStorageDirectory()));
                if (list.size() == 0) {
                    subscriber.onError(new Throwable());
                } else {
                    subscriber.onNext(list);
                }
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<LocalVideo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.OnFailure("1");
                    }

                    @Override
                    public void onNext(List<LocalVideo> localVideos) {
                        callback.OnSuccess(localVideos);
                    }
                });

    }

}
