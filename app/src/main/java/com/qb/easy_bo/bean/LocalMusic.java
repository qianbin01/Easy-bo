package com.qb.easy_bo.bean;


import java.io.Serializable;

public class LocalMusic implements Serializable{
    private String musicName;
    private String musicPath;
    private String image; // icon
    private String artist; // 艺术家
    private int length; // 长度
    private int id; // 音乐id
    private String title; // 音乐标题
    private Long Size;

    public LocalMusic() {
    }

    public Long getSize() {
        return Size;
    }

    public void setSize(Long size) {
        Size = size;
    }

    @Override
    public String toString() {
        return "LocalMusic{" +
                "musicName='" + musicName + '\'' +
                ", musicPath='" + musicPath + '\'' +
                ", image=" + image +
                ", artist='" + artist + '\'' +
                ", length=" + length +
                ", id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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
}
