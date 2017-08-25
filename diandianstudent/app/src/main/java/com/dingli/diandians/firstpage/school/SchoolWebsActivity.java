package com.dingli.diandians.firstpage.school;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.FirstPageFragment;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascript;
import com.dingli.diandians.firstpage.hybrid.JsInterface;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * Created by dingliyuangong on 2017/2/6.
 */

public class SchoolWebsActivity extends BaseActivity implements View.OnClickListener{
    ProgressBar bar;
    X5WebView daochuwebView;
    private String mFailingUrl = null;
    String urls;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()){
                switch (msg.what) {
                    case 2:
                        if (mFailingUrl != null) {
                            daochuwebView.loadUrl(mFailingUrl);
                        }
                        break;
                    case 1:
                        daochuwebView.loadUrl("javascript:homeLisWithID()");
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daochuweb);
        inits();
    }
    void inits(){
        ImageView daochuback=(ImageView) findViewById(R.id.daochuback);
        daochuback.setOnClickListener(this);
        bar=(ProgressBar) findViewById(R.id.daochuprogress);
        daochuwebView=(X5WebView) findViewById(R.id.daochuwebView);
        LinearLayout llkaoqing=(LinearLayout) findViewById(R.id.llkaoqing);
        TextView tvdaochu=(TextView) findViewById(R.id.tvdaochu);
        urls=getIntent().getStringExtra("url");
        String title=getIntent().getStringExtra("title");
         if (!TextUtils.isEmpty(title)){
             tvdaochu.setText(title);
             llkaoqing.setVisibility(View.VISIBLE);
         }else{
             llkaoqing.setVisibility(View.GONE);
         }
        daochuwebView.loadUrl(Constant.webwenzhang+urls);
        daochuwebView.addJavascriptInterface(new AndroidObjectInJavascript(this), "aizhixin");
        daochuwebView.addJavascriptInterface(new JsInterface(handler), "retry");
        daochuwebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                if (progress == 0) {
                    handler.sendEmptyMessage(1);
                }
                if (progress == 100) {
                    bar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }
        });
        daochuwebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mFailingUrl = failingUrl;
                view.loadUrl("file:///android_asset/error.html");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.daochuback:
                    finish();
                    overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                break;
        }
    }
}
