package com.komect.showroom;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.komect.showroom.databinding.ActivityWebBinding;

public class WebActivity extends AppCompatActivity {
    protected static final String EXTRA_BUNDLE = "bundle";

    private String mUrl = "https://m.taobao.com";

    private WebView mWebView;
    private ActivityWebBinding binding;
    private String sessionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        if (getIntent().hasExtra(EXTRA_BUNDLE)) {
            Bundle bundle = getIntent().getBundleExtra(EXTRA_BUNDLE);
            if (bundle.containsKey("sessionId")) {
                sessionId = bundle.getString("sessionId", "");
                mUrl = BuildConfig.H5_HOST + "?sessionId=" + sessionId;
                Log.d("tlog", "onCreate 拼接后: " + mUrl);
            }
        }

        mWebView = binding.webView;

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new ZTWebViewClient());
        mWebView.loadUrl(mUrl);
        Log.d("tlog", "onCreate 加载: " + mUrl);
    }


    private void refresh() {
        mWebView.reload();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) mWebView.destroy();
    }


    @Override protected void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
    }


    @Override protected void onResume() {
        super.onResume();
        if (mWebView != null) mWebView.onResume();
    }


    private class ChromeClient extends WebChromeClient {

        @Override public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            //mProgressbar.setProgress(newProgress);
            //if (newProgress == 100) {
            //    mProgressbar.setVisibility(View.GONE);
            //} else {
            //    mProgressbar.setVisibility(View.VISIBLE);
            //}
        }


        @Override public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            //setTitle(title);
            binding.toolbarTitle.setText(title);
        }
    }

    private class ZTWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 以"http","https"开头的url在本页用webview进行加载，其他链接进行跳转
            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            }
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
            }
            return true;
        }
    }
}
