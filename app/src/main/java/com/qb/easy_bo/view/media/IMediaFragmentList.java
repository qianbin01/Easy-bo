package com.qb.easy_bo.view.media;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qb.easy_bo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class IMediaFragmentList extends Fragment {
    public static final int VIDEO_LOCAL_MUSIC = 0;
    public static final int VIDEO_LOCAL_VIDEO = 1;
    public static final int VIDEO_NETWORK = 2;
    @Bind(R.id.tab_layout)
    TabLayout mTablayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.videos_fragment, null);
        ButterKnife.bind(this, view);
        setupViewPager(mViewPager);
        mTablayout.addTab(mTablayout.newTab().setText(R.string.local_music));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.local_video));
        mTablayout.addTab(mTablayout.newTab().setText(R.string.network));
        mTablayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void setupViewPager(ViewPager mViewPager) {
        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ILocalMusicFragment.newInstance(VIDEO_LOCAL_MUSIC), getResources().getString(R.string.local_music));
        adapter.addFragment(ILocalVideoFragment.newInstance(VIDEO_LOCAL_VIDEO), getResources().getString(R.string.local_video));
        adapter.addFragment(INetWorkFragment.newInstance(VIDEO_NETWORK), getResources().getString(R.string.network));
        mViewPager.setAdapter(adapter);
    }

    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
