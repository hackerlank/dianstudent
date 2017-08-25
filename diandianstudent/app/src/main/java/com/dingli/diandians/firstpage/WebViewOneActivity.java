package com.dingli.diandians.firstpage;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascripts;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import static android.view.KeyEvent.KEYCODE_BACK;


public class WebViewOneActivity extends Activity implements View.OnClickListener{

    private X5WebView webView;
    private String url="";
    private ImageView remenback;
    private TextView tvtoutiao;
    private String toutiao;
    String id;
    String  title;
    String weburl;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 1:
                        webView.loadUrl("javascript:accessTokenAddTokenType()");
                        webView.loadUrl("javascript:getClientType()");
                        webView.loadUrl("javascript:BundleShortVersion()");
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_viewone);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final ProgressBar bar = (ProgressBar)findViewById(R.id.myProgressBarone);
        getData();
        initView();
        fillWebView();
        webView.addJavascriptInterface(new AndroidObjectInJavascripts(this), "aizhixin");
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        webView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient(){
            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int i) {
                if (i == 0) {
                    handler.sendEmptyMessage(1);
                }
                if (i == 100) {
                    bar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(i);
                }
                super.onProgressChanged(webView, i);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                weburl=s;
                if (s!=null){
                    webView.loadUrl(s);
                }
                return true;
            }
        });
    }
    private void fillWebView() {
      webView.loadUrl(Constant.hy+"/mobileui_hy/");
    }
    private void getData() {
        url=getIntent().getStringExtra("url");
        title=getIntent().getStringExtra("title");
    }
    private void initView() {
        webView= (X5WebView) findViewById(R.id.webViewone);
        remenback=(ImageView) findViewById(R.id.remenbackone);
        tvtoutiao=(TextView)findViewById(R.id.tvtoutiaoone);
        remenback.setOnClickListener(this);
        LinearLayout webone=(LinearLayout) findViewById(R.id.webone);
            webone.setVisibility(View.GONE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remenbackone:
                if (!TextUtils.isEmpty(weburl)){
                    webView.goBack();
                    weburl="";
                }else {
                    this.finish();
                    overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                }
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


