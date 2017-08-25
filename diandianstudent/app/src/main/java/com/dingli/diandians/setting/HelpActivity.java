package com.dingli.diandians.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dingli.diandians.R;
import com.dingli.diandians.common.BaseActivity;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascripts;
import com.dingli.diandians.firstpage.hybrid.JsInterface;
import com.dingli.diandians.view.VerticalSwipeRefreshLayout;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by dingliyuangong on 2016/11/28.
 */
public class HelpActivity extends BaseActivity implements View.OnClickListener{

    X5WebView wbhelp;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    wbhelp.loadUrl("javascript:getClientType()");
                    break;
                case 2:
                    if(mFailingUrl !=null){
                        wbhelp.loadUrl(mFailingUrl);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private String mFailingUrl = null;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();
    }
    private void initView() {
        ImageView helpback = (ImageView) findViewById(R.id.helpback);
        bar=(ProgressBar)findViewById(R.id.helpProgressBars);
        helpback.setOnClickListener(this);
        wbhelp=(X5WebView) findViewById(R.id.wbhelp);
        wbhelp.addJavascriptInterface(new AndroidObjectInJavascripts(this), "aizhixin");
        wbhelp.addJavascriptInterface(new JsInterface(handler), "retry");
        wbhelp.loadUrl(Constant.webhelp+"/mobileui/helperCenter");
        wbhelp.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 0) {
                    handler.sendEmptyMessage(1);
                } else if (newProgress == 100) {
                    bar.setVisibility(View.GONE);
                }else{
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        wbhelp.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mFailingUrl = failingUrl;
                view.loadUrl("file:///android_asset/error.html");
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String s) {
                view.loadUrl(s);
                return true;
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.helpback:
                if (wbhelp.getUrl().equals(Constant.webhelp+"/mobileui/helperCenter")) {
                    finish();
                    overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
                }else{
                    wbhelp.loadUrl(Constant.webhelp+"/mobileui/helperCenter");
                }
                break;
        }
    }
}
