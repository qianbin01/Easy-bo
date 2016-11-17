package com.qb.easy_bo.model.media;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio;

import com.qb.easy_bo.bean.LocalMusic;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ILocalMusicModelImpl implements ILocalModel<LocalMusic> {
    private Context mContext;
    private List<LocalMusic> list = new ArrayList<>();

    public ILocalMusicModelImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void onLoadLocal(final OnLoadCallback callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalMusic>>() {
            @Override
            public void call(Subscriber<? super List<LocalMusic>> subscriber) {
                initData();
                if (list.size() == 0) {
                    subscriber.onError(new Throwable());
                } else {
                    subscriber.onNext(list);
                }
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<LocalMusic>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.OnFailure("1");
                    }

                    @Override
                    public void onNext(List<LocalMusic> localMusics) {
                        callback.OnSuccess(list);
                    }
                });

    }

    private void initData() {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                LocalMusic music = new LocalMusic();
//                music.setId(cursor.getInt(cursor.getColumnIndex(Audio.Media._ID))); //获取唯一id
                music.setMusicPath(cursor.getString(cursor.getColumnIndex(Audio.Media.DATA))); //文件路径
//                music.setMusicName(cursor.getString(cursor.getColumnIndex(Audio.Media.DISPLAY_NAME))); //文件名
                music.setArtist(cursor.getString(cursor.getColumnIndex(Audio.Media.ARTIST)));
                music.setLength(cursor.getInt(cursor.getColumnIndex(Audio.Media.DURATION)));
                music.setTitle(cursor.getString(cursor.getColumnIndex(Audio.Media.TITLE)));
                music.setSize(cursor.getLong(cursor.getColumnIndex(Audio.Media.SIZE)));
                list.add(music);
            }
            cursor.close();
        }
    }
}
