package com.qb.easy_bo.view.download;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.qb.easy_bo.Adapter.DownloadingAdapter;
import com.qb.easy_bo.R;
import com.qb.easy_bo.bean.DownloadingBean;
import com.qb.easy_bo.utils.common.NetWorkUtil;
import com.qb.easy_bo.utils.common.SharedPreUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IDownloadingFragment extends Fragment {

    @Bind(R.id.etSearch)
    EditText etSearch;

    @Bind(R.id.btnSearch)
    Button btnSearch;

    @Bind(R.id.recycle_view)
    RecyclerView mRecyclerView;

    private DownloadingAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<DownloadingBean> mList;
    private String mUrl;
    private DownloadingBean bean;
    private final String path = Environment.getExternalStorageDirectory() + "/MyDownloads";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.downloading_fragment, null);
        ButterKnife.bind(this, view);
        initConfig();
        return view;
    }

    private void initConfig() {
        mList = SharedPreUtils.getDownLoadList(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new DownloadingAdapter(getActivity(), mList);
        mAdapter.setPlayOrPauseClickListener(new DownloadingAdapter.onPlayOrPauseClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mList.get(position).isPlayFlag()) {
                    FileDownloader.getImpl().pause(mList.get(position).getId());
                    mList.get(position).setPlayFlag(false);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mList.get(position).setId(createDownloadTask(mList.get(position), mList.get(position).getUrl()).start());
                    mList.get(position).setPlayFlag(true);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        mAdapter.setOnStopClickListner(new DownloadingAdapter.onStopClickListner() {
            @Override
            public void onItemClick(final int position) {
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle("是否要停止" + mList.get(position).getName());
                builder.setMessage("停止后任务将直接被删除");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FileDownloader.getImpl().pause(mList.get(position).getId());
                        new File(mList.get(position).getPath()).delete();
                        new File(FileDownloadUtils.getTempPath(mList.get(position).getPath())).delete();
                        mList.remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();


            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.btnSearch)
    public void beginDown() {
        //隐藏键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getApplicationContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        //判断输入框是否正确
        if (!TextUtils.isEmpty(etSearch.getText().toString())) {
            mUrl = etSearch.getText().toString();
        } else {
            Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "地址不能为空", Snackbar.LENGTH_SHORT).show();
            return;
        }
        //判断网络连接
        if (NetWorkUtil.isNetworkConnected(getActivity())) {
            boolean wifiFlag = (boolean) SharedPreUtils.getData(getActivity(), "wifi", true);
            if (wifiFlag) {
                if (ConnectivityManager.TYPE_WIFI == NetWorkUtil.getConnectedType(getActivity())) {
                    if (mList.size() <= (int) SharedPreUtils.getData(getActivity(), "max_task", -1)) {
                        for (DownloadingBean bean1 : mList) {
                            if (mUrl.equals(bean1.getUrl())) {
                                etSearch.setText("");
                                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "任务已存在", Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        bean = new DownloadingBean();
                        bean.setUrl(mUrl);
                        mList.add(bean);
                        mAdapter.notifyDataSetChanged();
                        bean = mList.get(mList.size() - 1);
                        bean.setId(createDownloadTask(bean, mUrl).start());
                    } else {
                        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "最大下载数为" + SharedPreUtils.getData(getActivity(), "max_task", -1) + ",可在设置中修改", Snackbar.LENGTH_SHORT).show();

                    }
                    etSearch.setText("");
                } else {
                    Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "当前非wifi网络，请前往设置修改", Snackbar.LENGTH_SHORT).show();
                }
            } else {

                etSearch.setText("");
            }
        } else {
            Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "网络无连接", Snackbar.LENGTH_SHORT).show();
        }
    }

    private BaseDownloadTask createDownloadTask(final DownloadingBean bean, final String url) {
        return FileDownloader.getImpl().create(url)
                .setPath(path + "/" + getUrlName(url))
                .setCallbackProgressTimes(300)//回调进度延时
                .setMinIntervalUpdateSpeed(2000)//限速
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        bean.setNowLength(soFarBytes);
                        bean.setLength(totalBytes);
                        bean.setSpeed(task.getSpeed());
                        bean.setPlayFlag(true);
                        bean.setPath(path + "/" + getUrlName(url));
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        for (DownloadingBean bean1 : mList) {
                            if (task.getUrl().equals(bean1.getUrl())) {
                                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), bean.getName() + "地址有误，请重试", Snackbar.LENGTH_SHORT).show();
                                mList.remove(bean);
                                mAdapter.notifyDataSetChanged();
                                return;
                            }
                        }
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                        bean.setName(getUrlName(url));
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        for (DownloadingBean bean1 : mList) {
                            if (task.getUrl().equals(bean1.getUrl())) {
                                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), bean.getName() + "下载完成", Snackbar.LENGTH_SHORT).show();
                                mList.remove(bean);
                                mAdapter.notifyDataSetChanged();
                                return;
                            }
                        }
                        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "地址有误/或文件已存在，请重试", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                    }
                });
    }

    private String getUrlName(String url) {
        String name = url.substring(url.lastIndexOf("/") + 1);
        if (name == null) {
            return "test";
        }
        return name;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mList.size() != 0) {
            for (DownloadingBean bean2 : mList) {
                FileDownloader.getImpl().pause(bean2.getId());
                bean2.setPlayFlag(false);
                SharedPreUtils.saveInfobyJsonArray(getActivity(), mList);
            }
        } else {
            SharedPreUtils.saveInfobyJsonArray(getActivity(), mList);
        }
    }
}
