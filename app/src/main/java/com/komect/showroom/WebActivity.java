package com.komect.showroom;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.komect.showroom.databinding.ActivityWebBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsl on 2017/9/30.
 */
public class WebActivity extends AppCompatActivity {
    protected static final String EXTRA_BUNDLE = "bundle";
    public static final String BUNDLE_SESSION_ID = "sessionId";
    public static final String BUNDLE_MOBILE = "mobile";

    private ActivityWebBinding binding;
    private String mUrl;
    private WebView mWebView;
    private String sessionId;
    private String mobile;
    private List<String> historyTitles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        binding.includeTopbar.btLeft.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                webGoBack();
            }
        });

        if (getIntent().hasExtra(EXTRA_BUNDLE)) {
            Bundle bundle = getIntent().getBundleExtra(EXTRA_BUNDLE);
            if (bundle.containsKey(BUNDLE_SESSION_ID)) {
                sessionId = bundle.getString(BUNDLE_SESSION_ID, "");
                mobile = bundle.getString(BUNDLE_MOBILE, "");
                mUrl = BuildConfig.H5_HOST + "?sessionId=" + sessionId + "&mobile=" + mobile;
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


    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    webGoBack();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 返回
     */
    private void webGoBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            String title = historyTitles.get(historyTitles.size() - 2);
            historyTitles.remove(historyTitles.size() - 1);
            binding.includeTopbar.tvTitle.setText(title);
        } else {
            finish();
        }
    }


    private void onRefresh() {
        mWebView.reload();
    }


    /**
     *
     */
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
            binding.includeTopbar.tvTitle.setText(title);
            historyTitles.add(title);
        }
    }

    /**
     *
     */
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
}
