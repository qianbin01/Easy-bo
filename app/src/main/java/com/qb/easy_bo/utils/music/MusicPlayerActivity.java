package com.qb.easy_bo.utils.music;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qb.easy_bo.R;
import com.qb.easy_bo.bean.LocalMusic;
import com.qb.easy_bo.utils.MediaUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.VISIBLE;

public class MusicPlayerActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tvTitle)
    TextView tvTitle;

    @Bind(R.id.tvArtist)
    TextView tvArtist;

    @Bind(R.id.operation_tv)
    TextView mOperationTv;

    @Bind(R.id.tvTIme1)
    TextView tvTime1;

    @Bind(R.id.tvTIme2)
    TextView tvTime2;

    @Bind(R.id.ivImage)
    ImageView ivImage;

    @Bind(R.id.ivLast)
    ImageView ivLast;

    @Bind(R.id.ivBackward)
    ImageView ivBackward;

    @Bind(R.id.ivPause)
    ImageView ivPause;

    @Bind(R.id.ivForward)
    ImageView ivForward;

    @Bind(R.id.ivNext)
    ImageView ivNext;

    @Bind(R.id.operation_bg)
    ImageView mOperationBg;

    @Bind(R.id.operation_volume_brightness)
    RelativeLayout mVolumeBrightnessLayout;

    @Bind(R.id.seekBar)
    SeekBar mSeekBar;

    private RotateAnimation mAnimation;
    private int mMaxVolume;//最大声音
    private int mVolume = -1;//当前声音
    private GestureDetector mGestureDetector;
    private AudioManager mAudioManager;
    private MusicPlayService mService;
    private ServiceConnection mConnection;
    private String title, artist, time;
    private int position;
    private Intent intent;
    private Bundle bundle;
    private List<LocalMusic> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_music_player);
        ButterKnife.bind(this);
        initConfig();
        initAnim();
        seekBarChange();
        initClick();
        EventBus.getDefault().register(this);
    }

    //初始化设置
    private void initConfig() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mGestureDetector = new GestureDetector(this, new MyGestureListener());
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        title = getIntent().getStringExtra("title");
        artist = getIntent().getStringExtra("artist");
        time = getIntent().getIntExtra("time", 0) + "";
        position = getIntent().getIntExtra("position", -1);
        tvTitle.setText(title);
        tvArtist.setText(artist);
        tvTime2.setText(MediaUtil.getMinuteTime(Integer.parseInt(time)));
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                mService = ((MusicPlayService.MyPlayService) iBinder).getService();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }
        };
        intent = new Intent(this, MusicPlayService.class);
        intent.putExtra("url", getIntent().getStringExtra("url"));
        bundle = getIntent().getExtras();
        mList = (List<LocalMusic>) bundle.getSerializable("list");
        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    //按钮事件
    private void initClick() {
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                --position;
                if (position >= 0) {
                    mService.setUrl(mList.get(position).getMusicPath());
                    mService.toLast();
                    tvTitle.setText(mList.get(position).getTitle());
                    tvArtist.setText(mList.get(position).getArtist());
                    tvTime2.setText(MediaUtil.getMinuteTime(mList.get(position).getLength()));
                    tvTime1.setText(R.string._00_00);
                    ivPause.setImageResource(R.drawable.pause);
                    mService.setPlaying(true);
                    mSeekBar.setProgress(0);
                } else {
                    position++;
                    Snackbar.make(view, "没有上一首了", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        ivBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.backward();
            }
        });
        ivPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mService.isPlaying()) {
                    ivPause.setImageResource(R.drawable.play);
                } else {
                    ivPause.setImageResource(R.drawable.pause);
                }
                mService.PlayorPause();
            }
        });
        ivForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.forward();
            }
        });
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++position;
                if (position < mList.size()) {
                    mService.setUrl(mList.get(position).getMusicPath());
                    mService.goNext();
                    tvTitle.setText(mList.get(position).getTitle());
                    tvArtist.setText(mList.get(position).getArtist());
                    tvTime2.setText(MediaUtil.getMinuteTime(mList.get(position).getLength()));
                    tvTime1.setText(R.string._00_00);
                    ivPause.setImageResource(R.drawable.pause);
                    mService.setPlaying(true);
                    mSeekBar.setProgress(0);
                } else {
                    position--;
                    Snackbar.make(view, "没有下一首了", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    //进度条手动控制
    private void seekBarChange() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int dest = seekBar.getProgress();
                int time = mService.getDuration();
                int max = seekBar.getMax();
                mService.seekTo(time * dest / max);
            }
        });
    }

    //进度条自动改变
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void autoChangeUi(MusicEvent event) {
        tvTime1.setText(MediaUtil.getMinuteTime(event.msg));
        mSeekBar.setProgress(event.msg * 100 / Integer.parseInt(time));
    }

    //初始化动画
    private void initAnim() {
        mAnimation = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setDuration(2000);
        mAnimation.setInterpolator(new LinearInterpolator());//不停顿
        mAnimation.setRepeatCount(-1);//重复次数
        mAnimation.setFillAfter(true);
        ivImage.setAnimation(mAnimation);
        mAnimation.start();
    }

    //手势监听
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //当收拾结束，并且是单击结束时，控制器隐藏/显示
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowHeight = disp.getHeight();
            onVolumeSlide((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        //双击暂停或开始
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mService.isPlaying()) {
                ivPause.setImageResource(R.drawable.play);
            } else {
                ivPause.setImageResource(R.drawable.pause);
            }
            mService.PlayorPause();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

    }

    //修改音量
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            mVolumeBrightnessLayout.setVisibility(VISIBLE);
            mOperationTv.setVisibility(VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;
        if (index >= 10) {
            mOperationBg.setImageResource(R.drawable.volmn_100);
        } else if (index >= 5 && index < 10) {
            mOperationBg.setImageResource(R.drawable.volmn_60);
        } else if (index > 0 && index < 5) {
            mOperationBg.setImageResource(R.drawable.volmn_30);
        } else {
            mOperationBg.setImageResource(R.drawable.volmn_no);
        }
        mOperationTv.setText((int) (((double) index / mMaxVolume) * 100) + "%");
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
    }

    //滑动判断
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) return true;
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }
        return super.onTouchEvent(event);
    }

    //手势结束
    private void endGesture() {
        mVolume = -1;
        mVolumeBrightnessLayout.setVisibility(View.GONE);
        mOperationTv.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mService.stop();
        unbindService(mConnection);
        stopService(intent);
        EventBus.getDefault().unregister(this);
    }
}
