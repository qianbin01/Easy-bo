package com.qb.easy_bo.view.main;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.qb.easy_bo.R;
import com.qb.easy_bo.presenter.main.IMainPresenter;
import com.qb.easy_bo.presenter.main.IMainPresenterImpl;
import com.qb.easy_bo.view.about.IAboutFragment;
import com.qb.easy_bo.view.media.IMediaFragmentList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IMainActivity extends AppCompatActivity implements IMainView {
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private IMainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mMainPresenter = new IMainPresenterImpl(this);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open,
                R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerContent(mNavigationView);
        switch2Medias();
    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mMainPresenter.switchNavigation(menuItem.getItemId());
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    @Override
    public void switch2Medias() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new IMediaFragmentList(),"media").commit();
        mToolbar.setTitle(R.string.media);
    }

    @Override
    public void switch2Downloads() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new IMediaFragmentList()).commit();
        mToolbar.setTitle(R.string.download);
    }

    @Override
    public void switch2Settings() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new IMediaFragmentList()).commit();
        mToolbar.setTitle(R.string.setting);
    }

    @Override
    public void switch2About() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new IAboutFragment()).commit();
        mToolbar.setTitle(R.string.about);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }
}
