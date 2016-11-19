package com.qb.easy_bo.view.media;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qb.easy_bo.Adapter.NetWorkVideoAdapter;
import com.qb.easy_bo.R;
import com.qb.easy_bo.bean.NetWorkVideo;
import com.qb.easy_bo.presenter.media.INetWorkMediaPresenter;
import com.qb.easy_bo.presenter.media.INetWorkMediaPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class INetWorkFragment extends Fragment implements INetWorkView {

    @Bind(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycle_view)
    RecyclerView mRecyclerView;

    private List<NetWorkVideo> mList;
    private INetWorkMediaPresenter mPresenter;
    private NetWorkVideoAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.network_list_fragment, null);
        ButterKnife.bind(this, view);
        initConfig();
        return view;
    }

    private void initConfig() {
        mList = new ArrayList<>();
        mAdapter = new NetWorkVideoAdapter(getActivity(), mList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mPresenter = new INetWorkMediaPresenterImpl(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new NetWorkVideoAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, int position, NetWorkVideo netWorkVideo) {
                mPresenter.loadDetail(netWorkVideo,v);
            }
        });
        mPresenter.loadbegin();
    }

    @Override
    public void addNetList(List<NetWorkVideo> list) {
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
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
        Snackbar.make(mRecyclerView, "网络视频列表加载失败", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void openDetail(NetWorkVideo netWorkVideo,View v) {
        Intent intent = new Intent(getActivity(), INetWorkDetailActivity.class);
        intent.putExtra("url", netWorkVideo.getUrl());
        intent.putExtra("title", netWorkVideo.getName());
        intent.putExtra("image",netWorkVideo.getResId());
        View tranView = v.findViewById(R.id.ivImage);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        tranView, "transtion_view");
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }
}