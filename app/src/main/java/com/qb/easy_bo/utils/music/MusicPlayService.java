package com.qb.easy_bo.utils.music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;


public class MusicPlayService extends Service {
    private MediaPlayer mPlayer;
    private String mUrl;
    private boolean isPlaying = false;

    @Override
    public IBinder onBind(Intent intent) {
        return new MyPlayService();
    }

    public class MyPlayService extends Binder {
        MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mUrl = intent.getStringExtra("url");
        }
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(mUrl);
            mPlayer.prepare();
            mPlayer.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void sendPosition(MusicEvent event) {
        if (isPlaying) {
            EventBus.getDefault().post(new MusicEvent(mPlayer.getCurrentPosition()));
        }
    }

    public void PlayorPause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            EventBus.getDefault().unregister(this);
            isPlaying = false;
        } else {
            mPlayer.start();
            EventBus.getDefault().register(this);
            EventBus.getDefault().post(new MusicEvent(mPlayer.getCurrentPosition()));
            isPlaying = true;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void stop() {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    public int getDuration() {
        if (mPlayer != null) {
            return mPlayer.getDuration();
        }
        return 0;
    }

    public void seekTo(int i) {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.seekTo(i);
            EventBus.getDefault().post(new MusicEvent(mPlayer.getCurrentPosition()));
        }
    }

    public void setUrl(String url) {
        if(mPlayer!=null){
            mPlayer.stop();
            mPlayer = null;
        }
        this.mUrl = url;
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(mUrl);
            mPlayer.prepare();
            mPlayer.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //上一首
    public void toLast() {
        if (mPlayer != null) {
            mPlayer.start();
            EventBus.getDefault().post(new MusicEvent(mPlayer.getCurrentPosition()));
        }
    }

    //下一首
    public void goNext() {
        if (mPlayer != null) {
            mPlayer.start();
            EventBus.getDefault().post(new MusicEvent(mPlayer.getCurrentPosition()));
        }
    }

    //快进
    public void forward() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.seekTo(mPlayer.getCurrentPosition() + 5000);
            EventBus.getDefault().post(new MusicEvent(mPlayer.getCurrentPosition()));
        }
    }

    //快退
    public void backward() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.seekTo(mPlayer.getCurrentPosition() - 5000);
            EventBus.getDefault().post(new MusicEvent(mPlayer.getCurrentPosition()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
