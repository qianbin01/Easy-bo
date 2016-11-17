package com.qb.easy_bo.view.media;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.qb.easy_bo.Adapter.LocalVideoAdapter;
import com.qb.easy_bo.R;
import com.qb.easy_bo.bean.LocalVideo;
import com.qb.easy_bo.presenter.media.ILocalMediaPresenter;
import com.qb.easy_bo.presenter.media.IMediaPresenterImpl;
import com.qb.easy_bo.utils.video.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ILocalVideoFragment extends Fragment implements ILocalView<LocalVideo> {
    ILocalMediaPresenter mPresenter;

    @Bind(R.id.progress)
    ProgressBar mProgress;
    @Bind(R.id.recycle_view)
    RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;
    private List<LocalVideo> mList;
    private LocalVideoAdapter mAdapter;

    public static ILocalVideoFragment newInstance(int type) {
        Bundle args = new Bundle();
        ILocalVideoFragment fragment = new ILocalVideoFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new IMediaPresenterImpl(this, getActivity(), 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.local_video_fragment, null);
        ButterKnife.bind(this, view);
        mList = new ArrayList<>();
        mAdapter = new LocalVideoAdapter(getActivity(), mList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new LocalVideoAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, int position, LocalVideo video) {
                Intent intent=new Intent(getActivity(), VideoPlayerActivity.class);
                intent.putExtra("url",video.getVideoPath());
                startActivity(intent);
            }
        });
        mPresenter.loadLocalMedias();
        return view;
    }

    @Override
    public void addMedias(List<LocalVideo> t) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.clear();
        mList.addAll(t);
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
        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "本地音乐文件为空", Snackbar.LENGTH_SHORT).show();
    }

}
