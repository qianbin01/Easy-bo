package com.qb.easy_bo.bean;


import android.graphics.Bitmap;

public class DownloadFinishedBean {
    private String time;//下载时间
    private long length;//大小
    private int type;//0代表图片，1代表音频，2代表视频，3代表其他
    private String name;//名称
    private Bitmap bitmap;//缩略图
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
