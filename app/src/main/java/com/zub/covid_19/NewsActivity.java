package com.zub.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NewsActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Bundle bundle = this.getIntent().getExtras();
        String passingUrl = bundle.getString("passingUrl");
        TextView text = findViewById(R.id.url);
        progressBar = findViewById(R.id.progressBar2);
        WebView webView = findViewById(R.id.web_view);
        text.setText(passingUrl);
        webView.loadUrl(passingUrl);
        webView.setWebViewClient(new mWebViewClient());

    }

    private class mWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}
