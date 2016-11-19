package com.qb.easy_bo.bean;



public class DownloadingBean {
    private int length;//总大小
    private int nowLength;//当前大小
    private int id;//id
    private String name;//名称
    private boolean playFlag;//播放标志
    private int speed;
    private String url;//下载路径
    private String path;//保存路径

    @Override
    public String toString() {
        return "DownloadingBean{" +
                "length=" + length +
                ", nowLength=" + nowLength +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", playFlag=" + playFlag +
                ", speed=" + speed +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isPlayFlag() {
        return playFlag;
    }

    public void setPlayFlag(boolean playFlag) {
        this.playFlag = playFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getNowLength() {
        return nowLength;
    }

    public void setNowLength(int nowLength) {
        this.nowLength = nowLength;
    }


}
