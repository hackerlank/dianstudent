package com.dingli.diandians.survey;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dingli.diandians.R;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascript;
import com.dingli.diandians.firstpage.hybrid.JsInterface;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


public class WebViewSurveyActivity extends Activity implements View.OnClickListener{

    public X5WebView webView;
    private String url="";
    int visib;
private SwipeRefreshLayout swipeLayout;
    private String mFailingUrl = null;
    LinearLayout llsurvey;
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
                }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyweb_view);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        getData();
        initView();
        fillWebView();
    }

    private void fillWebView() {
        ImageView numbertestback=(ImageView) findViewById(R.id.numbertestback);
        numbertestback.setOnClickListener(this);
        llsurvey=(LinearLayout) findViewById(R.id.llsurvey);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.addJavascriptInterface(new AndroidObjectInJavascript(this), "aizhixin");
        webView.addJavascriptInterface(new JsInterface(handler), "retry");
        webView.loadUrl(url);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(url);
            }
        });

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.holo_blue_bright),
                getResources().getColor(R.color.holo_green_light),
                getResources().getColor(R.color.holo_orange_light),
                getResources().getColor(R.color.holo_red_light));
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 0) {
                    handler.sendEmptyMessage(1);
                }
                if (progress == 100) {
                    swipeLayout.setRefreshing(false);
                     swipeLayout.setEnabled(false);
                    llsurvey.setVisibility(View.GONE);
                    llsurvey.removeAllViews();
                    llsurvey.setBackgroundResource(R.color.bg_White);
                } else {
                    if (visib==0) {
                        visib=1;
                        llsurvey.setVisibility(View.VISIBLE);
                    }
                    if (!swipeLayout.isRefreshing()) {
                        swipeLayout.setRefreshing(true);
                    }
                }
                super.onProgressChanged(view, progress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                llsurvey.setVisibility(View.GONE);
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
        webView= (X5WebView) findViewById(R.id.webViews);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.numbertestback:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }
}


