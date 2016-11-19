package com.qb.easy_bo.utils.common;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowManager;

import com.qb.easy_bo.R;
import com.qb.easy_bo.bean.DownloadFinishedBean;
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
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DecimalFormat df1 = new DecimalFormat("###.##");

    public static String getMbSize(double size) {
        return df1.format(size);
    }

    public static String getMinuteTime(int length) {
        date = new Date(length);
        return sdf.format(date);
    }

    private static String getTime2(long length) {
        date = new Date(length);
        return sdf2.format(date);
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

    public static List<DownloadFinishedBean> getDownloadInfo(final Context context, final List<DownloadFinishedBean> list, String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
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
                        DownloadFinishedBean bean = new DownloadFinishedBean();
                        file.getUsableSpace();
                        bean.setLength(file.length());
                        bean.setName(file.getName());
                        bean.setTime(getTime2(file.lastModified()));
                        bean.setType(2);
                        bean.setPath(file.getAbsolutePath());
                        Bitmap bitmap;
                        bitmap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Images.Thumbnails.MICRO_KIND);
                        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 200, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                        bean.setBitmap(bitmap);
                        list.add(bean);
                        return true;
                    } else if (name.equalsIgnoreCase(".mp3")
                            || name.equalsIgnoreCase(".wav")
                            || name.equalsIgnoreCase(".wma")
                            || name.equalsIgnoreCase(".mod")
                            || name.equalsIgnoreCase(".ra")
                            || name.equalsIgnoreCase(".cd")
                            || name.equalsIgnoreCase(".md")
                            || name.equalsIgnoreCase(".asf")
                            || name.equalsIgnoreCase(".aac")
                            || name.equalsIgnoreCase(".vqf")
                            || name.equalsIgnoreCase(".3gpp2")
                            || name.equalsIgnoreCase(".flac")
                            || name.equalsIgnoreCase(".ape")
                            || name.equalsIgnoreCase(".mid")
                            || name.equalsIgnoreCase(".ogg")
                            || name.equalsIgnoreCase(".m4a")
                            || name.equalsIgnoreCase(".aac+")
                            || name.equalsIgnoreCase(".au")
                            || name.equalsIgnoreCase(".vqf")) {
                        DownloadFinishedBean bean = new DownloadFinishedBean();
                        file.getUsableSpace();
                        bean.setLength(file.length());
                        bean.setName(file.getName());
                        bean.setTime(getTime2(file.lastModified()));
                        bean.setType(1);
                        bean.setPath(file.getAbsolutePath());
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music_default);
                        bean.setBitmap(bitmap);
                        list.add(bean);
                        return true;
                    } else if (name.equalsIgnoreCase(".bmp")
                            || name.equalsIgnoreCase(".gif")
                            || name.equalsIgnoreCase(".jpg")
                            || name.equalsIgnoreCase(".JPEG")
                            || name.equalsIgnoreCase(".JPEG2000")
                            || name.equalsIgnoreCase(".tiff")
                            || name.equalsIgnoreCase(".psd")
                            || name.equalsIgnoreCase(".png")
                            || name.equalsIgnoreCase(".swf")
                            || name.equalsIgnoreCase(".svg")
                            || name.equalsIgnoreCase(".pcx")
                            || name.equalsIgnoreCase(".dxf")
                            || name.equalsIgnoreCase(".wmf")
                            || name.equalsIgnoreCase(".emf")
                            || name.equalsIgnoreCase(".lic")
                            || name.equalsIgnoreCase(".eps")
                            || name.equalsIgnoreCase(".tga")
                            ) {
                        DownloadFinishedBean bean = new DownloadFinishedBean();
                        file.getUsableSpace();
                        bean.setLength(file.length());
                        bean.setName(file.getName());
                        bean.setTime(getTime2(file.lastModified()));
                        bean.setType(0);
                        bean.setPath(file.getAbsolutePath());
                        Bitmap bitmap = adjustImage((Activity) context, file.getAbsolutePath());
                        bean.setBitmap(bitmap);
                        list.add(bean);
                    } else {
                        DownloadFinishedBean bean = new DownloadFinishedBean();
                        file.getUsableSpace();
                        bean.setLength(file.length());
                        bean.setName(file.getName());
                        bean.setTime(getTime2(file.lastModified()));
                        bean.setType(3);
                        bean.setPath(file.getAbsolutePath());
                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.about);
                        bean.setBitmap(bitmap);
                        list.add(bean);
                    }
                    //判断是不是目录
                } else if (file.isDirectory()) {
                    getDownloadInfo(context, list, file.getAbsolutePath());
                }
                return false;
            }
        });

        return list;
    }

    private static Bitmap adjustImage(Activity activity, String absolutePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;

        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 2;
        // 根据屏的大小和图片大小计算出缩放比例
        if (picWidth > picHeight) {
            if (picWidth > screenWidth)
                opt.inSampleSize = picWidth / screenWidth;
        } else {
            if (picHeight > screenHeight)
                opt.inSampleSize = picHeight / screenHeight;
        }
        opt.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(absolutePath, opt);
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }
}
