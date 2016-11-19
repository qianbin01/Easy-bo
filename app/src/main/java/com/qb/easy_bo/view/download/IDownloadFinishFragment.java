package com.qb.easy_bo.view.download;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.qb.easy_bo.Adapter.DownloadFinishedAdapter;
import com.qb.easy_bo.R;
import com.qb.easy_bo.bean.DownloadFinishedBean;
import com.qb.easy_bo.presenter.download.IDownloadFinishedPresenter;
import com.qb.easy_bo.presenter.download.IDownloadFinishedPresenterImpl;
import com.qb.easy_bo.utils.common.MediaUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IDownloadFinishFragment extends Fragment implements IDownloadFinishedView {

    @Bind(R.id.progress)
    ProgressBar mProgress;
    @Bind(R.id.recycle_view)
    RecyclerView mRecyclerView;

    private IDownloadFinishedPresenter mPresenter;
    private LinearLayoutManager mLayoutManager;
    private List<DownloadFinishedBean> mList;
    private DownloadFinishedAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.download_finished_fragment, null);
        ButterKnife.bind(this, view);
        mList=new ArrayList<>();
        mAdapter = new DownloadFinishedAdapter(getActivity(), mList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new IDownloadFinishedPresenterImpl(getActivity(), this);
        mPresenter.loadLocalMedias();
        mAdapter.setOnItemClickListener(new DownloadFinishedAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, final int position) {
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle("是否要删除"+mList.get(position).getName());
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MediaUtil.deleteFile(mList.get(position).getPath());
                        mList.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        mAdapter.notifyItemRangeChanged(0, mList.size());
                    }
                });
                builder.show();

            }
        });
        return view;
    }

    @Override
    public void addList(List<DownloadFinishedBean> list) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.clear();
        mList.addAll(list);
        mAdapter.setList(mList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);

    }

    @Override
    public void showLoadFailMsg() {
        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "历史下载文件为空", Snackbar.LENGTH_SHORT).show();

    }
}
