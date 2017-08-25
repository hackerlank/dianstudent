package com.dingli.diandians.firstpage;

import android.app.Activity;
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
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.common.X5WebView;
import com.dingli.diandians.firstpage.hybrid.AndroidObjectInJavascripts;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


public class WebViewFiveActivity extends Activity implements View.OnClickListener{

    private X5WebView webView;
    private String url="";
    private ImageView remenback;
    private TextView tvtoutiao;
    private String toutiao;
    String id;
    String list;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 1:
                        webView.loadUrl("javascript:homeLisWithID()");
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewfive);
        final ProgressBar bar = (ProgressBar)findViewById(R.id.myProgressBarfive);
        initView();
        webView.addJavascriptInterface(new AndroidObjectInJavascripts(this), "aizhixin");
        webView.setWebChromeClient(new WebChromeClient() {
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
    }
    private void initView() {
        webView= (X5WebView) findViewById(R.id.webViewfive);
        remenback=(ImageView) findViewById(R.id.remenbackfive);
        LinearLayout webviewfive=(LinearLayout) findViewById(R.id.webviewfive);
        tvtoutiao=(TextView)findViewById(R.id.tvtoutiaofive);
        remenback.setOnClickListener(this);
        url=getIntent().getStringExtra("targetUrl");
        String targetTitle=getIntent().getStringExtra("targetTitle");
        String id=getIntent().getStringExtra("id");
        DianApplication.user.wenzhangid=id;
        if (!TextUtils.isEmpty(targetTitle)){
            webviewfive.setVisibility(View.VISIBLE);
            tvtoutiao.setText(targetTitle);
        }else{
            webviewfive.setVisibility(View.GONE);
        }
        webView.loadUrl(url);
        setWebViewNEW();
    }

    public void setWebViewNEW() {
          webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
          webView.setWebViewClient(new WebViewClient(){
              @Override
              public boolean shouldOverrideUrlLoading(WebView view, String url) {
                  view.loadUrl(url);
                  return true;
              }
          });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remenbackfive:
                this.finish();
                overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit);
        }
    }
}


