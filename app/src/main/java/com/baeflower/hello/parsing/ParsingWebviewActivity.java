package com.baeflower.hello.parsing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.baeflower.hello.R;


public class ParsingWebviewActivity extends ActionBarActivity {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing_webview);

        webView = (WebView) findViewById(R.id.wv_parsing);
        progressBar = (ProgressBar) findViewById(R.id.pb_parsing_wv);

        Intent intent = getIntent();
        if (intent != null) {
            String link = intent.getStringExtra("link");
            webView.loadUrl(link);
            webView.setWebViewClient(new WebClient());
        }
    }

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
