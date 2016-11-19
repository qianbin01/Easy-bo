package com.qb.easy_bo.utils.common;


import android.content.Context;
import android.content.SharedPreferences;

import com.qb.easy_bo.bean.DownloadFinishedBean;
import com.qb.easy_bo.bean.DownloadingBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SharedPreUtils {
    private static final String SHAREDPREFERENCES_NAME = "SharedPre_Status";
    private static final String BIND__FLAG = "bind_flag";

    public static void saveData(Context context, String key, Object data) {

        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.apply();
    }

    public static Object getData(Context context, String key, Object defValue) {
        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);

        //defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }
        return null;
    }


    public static void saveInfobyJsonArray(Context context, List<DownloadingBean> datas) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (DownloadingBean bean : datas) {
            JSONObject temp = new JSONObject();
            try {
                temp.put("length", bean.getLength());
                temp.put("nowLength", bean.getNowLength());
                temp.put("id", bean.getId());
                temp.put("name", bean.getName());
                temp.put("playFlag", bean.isPlayFlag());
                temp.put("speed", bean.getSpeed());
                temp.put("url", bean.getUrl());
                temp.put("path", bean.getPath());
                jsonArray.put(temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            jsonObject.put("downloadInfo", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        saveData(context, "downloadInfo", jsonObject.toString());
    }

    public static List<DownloadingBean> getDownLoadList(Context context) {
        List<DownloadingBean> list = new ArrayList<>();
        String downloadInfo = (String) getData(context, "downloadInfo", "a");
        try {
            JSONObject jsonObject = new JSONObject(downloadInfo);
            JSONArray jsonArray = jsonObject.getJSONArray("downloadInfo");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    DownloadingBean bean = new DownloadingBean();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    bean.setId(jsonObject1.getInt("id"));
                    bean.setSpeed(jsonObject1.getInt("speed"));
                    bean.setPath(jsonObject1.getString("path"));
                    bean.setPlayFlag(jsonObject1.getBoolean("playFlag"));
                    bean.setName(jsonObject1.getString("name"));
                    bean.setLength(jsonObject1.getInt("length"));
                    bean.setNowLength(jsonObject1.getInt("nowLength"));
                    bean.setUrl(jsonObject1.getString("url"));
                    if (bean.getUrl() != null) {
                        list.add(bean);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
