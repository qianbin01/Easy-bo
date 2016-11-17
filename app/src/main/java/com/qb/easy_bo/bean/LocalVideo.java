package com.qb.easy_bo.bean;


import android.graphics.Bitmap;

public class LocalVideo {
    private String videoName;
    private String videoPath;
    private Bitmap image;
    private String artist; // 艺术家
    private int length; // 长度
    private int id; // id
    private String title; // 标题
    private Long Size;

    @Override
    public String toString() {
        return "LocalVideo{" +
                "videoName='" + videoName + '\'' +
                ", videoPath='" + videoPath + '\'' +
                ", image='" + image + '\'' +
                ", artist='" + artist + '\'' +
                ", length=" + length +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", Size=" + Size +
                '}';
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSize() {
        return Size;
    }

    public void setSize(Long size) {
        Size = size;
    }
}
