package com.qb.easy_bo.view.media;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.qb.easy_bo.R;
import com.qb.easy_bo.presenter.media.INetWorkMediaDetailPresenter;
import com.qb.easy_bo.presenter.media.INetWorkMediaDetailPresenterImpl;

import butterknife.Bind;
import butterknife.ButterKnife;

public class INetWorkDetailActivity extends AppCompatActivity implements INetWorkDetailView {
    @Bind(R.id.progress)
    ProgressBar mProgressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Bind(R.id.ivImage)
    ImageView imageView;
    @Bind(R.id.WebView)
    WebView mWebView;

    private String url;
    private INetWorkMediaDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_detail_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mCollapsingToolbarLayout.setTitle(getIntent().getStringExtra("title"));
        imageView.setImageResource(getIntent().getIntExtra("image",0));
        mPresenter = new INetWorkMediaDetailPresenterImpl(this);
        url=getIntent().getStringExtra("url");
        mPresenter.loadBegin(url);
    }

    @Override
    public void initWebView(String url) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mPresenter.loadSuccess();
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                CharSequence pnotfound = "404";
                if (title.contains(pnotfound)) {
                    view.stopLoading();
                    mPresenter.loadFailure();
                }
            }
        });
    }
    //WebView内返回键
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1: {
                    mWebView.goBack();
                }
                break;
            }
        }
    };

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoadFailMsg() {
        Snackbar.make(mWebView, "加载失败", Snackbar.LENGTH_SHORT).show();
    }
}
