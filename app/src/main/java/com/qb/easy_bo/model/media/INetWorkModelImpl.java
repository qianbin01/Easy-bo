package com.qb.easy_bo.model.media;


import com.qb.easy_bo.R;
import com.qb.easy_bo.bean.NetWorkVideo;

import java.util.ArrayList;
import java.util.List;

public class INetWorkModelImpl implements INetWorkModel {


    private List<NetWorkVideo> mList;

    private void initData() {
        mList = new ArrayList<>();
        NetWorkVideo youku = new NetWorkVideo();
        youku.setName("优酷视频");
        youku.setUrl("http://www.youku.com");
        youku.setResId(R.drawable.youku);
        mList.add(youku);
        NetWorkVideo tengxun = new NetWorkVideo();
        tengxun.setName("腾讯视频");
        tengxun.setUrl("http://m.v.qq.com");
        tengxun.setResId(R.drawable.tentxun);
        mList.add(tengxun);
        NetWorkVideo aiqiyi = new NetWorkVideo();
        aiqiyi.setName("爱奇艺");
        aiqiyi.setUrl("http://m.iqiyi.com");
        aiqiyi.setResId(R.drawable.aiqiyi);
        mList.add(aiqiyi);
        NetWorkVideo tudou = new NetWorkVideo();
        tudou.setName("土豆视频");
        tudou.setUrl("http://www.tudou.com");
        tudou.setResId(R.drawable.tudou);
        mList.add(tudou);
        NetWorkVideo xinlang = new NetWorkVideo();
        xinlang.setName("新浪视频");
        xinlang.setUrl("http://video.sina.com.cn");
        xinlang.setResId(R.drawable.xinlang);
        mList.add(xinlang);
        NetWorkVideo souhu = new NetWorkVideo();
        souhu.setName("搜狐视频");
        souhu.setUrl("http://m.tv.sohu.com");
        souhu.setResId(R.drawable.sohu);
        mList.add(souhu);
        NetWorkVideo pptv = new NetWorkVideo();
        pptv.setName("pptv");
        pptv.setUrl("http://m.pptv.com");
        pptv.setResId(R.drawable.pptv);
        mList.add(pptv);
        NetWorkVideo fenghuang = new NetWorkVideo();
        fenghuang.setName("凤凰视频");
        fenghuang.setUrl("http://tv.ifeng.com");
        fenghuang.setResId(R.drawable.fenghuang);
        mList.add(fenghuang);
    }

    @Override
    public void onLoadNetModel(OnLoadCallback<NetWorkVideo> callback) {
        initData();
        if (mList.size() != 0) {
            callback.OnSuccess(mList);
        } else {
            callback.OnFailure("1");
        }
    }
}
