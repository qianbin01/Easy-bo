package com.qb.easy_bo.utils.video;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.qb.easy_bo.R;

import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;


public class MyMediaController extends MediaController {

    private static final int HIDEFRAM = 0;

    private GestureDetector mGestureDetector;
    private ImageButton img_back;//返回键
    private ImageView img_Battery;//电池电量显示
    private TextView textViewTime;//时间提示
    private TextView textViewBattery;//文字显示电池
    private VideoView mVideoView;
    private Activity activity;
    private Context context;
    private int controllerWidth = 0;//设置mediaController高度为了使横屏时top显示在屏幕顶端

    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private TextView mOperationTv;
    private AudioManager mAudioManager;
    private SeekBar progress;
    //最大声音
    private int mMaxVolume;
    // 当前声音
    private int mVolume = -1;
    //当前亮度
    private float mBrightness = -1f;

    //返回监听
    private View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (activity != null) {
                activity.finish();
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {

        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {

        }

        public void onStopTrackingTouch(SeekBar bar) {

        }
    };


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HIDEFRAM:
                    mVolumeBrightnessLayout.setVisibility(View.GONE);
                    mOperationTv.setVisibility(View.GONE);
                    break;
            }
        }
    };


    //videoview 用于对视频进行控制的等，activity为了退出
    public MyMediaController(Context context, VideoView videoView, Activity activity) {
        super(context);
        this.context = context;
        this.mVideoView = videoView;
        this.activity = activity;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        controllerWidth = wm.getDefaultDisplay().getHeight();
        mGestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    @Override
    protected View makeControllerView() {
        View v = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(getResources().getIdentifier("mymediacontroller", "layout", getContext().getPackageName()), this);
        v.setMinimumHeight(controllerWidth);
        img_back = (ImageButton) v.findViewById(R.id.mediacontroller_top_back);
        img_Battery = (ImageView) v.findViewById(R.id.mediacontroller_imgBattery);
        img_back.setOnClickListener(backListener);
        textViewBattery = (TextView) v.findViewById(R.id.mediacontroller_Battery);
        textViewTime = (TextView) v.findViewById(R.id.mediacontroller_time);
        progress = (SeekBar) v.findViewById(R.id.mediacontroller_seekbar);
        progress.setOnSeekBarChangeListener(seekListener);
        //mid
        mVolumeBrightnessLayout = v.findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) v.findViewById(R.id.operation_bg);
        mOperationTv = (TextView) v.findViewById(R.id.operation_tv);
        mOperationTv.setVisibility(View.GONE);
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        return v;

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        System.out.println("MYApp-MyMediaController-dispatchKeyEvent");
        return true;
    }

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

    /**
     * 手势结束
     */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
//        mVolumeBrightnessLayout.setVisibility(View.GONE);
//        mOperationTv.setVisibility(View.GONE);
        myHandler.removeMessages(HIDEFRAM);
//        mHandler.sendEmptyMessageDelayed(0, 500);
        myHandler.sendEmptyMessageDelayed(HIDEFRAM, 1);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //当收拾结束，并且是单击结束时，控制器隐藏/显示
            toggleMediaControlsVisiblity();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = activity.getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();
            if (mOldX > windowWidth / 2.0) {// 右边滑动 屏幕一半
                onVolumeSlide((mOldY - y) / windowHeight);
            } else if (mOldX < windowWidth / 2.0) {// 左边滑动 一半
                onBrightnessSlide((mOldY - y) / windowHeight);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        //双击暂停或开始
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            playOrPause();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 显示
//            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
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
        //DecimalFormat    df   = new DecimalFormat("######0.00");
        mOperationTv.setText((int) (((double) index / mMaxVolume) * 100) + "%");
        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

    }

    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = activity.getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            //mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
            mOperationTv.setVisibility(VISIBLE);

        }


        WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        activity.getWindow().setAttributes(lpa);

        mOperationTv.setText((int) (lpa.screenBrightness * 100) + "%");
        if (lpa.screenBrightness * 100 >= 90) {
            mOperationBg.setImageResource(R.drawable.light_100);
        } else if (lpa.screenBrightness * 100 >= 80 && lpa.screenBrightness * 100 < 90) {
            mOperationBg.setImageResource(R.drawable.light_90);
        } else if (lpa.screenBrightness * 100 >= 70 && lpa.screenBrightness * 100 < 80) {
            mOperationBg.setImageResource(R.drawable.light_80);
        } else if (lpa.screenBrightness * 100 >= 60 && lpa.screenBrightness * 100 < 70) {
            mOperationBg.setImageResource(R.drawable.light_70);
        } else if (lpa.screenBrightness * 100 >= 50 && lpa.screenBrightness * 100 < 60) {
            mOperationBg.setImageResource(R.drawable.light_60);
        } else if (lpa.screenBrightness * 100 >= 40 && lpa.screenBrightness * 100 < 50) {
            mOperationBg.setImageResource(R.drawable.light_50);
        } else if (lpa.screenBrightness * 100 >= 30 && lpa.screenBrightness * 100 < 40) {
            mOperationBg.setImageResource(R.drawable.light_40);
        } else if (lpa.screenBrightness * 100 >= 20 && lpa.screenBrightness * 100 < 20) {
            mOperationBg.setImageResource(R.drawable.light_30);
        } else if (lpa.screenBrightness * 100 >= 10 && lpa.screenBrightness * 100 < 20) {
            mOperationBg.setImageResource(R.drawable.light_20);
        }


    }


    public void setTime(String time) {
        if (textViewTime != null)
            textViewTime.setText(time);
    }

    //显示电量，
    public void setBattery(String stringBattery) {
        if (textViewTime != null && img_Battery != null) {
            textViewBattery.setText(stringBattery + "%");
            int battery = Integer.valueOf(stringBattery);
            if (battery < 15)
                img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_15));
            if (battery < 30 && battery >= 15)
                img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_15));
            if (battery < 45 && battery >= 30)
                img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_30));
            if (battery < 60 && battery >= 45)
                img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_45));
            if (battery < 75 && battery >= 60)
                img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_60));
            if (battery < 90 && battery >= 75)
                img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_75));
            if (battery > 90)
                img_Battery.setImageDrawable(getResources().getDrawable(R.drawable.battery_90));
        }
    }

    //隐藏/显示
    private void toggleMediaControlsVisiblity() {
        if (isShowing()) {
            hide();
        } else {
            show();
        }
    }

    //播放与暂停
    private void playOrPause() {
        if (mVideoView != null)
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
            } else {
                mVideoView.start();
            }
    }
}

