package com.zub.covid_19;

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

    @BindView(R.id.progressBar2)
    RelativeLayout progressBar;
    @BindView(R.id.web_view_toolbar)
    Toolbar mWebViewToolbar;
    @BindView(R.id.web_view)
    WebView mWebView;

    String passingURL, passingTitle;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        mWebViewToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_back));
        mWebViewToolbar.setNavigationOnClickListener(view -> finish());
        Bundle bundle = this.getIntent().getExtras();
        assert bundle != null;
        passingURL = bundle.getString("passingURL");
        passingTitle = bundle.getString("passingTitle");
        mWebView.loadUrl(passingURL);

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
