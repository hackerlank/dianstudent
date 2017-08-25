package com.dingli.diandians.firstpage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascript;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascripts;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by dingliyuangong on 2016/12/23.
 */
public class WebViewsActivity extends Activity implements View.OnClickListener{
    X5WebView webView;
    ProgressBar bar;
    String url;
    String toutiao;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
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
        setContentView(R.layout.activity_web_views);
        initview();
        webView.loadUrl(Constant.webwenzhang+ url);
        webView.addJavascriptInterface(new AndroidObjectInJavascripts(this), "aizhixin");
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
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
    }
    void initview(){
        url=getIntent().getStringExtra("url");
        toutiao=getIntent().getStringExtra("title");
         webView=(X5WebView)findViewById(R.id.webViewd);
         bar=(ProgressBar)findViewById(R.id.myProgressBard);
        TextView tvtoutiaos=(TextView)findViewById(R.id.tvtoutiaos);
        if (url.contains("allArticle")){
            tvtoutiaos.setText("点点心理");
        }else{
            tvtoutiaos.setText(toutiao);
        }
        ImageView remenbacks=(ImageView)findViewById(R.id.remenbacks);
        remenbacks.setOnClickListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.remenbacks:
                finish();
                overridePendingTransition(R.anim.activity_pop_enter,R.anim.activity_pop_exit);
                break;
        }
    }
}
