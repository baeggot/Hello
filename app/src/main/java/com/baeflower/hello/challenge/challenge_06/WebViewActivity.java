package com.baeflower.hello.challenge.challenge_06;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baeflower.hello.R;

public class WebViewActivity extends ActionBarActivity {


    private Button mShowBtn;
    private LinearLayout mInputLayout;
    private boolean open = true;

    private Button mOpenWebBtn;
    private EditText mWebUriEditText;
    private WebView mOpenWebWebView;

    private Animation animFadeIn;
    private Animation animSlideUp;
    private Animation mAnimTranslation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // 이렇게 해야 없어지네네
        // 이유는 상속받은 ActionBarActivity 때문이다!(Activity 상속받은 경우는 getActionBar() 썼음)
        // ActionBar actionBar = getSupportActionBar();
        // actionBar.hide();

        mShowBtn = (Button) findViewById(R.id.btn_show);
        mInputLayout = (LinearLayout) findViewById(R.id.layout_input);
        mOpenWebBtn = (Button) findViewById(R.id.btn_openWeb);
        mWebUriEditText = (EditText) findViewById(R.id.editText_webUrl);
        mOpenWebWebView = (WebView) findViewById(R.id.webView_openWeb);

        mShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputLayout.getVisibility() == View.GONE){
                    mInputLayout.setVisibility(View.VISIBLE);
                    mShowBtn.setText("CLOSE");
                    mInputLayout.startAnimation(mAnimTranslation);

                } else if (mInputLayout.getVisibility() == View.VISIBLE) {
                    mInputLayout.setVisibility(View.GONE);
                    mShowBtn.setText("OPEN");
                }
            }
        });

        // Animation 객체 초기화
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        mAnimTranslation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translation);

        mOpenWebBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mWebUriEditText.getText().toString();

                if (TextUtils.isEmpty(url)){
                    Toast.makeText(getApplicationContext(), "url을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 웹뷰에서 자바스크립트를 쓸수있게 한다는데...
                    mOpenWebWebView.getSettings().setJavaScriptEnabled(true);
                    mOpenWebWebView.loadUrl(url);
                    // 이걸 해야 새 activity가 아니라 webview안에 인터넷이 열림
                    // 왜그러지?
                    mOpenWebWebView.setWebViewClient(new WebClient());

                    // slideUp animation 추가, 이렇게 하면 되나?
                    mOpenWebWebView.startAnimation(animSlideUp);
                }
            }
        });

    }

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    // 뭐하는 놈이지?
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mOpenWebWebView.canGoBack()) {
            mOpenWebWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
