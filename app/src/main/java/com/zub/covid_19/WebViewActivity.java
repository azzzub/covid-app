package com.zub.covid_19;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.web_view_progress_bar)
    RelativeLayout progressBar;
    @BindView(R.id.web_view_toolbar)
    Toolbar mWebViewToolbar;
    @BindView(R.id.web_view)
    WebView mWebView;

    String passingURL, passingTitle;

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        mWebViewToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_back));
        mWebViewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.clearCache(true);
                mWebView.clearHistory();
                finish();
            }
        });
        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        passingURL = bundle.getString("passingURL");
        passingTitle = bundle.getString("passingTitle");
        mWebView.loadUrl(passingURL);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new mWebViewClient());
    }

    private class mWebViewClient extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            mWebViewToolbar.setTitle(passingTitle);
        }
    }
}
