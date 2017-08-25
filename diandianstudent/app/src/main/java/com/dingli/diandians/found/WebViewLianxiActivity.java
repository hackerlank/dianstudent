package com.dingli.diandians.found;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.dingli.diandians.R;
import com.dingli.diandians.bean.InJavaScriptInterface;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascript;
import com.dingli.diandians.firstpage.hybrid.JsInterface;
import com.dingli.diandians.lostproperty.WebViewLostActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import static android.view.KeyEvent.KEYCODE_BACK;


public class WebViewLianxiActivity extends Activity implements View.OnClickListener{

    public X5WebView webView;
    private String url="";
    private String mFailingUrl = null;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()){
                switch (msg.what) {
                    case 2:
                        if (mFailingUrl != null) {
                            webView.loadUrl(mFailingUrl);
                        }
                        break;
                    case 1:
                        webView.loadUrl("javascript:accessTokenAddTokenType()");
                        break;
                    case 3:
                        webView.loadUrl("javascript:callWebAppShowConfirm()");
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    public LinearLayout llayouts;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lianxiweb_view);
        getData();
        initView();
        fillWebView();
    }

    private void fillWebView(){
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.addJavascriptInterface(new AndroidObjectInJavascript(this), "aizhixin");
        webView.addJavascriptInterface(new JsInterface(handler), "retry");
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 0) {
                    handler.sendEmptyMessage(1);
                }else if (progress==100){
                    llayouts.setVisibility(View.GONE);
                    bar.setVisibility(View.GONE);
                }else {
                    if (View.GONE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(progress);
                }
                super.onProgressChanged(view, progress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    view.loadUrl(url);
                }
                return true;
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mFailingUrl = failingUrl;
                view.loadUrl("file:///android_asset/error.html");
            }
        });
    }
    private void getData() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            url=bundle.getString("url");
        }
    }
    private void initView() {
        webView= (X5WebView) findViewById(R.id.webViewslian);
        llayouts=(LinearLayout)findViewById(R.id.llayouts);
        bar=(ProgressBar) findViewById(R.id.lianxiprogress);
        ImageView lianxibacks=(ImageView)findViewById(R.id.lianxibacks);
        lianxibacks.setOnClickListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lianxibacks:
                WebViewLianxiActivity.this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(event.getAction()==KeyEvent.ACTION_DOWN
                &&KeyEvent.KEYCODE_BACK==keyCode){
//                if (!TextUtils.isEmpty(DianApplication.user.weburl)) {
//                    if (DianApplication.user.weburl.equals("exam")) {
                        handler.sendEmptyMessage(3);
//                    } else if (DianApplication.user.weburl.equals("examinfo")) {
//                        webView.loadUrl(url);
//                    }
//                 }else{
//                    if (webView.getUrl().contains("home")) {
//                        webView.goBack();
//                    }else{
//                        webView.goBack();
                        return true;
//                    }
//                }
        }
        return super.onKeyDown(keyCode,event);
    }
}


