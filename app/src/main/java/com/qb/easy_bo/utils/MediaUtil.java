package com.qb.easy_bo.utils;


import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.qb.easy_bo.bean.LocalVideo;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MediaUtil {
    private static Date date;
    private static SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    private static DecimalFormat df1 = new DecimalFormat("###.##");

    public static String getMbSize(double size) {
        return df1.format(size);
    }

    public static String getMinuteTime(int length) {
        date = new Date(length);
        return sdf.format(date);
    }

    public static List<LocalVideo> getLocalVideo(final List<LocalVideo> list, File file) {
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName();
                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4")
                            || name.equalsIgnoreCase(".3gp")
                            || name.equalsIgnoreCase(".wmv")
                            || name.equalsIgnoreCase(".ts")
                            || name.equalsIgnoreCase(".rmvb")
                            || name.equalsIgnoreCase(".mov")
                            || name.equalsIgnoreCase(".m4v")
                            || name.equalsIgnoreCase(".avi")
                            || name.equalsIgnoreCase(".m3u8")
                            || name.equalsIgnoreCase(".3gpp")
                            || name.equalsIgnoreCase(".3gpp2")
                            || name.equalsIgnoreCase(".mkv")
                            || name.equalsIgnoreCase(".flv")
                            || name.equalsIgnoreCase(".divx")
                            || name.equalsIgnoreCase(".f4v")
                            || name.equalsIgnoreCase(".rm")
                            || name.equalsIgnoreCase(".asf")
                            || name.equalsIgnoreCase(".ram")
                            || name.equalsIgnoreCase(".mpg")
                            || name.equalsIgnoreCase(".v8")
                            || name.equalsIgnoreCase(".swf")
                            || name.equalsIgnoreCase(".m2v")
                            || name.equalsIgnoreCase(".asx")
                            || name.equalsIgnoreCase(".ra")
                            || name.equalsIgnoreCase(".ndivx")
                            || name.equalsIgnoreCase(".xvid")) {
                        LocalVideo video = new LocalVideo();
                        file.getUsableSpace();
                        video.setSize(file.length());
                        video.setVideoName(file.getName());
                        video.setVideoPath(file.getAbsolutePath());
                        Bitmap bitmap;
                        bitmap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Images.Thumbnails.MICRO_KIND);
                        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 200, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                        video.setImage(bitmap);
                        list.add(video);
                        return true;
                    }
                    //判断是不是目录
                } else if (file.isDirectory()) {
                    getLocalVideo(list, file);
                }
                return false;
            }
        });

        return list;
    }

    public static List<LocalVideo> filterVideo(List<LocalVideo> videoInfos) {
        List<LocalVideo> newVideos = new ArrayList<>();
        for (LocalVideo video : videoInfos) {
            File f = new File(video.getVideoPath());
            if (f.exists() && f.isFile() && f.length() > 1048576) {
                newVideos.add(video);
            }
        }
        return newVideos;
    }
}
